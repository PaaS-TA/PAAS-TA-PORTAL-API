package org.openpaas.paasta.portal.api.service;

import org.cloudfoundry.client.v3.Relationship;
import org.cloudfoundry.client.v3.ToOneRelationship;
import org.cloudfoundry.client.v3.droplets.ListDropletsResponse;
import org.cloudfoundry.client.v3.packages.*;
import org.cloudfoundry.reactor.client.ReactorCloudFoundryClient;
import org.openpaas.paasta.portal.api.common.Common;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class PackageServiceV3 extends Common {
    private final Logger LOGGER = getLogger(this.getClass());

    public GetPackageResponse getPackage(String packageId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.packages().get(GetPackageRequest.builder().packageId(packageId).build()).block();
    }

    public ListPackagesResponse listPackage(String applicationId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.packages().list(ListPackagesRequest.builder().applicationId(applicationId).build()).block();
    }

    public ListPackageDropletsResponse listPackageDroplet(String packageId, String token){
        ReactorCloudFoundryClient reactorCloudFoundryClient = cloudFoundryClient(tokenProvider(token));
        return reactorCloudFoundryClient.packages().listDroplets(ListPackageDropletsRequest.builder().packageId(packageId).build()).block();
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
