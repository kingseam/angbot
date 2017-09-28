package com.angbot.service;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.codehaus.jackson.map.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.openkoreantext.processor.util.KoreanPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.angbot.commands.CommCommand;
import com.angbot.common.GitHubRestTemplate;
import com.angbot.common.NaverRestTemplate;
import com.angbot.common.SlackRestTemplate;
import com.angbot.domain.User;
import com.angbot.slack.dto.ApiChannelDto;
import com.angbot.slack.dto.ApiPresenceDto;
import com.angbot.slack.dto.ApiUserDto;
import com.angbot.slack.object.Channel;
import com.angbot.slack.object.SUser;
import com.angbot.util.CodeGitHub;
import com.angbot.util.CodeNaver;
import com.angbot.util.CodeSlack;
import com.angbot.util.PrintToSlackUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import scala.collection.JavaConverters;

@Service
public class CommandApiService {

	@Autowired
	SlackRestTemplate slackRestTemplate;

	@Autowired
	NaverRestTemplate naverRestTemplate;

	@Autowired
	GitHubRestTemplate gitHubRestTemplate;

	@Value("${slack.api.token}")
	private String token;

	public static final Logger LOG = LoggerFactory.getLogger(CommandApiService.class);

	public void initUser() {
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
				user = new User(user, result.getPresence());
				SlackCmdCache.userMap.put(user.getId(), user);
			}
		}

		/* Query Active User */
		//Specifications<User> specifications = Specifications.where(SlackSpecification.activeUser("active"));
		//List<User> list = userRepository.findAll(specifications);
		//List<User> list

		//return "기능 고도화중";//PrintToSlackUtil.printUser(list);
	}

	public String searchRecipe(StringTokenizer tokener) {
		String token = "";
		while (tokener.hasMoreTokens()) {
			token += tokener.nextToken();
			if (tokener.countTokens() > 0)
				token += " ";
		}

		Collection<String> lst = SlackCmdCache.vec.wordsNearest(token.toString().toLowerCase(), 25);
		if(lst.size() < 1){
			CharSequence normalized = OpenKoreanTextProcessorJava.normalize(token.toString().toLowerCase());
		    List<KoreanTokenizer.KoreanToken> tokens = JavaConverters.seqAsJavaList(OpenKoreanTextProcessorJava.tokenize(normalized));
		    for(KoreanTokenizer.KoreanToken tokenk : tokens){
		    	if ( tokenk.pos() == KoreanPos.Noun() ){
			    	lst.addAll(SlackCmdCache.vec.wordsNearest(tokenk.text(), 5));
		    	}
		    }
		}

		List<String> tempList = new ArrayList<String>(lst);

		if(token.toString().toLowerCase().contains("제육")){
			tempList.add(0, token.toString().toLowerCase()+"_돼지고기");
		}
		int j = 0;
		for(int i=0; i<tempList.size(); i++){

			if(tempList.get(i).split("_").length > 1){
				if(token.toString().toLowerCase().contains(tempList.get(i).split("_")[1])){
					if(
							   (token.toString().toLowerCase().contains("김치") && tempList.get(i).split("_")[1].equals("김"))
							|| (token.toString().toLowerCase().contains("떡갈비") && tempList.get(i).split("_")[1].equals("떡"))
							|| ((token.toString().toLowerCase().contains("파스타") || token.toString().toLowerCase().contains("마파")) && tempList.get(i).split("_")[1].equals("파"))
							|| (token.toString().toLowerCase().contains("무침") && !token.toString().toLowerCase().contains("무말랭이") && tempList.get(i).split("_")[1].equals("무"))
							|| (token.toString().toLowerCase().contains("콩나물") && tempList.get(i).split("_")[1].equals("콩"))
							|| (token.toString().toLowerCase().contains("오징어채") && tempList.get(i).split("_")[1].equals("오징어"))
							|| (token.toString().toLowerCase().contains("치킨마요") && tempList.get(i).split("_")[1].equals("마"))
							|| (token.toString().toLowerCase().contains("배추") && tempList.get(i).split("_")[1].equals("배"))
							|| (token.toString().toLowerCase().contains("햄버거") && tempList.get(i).split("_")[1].equals("햄"))
							|| (token.toString().toLowerCase().contains("스파게티") && tempList.get(i).split("_")[1].equals("파"))
					){

					}else{
					    tempList.add(0, tempList.get(i));
						tempList.remove(i+1);
					}
				}
			}else{
				tempList.remove(i);
			}
		}

		if(tempList.size() == 0){
			return token.toString() + ":" + "표본 데이터 너무 적음 - 추천 불가";
		}


		return token.toString() + ":" + tempList.toString();
	}

	public String userList() {
		return PrintToSlackUtil.printUser();
	}

	public String issueList(StringTokenizer token) {
		String msg = "";
		try {
			System.out.println(token.countTokens() );
			String state = "open";
			if(token.countTokens() > 0){
				String query = token.nextToken();
				if(query.equals("전체")){
					state = "all";
				} else if(query.equals("완료")){
					state = "closed";
				}
			}


			Map<String, Object> param = Maps.newConcurrentMap();
			param.put("state", state);

			String result = gitHubRestTemplate.getApiCaller(CodeGitHub.GET_ISSUES.getUrl(), param);

			ObjectMapper om = new ObjectMapper();
			List<Map<String,Object>> list = Lists.newArrayList();
			list = om.readValue(result, List.class);

			msg = PrintToSlackUtil.printIssue(list);
		}catch(Exception e){
			e.printStackTrace();
		}

		return msg;
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

	/**
	 * 날씨 구하기
	 *
	 * @param
	 * @return
	 * @exception @see
	 */
	public String getWeathers(StringTokenizer token) {
		Document doc = null;

		String query = "";

		while (token.hasMoreTokens()) {
			query += token.nextToken();
			if (token.countTokens() > 0)
				query += " ";
		}

		query += "%20날씨";

		try {
			doc = Jsoup.connect(
					"http://m.search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=" + query + "&where=m")
					.get();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (doc != null) {

			Elements weatherElements = doc.select("div.status_bx.type1");
			String weatherText = weatherElements.select("span.u_hc:eq(0)").text();
			String weatherTemperature = weatherElements.select("div.wt_text strong em").text();

			String line = "================\n";

			// timeLine info
			StringBuilder timeLineInfos = new StringBuilder();
			timeLineInfos.append(line);
			Elements weatherSection = doc.select("div.flick-ct div.grap_box");
			if(!weatherSection.isEmpty()) {
				weatherSection.get(0).select("ul>li").forEach(e -> {
					timeLineInfos.append(e.select("strong").text() + "시 " + e.select("span.wt_status").text() + " " + e.select("em").text() + "℃\n");
				});
			} else {
				return "동네를 제대로 입력해주세염 ㅇ(^ㅁ~)ㅇ";
			}
			timeLineInfos.append(line);
			return PrintToSlackUtil.printWeather(weatherText + " " + weatherTemperature + "℃",
					timeLineInfos.toString());
		}
		return null;
	}

	public String channelList() {
		/* Set Slack User Info Param */
		boolean isCreator = false;
		Map<String, Object> param = Maps.newConcurrentMap();
		param.put("token", token);

		ApiChannelDto chanDto = new ApiChannelDto();
		chanDto = slackRestTemplate.getApiCaller(CodeSlack.GET_CHANEELS.getUrl(), chanDto.getClass(), param);

		for (Channel channel : chanDto.getResponseItem()) {
			if (SlackCmdCache.userMap.containsKey(channel.getTopic().getCreator())) {
				channel.setId(SlackCmdCache.userMap.get(channel.getTopic().getCreator()).getNick());
				isCreator = true;
			}
			if (SlackCmdCache.userMap.containsKey(channel.getPurpose().getCreator())) {
				if (!isCreator) {
					channel.setId(SlackCmdCache.userMap.get(channel.getPurpose().getCreator()).getNick());
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