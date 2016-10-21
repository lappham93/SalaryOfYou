package com.mit.models;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.google.api.services.plus.model.Person.Emails;
import com.mit.constants.Gender;
import com.mit.entities.facebook.FBUserInfo;
import com.mit.utils.DateTimeUtils;
import com.mit.utils.JsonUtils;

public class GoogleModel {	
	private final String _appName = "FConnect";
	public static final GoogleModel Instance = new GoogleModel();
	
	public FBUserInfo getUserInfo(String accessToken) {
		FBUserInfo fbUserInfo = null;
		
		try {
			GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
			Plus plus = new Plus.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
			    .setApplicationName(_appName)
			    .build();
			
			Person mePerson = plus.people().get("me").execute();
			
			fbUserInfo = new FBUserInfo();
			fbUserInfo.setId(mePerson.getId());
			fbUserInfo.setFirstName(mePerson.getName().getGivenName());
			fbUserInfo.setLastName(mePerson.getName().getFamilyName());
			
			String birthday = null;			
			if (mePerson.getBirthday() != null) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date dob = sdf.parse(mePerson.getBirthday().replace("0000", "1970"));
					birthday = DateTimeUtils.formatDate(dob);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			if (birthday == null) {
				birthday = "";
			}
			
			fbUserInfo.setBirthday(birthday);
			
			int gender = 0;			
			if (mePerson.getGender().equals("male")) {
				gender = Gender.MALE;
			} else if (mePerson.getGender().equals("female")) {
				gender = Gender.FEMALE;
			}	
			
			fbUserInfo.setGender(gender);
			
			String email = "";
			
			for (Emails ema: mePerson.getEmails()) {
				if (ema.getType().equals("account")) {
					email = ema.getValue();
					break;
				}
			}
			
			if (email.isEmpty() && mePerson.getEmails().size() > 0) {
				email = mePerson.getEmails().get(0).getValue();
			}
			
			fbUserInfo.setEmail(email);
			
			int interestedInGender = 0;
			fbUserInfo.setInterestedInGender(interestedInGender);
			
			String avatar = mePerson.getImage().getUrl();
			avatar = avatar.substring(0, avatar.indexOf('?'));
			fbUserInfo.setAvatar(avatar + "?sz=800");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fbUserInfo;
	}
	
	public String convertId2UserName(String ggId) {
		return "gg_" + ggId;
	}
	
	public String convertUserName2Id(String userName) {
		return userName.replace("gg_", "");
	}
	
	public static void main(String[] args) throws GeneralSecurityException, IOException {
		FBUserInfo fbUserInfo = GoogleModel.Instance.getUserInfo("ya29.lAEu-CoEBmqpi7ej_eOK0GWUMONWLQmc4BZ6f-8WKlgcyXtX1axBdFCLiCmUj80vnZQ1Gt0M67l9wprM_Z-zvS7sWaOjWDUsEIDKQRmznc02kcO3oWeKHjzvwn49u7t93rur6X6s62wjcw");
		if (fbUserInfo != null) {
			System.out.println(JsonUtils.Instance.toJson(fbUserInfo));
		}
	}
}
