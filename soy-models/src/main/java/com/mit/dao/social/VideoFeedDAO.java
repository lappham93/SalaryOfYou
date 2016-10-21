package com.mit.dao.social;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.LoggerFactory;
import com.mit.entities.social.FeedType;


public class VideoFeedDAO extends FeedDAO{
	private static Lock _lock = new ReentrantLock();
	private static VideoFeedDAO _instance; 
	
	public static VideoFeedDAO getInstance() {
		if (_instance == null) {
			_lock.lock();
			try {
				if (_instance == null) {
					_instance = new VideoFeedDAO();
				}
			} finally {
				_lock.unlock();
			}
		}

		return _instance;
	}
	
	private VideoFeedDAO() {
		super();
		this.setFeedType(FeedType.LIVE.getValue());
		this.setLogger(LoggerFactory.getLogger(VideoFeedDAO.class));
	}
}
