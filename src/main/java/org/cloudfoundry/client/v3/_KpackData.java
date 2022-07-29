package org.cloudfoundry.client.v3;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.cloudfoundry.Nullable;
import org.immutables.value.Value;

import java.util.List;

/**
 * Data type for the Kpack
 */
@JsonDeserialize
@Value.Immutable
abstract class _KpackData implements LifecycleData {

    /**
     * The buildpack
     */
    @JsonProperty("buildpacks")
    @Nullable
    abstract List<String> getBuildpacks();

}