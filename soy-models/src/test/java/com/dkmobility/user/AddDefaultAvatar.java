package com.dkmobility.user;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.mit.dao.upload.UserAvatarDAO;

public class AddDefaultAvatar {
	public static void main(String[] args) {
		try {
			String avtPath = "./default-biz.png";
			Path path = Paths.get(avtPath);
			byte[] data = Files.readAllBytes(path);
			UserAvatarDAO.getInstance().updateData(0, 1, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
