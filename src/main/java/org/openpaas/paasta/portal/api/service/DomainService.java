package org.openpaas.paasta.portal.api.service;

import com.netflix.ribbon.proxy.annotation.Http;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.v2.Metadata;
import org.cloudfoundry.client.v2.PaginatedResponse;
import org.cloudfoundry.client.v2.domains.*;
import org.cloudfoundry.client.v2.jobs.ErrorDetails;
import org.cloudfoundry.client.v2.privatedomains.CreatePrivateDomainRequest;
import org.cloudfoundry.client.v2.privatedomains.CreatePrivateDomainResponse;
import org.cloudfoundry.client.v2.privatedomains.ListPrivateDomainsRequest;
import org.cloudfoundry.client.v2.privatedomains.ListPrivateDomainsResponse;
import org.cloudfoundry.client.v2.shareddomains.CreateSharedDomainRequest;
import org.cloudfoundry.client.v2.shareddomains.CreateSharedDomainResponse;
import org.cloudfoundry.client.v2.shareddomains.ListSharedDomainsRequest;
import org.cloudfoundry.client.v2.shareddomains.ListSharedDomainsResponse;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.tokenprovider.PasswordGrantTokenProvider;
import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 도메인 컨트롤러 - 도메인 정보를 조회, 수정, 삭제한다.
 *
 * @author 박철한
 * @version 2.0
 * @since 2018.4.30
 */
@Service
public class DomainService extends Common {
    private static final Logger LOGGER = LoggerFactory.getLogger( DomainService.class );

    @Autowired
    PasswordGrantTokenProvider adminTokenProvider;

    /**
     * 도메인 가져오기 - status 값을 받아 private, shared 중 선택하여 가져오거나 모두 가져올수 있음
     *
     * @param token  the token
     * @param status the status
     * @return domains response (V2)
     * @throws Exception the exception
     * @author 박철한, 조현구
     * @since 2018.4.30
     */
    public PaginatedResponse getDomains ( String token, String status ) throws Exception {
        LOGGER.debug( "Start getDomains service. status : " + status );
        if ( !stringNullCheck( status ) ) {
            throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "Bad Request", "Invalid status" );
        }

        switch ( status ) {
            case "all":
                return getAllDomains( connectionContext(), tokenProvider( token ) );
            case "private":
                return getPrivateDomains( connectionContext(), tokenProvider( token ) );
            case "shared":
                return getSharedDomains( connectionContext(), tokenProvider( token ) );
            default:
                throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "Bad Request", "Invalid status" );
        }
    }

    private ListDomainsResponse getAllDomains( final ConnectionContext context, final TokenProvider tokenProvider ) {
        return Common.cloudFoundryClient( context, tokenProvider )
            .domains().list( ListDomainsRequest.builder().build() ).block();
    }

    private ListDomainsResponse getAllDomains( final ConnectionContext context, final TokenProvider tokenProvider, final String[] names) {
        return Common.cloudFoundryClient( context, tokenProvider )
            .domains().list( ListDomainsRequest.builder().name( names ).build() ).block();
    }

    private ListPrivateDomainsResponse getPrivateDomains( final ConnectionContext context, final TokenProvider tokenProvider ) {
        return Common.cloudFoundryClient( context, tokenProvider )
            .privateDomains().list( ListPrivateDomainsRequest.builder().build() ).block();
    }

    private ListSharedDomainsResponse getSharedDomains( final ConnectionContext context, final TokenProvider tokenProvider ) {
        return Common.cloudFoundryClient( context, tokenProvider )
            .sharedDomains().list( ListSharedDomainsRequest.builder().build() ).block();
    }

    /**
     * 도메인 추가 (without 'is a domain shared' value, default: false)
     * @param token
     * @param domainName
     * @param orgId
     * @return
     * @throws Exception
     */
    public boolean addDomain ( String token, String domainName, String orgId ) throws Exception {
        return addDomain( token, domainName, orgId,false );
    }

    /**
     * 도메인 추가
     *
     * @param token      the token
     * @param domainName the domain name
     * @param orgId      the organization id
     * @return isShared  Is it the shared domain?
     * @throws Exception the exception
     * @author 조현구
     * @since 2018.5.15
     */
    public boolean addDomain ( String token, final String domainName, final String orgId, boolean isShared ) {
        LOGGER.info( "Start addDomain service. domainName : " + domainName );

        if ( !stringNullCheck( token, domainName ) )
            throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "Bad Request",
                "Required request body content is missing : token or domain name" );

        if ( !isShared && !stringNullCheck( orgId ) ) {
            throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "Bad Request",
                "Required request body content is missing : Organization ID; " +
                    "Creating shared domain will need a org id." );
        }

        boolean anyMatch = getAllDomains( connectionContext(), tokenProvider( token ) )
            .getResources().stream()
            .anyMatch( domainResource -> domainName.equals( domainResource.getEntity().getName() ) );

        if ( anyMatch )
            throw new CloudFoundryException( HttpStatus.CONFLICT, "Conflict domains", "Domain name already exist." );

        final String addedDomainName;

        if ( isShared ) {
            final CreateSharedDomainResponse response =
                addSharedDomain( connectionContext(), adminTokenProvider, domainName );
            LOGGER.debug( "Response for adding shared domain is... {}", response);
            addedDomainName = response.getEntity().getName();
        } else {
            final CreatePrivateDomainResponse response =
                addPrivateDomain( connectionContext(), tokenProvider( token ), domainName, orgId );
            LOGGER.debug( "Response for adding private domain is... {}", response);
            addedDomainName = response.getEntity().getName();
        }

        if (!addedDomainName.equals( domainName )) {
            LOGGER.error( "It can't add domain with given domain name : {}", domainName );
            return false;
        } else {
            return true;
        }
    }

    private CreateSharedDomainResponse addSharedDomain( ConnectionContext context, TokenProvider tokenProvider, String domainName ) {
        return Common.cloudFoundryClient( context, tokenProvider ).sharedDomains()
            .create( CreateSharedDomainRequest.builder().name( domainName ).build() )
            .block();
    }

    public CreatePrivateDomainResponse addPrivateDomain( ConnectionContext context, TokenProvider tokenProvider,
                                                         String domainName, String orgId ) {
        return Common.cloudFoundryClient( context, tokenProvider ).privateDomains()
            .create( CreatePrivateDomainRequest.builder().name( domainName ).owningOrganizationId( orgId ).build() )
            .block();
    }


    /**
     * 도메인 삭제
     *
     * @param token      the token
     * @param domainName the domain name
     * @return Boolean did a domain delete surely?
     * @throws Exception the exception
     * @author 조현구
     * @since 2018.5.15
     */
    public boolean deleteDomain ( String token, String domainName ) throws Exception {
        LOGGER.info( "Start deleteDomain service. domainName : " + domainName );

        if ( !stringNullCheck( token, domainName ) ) {
            throw new CloudFoundryException( HttpStatus.BAD_REQUEST, "Bad Request", "Required request body content is missing" );
        }

        List<DomainResource> domains = getAllDomains( connectionContext(), tokenProvider( token ) ).getResources()
            .stream().filter( domain -> domainName.equals( domain.getEntity().getName() ) )
            .collect(Collectors.toList());

        LOGGER.debug("Counts of filter domains with domainName({}) : {}", domainName, domains.size());
        if (domains.size() > 0) {
            final DeleteDomainResponse response = Common.cloudFoundryClient( connectionContext(), adminTokenProvider )
                .domains().delete(
                    DeleteDomainRequest.builder().domainId(
                        domains.get( 0 ).getMetadata().getId() ).build()
            ).block();
            Objects.requireNonNull(response, "Delete domain response");

            if (response.getEntity().getErrorDetails() == null)
                return true;
            else {
                final ErrorDetails errorDetails = response.getEntity().getErrorDetails();
                throw new CloudFoundryException(
                    HttpStatus.CONFLICT, errorDetails.getDescription(), errorDetails.getErrorCode() );
            }
        } else {
            LOGGER.warn( "Cannot find to delete a domain! : {}", domainName );
            return true;
            /*
            throw new CloudFoundryException(
                HttpStatus.SERVICE_UNAVAILABLE, "Cannot delete", "Cannot find to delete a domain : " + domainName);
            */
        }
    }
}
