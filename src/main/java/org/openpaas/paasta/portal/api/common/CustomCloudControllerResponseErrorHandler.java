package org.openpaas.paasta.portal.api.common;

import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.client.lib.NotFinishedStagingException;
import org.cloudfoundry.client.lib.StagingErrorException;
import org.cloudfoundry.client.lib.util.CloudUtil;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.openpaas.paasta.portal.api.service.CatalogService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Map;


/**
 * Created by mg on 2016-06-23.
 */
//NOSONAR
public class CustomCloudControllerResponseErrorHandler extends DefaultResponseErrorHandler {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CatalogService.class);

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();
        switch (statusCode.series()) {
            case CLIENT_ERROR:
                throw getException(response);
            case SERVER_ERROR:
                throw getException(response);
            default:
                throw new RestClientException("Unknown status code [" + statusCode + "]");
        }
    }

    //NOSONAR
    private static CloudFoundryException getException(ClientHttpResponse response) throws IOException {
        HttpStatus statusCode = response.getStatusCode();
        CloudFoundryException cloudFoundryException = null;

        String description = "Client error";
        String statusText = response.getStatusText();

        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally

        if (response.getBody() != null) {
            try {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = mapper.readValue(response.getBody(), Map.class);
                description = CloudUtil.parse(String.class, map.get("description"));
                if ("".equals(description) || description==null) {
                    description = CloudUtil.parse(String.class, map.get("error_description"));
                }

                int cloudFoundryErrorCode = CloudUtil.parse(Integer.class, map.get("code"));

                if (cloudFoundryErrorCode >= 0) {
                    switch (cloudFoundryErrorCode) {
                        case StagingErrorException.ERROR_CODE:
                            cloudFoundryException = new StagingErrorException(
                                    statusCode, statusText);
                            break;
                        case NotFinishedStagingException.ERROR_CODE:
                            cloudFoundryException = new NotFinishedStagingException(
                                    statusCode, statusText);
                            break;
                        default:
                            break;
                    }
                }
            } catch (JsonParseException e) {
                System.out.println(e);
                // Fall through. Handled below.
            } catch (IOException e) {
                System.out.println(e);
                // Fall through. Handled below.
            }
        }

        if (cloudFoundryException == null) {
            cloudFoundryException = new CloudFoundryException(statusCode,
                    statusText);
        }
        cloudFoundryException.setDescription(description);

        return cloudFoundryException;
    }
}
