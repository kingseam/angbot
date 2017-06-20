package com.angbot.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specifications;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.angbot.common.BaseRestTemplate;
import com.angbot.common.JsonResponseHandler;
import com.angbot.common.SlackMessageHandler;
import com.angbot.common.SlackRestTemplate;
import com.angbot.common.WebsocketClientEndpoint;
import com.angbot.domain.User;
import com.angbot.repository.UserRepository;
import com.angbot.service.CommandApiService;
import com.angbot.slack.dto.ApiChannelDto;
import com.angbot.slack.dto.ApiPresenceDto;
import com.angbot.slack.dto.ApiRealTimeMessageDto;
import com.angbot.slack.dto.ApiUserDto;
import com.angbot.slack.object.Channel;
import com.angbot.slack.object.SUser;
import com.angbot.spac.SlackSpecification;
import com.angbot.util.ApiResDto;
import com.angbot.util.CodeSlack;
import com.google.common.collect.Maps;
import org.codehaus.jackson.map.ObjectMapper;

@RestController
@RequestMapping("/api/slack")
public class SlackApiController extends BaseApiController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	BaseRestTemplate bestRestTemplate;

	@Autowired
	SlackRestTemplate slackRestTemplate;

	WebsocketClientEndpoint websocket;
	
	@Autowired
	JsonResponseHandler jsonHandler;
	
	@Autowired
	CommandApiService slackCommService;

	@Value("${slack.api.token}")
	private String token;

	public static final Logger LOG = LoggerFactory.getLogger(SlackApiController.class);

	//@RequestMapping(value = "/user/list", method = RequestMethod.GET)
	public @ResponseBody ApiResDto activeUser() {
		ApiResDto resDto = new ApiResDto("activeUser");

		/* Set Slack User Info Param */
		Map<String, Object> param = Maps.newConcurrentMap();
		param.put("token", token);
		param.put("pretty", 1);

		ApiUserDto userDto = new ApiUserDto();
		userDto = slackRestTemplate.getApiCaller(CodeSlack.GET_USERS.getUrl(), userDto.getClass(), param);

		/* Response Api */
		if (userDto.isResult()) {
			User user = new User();
			for (SUser sUser : userDto.getResponseItem()) {
				user = new User(sUser);
				param.put("user", user.getId());
				ApiPresenceDto result = slackRestTemplate.getApiCaller(CodeSlack.GET_Active.getUrl(), ApiPresenceDto.class, param);
				userRepository.save(new User(user, result.getPresence()));
			}
		}

		/* Query Active User */
		Specifications<User> specifications = Specifications.where(SlackSpecification.activeUser("active"));
		List<User> list = userRepository.findAll(specifications);

		resDto.getData().put("userList", list);

		return resDto;
	}

	@RequestMapping(value = "/rtm/connect", method = RequestMethod.GET)
	public @ResponseBody ApiResDto rtmConnect() throws Exception {
		ApiResDto resDto = new ApiResDto("rtmConnect");
		ApiRealTimeMessageDto rtmDto = new ApiRealTimeMessageDto();

		/* Set Slack User Info Param */
		Map<String, Object> param = Maps.newConcurrentMap();
		param.put("token", token);
		param.put("pretty", 1);		
		if(websocket == null || websocket.userSession == null){
			rtmDto = slackRestTemplate.getApiCaller(CodeSlack.GET_RTMSTART.getUrl(), rtmDto.getClass(), param);
			if (rtmDto.isResult()) {
				slackCommService.initCmd();
				websocket = new WebsocketClientEndpoint.WebsocketClientBuilder().setURI(rtmDto.getUrl()).setServer(new SlackMessageHandler()).build();
				Map<String, String> message = Maps.newConcurrentMap();
				message.put("type", "message");
				message.put("channel", "C2F31LCTZ");
				message.put("text", "`angbot RTM serv start...`");
				
				ObjectMapper om = new ObjectMapper();			
			//	websocket.sendMessage(om.writeValueAsString(message));
				Thread.sleep(25000);
			}
		}else{
			slackCommService.initCmd();
		}

		return resDto;
	}

	@RequestMapping(value = "/rtm/close", method = RequestMethod.GET)
	public @ResponseBody ApiResDto rtmClose() throws IOException {
		ApiResDto resDto = new ApiResDto("rtmConnect");
		Map<String, String> message = Maps.newConcurrentMap();
		message.put("type", "message");
		message.put("channel", "C2F31LCTZ");
		//message.put("text", "`angbot RTM serv close...`");
		
		ObjectMapper om = new ObjectMapper();			
		websocket.sendMessage(om.writeValueAsString(message));
		
		if(websocket != null){
			if(websocket.userSession != null){
				websocket.userSession.close();
			}
		}
		
		websocket = null;
		return resDto;
	}

}