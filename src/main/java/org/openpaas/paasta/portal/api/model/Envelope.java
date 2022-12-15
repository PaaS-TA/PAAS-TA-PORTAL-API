package org.openpaas.paasta.portal.api.model;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class Envelope {
    private String instance_id;

    private Log log;

    private String source_id;

    Map<String, String> tags;

    private Long timestamp;

    Map<String, String> deprecated_tags;

    public class Log{
        public String getPayloadAsText(String payload) {
            final byte[] decodedPayload = Base64.getDecoder().decode(payload);
            return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(decodedPayload)).toString();
        }
        private String payload;

        private LogType type;

        public void setPayload(String payload) {
            this.payload = getPayloadAsText(payload);
        }

        public String getPayload() {
            return payload;
        }

        public LogType getType() {
            return type;
        }

        public void setType(LogType type) {
            this.type = type;
        }
    }


    public enum LogType {

        ERR("ERR"),

        OUT("OUT");

        private final String value;

        LogType(String value) {
            this.value = value;
        }

        public static LogType from(String s) {
            switch (s.toLowerCase()) {
                case "err":
                    return ERR;
                case "out":
                    return OUT;
                default:
                    throw new IllegalArgumentException(String.format("Unknown log type: %s", s));
            }
        }
        public String getValue() {
            return this.value;
        }
        public String toString() {
            return getValue();
        }


    }

    public String getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(String instance_id) {
        this.instance_id = instance_id;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getDeprecated_tags() {
        return deprecated_tags;
    }

    public void setDeprecated_tags(Map<String, String> deprecated_tags) {
        this.deprecated_tags = deprecated_tags;
    }

    @Override
    public String toString() {
        return "Envelope{" +
                "instance_id='" + instance_id + '\'' +
                ", log=" + log +
                ", source_id='" + source_id + '\'' +
                ", tags=" + tags +
                ", timestamp=" + timestamp +
                ", deprecated_tags=" + deprecated_tags +
                '}';
    }
}
