package org.openpaas.paasta.portal.api.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.cloudfoundry.client.lib.CloudFoundryException;
import org.cloudfoundry.identity.uaa.error.UaaException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openpaas.paasta.portal.api.controller.AppController;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.expression.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.http.HTTPException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Locale;

/**
 * org.openpaas.paasta.portal.api.exception
 *
 * @author 김도준
 * @version 1.0
 * @since 2016.07.07
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    private  static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @Autowired
    public MessageSource messageSource;


    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseBody
    public boolean handleCloudFoundryException(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
        LOGGER.error("IllegalArgumentException : " + ex );
        String msg = "";
        if (ex.getMessage().contains("Organization") &&  ex.getMessage().contains("not found")) {
            msg = messageSource.getMessage("Organization_not_found", null, Locale.KOREA);
        } else if (ex.getMessage().contains("Domain not found for URI")) {
            msg = messageSource.getMessage("Domain_not_found_for_URI", null, Locale.KOREA);
        } else if (ex.getMessage().contains("No matching organization and space found for org")) {
            msg = messageSource.getMessage("No_matching_organization_and_space_found_for_org", null, Locale.KOREA);
        } else if(ex.getMessage().contains("Host") && ex.getMessage().contains("not found for domain")){
            msg = messageSource.getMessage("Host_not_found_for_domain",null, Locale.KOREA);
        } else{
            msg = messageSource.getMessage(HttpStatus.BAD_REQUEST.toString(), null, Locale.KOREA);
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
        return false;
    }




    @ExceptionHandler({CloudFoundryException.class})
    @ResponseBody
    public boolean handleCloudFoundryException(CloudFoundryException ex, HttpServletResponse response) throws Exception {

        LOGGER.error("CloudFoundryException : " + ex );

        String[] message;
        String msg;
        try {
            message = ex.getDescription().replace(" ", "_").split(":");
            LOGGER.error("message : " + message[0] );
            msg = messageSource.getMessage(message[0], null, Locale.KOREA);
        }catch(Exception e){
            msg = messageSource.getMessage(HttpStatus.BAD_REQUEST.toString(), null, Locale.KOREA);
        }

        response.sendError(ex.getStatusCode().value(), msg);
        return false;
    }


    @ExceptionHandler({UaaException.class})
    @ResponseBody
    public boolean handleUaaException(UaaException ex, HttpServletResponse response) throws Exception {

        LOGGER.error("UaaException : " + ex );

        String[] message;
        String msg;
        try {
            message = ex.getMessage().replace(" ", "_").split(":");
            LOGGER.error("message : " + message[0] );
            msg = messageSource.getMessage(message[0], null, Locale.KOREA);
        }catch(Exception e){
            msg = messageSource.getMessage(HttpStatus.BAD_REQUEST.toString(), null, Locale.KOREA);
        }

        response.sendError(ex.getHttpStatus(), msg);
        return false;
    }


    @ResponseBody
    public boolean handleUnauthenticationException(Exception e,HttpServletResponse response ) throws IOException {
        return errorResponse(e, HttpStatus.BAD_REQUEST, response);
    }



    @ExceptionHandler({DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
    @ResponseBody
    public boolean handleConflictException(Exception e ,HttpServletResponse response ) throws IOException {
        return errorResponse(e, HttpStatus.CONFLICT, response);
    }



    @ExceptionHandler({SQLException.class, DataAccessException.class, RuntimeException.class, PSQLException.class})
    @ResponseBody
    public boolean handleSQLException(Exception e, HttpServletResponse response) throws IOException {
        return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, response);
    }



    @ExceptionHandler({Exception.class})
    @ResponseBody
    public boolean handleAnyException(Exception e, HttpServletResponse response) throws IOException {
        return errorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR , response);
    }



    //common message
    private boolean errorResponse(Throwable throwable, HttpStatus status, HttpServletResponse response) throws IOException {
        LOGGER.info(response.toString());
        response.sendError(status.value(), messageSource.getMessage(status.toString(), null, Locale.KOREA));

        return false;
    }

}
