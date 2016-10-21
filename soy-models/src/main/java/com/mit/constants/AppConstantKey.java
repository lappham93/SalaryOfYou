package com.mit.constants;

public enum AppConstantKey {
	BC_BACK_PERCENT("loyalty.bconnect.backPercent"),
	CND_EXPIRATION_LENGTH("cnd.expiration.length"),
	MSG_CND_LEVEL_UPG("msg.cnd.level.upgrade"),
	MSG_CND_LEVEL_DWNG("msg.cnd.level.downgrade"),
	MSG_CND_LEVEL_UPG_WARN("msg.cnd.level.upgrade.warning"),
	MSG_CND_LEVEL_DWNG_WARN("msg.cnd.level.downgrade.warning"),
	MSG_CND_LEVEL_RETAIN("msg.cnd.level.retain"),
	ADMIN_EMAILS("admin.emails"),
	ADMIN_US_EMAILS("admin.us.emails"),
	ADMIN_INTL_EMAILS("admin.intl.emails")
	;
	
	private String key;
	
	private AppConstantKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public static AppConstantKey findByValue(String key) {
		for (AppConstantKey value: values()) {
			if (value.getKey().equals(key)) {
				return value;
			}
		}
		
		return null;
	}
}
