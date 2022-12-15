package org.cloudfoundry.client.v3;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

/**
 * The lifecycle type
 */
@JsonDeserialize
@Value.Immutable
abstract class _Lifecycle {

    /**
     * The data for the lifecycle
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(name = "buildpack", value = BuildpackData.class),
        @JsonSubTypes.Type(name = "docker", value = DockerData.class),
        @JsonSubTypes.Type(name = "kpack", value = KpackData.class)
    })
    @JsonProperty("data")
    abstract LifecycleData getData();

    /**
     * The type
     */
    @JsonProperty("type")
    abstract LifecycleType getType();

}