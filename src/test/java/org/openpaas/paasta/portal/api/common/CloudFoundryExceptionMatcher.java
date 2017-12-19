package org.openpaas.paasta.portal.api.common;

import org.cloudfoundry.client.lib.CloudFoundryException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.http.HttpStatus;

/**
 * Created by Dojun on 2016-07-18.
 */
public class CloudFoundryExceptionMatcher extends TypeSafeMatcher<CloudFoundryException> {

    private final HttpStatus expectedStatus;
    private final String expectedDescription;

    public CloudFoundryExceptionMatcher(HttpStatus expectedStatus, String expectedDescription) {
        this.expectedStatus = expectedStatus;
        this.expectedDescription = expectedDescription;
    }

    @Override
    protected boolean matchesSafely(CloudFoundryException item) {
        return item.getStatusCode().equals(expectedStatus)
                && item.getDescription().equals(expectedDescription);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("expects CloudFoundry Message ")
                .appendValue(expectedDescription);
    }
}

