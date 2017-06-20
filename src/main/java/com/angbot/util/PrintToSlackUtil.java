package com.angbot.util;

import java.util.List;
import java.util.Map;

import com.angbot.domain.User;
import com.angbot.slack.dto.ApiBaseDto;
import com.angbot.slack.dto.ApiChannelDto;
import com.angbot.slack.object.Channel;

public class PrintToSlackUtil{
	
	public static String printChannel(ApiChannelDto dtoList){		
		StringBuffer resultMsg = new StringBuffer();
		int rank = 1;
		
		resultMsg.append("```");
		for(Channel dto : dtoList.getResponseItem()){
			resultMsg.append(String.valueOf(rank));
			resultMsg.append("위 : #"+dto.getName());
			resultMsg.append("("+dto.getSubject()+")");
			resultMsg.append("("+dto.getNum_members()+"명)");
			resultMsg.append(""+dto.getId().substring(0, dto.getId().length()-1)+"*");
			resultMsg.append("\n");
			rank++;
		}
		resultMsg.append("```");
		
		return resultMsg.toString();
	}
	

	public static String printHelp(){		
		StringBuffer resultMsg = new StringBuffer();		
		resultMsg.append("```\n !어디야 : 찾고자 하는 명칭의 관련된 위치를 불러온다.");
		resultMsg.append("\n !카페 : 남궁성코드초보스터디 카페 최신글을 가져온다");
		resultMsg.append("\n !검색 : 네이버 검색 중 유사도 높은 1개를 가져온다.");
		resultMsg.append("\n !이미지 : 네이버 유사도 높은 이미지 1개를 가져온다.");
		resultMsg.append("\n !사전 : 네이버 용어 사전 검색");
		resultMsg.append("\n !유저 : 현재 접속 유저 ");
		resultMsg.append("\n !채널 : 채널 목록```");
		return resultMsg.toString();
	}
	
	public static String printMap(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("[");
			resultMsg.append("*"+map.get("title").replaceAll("\\<.*?>","")+"*");
			resultMsg.append("]\n");
			resultMsg.append("https://openapi.naver.com/v1/map/staticmap.bin?clientId=tVMlV0yBN6vVmO7VgF_g&url=http://localhost&center=");
			resultMsg.append(map.get("mapx")+","+map.get("mapy")+"&level=7&w=200&h=200&baselayer=default&crs=NHN:128&markers=");
			resultMsg.append(map.get("mapx")+","+map.get("mapy"));
		}
		
		return resultMsg.toString();
	}
	
	public static String printDocument(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("*[");
			resultMsg.append(map.get("description").replaceAll("\\<.*?>",""));
			resultMsg.append("]*\n");
			resultMsg.append("[");
			resultMsg.append(map.get("link").replaceAll("\\<.*?>",""));
			resultMsg.append("]");
		}
		
		return resultMsg.toString();
	}
	
	public static String printCafe(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("*[");
			resultMsg.append(map.get("title").replaceAll("\\<.*?>",""));
			resultMsg.append("]*\n");
			resultMsg.append("[");
			resultMsg.append(map.get("link").replaceAll("\\<.*?>",""));
			resultMsg.append("](코드초보스터디카페)\n");
		}
		
		return resultMsg.toString();
	}
	
	
	public static String printBlog(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("*[");
			resultMsg.append(map.get("title").replaceAll("\\<.*?>",""));
			resultMsg.append("]*\n");
			resultMsg.append("[");
			resultMsg.append(map.get("link").replaceAll("\\<.*?>",""));
			resultMsg.append("](블로그)\n");
		}
		
		return resultMsg.toString();
	}
	
	public static String printImage(List<Map<String, String>> items){
		StringBuffer resultMsg = new StringBuffer();
		
		for(Map<String, String> map : items){
			resultMsg.append("*[");
			resultMsg.append(map.get("title").replaceAll("\\<.*?>",""));
			resultMsg.append("]*\n");
			resultMsg.append("[");
			resultMsg.append(map.get("link").replaceAll("\\<.*?>",""));
			resultMsg.append("]");
		}
		
		return resultMsg.toString();
	}
	
	public static String printUser(List<User> list){
		StringBuffer resultMsg = new StringBuffer();
		
		resultMsg.append("```");
		resultMsg.append("*접속 유저 정보("+list.size()+"명)*\n");
		for(User user : list){
			resultMsg.append(user.getNick().substring(0, user.getNick().length()-1)+"*");
			if(user.getNick().equals("angbot")){
				resultMsg.append(" => 얘는 봇 ");
			}
			if(user.getNick().equals("eminency")){
				resultMsg.append(" => 방장님 ");
			}
			if(user.getNick().equals("loustler")){
				resultMsg.append(" => 모쏠 ");
			}
			resultMsg.append("\n");
		}
		resultMsg.append("```");
		
		return resultMsg.toString();
	}
}
