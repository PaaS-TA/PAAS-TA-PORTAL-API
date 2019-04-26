package org.openpaas.paasta.portal.api.controller;

import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v3.packages.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.openpaas.paasta.portal.api.common.Constants;
import org.openpaas.paasta.portal.api.service.PackageServiceV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;

@RestController
public class PackageControllerV3 extends Common {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////                            * CLOUD FOUNDRY CLIENT API VERSION 3                                      //////
    //////             Document : https://v3-apidocs.cloudfoundry.org/version/3.70.0/#packages                  //////
    //////                                     Not-implemented                                                  //////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Autowired
    PackageServiceV3 packageServiceV3;

    @GetMapping(Constants.V3_URL + "/packages/{packageId:.+}")
    public GetPackageResponse getPackage(@PathVariable String packageId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return packageServiceV3.getPackage(packageId, token);
    }

    @GetMapping(Constants.V3_URL + "/packages/{applicationId:.+}/apps")
    public ListPackagesResponse listPackage(@PathVariable String applicationId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return packageServiceV3.listPackage(applicationId, token);
    }

    @GetMapping(Constants.V3_URL + "/packages/{packageId:.+}/droplet")
    public ListPackageDropletsResponse listPackageDroplet(@PathVariable String packageId, @RequestHeader(AUTHORIZATION_HEADER_KEY) String token){
        return packageServiceV3.listPackageDroplet(packageId, token);
    }


    public byte[] downloadPackage(String packageId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.packages().download(DownloadPackageRequest.builder().packageId(packageId).build()).blockLast();
    }

    public UploadPackageResponse uploadPackage(String pacakgeId, Path bit, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.packages().upload(UploadPackageRequest.builder().packageId(pacakgeId).bits(bit).build()).block();
    }

    public CopyPackageResponse copyPackage(String applicationId, String packageId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.packages().copy(CopyPackageRequest.builder()
                .relationships(PackageRelationships.builder()
                        .application(ToOneRelationship.builder()
                                .data(Relationship.builder().id(applicationId).build()).build()).build())
                .sourcePackageId(packageId)
                .build()).block();
    }

    public String deletePackage(String packageId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.packages().delete(DeletePackageRequest.builder().packageId(packageId).build()).block();
    }
}
