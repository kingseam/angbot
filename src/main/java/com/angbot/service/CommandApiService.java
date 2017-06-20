package com.angbot.service;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.angbot.commands.CommCommand;
import com.angbot.commands.HelpCommand;
import com.angbot.commands.NaverMapCommand;
import com.angbot.commands.SlackChannelCommand;
import com.angbot.commands.SlackUserCommand;
import com.angbot.common.BaseRestTemplate;
import com.angbot.common.JsonResponseHandler;
import com.angbot.common.NaverRestTemplate;
import com.angbot.common.SlackRestTemplate;
import com.angbot.common.WebsocketClientEndpoint;
import com.angbot.controller.SlackApiController;
import com.angbot.domain.User;
import com.angbot.repository.UserRepository;
import com.angbot.slack.dto.ApiChannelDto;
import com.angbot.slack.dto.ApiPresenceDto;
import com.angbot.slack.dto.ApiUserDto;
import com.angbot.slack.object.Channel;
import com.angbot.slack.object.SUser;
import com.angbot.spac.SlackSpecification;
import com.angbot.util.ApiResDto;
import com.angbot.util.CodeNaver;
import com.angbot.util.CodeSlack;
import com.angbot.util.PrintToSlackUtil;
import com.google.common.collect.Maps;


@Service
public class CommandApiService {	
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	SlackRestTemplate slackRestTemplate;
	
	@Autowired
	NaverRestTemplate naverRestTemplate;

	@Value("${slack.api.token}")
	private String token;

	public static final Logger LOG = LoggerFactory.getLogger(CommandApiService.class);
	
	public String searchMap(StringTokenizer token){		
		String msg = "";
		
		try{
			String query = "";
			
			while(token.hasMoreTokens()){
				query += token.nextToken();
				if(token.countTokens() > 0) query += " ";
			}
			
			Map<String, Object> param = Maps.newConcurrentMap();
			param.put("query", query);
			
			String result = naverRestTemplate.getApiCaller(CodeNaver.GET_MAP.getUrl(), param);
			
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> map = Maps.newConcurrentMap();
			map = om.readValue(result, Map.class);
			
			if(map.get("items") != null && ((List)map.get("items")).size() > 0){
				msg = PrintToSlackUtil.printMap((List<Map<String,String>>)map.get("items"));
			}
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return msg;
	}
	
	public String channelList() {
		/* Set Slack User Info Param */
		boolean isCreator = false;
		Map<String, Object> param = Maps.newConcurrentMap();
		param.put("token", token);

		ApiChannelDto chanDto = new ApiChannelDto();
		chanDto = slackRestTemplate.getApiCaller(CodeSlack.GET_CHANEELS.getUrl(), chanDto.getClass(), param);

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
			subject = channel.getTopic().getValue() != null && !channel.getTopic().getValue().equals("") ? channel.getTopic().getValue() : channel.getPurpose().getValue();
			channel.setSubject(subject);
		}

		// channel member sort desc
		chanDto.getResponseItem().sort((Channel x, Channel y) -> Integer.valueOf(y.getNum_members()) - Integer.valueOf(x.getNum_members()));

		return PrintToSlackUtil.printChannel(chanDto).toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initCmd(){
		System.out.println("init");
		CommCommand cmd = null;		
		try{
			for(Class clazz : SlackCmdCache.registerdCommands){
				Constructor cons = clazz.getConstructor(new Class[] {CommandApiService.class});
				cmd = (CommCommand) cons.newInstance(this);
				SlackCmdCache.cmdMap.put(cmd.command(), cmd);				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}