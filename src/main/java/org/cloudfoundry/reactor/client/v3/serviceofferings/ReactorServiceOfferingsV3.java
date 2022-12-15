package org.cloudfoundry.reactor.client.v3.serviceofferings;

import org.cloudfoundry.client.v3.serviceofferings.DeleteServiceOfferingRequest;
import org.cloudfoundry.client.v3.serviceofferings.GetServiceOfferingRequest;
import org.cloudfoundry.client.v3.serviceofferings.GetServiceOfferingResponse;
import org.cloudfoundry.client.v3.serviceofferings.ListServiceOfferingsRequest;
import org.cloudfoundry.client.v3.serviceofferings.ListServiceOfferingsResponse;
import org.cloudfoundry.client.v3.serviceofferings.ServiceOfferingsV3;
import org.cloudfoundry.client.v3.serviceofferings.UpdateServiceOfferingRequest;
import org.cloudfoundry.client.v3.serviceofferings.UpdateServiceOfferingResponse;
import org.cloudfoundry.client.v3.serviceofferings.ServiceOfferingsV3;
import org.cloudfoundry.reactor.ConnectionContext;
import org.cloudfoundry.reactor.TokenProvider;
import org.cloudfoundry.reactor.client.v3.AbstractClientV3Operations;
import reactor.core.publisher.Mono;

import java.util.Map;

public final class ReactorServiceOfferingsV3 extends AbstractClientV3Operations implements ServiceOfferingsV3 {

    /**
     * Creates an instance
     *
     * @param connectionContext the {@link ConnectionContext} to use when communicating with the server
     * @param root              the root URI of the server. Typically something like {@code https://api.run.pivotal.io}.
     * @param tokenProvider     the {@link TokenProvider} to use when communicating with the server
     * @param requestTags       map with custom http headers which will be added to web request
     */
    public ReactorServiceOfferingsV3(ConnectionContext connectionContext, Mono<String> root, TokenProvider tokenProvider) {
        super(connectionContext, root, tokenProvider);
    }

    @Override
    public Mono<Void> delete(DeleteServiceOfferingRequest request) {
        return delete(request, Void.class, builder -> builder.pathSegment("service_offerings", request.getServiceOfferingId()))
                .checkpoint();
    }

    @Override
    public Mono<GetServiceOfferingResponse> get(GetServiceOfferingRequest request) {
        return get(request, GetServiceOfferingResponse.class, builder -> builder.pathSegment("service_offerings", request.getServiceOfferingId()))
                .checkpoint();
    }

    @Override
    public Mono<ListServiceOfferingsResponse> list(ListServiceOfferingsRequest request) {
        return get(request, ListServiceOfferingsResponse.class, builder -> builder.pathSegment("service_offerings"))
                .checkpoint();
    }

    @Override
    public Mono<UpdateServiceOfferingResponse> update(UpdateServiceOfferingRequest request) {
        return patch(request, UpdateServiceOfferingResponse.class, builder -> builder.pathSegment("service_offerings", request.getServiceOfferingId()))
                .checkpoint();
    }

}
