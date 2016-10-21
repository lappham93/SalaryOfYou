package com.dkmobility.user;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.mit.utils.JsonUtils;

public class TestGoogle2 {
	public static void main(String[] args) throws GeneralSecurityException, IOException {
		String CLIENT_ID = "1018981987732-akv1sk0tgjl5s8nphjgvspj1defdfi1n.apps.googleusercontent.com";
		JsonFactory jsonFactory = new GsonFactory();
		NetHttpTransport transport = new NetHttpTransport();
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
				transport, jsonFactory).setAudience(Arrays.asList(CLIENT_ID))
		// If you retrieved the token on Android using the Play Services 8.3 API
		// or newer, set
		// the issuer to "https://accounts.google.com". Otherwise, set the
		// issuer to
		// "accounts.google.com". If you need to verify tokens from multiple
		// sources, build
		// a GoogleIdTokenVerifier for each issuer and try them both.
				.setIssuer("https://accounts.google.com").build();

		// (Receive idTokenString by HTTPS POST)
		String idTokenString = "eyJhbGciOiJSUzI1NiIsImtpZCI6ImJkYjcxNDljZTMwMThhMTdhMjI1ZGIwYWJjNDkzOWJlYTUwNTczYjIifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhdWQiOiIxMDE4OTgxOTg3NzMyLWFrdjFzazB0Z2psNXM4bnBoamd2c3BqMWRlZmRmaTFuLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwic3ViIjoiMTE3NjQwODIzMjg5MzMxMDkyODA2IiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF6cCI6IjEwMTg5ODE5ODc3MzItazBnYTFiNjg1cjV0ZDlyYjRuNXRodnBrMzNsNzNjZzAuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJlbWFpbCI6Im5ndXllbnRoYW5odGFuNzQxMUBnbWFpbC5jb20iLCJpYXQiOjE0NjAzNzAzNDgsImV4cCI6MTQ2MDM3Mzk0OCwibmFtZSI6IlThuqVuIE5ndXnhu4VuIFRoYW5oIiwicGljdHVyZSI6Imh0dHBzOi8vbGg2Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tQzRReHFKU3FNeDQvQUFBQUFBQUFBQUkvQUFBQUFBQUFBQ00vS2RnYmdlcGwtY1Uvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6IlThuqVuIiwiZmFtaWx5X25hbWUiOiJOZ3V54buFbiBUaGFuaCIsImxvY2FsZSI6InZpIn0.dix8aev3DhH8R_nV7EaeFKl964XAMyqlPTdTBDNGqrGEvVyM6AM6eGLHh3E4I88GstE4llMzgoWHLBODsTNTwakKZcKMDRt0JfxxUVeLBhT84gEazk5C8olXx51vDUKE_S5uRKRfxVjT7dIk-WMCYUKbnFZMhUG4-m6T_ZFaQC6WQopYs6azvFyBD4_8lWsyx4Gl_5qBzUS3LfC9Hakz8vRHgZxSAlo5c9RdhavQovPMiR4JC_nx_rSvK1H4fw36UVDPfEVHJ5kl_CPPAmLlCBhh2ldMr0yrSj-i03YZT5rBAWkQyxt5DyMFqMnH2SjiGtnHNDSQ1nWMQf8Qw6LsRA";

		GoogleIdToken idToken = verifier.verify(idTokenString);
		if (idToken != null) {
			Payload payload = idToken.getPayload();

			// Print user identifier
			String userId = payload.getSubject();
			System.out.println("User ID: " + userId);

			// Get profile information from payload
			String email = payload.getEmail();
			boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
			String name = (String) payload.get("name");
			String pictureUrl = (String) payload.get("picture");
			String locale = (String) payload.get("locale");
			String familyName = (String) payload.get("family_name");
			String givenName = (String) payload.get("given_name");

			// Use or store profile information
			// ...
			System.out.println(JsonUtils.Instance.toJson(payload));
//			System.out.println("userId: " + userId + ", email: " + email + ", name:" + name + ", pictureUrl:" + pictureUrl);
		} else {
			System.out.println("Invalid ID token.");
		}
	}
}
