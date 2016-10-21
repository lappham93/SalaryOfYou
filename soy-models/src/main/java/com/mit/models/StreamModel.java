package com.mit.models;

import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.mit.common.conts.Common;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.dao.stream.SocialStreamDAO;
import com.mit.dao.stream.StreamErrCode;
import com.mit.dao.user.UserInfoDAO;
import com.mit.entities.stream.SocialStream;
import com.mit.entities.stream.SocialStream.SocialStreamStatus;
import com.mit.entities.user.UserInfo;

public class StreamModel {
	public static final StreamModel Instance = new StreamModel();
	
	public String generateStreamName(int userId) {
		return "SC_" + System.currentTimeMillis() + "_" + userId;
	}
	
	public String generateToken(int userId) {
		StringBuilder session = new StringBuilder(Base64.encodeBase64String(DigestUtils.sha256(userId + "_" + System.nanoTime())));
		return session.toString();
	}
	
	public Map<String, Object> publishStream(int userId, String title) {
		int err = StreamErrCode.SUCCESS;
		Map<String, Object> rs = new HashMap<String, Object>();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -30);
		
		SocialStream ss = SocialStreamDAO.getInstance().getByLastActive(userId, cal.getTimeInMillis());
		
		if (ss != null) {
			err = StreamErrCode.OLD_STREAM_AVAILABLE;
		} else {
			String streamName = generateStreamName(userId);
			String token = generateToken(userId);
			long feedId = MIdGenLongDAO.getInstance("feed").getNext();
			ss = new SocialStream(0, userId, streamName, title, token, feedId, SocialStreamStatus.ACTIVE.getValue(), 0, 0);
			SocialStreamDAO.getInstance().insert(ss);
		}
		
		rs.put("chatUrl", Common.DOMAIN_CHAT_LIVE + "/socket/live");
		rs.put("liveId", ss.getId());
		rs.put("host", "115.79.45.86");
		rs.put("port", "1935");
		rs.put("appName", "liveorigin");
		rs.put("streamName", ss.getName());
		rs.put("token", ss.getToken());
		rs.put("feedId", ss.getFeedId());
		
		rs.put("err", err);
		
		return rs;
	}
	
	public Map<String, Object> getListStream(int viewUserId, int from, int count) {
		Map<String, Object> rs = new HashMap<String, Object>();
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -30);
		List<SocialStream> sss = SocialStreamDAO.getInstance().getSliceActive(from, count+1, cal.getTimeInMillis());
		boolean hasMore = false;
		if (sss.size() > count) {
			sss = sss.subList(0, count);
			hasMore = true;
		}
		
		Set<Integer> userIds = new HashSet<Integer>();
		for (SocialStream ss: sss) {
			userIds.add(ss.getUserId());
		}
		
		Map<Integer, UserInfo> userInfos = UserInfoDAO.getInstance().getMapFromIdList(new LinkedList<Integer>(userIds));
		
		List<SocialStream.UserView> streamViews = new LinkedList<SocialStream.UserView>();
		for (SocialStream ss: sss) {
			SocialStream.UserView streamView = ss.buildUserView().setUserInfo(userInfos.get(ss.getUserId()).buildSocialView(viewUserId));
			
			streamViews.add(streamView);
		}
		
		rs.put("hasMore", hasMore);
		rs.put("socialStreams", streamViews);
		
		return rs;
	}
}
