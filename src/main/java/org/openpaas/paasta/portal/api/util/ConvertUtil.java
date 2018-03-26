package org.openpaas.paasta.portal.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertUtil {

	private static ObjectMapper objectMapper = new ObjectMapper();
	
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

}

