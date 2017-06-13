package com.angbot.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.angbot.common.BaseRestTemplate;
import com.angbot.common.SlackRestTemplate;
import com.angbot.domain.User;
import com.angbot.repository.UserRepository;
import com.angbot.slack.dto.ApiPresenceDto;
import com.angbot.slack.dto.ApiUserDto;
import com.angbot.slack.object.SUser;
import com.angbot.spac.SlackSpecification;
import com.angbot.util.ApiResDto;
import com.angbot.util.BizException;
import com.angbot.util.CodeS;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@RestController
@RequestMapping("/api/slack")
public class SlackApiController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BaseRestTemplate bestRestTemplate;
	
	@Autowired
	SlackRestTemplate slackRestTemplate;

	@Value("${slack.api.token}")
	private String token;

	public static final Logger LOG = LoggerFactory.getLogger(SlackApiController.class);
	
	@RequestMapping(value = "/user/list", method = RequestMethod.GET)
	public @ResponseBody ApiResDto activeUser() {		
		Map<String, Object> param = Maps.newConcurrentMap();
		param.put("token", token);
		param.put("pretty", 1);
		
		ApiUserDto userDto = new ApiUserDto();
		userDto = slackRestTemplate.getApiCaller(CodeS.GET_USERS.getUrl(), userDto.getClass(), param);
		
		if(userDto.isResult()){
			User user = new User();
			for(SUser sUser : userDto.getResponseItem()){
				user = new User(sUser);				
				param.put("user", user.getId());
				ApiPresenceDto result = slackRestTemplate.getApiCaller(CodeS.GET_Active.getUrl(), ApiPresenceDto.class, param);
				userRepository.save( new User(user, result.getPresence()));
			}			
		}
		
		Specifications<User> specifications = Specifications.where(SlackSpecification.activeUser("active"));
		List<User> list = userRepository.findAll(specifications);
		
		ApiResDto resDto  = new ApiResDto("activeUser");
		resDto.getData().put("userList", list);		
		
		return resDto;
	}

}