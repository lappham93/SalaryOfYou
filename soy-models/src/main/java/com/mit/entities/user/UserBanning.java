package com.mit.entities.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mit.entities.chat.LockUserRoom;
import com.mit.utils.JsonUtils;

public class UserBanning {
	
	public static final int ACTIVE = 1;
	
	public static enum BanningType {
		CHAT(1, "Chat"),
		POST_FEED(2, "Post Feed");
		
		private int value;
		
		private String desc;
		
		private BanningType(int value, String desc) {
			this.value = value;
			this.desc = desc;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public String getDesc() {
			return this.desc;
		}
		
		public static BanningType getBanningType(int value) {
			for (BanningType ban : BanningType.values()) {
				if (ban.getValue() == value) {
					return ban;
				}
			}
			
			return null;
		}
		
		public static BanningType getBanningType(String name) {
			for (BanningType ban : BanningType.values()) {
				if (ban.getDesc().equalsIgnoreCase(name)) {
					return ban;
				}
			}
			
			return null;
		}
	}
	
	private long id;
	private int userId;
	private int type;
	private int status;
	private long time;
	
	public UserBanning(){}
	
	public UserBanning(long id, int userId, int type) {
		super();
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.status = ACTIVE;
	}
	
	public UserBanning(long id, int userId, int type, int status, long time) {
		super();
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.status = status;
		this.time = time;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getType() {
		return type;
	}
	public void setType(int status) {
		this.type = status;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	/* 
     * @params: trus has been sorted with userId field 
     */
    public static List<UserBanningMerge> buildMerge(List<UserBanning> trus) {
    	UserBanning context = new UserBanning();
		List<UserBanningMerge> rs = new ArrayList<>();
		if (trus != null) {
			int reports = 0;
			UserBanningMerge trm = null;
			for (int idx = 0; idx < trus.size(); idx++) {
				UserBanning tru = trus.get(idx);
				if (idx - 1 < 0 || trus.get(idx - 1).getUserId() != tru.getUserId()) {
					trm = context.new UserBanningMerge();
					trm.setUserId(tru.getUserId());
				}
				trm.addBanning(tru.getId());
				reports ++;
				
				if (idx + 1 >= trus.size() || trus.get(idx + 1).getUserId() != tru.getUserId()) {
					trm.setNumBanning(reports);
					reports = 0;
					rs.add(trm);
				}
			}
		}
		return rs;
	}
    
    public static List<RoomBanningView> buildRoomBanningView(List<List<UserBanning>> lstUb) {
    	UserBanning context = new UserBanning();
    	List<RoomBanningView> rs = new ArrayList<>();
    	if (lstUb != null && !lstUb.isEmpty()) {
    		for (List<UserBanning> ub : lstUb) {
    			rs.add(context.new RoomBanningView(ub));
    		}
    	}
    	
    	return rs;
    }
	
	public class UserBanningMerge {
		private int userId;
    	private List<Long> banningIds;
    	private int numBanning;
    	
    	public UserBanningMerge() {}
    	
    	public void setUserId(int userId) {
    		this.userId = userId;
    	}
    	
    	public void setNumBanning(int numBanning) {
    		this.numBanning = numBanning;
    	}
    	
    	public void addBanning(long banningId) {
    		if (this.banningIds == null) {
    			this.banningIds = new ArrayList<>();
    		}
    		if (!this.banningIds.contains(banningId)) {
    			this.banningIds.add(banningId);
    		}
    	}
    	
    	public int getUserId() {
    		return userId;
    	}
    	
    	public List<Long> getBanningIds() {
    		return banningIds;
    	}
    	
    	public int getNumBanning() {
    		return numBanning;
    	}
    	
    	@Override
    	public String toString() {
    		return "userId=" + userId + ", banningIds=" + JsonUtils.Instance.toJson(banningIds) + ", numBanning=" + numBanning;
    	}
	}
	
	public class RoomBanningView {
		private int userId;
		private Set<Integer> roomIds;
		
		public RoomBanningView(List<UserBanning> ubs) {
			if (ubs != null && !ubs.isEmpty()) {
				this.userId = ubs.get(0).getUserId();
				this.roomIds = new HashSet<>();
				for (UserBanning ub : ubs) {
					if (ub.getType() == UserBanning.BanningType.CHAT.getValue() && ub.getStatus() > 0) {
						roomIds.add(((LockUserRoom) ub).getRoomId()); 
					}
				}
			}
		}

		public int getUserId() {
			return userId;
		}

		public Set<Integer> getRoomIds() {
			return roomIds;
		}
		
	}
	
	
}
