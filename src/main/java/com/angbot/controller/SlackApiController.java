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

	@Value("${slack.api.token}")
	private String token;

	public static final Logger LOG = LoggerFactory.getLogger(SlackApiController.class);

	@RequestMapping(value = "/user/list", method = RequestMethod.GET)
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
				ApiPresenceDto result = slackRestTemplate.getApiCaller(CodeSlack.GET_Active.getUrl(),
						ApiPresenceDto.class, param);
				userRepository.save(new User(user, result.getPresence()));
			}
		}

		/* Query Active User */
		Specifications<User> specifications = Specifications.where(SlackSpecification.activeUser("active"));
		List<User> list = userRepository.findAll(specifications);

		resDto.getData().put("userList", list);

		return resDto;
	}

	@RequestMapping(value = "/channel/list", method = RequestMethod.GET)
	public @ResponseBody ApiResDto channelList() {
		ApiResDto resDto = new ApiResDto("channelList");

		/* Set Slack User Info Param */
		boolean isCreator = false;
		Map<String, Object> param = Maps.newConcurrentMap();
		param.put("token", token);

		ApiChannelDto chanDto = new ApiChannelDto();
		chanDto = slackRestTemplate.getApiCaller(CodeSlack.GET_CHANEELS.getUrl(), chanDto.getClass(), param);

		LOG.info("channels = {}", chanDto.getResponseItem());

		List<User> list = userRepository.findAll();

		// id 기준 정렬 그냥 심심해서 람다 써봄 ㅋ 의미 없음.
		list.sort((User x, User y) -> x.getId().compareTo(y.getId()));

		for (Channel channel : chanDto.getResponseItem()) {
			for (User user : list) {
				if (channel.getTopic().getCreator().equals(user.getId())) {
					channel.setId(user.getNick());
					isCreator = true;
					break;
				}
				if (channel.getPurpose().getCreator().equals(user.getId())) {
					if (!isCreator) {
						channel.setId(user.getNick());
						break;
					}
				}
			}
			isCreator = false;
			String subject = "";
			subject = channel.getTopic().getValue() != null && !channel.getTopic().getValue().equals("")
					? channel.getTopic().getValue() : channel.getPurpose().getValue();
			channel.setSubject(subject);
		}

		// channel member sort desc
		chanDto.getResponseItem().sort((Channel x, Channel y) -> Integer.valueOf(y.getNum_members()) - Integer.valueOf(x.getNum_members()));

		resDto.getData().put("channelList", chanDto.getResponseItem());

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
				websocket = new WebsocketClientEndpoint.WebsocketClientBuilder().setURI(rtmDto.getUrl()).setServer(new SlackMessageHandler()).build();
				Map<String, String> message = Maps.newConcurrentMap();
				message.put("type", "message");
				message.put("channel", "C2F31LCTZ");
				message.put("text", "`angbot RTM serv start...`");
				
				ObjectMapper om = new ObjectMapper();			
				websocket.sendMessage(om.writeValueAsString(message));
				Thread.sleep(25000);
			}
		}

		return resDto;
	}

	@RequestMapping(value = "/rtm/close", method = RequestMethod.GET)
	public @ResponseBody ApiResDto rtmClose() throws IOException {
		ApiResDto resDto = new ApiResDto("rtmConnect");
		Map<String, String> message = Maps.newConcurrentMap();
		message.put("type", "message");
		message.put("channel", "C2F31LCTZ");
		message.put("text", "`angbot RTM serv close...`");
		
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