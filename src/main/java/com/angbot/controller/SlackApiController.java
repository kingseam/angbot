package com.angbot.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.angbot.common.SlackRestTemplate;
import com.angbot.domain.User;
import com.angbot.repository.UserRepository;
import com.angbot.slack.dto.ApiUserDto;
import com.angbot.slack.object.SUser;
import com.angbot.util.BizException;
import com.angbot.util.CodeS;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/api/slack")
public class SlackApiController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	SlackRestTemplate slackRestTemplate;

	@Value("${slack.api.token}")
	private String token;

	public static final Logger LOG = LoggerFactory.getLogger(SlackApiController.class);

	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public ResponseEntity<?> listUsers() {		
		Map<String, Object> param = Maps.newConcurrentMap();
		param.put("token", token);
		param.put("pretty", 1);		
		
		ApiUserDto userDto = new ApiUserDto();
		userDto = slackRestTemplate.getApiCaller(CodeS.GET_USERS.getUrl(), userDto.getClass(), param);
		
		if(userDto.isResult()){
			User user = new User();
			for(SUser sUser : userDto.getResponseItem()){
				user = new User(sUser);				
				if(!userRepository.exists(sUser.getId())){
					userRepository.save(user);
				}
			}			
		}else{
			return new ResponseEntity<BizException>(new BizException(CodeS.S_E001), HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<String>("slack api ok", HttpStatus.CREATED);
	}

}