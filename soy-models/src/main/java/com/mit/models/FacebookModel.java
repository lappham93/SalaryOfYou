package com.mit.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.constants.Gender;
import com.mit.dao.facebook.FacebookUserDAO;
import com.mit.entities.facebook.FBUser;
import com.mit.entities.facebook.FBUserInfo;
import com.mit.http.HttpsRequest;
import com.mit.utils.ConfigUtils;
import com.mit.utils.JsonUtils;

public class FacebookModel {
	private final Logger logger = LoggerFactory.getLogger(FacebookModel.class);
	
	private final String _facebookToken = ConfigUtils.getConfig().getString("facebook.token");
	protected final String _facebookGraphUrl = "https://graph.facebook.com/v2.5/";
	private final int OK = 200;
	public static FacebookModel Instance = new FacebookModel();
	
	public void addFacebookId(int userId, String facebookId) {
		FBUser fbUser = new FBUser(userId, facebookId);
		FacebookUserDAO.getInstance().upsert(fbUser);
	}
	
	public boolean checkFacebookUser(int userId) {
		FBUser fbUser = FacebookUserDAO.getInstance().getByUserId(userId);
		return fbUser != null;
	}
	
	public String publishFeed(String facebookId, String message, String link) {
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("message", message);
		// params.put("link", link);
		// params.put("access_token", accessToken);
		String accessToken = _facebookToken;
		String paramsScrape = "id=" + UrlEncoded.encodeString(link)
				+ "&scrape=true"
				+ "&access_token=" + UrlEncoded.encodeString(accessToken);
		String params = "message=" + UrlEncoded.encodeString(message)
				+ "&link=" + UrlEncoded.encodeString(link)
				+ "&access_token=" + UrlEncoded.encodeString(accessToken);
		String urlScrape = _facebookGraphUrl + "/";
		String url = _facebookGraphUrl + facebookId + "/feed";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(HttpHeader.CONTENT_TYPE.asString(), "application/x-www-form-urlencoded");
		try {
			HttpsRequest.Instance.doPost(urlScrape, headers,
					paramsScrape);
		} catch (Exception e) {
			logger.error("error ", e);
		}
		ContentResponse response = HttpsRequest.Instance.doPost(url, headers,
				params);
		return response.getContentAsString();
	}
	
	public FBUserInfo getUserInfo(String accessToken) {
		FBUserInfo fbUserInfo = null;
		
		String params = "fields=id,first_name,last_name,birthday,picture.width(800).height(800),gender,interested_in,email" 
				+ "&access_token=" + UrlEncoded.encodeString(accessToken);
		String url = _facebookGraphUrl + "me?" + params;
		ContentResponse response = HttpsRequest.Instance.doGet(url);
		
		if (response.getStatus() == OK) {
			Map<String, Object> obj = JsonUtils.Instance.getMapObject(response.getContentAsString());

			fbUserInfo = new FBUserInfo();
			fbUserInfo.setId(String.valueOf(obj.get("id")));
			fbUserInfo.setFirstName(String.valueOf(obj.get("first_name")));
			fbUserInfo.setLastName(String.valueOf(obj.get("last_name")));
			
			if (obj.get("picture") instanceof Map) {
				Map<String, Object> picture = (Map)obj.get("picture");
				
				if (picture.get("data") instanceof Map) {
					Map<String, Object> data = (Map)picture.get("data");
					fbUserInfo.setAvatar(String.valueOf(data.get("url")));					
				}
			}
			
			String birthday = obj.get("birthday") != null ? String.valueOf(obj.get("birthday")) : "";
			birthday = birthday.replace("/", "-");
			fbUserInfo.setBirthday(birthday);
			
			String gender = String.valueOf(obj.get("gender"));
			int genderId = 0;
			
			if (gender.equals("male")) {
				genderId = Gender.MALE;
			} else if (gender.equals("female")) {
				genderId = Gender.FEMALE;
			}
			
			fbUserInfo.setGender(genderId);
			
			if (obj.get("interested_in") instanceof List) {
				int interestedInGender = 0;
				List<String> interestedInList = (List)obj.get("interested_in");
				
				for (String interestedIn: interestedInList) {
					if (interestedIn.equals("male")) {
						interestedInGender |= Gender.MALE;
					} else if (interestedIn.equals("female")) {
						interestedInGender |= Gender.FEMALE;						
					}
				}
				
				fbUserInfo.setInterestedInGender(interestedInGender);
			}
			
			fbUserInfo.setEmail(String.valueOf(obj.get("email")));
		}
		
		return fbUserInfo;
	}
	
	public String convertId2UserName(String fbId) {
		return "fb_" + fbId;
	}
	
	public String convertUserName2Id(String userName) {
		return userName.replace("fb_", "");
	}
	
	public static void main(String[] args) {
		FBUserInfo userInfo = FacebookModel.Instance.getUserInfo("CAAHf0R2TqcgBABw4adfGVHNlGYu6l3B6Gc1xZA8EpBV8cLhRkSETMCM5dWlykQFLjZAx992uypDkka2Dm3kBewZAFeE3wGSsZARnLAeCBolv4ZAcMHLY2ZCTsAGBC0sbLeZBFzzeIWzYC90tQ3zsnNZCn1TymNgZAzjsVLNN1YjCqPcoKH1wRZAH5fLIMxXUFYFbuT1nYpC3wxHQZDZD");
		System.out.println(JsonUtils.Instance.toJson(userInfo));
	}
}
