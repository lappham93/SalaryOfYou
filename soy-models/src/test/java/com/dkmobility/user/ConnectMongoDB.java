package com.dkmobility.user;

import org.bson.Document;

import com.mit.connect.MDBConnect;
import com.mit.utils.ConfigUtils;
import com.mongodb.client.MongoDatabase;

public class ConnectMongoDB {

	public static void main(String[] args) {
		String configName = "luv";
		MDBConnect connect = MDBConnect.getInstance(configName);
		if(connect != null) {
			MongoDatabase dbSource = connect.getClient().getDatabase(ConfigUtils.getConfig().getString(configName + ".mongodb.db"));
			//dbSource.getCollection("test").drop();
			dbSource.getCollection("test").insertOne(new Document("a", "b"));
		}
	}
}
