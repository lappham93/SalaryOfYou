package com.dkmobility.user;

import java.math.BigDecimal;

import com.mit.constants.AppConstant;
import com.mit.constants.AppConstantKey;
import com.mit.dao.AppConstantDAO;


public class ImportAppConstant {
	public static void main(String[] args) {
		AppConstant backPercent = new AppConstant(AppConstantKey.BC_BACK_PERCENT.getKey(), BigDecimal.ONE.toString(), "bConnect back percent", 0, 0);
        AppConstantDAO.getInstance().upsert(backPercent);
		AppConstant cndExpirationLength = new AppConstant(AppConstantKey.CND_EXPIRATION_LENGTH.getKey(), 90 + "", "CND expiration length", 0, 0);
        AppConstantDAO.getInstance().upsert(cndExpirationLength);
		AppConstant msgCndLvDwngWarn = new AppConstant(AppConstantKey.MSG_CND_LEVEL_DWNG_WARN.getKey(), "You need $%1$s to retain %2$s level. Time to Shopping!", 
				"CND downgrade warning message", 0, 0);
        AppConstantDAO.getInstance().upsert(msgCndLvDwngWarn);
		AppConstant msgCndLvDwng = new AppConstant(AppConstantKey.MSG_CND_LEVEL_DWNG.getKey(), "You have downgraded to %1$s level!", 
				"CND downgrade message", 0, 0);
        AppConstantDAO.getInstance().upsert(msgCndLvDwng);
		AppConstant msgCndLvUpgWarn = new AppConstant(AppConstantKey.MSG_CND_LEVEL_UPG_WARN.getKey(), "You need $%1$s for upgrade %2$s level. Time to Shopping!", 
				"CND upgrade warning message", 0, 0);
        AppConstantDAO.getInstance().upsert(msgCndLvUpgWarn);	
		AppConstant msgCndLvUpg = new AppConstant(AppConstantKey.MSG_CND_LEVEL_UPG.getKey(), "Congratulations, you have reached %1$s level!", 
				"CND upgrade message", 0, 0);
        AppConstantDAO.getInstance().upsert(msgCndLvUpg);	
        AppConstant adminEmails = new AppConstant(AppConstantKey.ADMIN_EMAILS.getKey(), "lqhung88@gmail.com,ngandang2012@gmail.com", 
				"Admin emails. Split by comma (,)", 0, 0);
        AppConstantDAO.getInstance().upsert(adminEmails);	
        AppConstant adminUsEmails = new AppConstant(AppConstantKey.ADMIN_US_EMAILS.getKey(), "congnghia0609@gmail.com", 
				"US Admin emails. Split by comma (,)", 0, 0);
        AppConstantDAO.getInstance().upsert(adminUsEmails);	
        AppConstant adminIntlEmails = new AppConstant(AppConstantKey.ADMIN_INTL_EMAILS.getKey(), "congnghia0609@gmail.com", 
				"International admin emails. Split by comma (,)", 0, 0);
        AppConstantDAO.getInstance().upsert(adminIntlEmails);	        
        AppConstant msgCndLvRetain = new AppConstant(AppConstantKey.MSG_CND_LEVEL_RETAIN.getKey(), "Congratulations, you have retained %1$s level!", 
				"CND retain message", 0, 0);
        AppConstantDAO.getInstance().upsert(msgCndLvRetain);
        
	}
}
