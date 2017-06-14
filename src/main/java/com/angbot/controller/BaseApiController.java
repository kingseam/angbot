package com.angbot.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.angbot.util.ApiResDto;

public class BaseApiController {

	private final Logger LOG = LoggerFactory.getLogger(BaseApiController.class);

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)    
    public @ResponseBody Object handleException(Exception exec, HttpServletRequest request, HttpServletResponse response) {     
        if(request.getRequestURL().toString().contains(".json")) {
            response.setContentType("application/json"); 
        } else {        
            String accept = request.getHeader("accept") == null ? "application/json" : request.getHeader("accept");
            if(accept.toLowerCase().equals("application/xml")) {            
                response.setContentType("application/xml");                
            } else {            
                response.setContentType("application/json");
            }
        }
        
        LOG.error("MapiBaseController error, user-agent : {}, uri : {}", request.getHeader("User-Agent"), request.getRequestURI(), exec);
        ApiResDto dto = new ApiResDto();
        dto.setRes_code("500");
        dto.setRes_message("error");

        return dto;
    }

}