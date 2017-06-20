package com.angbot.service;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.angbot.commands.CommCommand;
import com.angbot.common.NaverRestTemplate;
import com.angbot.common.SlackRestTemplate;
import com.angbot.domain.User;
import com.angbot.repository.UserRepository;
import com.angbot.slack.dto.ApiChannelDto;
import com.angbot.slack.dto.ApiPresenceDto;
import com.angbot.slack.dto.ApiUserDto;
import com.angbot.slack.object.Channel;
import com.angbot.slack.object.SUser;
import com.angbot.spac.SlackSpecification;
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

	public String userList() {
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

		return PrintToSlackUtil.printUser(list);
	}

	public String searchImage(StringTokenizer token) {
		String msg = "";

		try {
			String query = "";

			while (token.hasMoreTokens()) {
				query += token.nextToken();
				if (token.countTokens() > 0)
					query += " ";
			}

			Map<String, Object> param = Maps.newConcurrentMap();
			param.put("query", query);

			String result = naverRestTemplate.getApiCaller(CodeNaver.GET_IMAGE.getUrl(), param);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> map = Maps.newConcurrentMap();
			map = om.readValue(result, Map.class);

			if (map.get("items") != null && ((List) map.get("items")).size() > 0) {
				msg = PrintToSlackUtil.printImage((List<Map<String, String>>) map.get("items"));
			} else {
				msg = "`검색 결과가 없습니다.`";
			}

		} catch (Exception e) {
			// TODO 예외 확인 필요.
			e.printStackTrace();
		}

		return msg;
	}

	public String searchBlog(StringTokenizer token) {
		String msg = "";

		try {
			String query = "";

			while (token.hasMoreTokens()) {
				query += token.nextToken();
				if (token.countTokens() > 0)
					query += " ";
			}

			Map<String, Object> param = Maps.newConcurrentMap();
			param.put("query", query);

			String result = naverRestTemplate.getApiCaller(CodeNaver.GET_BLOG.getUrl(), param);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> map = Maps.newConcurrentMap();
			map = om.readValue(result, Map.class);

			if (map.get("items") != null && ((List) map.get("items")).size() > 0) {
				msg = PrintToSlackUtil.printBlog((List<Map<String, String>>) map.get("items"));
			} else {
				msg = "`검색 결과가 없습니다.`";
			}

		} catch (Exception e) {
			// TODO 예외 확인 필요.
			e.printStackTrace();
		}

		return msg;
	}

	public String searchDocument(StringTokenizer token) {
		String msg = "";

		try {
			String query = "";

			while (token.hasMoreTokens()) {
				query += token.nextToken();
				if (token.countTokens() > 0)
					query += " ";
			}

			Map<String, Object> param = Maps.newConcurrentMap();
			param.put("query", query);

			String result = naverRestTemplate.getApiCaller(CodeNaver.GET_DOCUMENT.getUrl(), param);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> map = Maps.newConcurrentMap();
			map = om.readValue(result, Map.class);

			if (map.get("items") != null && ((List) map.get("items")).size() > 0) {
				msg = PrintToSlackUtil.printDocument((List<Map<String, String>>) map.get("items"));
			} else {
				msg = "`검색 결과가 없습니다.`";
			}

		} catch (Exception e) {
			// TODO 예외 확인 필요.
			e.printStackTrace();
		}

		return msg;
	}

	public String searchCafe(StringTokenizer token) {
		String msg = "";

		try {
			String query = "";

			while (token.hasMoreTokens()) {
				query += token.nextToken();
				if (token.countTokens() > 0)
					query += " ";
			}

			Map<String, Object> param = Maps.newConcurrentMap();
			param.put("query", query);

			String result = naverRestTemplate.getApiCaller(CodeNaver.GET_CAFE.getUrl(), param);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> map = Maps.newConcurrentMap();
			map = om.readValue(result, Map.class);

			if (map.get("items") != null && ((List) map.get("items")).size() > 0) {
				msg = PrintToSlackUtil.printCafe((List<Map<String, String>>) map.get("items"));
			} else {
				msg = "`검색 결과가 없습니다.`";
			}

		} catch (Exception e) {
			// TODO 예외 확인 필요.
			e.printStackTrace();
		}

		return msg;
	}

	public String searchMap(StringTokenizer token) {
		String msg = "";

		try {
			String query = "";

			while (token.hasMoreTokens()) {
				query += token.nextToken();
				if (token.countTokens() > 0)
					query += " ";
			}

			Map<String, Object> param = Maps.newConcurrentMap();
			param.put("query", query);

			String result = naverRestTemplate.getApiCaller(CodeNaver.GET_MAP.getUrl(), param);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> map = Maps.newConcurrentMap();
			map = om.readValue(result, Map.class);

			if (map.get("items") != null && ((List) map.get("items")).size() > 0) {
				msg = PrintToSlackUtil.printMap((List<Map<String, String>>) map.get("items"));
			} else {
				msg = "`검색 결과가 없습니다.`";
			}

		} catch (Exception e) {
			// TODO 예외 확인 필요.
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
			subject = channel.getTopic().getValue() != null && !channel.getTopic().getValue().equals("")
					? channel.getTopic().getValue() : channel.getPurpose().getValue();
			channel.setSubject(subject);
		}

		// channel member sort desc
		chanDto.getResponseItem().sort(
				(Channel x, Channel y) -> Integer.valueOf(y.getNum_members()) - Integer.valueOf(x.getNum_members()));

		return PrintToSlackUtil.printChannel(chanDto).toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initCmd() {
		System.out.println("init");
		CommCommand cmd = null;
		try {
			for (Class clazz : SlackCmdCache.registerdCommands) {
				Constructor cons = clazz.getConstructor(new Class[] { CommandApiService.class });
				cmd = (CommCommand) cons.newInstance(this);
				SlackCmdCache.cmdMap.put(cmd.command(), cmd);
			}
		} catch (Exception e) {
			// TODO 예외 확인 필요
			e.printStackTrace();
		}
	}

}