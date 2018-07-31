package org.openpaas.paasta.portal.api.exception;

import org.apache.commons.io.IOUtils;
import org.cloudfoundry.client.v2.ClientV2Exception;
import org.cloudfoundry.identity.uaa.error.UaaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;


@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    private static final Locale DEFAULT_LOCALE = Locale.US;
    //private static final Locale DEFAULT_LOCALE = Locale.KOREA;

    @Autowired
    public MessageSource messageSource;


    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseBody
    public boolean handleCloudFoundryException(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
        LOGGER.error("IllegalArgumentException : " + ex);
        String msg = "";
        if (ex.getMessage().contains("Organization") && ex.getMessage().contains("not found")) {
            msg = messageSource.getMessage("Organization_not_found", null, DEFAULT_LOCALE);
        } else if (ex.getMessage().contains("Domain not found for URI")) {
            msg = messageSource.getMessage("Domain_not_found_for_URI", null, DEFAULT_LOCALE);
        } else if (ex.getMessage().contains("No matching organization and space found for org")) {
            msg = messageSource.getMessage("No_matching_organization_and_space_found_for_org", null, DEFAULT_LOCALE);
        } else if (ex.getMessage().contains("Host") && ex.getMessage().contains("not found for domain")) {
            msg = messageSource.getMessage("Host_not_found_for_domain", null, DEFAULT_LOCALE);
        } else {
            msg = messageSource.getMessage(HttpStatus.BAD_REQUEST.toString(), null, DEFAULT_LOCALE);
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        return false;
    }

    @ExceptionHandler({ClientV2Exception.class})
    @ResponseBody
    public void handleClientV2Exception(ClientV2Exception ex, HttpServletResponse response) throws Exception {

        LOGGER.error("CloudFoundryException : " + ex);

        //String[] message;
        String msg;
        try {
            msg = messageSource.getMessage(String.valueOf(ex.getCode()), null, "Not Found Message.", response.getLocale());
        } catch (Exception e) {
            msg = getStackTraceString(e);
        }

        String message = "{\"message\":\"" + msg + "\"" + ",\"status\":\"" + HttpServletResponse.SC_BAD_REQUEST + "\"" + ",\"code\":\"" + ex.getCode() + "\"}";

        response.resetBuffer();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().print(message);
        response.flushBuffer();
    }

    @ExceptionHandler({UaaException.class})
    @ResponseBody
    public boolean handleUaaException(UaaException ex, HttpServletResponse response) throws Exception {

        LOGGER.error("UaaException : " + ex);

        String[] message;
        String msg;
        try {
            message = ex.getMessage().replace(" ", "_").split(":");
            LOGGER.error("message : " + message[0]);
            msg = messageSource.getMessage(message[0], null, DEFAULT_LOCALE);
        } catch (Exception e) {
            msg = messageSource.getMessage(HttpStatus.BAD_REQUEST.toString(), null, DEFAULT_LOCALE);
        }

        response.sendError(ex.getHttpStatus(), msg);
        return false;
    }


    @ResponseBody
    public boolean handleUnauthenticationException(Exception e, HttpServletResponse response) throws IOException {
        return errorResponse(e, HttpStatus.BAD_REQUEST, response);
    }


    @ExceptionHandler({Exception.class})
    @ResponseBody
    public boolean handleAnyException(Exception e, HttpServletResponse response) throws IOException {
        return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    private String getStackTraceString(Throwable t) {
        final StringWriter swriter = new StringWriter();
        final PrintWriter pwriter = new PrintWriter(swriter);
        t.printStackTrace(pwriter);
        pwriter.flush();

        final String result = swriter.toString();
        IOUtils.closeQuietly(pwriter);
        IOUtils.closeQuietly(swriter);

        return result;
    }

    //common message
    private boolean errorResponse(Throwable throwable, HttpStatus status, HttpServletResponse response) throws IOException {
        LOGGER.info(response.toString());
        //response.sendError(status.value(), messageSource.getMessage(status.toString(), null, DEFAULT_LOCALE));
        final StringBuffer buffer = new StringBuffer();
        buffer.append("Response : ").append(response.toString()).append('\n').append("Occured an exception : ").append(throwable.getMessage()).append('\n').append("Caused by... ").append('\n').append(getStackTraceString(throwable));


        response.sendError(status.value(), buffer.toString());
        LOGGER.error("Http status : {}", status.value());
        LOGGER.error("Error message : {}", buffer.toString());

        return false;
    }

}

