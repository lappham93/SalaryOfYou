package com.dkmobility.user;

import com.mit.dao.AppKeyDAO;
import com.mit.entities.app.AppKey;

public class AppKeyImport {

	public static void main(String[] args) {
//		AppKeyDAO.getInstance().truncate();
		AppKey appKeyAnd = new AppKey(1, 1, "bConnect", 1, "bConnect", "3960e0dc75e6fe8689e11f7ab597ffa9", "f72544ccc95099082f5705093ea206fa");
		AppKey appKeyIOS = new AppKey(2, 1, "bConnect", 1, "bConnect", "c631940637de3f7f2dd2ef0858e00bcd", "4ac08c94334359777be0c41bf4d4b8b5");
		AppKeyDAO.getInstance().insert(appKeyAnd);
		AppKeyDAO.getInstance().insert(appKeyIOS);

//		UserSettings setting = new UserSettings(3, new NearbySetting(), new GlobalSetting(), Collections.emptyList(), Collections.emptyList());
//		UserSettingDAO.getInstance().updateNearBySetting(3, new NearbySetting());
//		UserSettings setting1 = new UserSettings(4, new NearbySetting(), new GlobalSetting(), Collections.emptyList(), Collections.emptyList());
//		UserSettingDAO.getInstance().updateNearBySetting(4, new NearbySetting());
//		System.out.println(UserSettingDAO.getInstance().getNearBySetting(3).toDocument());
		//System.out.println(UserSettingDAO.getInstance().isBanMsg(4, 3));
//		System.out.println(IDGeneration.Instance.generateId("license_acceptance"));
	}

}