package com.dkmobility.user;

import com.mit.dao.user.UserAccountDAO;
import com.mit.dao.user.UserInfoDAO;
import com.mit.entities.user.UserInfo;

public class DeleteUser {
	public static void main(String[] args) {
//		UserAccountDAO.getInstance().hardDeleteByUserName("84903537163");
//		UserAccountDAO.getInstance().hardDeleteByUserName("841683191975");
//		UserAccountDAO.getInstance().hardDeleteByUserName("841222421115");
//		UserAccountDAO.getInstance().hardDeleteByUserName("84979654422");
//		UserAccountDAO.getInstance().hardDeleteByUserName("84908312490");
//		UserAccountDAO.getInstance().hardDeleteByEmail("ngandang2012@gmail.com");
//		UserAccountDAO.getInstance().hardDeleteByEmail("elvydang87@gmail.com");
//		UserAccountDAO.getInstance().hardDeleteByEmail("ellydang87316@gmail.com");
//		UserAccountDAO.getInstance().hardDeleteByEmail("testerdang01@gmail.com");
//		UserAccountDAO.getInstance().hardDeleteByEmail("ngandang2012@gmail.com");
//		UserAccountDAO.getInstance().hardDeleteByEmail("ngandang1404@gmail.com");
//		UserAccountDAO.getInstance().hardDeleteByEmail("testerdang2000@gmail.com");
        
        String email = "ellydang87316@gmail.com";
		int err1 = UserAccountDAO.getInstance().hardDeleteByEmail(email);
        System.out.println("err1: " + err1);
        int err2 = UserInfoDAO.getInstance().hardDeleteByEmail(email);
        System.out.println("err2: " + err2);
        
	}
}
