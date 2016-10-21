package com.mit.entities.user;

import java.util.LinkedList;
import java.util.List;

public class DiscountCode {	
	public static final int ACTIVE = 1;
	
	private long id;
	private long parentId;
	private int userId;
	private String code;
	private int type;
	private int level;
	private int status;
	private long createTime;
	private long updateTime;

	public DiscountCode() {
		super();
	}
	
	public DiscountCode(long id, long parentId, int userId, String code, int type, int level, int status,
			long createTime, long updateTime) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.userId = userId;
		this.code = code;
		this.type = type;
		this.level = level;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	public TreeView buildTreeView(boolean calcCommission) {
		return new TreeView(this, calcCommission);
	}
	
	public class TreeView {			
		private long id;
		private TreeView parent;
		private int userId;
		private String code;
		private int type;
		private int level;
		private List<TreeView> childrend;
		private boolean calcCommission;
		private int status;
		private long createTime;
		private long updateTime;
		
		private TreeView(DiscountCode discountCode, boolean calcCommission) {
			this.id = discountCode.getId();
			this.userId = discountCode.getUserId();
			this.code = discountCode.getCode();
			this.type = discountCode.getType();
			this.level = discountCode.getLevel();
			this.status = discountCode.getStatus();
			this.createTime = discountCode.getCreateTime();
			this.updateTime = discountCode.getUpdateTime();
			this.childrend = new LinkedList<TreeView>();
			this.calcCommission = calcCommission;
		}

		public TreeView getParent() {
			return parent;
		}

		public TreeView setParent(TreeView parent) {
			this.parent = parent;
			return this;
		}

		public List<TreeView> getChildrend() {
			return childrend;
		}

		public TreeView setChildrend(List<TreeView> childrend) {
			this.childrend = childrend;
			return this;
		}

		public long getId() {
			return id;
		}

		public int getUserId() {
			return userId;
		}

		public String getCode() {
			return code;
		}

		public int getType() {
			return type;
		}

		public int getLevel() {
			return level;
		}

		public boolean isCalcCommission() {
			return calcCommission;
		}

		public int getStatus() {
			return status;
		}

		public long getCreateTime() {
			return createTime;
		}

		public long getUpdateTime() {
			return updateTime;
		}
	}
}
