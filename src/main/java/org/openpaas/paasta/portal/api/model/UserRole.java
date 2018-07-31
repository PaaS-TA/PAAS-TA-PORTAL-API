package org.openpaas.paasta.portal.api.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;


public class UserRole implements Comparable<UserRole> {
    private final String userId;
    private final String userEmail;
    private final Set<String> roles;
    private final boolean modifiableRoles;

    private UserRole ( UserRole.Builder builder) {
        this.userId = builder.userId;
        this.userEmail = builder.userEmail;
        this.modifiableRoles = builder.modifiableRoles;
        if (this.modifiableRoles) {
            if (builder.roles == null)
                this.roles = Collections.EMPTY_SET;
            else
                this.roles = builder.roles;
        } else {
            if ( builder.roles == null )
                this.roles = createUnmodifiableSet( null );
            else
                this.roles = createUnmodifiableSet( builder.roles );
        }
    }

    @JsonProperty("user_id")
    public String getUserId() { return this.userId; }

    @JsonProperty("user_email")
    public String getUserEmail() { return this.userEmail; }

    @JsonProperty("roles")
    public Set<String> getRoles () { return this.roles; }

    public boolean addRole(String argRole) {
        Objects.requireNonNull( argRole, "Role" );
        if ( !modifiableRoles )
            throw new RuntimeException( new IllegalAccessException( "Roles' set in UserRole cannot add new role." ) );
        return this.roles.add( argRole );
    }

    @Override
    public boolean equals ( Object other ) {
        if (this == other)
            return true;

        return other instanceof UserRole && this.compareTo( (UserRole ) other ) == 0;
    }

    @Override
    public int compareTo ( UserRole other ) {
        final int compareEmail = this.getUserEmail().compareTo( other.getUserEmail() );
        if (compareEmail != 0)
            return compareEmail;

        return this.getUserId().compareTo( other.getUserId() );
    }

    @Override
    public int hashCode () {
        int generateHash = 9239;
        generateHash += (generateHash << 5) + Objects.hashCode(userEmail);
        generateHash += (generateHash << 5) + Objects.hashCode(userId);

        return generateHash;
    }

    @Override
    public String toString () {
        StringBuilder builder = new StringBuilder( UserRole.class.getSimpleName() );
        builder.append( "{" )
            .append( "userEmail=" ).append( userEmail )
            .append( ", userId=" ).append( userId )
            .append( ", roles=[" );
        boolean first = true;
        for ( String role : roles ) {
            if ( first ) {
                first = !first;
                builder.append( role );
            } else {
                builder.append( "," ).append( role );
            }
        }
        builder.append( "]}" );
        return builder.toString();
    }

    private static <T> Set<T> createUnmodifiableSet ( Set<T> innerSet ) {
        if ( innerSet == null )
            return Collections.unmodifiableSet( Collections.emptySet() );
        else
            return Collections.unmodifiableSet( innerSet );
    }

    public static UserRole.Builder builder() {
        return new UserRole.Builder();
    }

    public static final class Builder {
        private String userId;
        private String userEmail;
        private Set<String> roles = new HashSet<>();
        private boolean modifiableRoles = false;

        public final Builder from(UserRole instance) {
            Objects.requireNonNull( instance, "instance" );

            final String localUserId = instance.getUserId();
            final String localUserEmail = instance.getUserEmail();
            final Set<String> localRoles = instance.getRoles();
            Objects.requireNonNull( userId, "userId" );
            Objects.requireNonNull( userEmail, "userEmail" );

            userId(localUserId);
            userEmail(localUserEmail);
            roles(localRoles);

            return this;
        }

        @JsonProperty("user_id")
        public final Builder userId(String argId) {
            this.userId = argId;
            return this;
        }

        @JsonProperty("user_email")
        public final Builder userEmail(String argEmail) {
            this.userEmail = argEmail;
            return this;
        }

        @JsonProperty("roles")
        public final Builder roles(Collection<String> argRoles) {
            if (argRoles != null && argRoles.size() > 0) {
                this.roles.addAll( argRoles );
            }
            return this;
        }

        public final Builder modifiableRoles(boolean argModifiableRoles) {
            this.modifiableRoles = argModifiableRoles;
            return this;
        }

        public final Builder rolesFromArray(String[] argRoles) {
            if (this.roles == null)
                this.roles = new HashSet<>();
            Collections.addAll( roles, argRoles );
            return this;
        }

        public final Builder addRole(String argRole) {
            if (argRole != null && "".equals( argRole.trim() ) == false)
                this.roles.add( argRole );
            return this;
        }

        public UserRole build() {
            return new UserRole(this );
        };
    }

    @JsonDeserialize
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
    static final class Json {
        String userId;
        String userEmail;
        Set<String> roles;

        @JsonProperty("user_id")
        public void setUserId(String argUserId) {
            this.userId = argUserId;
        }

        @JsonProperty("user_email")
        public void setUserEmail(String argUserEmail) {
            this.userEmail = argUserEmail;
        }

        /*
        @JsonProperty("roles")
        public void setRoles(String argRoles) {
            this.roles = new HashSet<String>();
            this.roles.addAll( Arrays.asList( argRoles.split( "," ) ) );
        }
        */
        @JsonProperty("roles")
        public void setRoles(String[] argRoles) {
            this.roles = new HashSet<>();
            this.roles.addAll( Arrays.asList( argRoles ) );
        }

        public String getUserId () {
            return userId;
        }

        public String getUserEmail () {
            return userEmail;
        }

        public Set<String> getRoles () {
            return roles;
        }
    }

    /**
     * Create UserRole from Json object. Do not use this method directly. Use the Jackson binding.
     * @param json JSON binding data
     * @return An immutable UserRole value
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    static UserRole fromJson(Json json) {
        final UserRole.Builder builder = UserRole.builder();
        return builder.userId( json.getUserId() )
            .userEmail( json.getUserEmail() )
            .roles( json.getRoles() )
            .build();
    }

    public static final class RequestBody {
        private String userId;
        private String role;

        public RequestBody() {
            // empty
        }

        public String getUserId () {
            return userId;
        }

        public String getRole () {
            return role;
        }

        public void setUserId ( String userId ) {
            this.userId = userId;
        }

        public void setRole ( String role ) {
            this.role = role;
        }
    }
}
