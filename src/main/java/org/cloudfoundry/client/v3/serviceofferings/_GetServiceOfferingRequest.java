package org.cloudfoundry.client.v3.serviceofferings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.immutables.value.Value;

@Value.Immutable
abstract class _GetServiceOfferingRequest {
    /**     * The service offering id     */
    @JsonIgnore
    abstract String getServiceOfferingId();
}
