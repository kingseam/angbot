package com.angbot.controller;

import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.angbot.common.BaseRestTemplate;
import com.angbot.common.JsonResponseHandler;
import com.angbot.common.SlackMessageHandler;
import com.angbot.common.SlackRestTemplate;
import com.angbot.common.WebsocketClientEndpoint;
import com.angbot.repository.UserRepository;
import com.angbot.service.CommandApiService;
import com.angbot.slack.dto.ApiRealTimeMessageDto;
import com.angbot.util.ApiResDto;
import com.angbot.util.CodeSlack;
import com.google.common.collect.Maps;

@RestController
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

	@RequestMapping(value = {"/", "/rtm/connect"}, method = {RequestMethod.GET, RequestMethod.POST})
	public @ResponseBody ApiResDto rtmConnect() throws Exception {
		ApiResDto resDto = new ApiResDto("rtmConnect");
		ApiRealTimeMessageDto rtmDto = new ApiRealTimeMessageDto();

		/* Set Slack User Info Param */
		Map<String, Object> param = Maps.newConcurrentMap();
		param.put("token", token);
		param.put("pretty", 1);
		if (websocket == null || websocket.userSession == null) {
			rtmDto = slackRestTemplate.getApiCaller(CodeSlack.GET_RTMSTART.getUrl(), rtmDto.getClass(), param);
			if (rtmDto.isResult()) {
				slackCommService.initCmd();
				websocket = new WebsocketClientEndpoint.WebsocketClientBuilder().setURI(rtmDto.getUrl())
						.setServer(new SlackMessageHandler()).build();
				Map<String, String> message = Maps.newConcurrentMap();
				message.put("type", "message");
				message.put("channel", "C2F31LCTZ");
				message.put("text", "`angbot RTM serv start...`");

				slackCommService.initUser();
				ObjectMapper om = new ObjectMapper();
				// websocket.sendMessage(om.writeValueAsString(message));
				Thread.sleep(25000);
			}
		} else {
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
		// message.put("text", "`angbot RTM serv close...`");

		ObjectMapper om = new ObjectMapper();
		websocket.sendMessage(om.writeValueAsString(message));

		if (websocket != null) {
			if (websocket.userSession != null) {
				websocket.userSession.close();
			}
		}

		websocket = null;
		return resDto;
	}

	@RequestMapping(value = "/user/init", method = RequestMethod.GET)
	public @ResponseBody ApiResDto userInit() throws IOException {
		ApiResDto resDto = new ApiResDto("rtmConnect");

		slackCommService.initUser();

		return resDto;
	}

}