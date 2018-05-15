package org.openpaas.paasta.portal.api.service;

import com.google.gson.Gson;
import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.domain.CloudUser;
import org.cloudfoundry.client.v2.spacequotadefinitions.GetSpaceQuotaDefinitionRequest;
import org.cloudfoundry.client.v2.spacequotadefinitions.GetSpaceQuotaDefinitionResponse;
import org.cloudfoundry.client.v2.spaces.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.model.App;
import org.openpaas.paasta.portal.api.model.Org;
import org.openpaas.paasta.portal.api.model.Space;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

//import org.openpaas.paasta.portal.api.mapper.cc.OrgMapper;
//import org.openpaas.paasta.portal.api.mapper.cc.SpaceMapper;

/**
 * 공간 서비스 - 공간 목록 , 공간 이름 변경 , 공간 생성 및 삭제 등을 제공한다.
 *
 * @author 조민구
 * @version 1.0
 * @since 2016.4.4 최초작성
 */
@Service
public class SpaceService extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceService.class);

    @Autowired
    private AsyncUtilService asyncUtilService;

    @Autowired
    @Lazy // To resolve circular reference
    private OrgService orgService;

    /**
     * 공간(스페이스) 목록 조회한다.
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param token the token
     * @return ListSpacesResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public ListSpacesResponse getSpaces(String token) {
        ListSpacesResponse response = Common
            .cloudFoundryClient( connectionContext(), tokenProvider( token ) ).spaces()
            .list( ListSpacesRequest.builder().build() ).block();

        return response;
    }

    /**
     * 공간(스페이스) 목록 조회한다.
     * 특정 조직을 인자로 받아 해당 조직의 공간을 조회한다.
     *
     * @param org   the org
     * @param token the token
     * @return ListSpacesResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public ListSpacesResponse getSpaces(Org org, String token) {
        String orgId = null;
        if (org.getGuid() != null) {
            orgId = org.getGuid().toString();
        } else if (org.getName() != null) {
            orgId = orgService.getOrgId( org.getName(), token );
        } else {
            throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "To get spaces in org, you must be require org name or org id." );
        }

        Objects.requireNonNull( orgId, "Org id must not be null." );
        ListSpacesResponse response = Common
            .cloudFoundryClient( connectionContext(), tokenProvider( token ) ).spaces()
            .list( ListSpacesRequest.builder().organizationId( orgId ).build() ).block();

        return response;
    }

    /**
     * 공간을 생성한다. (Space : Create)
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public CreateSpaceResponse createSpace(Space space, String token) {
        Objects.requireNonNull( space.getSpaceName(), "Space name must not be null. Required request body is space name(spaceName) and org GUID (orgGuid)." );
        Objects.requireNonNull( space.getOrgGuid(), "Space name must not be null. Required request body is space name(spaceName) and org GUID (orgGuid)." );

        return Common.cloudFoundryClient(connectionContext(), tokenProvider(token))
            .spaces().create(CreateSpaceRequest.builder()
                .name(space.getSpaceName()).organizationId(space.getOrgGuid()).build())
            .block();
    }

    /**
     * 공간의 정보를 가져온다. (Space : Read)
     *
     * @param spaceId
     * @param token
     * @return GetSpaceResponse
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public GetSpaceResponse getSpace(String spaceId, String token) {
        return Common.cloudFoundryClient( connectionContext(), tokenProvider( token ) )
            .spaces().get( GetSpaceRequest.builder().spaceId( spaceId ).build() ).block();
    }

    /**
     * 공간명을 변경한다. (Space : Update)
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public UpdateSpaceResponse renameSpace(Space space, String token) throws Exception{
        String spaceGuid = space.getGuid().toString();
        String newSpaceName = space.getNewSpaceName();
        Objects.requireNonNull( spaceGuid, "Space GUID(guid) must be not null. Request body is made space GUID(guid) and new space name(newSpaceName)." );
        Objects.requireNonNull( newSpaceName, "New space name must be not null. Request body is made space GUID(guid) and new space name(newSpaceName)." );
        if(!stringNullCheck(spaceGuid,newSpaceName)) {
            throw new CloudFoundryException(HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content(guid or newSpaceName) is missing.");
        }

        return Common.cloudFoundryClient( connectionContext(), tokenProvider( token ) )
            .spaces().update( UpdateSpaceRequest.builder().spaceId( spaceGuid )
                .name( newSpaceName ).build() )
            .block();
    }

    /**
     * 공간을 삭제한다. (Space : Delete)
     *
     * @param space the space
     * @param token the token
     * @return boolean boolean
     * @throws Exception the exception
     * @author hgcho
     * @version 2.0
     * @since 2018.5.3
     */
    public DeleteSpaceResponse deleteSpace(Space space, String token) {
        Objects.requireNonNull( space.getGuid(), "Space GUID must not be null. Require parameters; spaceGuid[, recursive]" );

        String spaceGuid = space.getGuid().toString();
        boolean recursive = space.isRecursive();
        if ( !stringNullCheck( spaceGuid ) ) {
            throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing" );
        }

        return Common.cloudFoundryClient( connectionContext(), tokenProvider( token ) ).spaces()
            .delete( DeleteSpaceRequest.builder().spaceId( spaceGuid )
                .recursive( recursive ).async( true ).build() ).block();
    }





    /**
     * 공간 요약 정보를 조회한다.
     *
     * @param spaceId the spaceId
     * @param token the token
     * @return space summary
     * @throws Exception the exception
     */
    public GetSpaceSummaryResponse getSpaceSummary(String spaceId, String token) throws Exception {
        ReactorCloudFoundryClient cloudFoundryClient = Common.cloudFoundryClient( connectionContext(), tokenProvider( token ) );

        GetSpaceSummaryResponse respSapceSummary =
            cloudFoundryClient.spaces()
                .getSummary( GetSpaceSummaryRequest.builder()
                    .spaceId( spaceId ).build()
                ).block();

        return respSapceSummary;
    }
}
