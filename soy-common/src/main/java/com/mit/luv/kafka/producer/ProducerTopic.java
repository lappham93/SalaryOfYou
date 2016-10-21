package com.mit.luv.kafka.producer;

public class ProducerTopic {
	public static final String SEND_SMS = "ts_sms_send";
	public static final String DELIVER_SMS = "ts_sms_deliver";
	
	public static final String SEND_VOICE = "ts_voice_send";
	public static final String DELIVER_VOICE_STATUS = "ts_voice_status_deliver";
	
	public static final String SEND_EMAIL = "ts_email_send";
    public static final String SEND_EMAIL_ORDER = "ts_email_order";
    public static final String SEND_EMAIL_REGISTER = "ts_email_register";
    //public static final String SEND_EMAIL_RESET_PASS = "ts_email_reset_pass";

	public static final String NOTIFICATION_ANDROID = "ts_notif_android";
	public static final String NOTIFICATION_IOS = "ts_notif_ios";

	public static final String NOTIFICATION_ANDROID_MULTI_DEST = "ts_notif_android_multi_dest";
	public static final String NOTIFICATION_IOS_MULTI_DEST = "ts_notif_ios_multi_dest";

	public static final String UPLOAD_CHAT = "ts_chat_upload";
	public static final String ADD_LOCATION = "ts_biz_location";

	public static final String UPLOAD_AVATAR_FROM_URL = "ts_avt_url_upload";

	public static final String UPDATE_USERINFO = "ts_update_info";
	public static final String UPDATE_USERLOCATION = "ts_update_user_location";	

	public static final String USER_REGISTER = "ts_user_register";

	public static final String PROMOTION_NEW= "ts_promotion_addnew";
	public static final String PROMOTION_UPDATE_VIEW = "ts_promotion_update_view";
	public static final String PROMOTION_UPDATE_LIKE = "ts_promotion_update_like";
	public static final String REC_PROMOTION_UPDATE_VIEW = "ts_rec_promotion_update_view";
	public static final String FACEBOOK_PUBLISH_FEED = "ts_facebook_publish_feed";
	public static final String PROMOTION_SHARE = "ts_promotion_share";
	public static final String PROMOTION_UPDATE_USE = "ts_promotion_update_use";
	public static final String BIZ_INIT = "ts_biz_init";
	public static final String BIZ_UPDATE_INFO = "ts_biz_update";
    
    public static final String UPLOAD_MMEDIA = "ts_mmedia";
    

	public static final String SUGGESTER_PROMOTION_UPDATE = "ts_suggester_promotion_update";
	
	public static final String PRODUCT_VIEW = "ts_product_view";
	public static final String PRODUCT_LIKE = "ts_product_like";
	public static final String PRODUCT_WISH = "ts_product_wish";
	public static final String PRODUCT_ORDER = "ts_product_order";
	public static final String PRODUCT_ADD_CART = "ts_product_addcart";
	public static final String PRODUCT_COMMENT = "ts_product_comment";
	
	public static final String NEWS_VIEW = "ts_news_view";
	public static final String NEWS_PRODUCT = "ts_news_product";
	public static final String NEWS_WEB = "ts_news_web";
	public static final String ORDER_SEND_SE = "ts_order_send_se";
	public static final String NEWS_EVENT = "ts_news_event";
	public static final String NEWS_BOOTH = "ts_news_booth";
	
	public static final String SHOP_VIDEO_SHARE = "ts_shop_video_share";
	public static final String SHOP_VIDEO_COMMENT = "ts_shop_video_comment";
	
	public static final String FEED_COMMENT = "ts_feed_comment";
	public static final String FEED_NOFITY = "ts_feed_notify";
	public static final String FEED_SHARE = "ts_feed_share";
    public static final String LIVE_FEED_COMMENT = "b_socket_live_feed_comment";
    public static final String FEED_LIKE = "ts_feed_like";
    public static final String COMMENT_LIKE = "ts_comment_like";
    
    public static final String ROOM_ADD = "ts_room_add";
    public static final String ROOM_UPLOAD_ADD = "ts_room_upload_add";
    public static final String ROOM_UPDATE = "ts_room_update";
    public static final String ROOM_UPLOAD_UPDATE = "ts_room_upload_update";
    
	public static final String REACT_SHARE = "ts_react_share";
	public static final String LIVE = "ts_live";
	
	//notify
	public static final String FEED_ADD = "ts_feed_add";
	public static final String COMMENT_ADD = "ts_comment_add";
	public static final String LIKE_ADD = "ts_like_add";
    
}
