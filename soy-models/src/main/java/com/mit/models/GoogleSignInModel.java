package com.mit.models;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.mit.entities.app.AppKey;
import com.mit.entities.facebook.FBUserInfo;
import com.mit.utils.ConfigUtils;
import com.mit.utils.JsonUtils;

public class GoogleSignInModel {	
	private final String ANDROID_CLIENT_ID = ConfigUtils.getConfig().getString("google.android.clientId");
	private final String IOS_CLIENT_ID = ConfigUtils.getConfig().getString("google.ios.clientId");
	private final Map<Integer, String> _clientIds; 
	
	public static final GoogleSignInModel Instance = new GoogleSignInModel();
	
	private GoogleSignInModel() {
		_clientIds = new HashMap<Integer, String>();
		_clientIds.put(AppKey.ANDROID, ANDROID_CLIENT_ID);
		_clientIds.put(AppKey.IOS, IOS_CLIENT_ID);
	}
	
	public FBUserInfo getUserInfo(int os, String idTokenString) {
		FBUserInfo fbUserInfo = null;
		
		try {
			JsonFactory jsonFactory = new GsonFactory();
			NetHttpTransport transport = new NetHttpTransport();
			GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
					transport, jsonFactory).setAudience(Arrays.asList(ANDROID_CLIENT_ID, IOS_CLIENT_ID))
					.setIssuer("https://accounts.google.com").build();
			
			GoogleIdToken idToken = verifier.verify(idTokenString);
			if (idToken != null) {
				Payload payload = idToken.getPayload();

				fbUserInfo = new FBUserInfo();
				fbUserInfo.setId(payload.getSubject());
				fbUserInfo.setFirstName((String) payload.get("given_name"));
				fbUserInfo.setLastName((String) payload.get("family_name"));
				
				String birthday = null;		
				
				if (birthday == null) {
					birthday = "";
				}
				
				fbUserInfo.setBirthday(birthday);
				
				int gender = 0;			
				
				fbUserInfo.setGender(gender);
				
				String email = payload.getEmail();
				
				fbUserInfo.setEmail(email);
				
				int interestedInGender = 0;
				fbUserInfo.setInterestedInGender(interestedInGender);
				
				String avatar = (String) payload.get("picture");
				if (avatar != null) {
					if (avatar.indexOf('?') > 0) {
						avatar = avatar.substring(0, avatar.indexOf('?'));
					}
					fbUserInfo.setAvatar(avatar + "?sz=800");
				} else {
					fbUserInfo.setAvatar("");
				}
			} else {
				System.out.println("Invalid ID token.");
			}
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
		FBUserInfo fbUserInfo = GoogleSignInModel.Instance.getUserInfo(AppKey.IOS, "eyJhbGciOiJSUzI1NiIsImtpZCI6IjY2MzkzOTBjNTk4MjQwNWI1ZWJjMDQ2YmUwOWY5MmY1ZDhhZmNmZDkifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdWQiOiIxMDcxNTIzMDQ4My1hb2VndDVwNmQ0amFiYWxtNzNpb2xoYzJxMTM0aWhqNC5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsInN1YiI6IjEwODkyNDI4NzYxODA3NjY3NzIwMSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhenAiOiIxMDcxNTIzMDQ4My0wYjQ2MmllbTd0cmcydGNubWx1dG12dTFwZnMxYjJ2Ny5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImVtYWlsIjoiZGllbW15c2hvcDIwMTVAZ21haWwuY29tIiwiaWF0IjoxNDY4ODM1MDE2LCJleHAiOjE0Njg4Mzg2MTYsIm5hbWUiOiJEaeG7hW0gTXkgVHLhuqduIiwiZ2l2ZW5fbmFtZSI6IkRp4buFbSBNeSIsImZhbWlseV9uYW1lIjoiVHLhuqduIiwibG9jYWxlIjoidmkifQ.U-z3qWu0dgjMPjInqPaQTavTSRodPGNT_v_ybgcpu2Iu0CGetBYj01dcl4yEqHKbkADpwREaKcHZUAXQwT2Jh8HLEVfeuJSzHaE9yokPdpuMpVG8bSfz3-BpF0oVXXxw2ZuVu5aaDAJoQAbfn2eyi4PPf2ApiYAbAMREzdHZHl1tqAZ3e3ctzyOJ7gZIYmEp7qRdbkYFxOSpO7-eUuHNDIdrrhvD7VswHXNny4DyTvu0YDbNVS5izmFPYgWBd9aQIrhlqmOJhoOeU2I1_2XPEGmD4lbl11XvwNDWHuxXoJzQSpuiPZbI6oiR7Edx-SF66bFzDXopfoTM1CvHV4CIHw");
		if (fbUserInfo != null) {
			System.out.println(JsonUtils.Instance.toJson(fbUserInfo));
		}
	}
}
