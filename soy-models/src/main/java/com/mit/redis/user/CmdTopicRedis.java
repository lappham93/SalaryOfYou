package com.mit.redis.user;
//package com.dkmobility.redis.user;
//
//import org.redisson.Redisson;
//import org.redisson.core.RMap;
//
//import com.dkmobility.redis.RedisClient;
//
//public class CmdTopicRedis {
//	private final String keyName = "dkmobility.topic";
//	private final String _prefixAccessConf = "topic.";
//
//	private final Redisson redisson;
//
//	public static CmdTopicRedis Instance = new CmdTopicRedis();
//
//	protected CmdTopicRedis() {
//		redisson = RedisClient.getInstance("activecode").getConnect();
//		if(redisson == null) {
//			System.out.println("Don't create connect to redis - activecode");
//		}
//	}
//
//	public String getTopic(int cmd) {
//		RMap<Integer, String> map = redisson.getMap(keyName);
//		return map.get(cmd);
//	}
//
//	public void setTopic(int cmd, String topic) {
//		RMap<Integer, String> map = redisson.getMap(keyName);
//		map.put(cmd, topic);
//	}
//}
