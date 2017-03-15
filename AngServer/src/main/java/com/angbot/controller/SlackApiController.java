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
import com.angbot.util.CodeS;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;

@RestController
@RequestMapping("/api/slack")
public class SlackApiController {

	@Autowired
	UserRepository angbotRepository;

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
		
		//TODO User Class 부터 만들고 호출 . 현재 slack json model 확인중 
		slackRestTemplate.getApiCaller(CodeS.GET_USERS.getUrl(), User.class, param);

		return new ResponseEntity<String>("ok", HttpStatus.CREATED);
	}

}