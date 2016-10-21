package com.mit.utils;

import com.mit.common.conts.Common;
import com.mit.midutil.MIdNoise;


public class LinkBuilder {
	public static final String _defaultAvatar = "http://usavall.com/statics/images/default-user.png";
	public static final String _defaultBizAvatar = "http://usavall.com/statics/images/default-biz.png";
	public static final String _defaultCover = "http://usavall.com/statics/images/default-cover.jpg";
	public static final String _staticDomain = ConfigUtils.getConfig().getString("static.domain", "dev.spakonect.com");
	
	public static String buildFeedPhotoLink(long id) {
    	String link = "";
		if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	    	link = Common.DOMAIN_FILE + "/ts/load/feed/photo?photoId=" + idNoise;
    	}
    	return link;
    }
	
	public static String buildUserAvatarLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/user/photo?photoId=" + idNoise;
    	}
    	return link;
	}
	
	public static String buildEventPhotoLink(long id) {
    	String link = "";
		if (id > 0) {
			String linkIdNoise = MIdNoise.enNoiseLId(id);
			link = Common.DOMAIN_FILE + "/ts/load/event/photo?photoId=" + linkIdNoise;
		}
		
		return link;
    }
	
	public static String buildBoothPhotoLink(long id) {
    	String link = "";
		if (id > 0) {
			String linkIdNoise = MIdNoise.enNoiseLId(id);
			link = Common.DOMAIN_FILE + "/ts/load/booth/photo?photoId=" + linkIdNoise;
		}
		
		return link;
    }
	
	public static String buildBoothArModelLink(long id) {
    	String link = "";
		if (id > 0) {
			String linkIdNoise = MIdNoise.enNoiseLId(id);
			link = Common.DOMAIN_FILE + "/ts/load/booth/model3d?modelId=" + linkIdNoise;
		}
		
		return link;
    }
	
	public static String buildBoothProductPhotoLink(long id) {
    	String link = "";
		if (id > 0) {
			String linkIdNoise = MIdNoise.enNoiseLId(id);
			link = Common.DOMAIN_FILE + "/ts/load/product/photo?photoId=" + linkIdNoise;
		}
		
		return link;
    }
	
//	public static String buildMapPhotoLink(long id) {
//    	String link = "";
//		if (id > 0) {
//			String linkIdNoise = MIdNoise.enNoiseLId(id);
//			link = Common.DOMAIN_FILE + "/ts/load/map/photo?photoId=" + linkIdNoise;
//		}
//		
//		return link;
//    }
	
	public static String buildMapPhotoLink(long floorId) {
    	String link = "";
		if (floorId > 0) {
			String linkIdNoise = MIdNoise.enNoiseLId(floorId);
			link = Common.DOMAIN_FILE + "/ts/load/map/photo?floorId=" + linkIdNoise;
		}
		
		return link;
    }
	
	public static String buildCmtPhotoLink(long id) {
    	String link = "";
		if (id > 0) {
			String linkIdNoise = MIdNoise.enNoiseLId(id);
			link = Common.DOMAIN_FILE + "/ts/load/cmt/photo?photoId=" + linkIdNoise;
		}
		
		return link;
    }
	
	public static String buildLiveThumbLink(long streamId) {
		String link = _defaultCover;
		if (streamId > 0) {
			//TODO: get thumbnail of live stream
		}
		
		return link;
	}

	public static String buildAvatarLink(int uid, int avtVer, long tmpId, int size) {
		//String noiseString = NoiseIdUtils.encryptString(System.currentTimeMillis() + "");
		String link = _defaultAvatar;
		if(avtVer > 0) {
			String noiseData = NoiseIdUtils.encryptString(uid + "," + avtVer + "," + tmpId);
			link = "http://" + _staticDomain + "/ts/img/avt/" + noiseData;
		}
		return link;
	}
	
	public static String buildBizAvatarLink(long id) {
		String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/biz/photo?p=" + idNoise;
    	}
    	return link;
	}

    public static String buildBizCoverLink(long id) {
		String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/biz/photo?p=" + idNoise;
    	}
    	return link;
	}
    
	public static String buildCoverLink(long id) {
		String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
//	        link = Common.DOMAIN_APP + Common.PHOTO_LOAD_SERV_PATH + "?p=" + idNoise;
	        link = Common.DOMAIN_FILE + "/ts/load/biz/photo?p=" + idNoise;
    	}
    	return link;
	}

	public static String buildChatLink(int uid, long tmpId, int size) {
		//String noiseString = NoiseIdUtils.encryptString(System.currentTimeMillis() + "");
		String noiseData = NoiseIdUtils.encryptString(tmpId + "," + uid + "," + System.currentTimeMillis());
		String link = "http://" + _staticDomain + "/ts/img/c/" + noiseData;

		return link;
	}

	public static String buildVTemplateLink(int id, int tmlVer, int size) {
		String noiseData = NoiseIdUtils.encryptString(id + "," + tmlVer);
		String link = "http://" + _staticDomain + "/ts/img/tml/v/" + noiseData;
		return link;
	}

	public static String buildHTemplateLink(int id, int tmlVer, int size) {
		String noiseData = NoiseIdUtils.encryptString(id + "," + tmlVer);
		String link = "http://" + _staticDomain + "/ts/img/tml/h/" + noiseData;
		return link;
	}
	
	public static String buildVideoLink(long id) {
		String idNoise = MIdNoise.enNoiseLId(id);
        return Common.DOMAIN_APP + Common.MEDIA_LOAD_SERV_PATH + "?v=" + idNoise;
	}
    
    public static String buildChatRoomLink(int id) {
        return Common.DOMAIN_CHAT_ROOM + "/socket/room?roomId=" + id;
	}
    
    public static String buildUploadRoomLink(int id) {
        return Common.DOMAIN_UPLOAD_ROOM + "/upload/room?roomId=" + id;
	}
    
    public static String buildLongPollingFeedLink(long feedId) {
        return Common.DOMAIN_POLLING_FEED + "/polling/feed?feedId=" + feedId;
	}
    
    public static String buildAvtRoomLink(long id) {
        String link = "";
    	if (id > 0) {
            String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/room/photo?p=" + idNoise;
        }
        return link;
	}
    
    public static String buildPhotoLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_APP + Common.PHOTO_LOAD_SERV_PATH + "?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildProductPhotoLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/pro/photo?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildEduBannerPhotoLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/pro/photo?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildEduContentLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_ADMIN + "/web/admin/load/edu/content?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildUserPhotoLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/user/photo?photoId=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildNewsThumbLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/bn/photo?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildBannerThumbLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/bn/photo?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildSaleRepPhotoLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/user/photo?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildStreamMediaLink(long id){
        String v = MIdNoise.enNoiseLId(id);
        return Common.DOMAIN_APP + ":" + Common.PORT_RTMP + "/media_server/" + v + "/" + v + ".m3u8";
    }
    
    public static String buildProductShareLink(long productId, long skuId) {
    	String productIdNoise = MIdNoise.enNoiseLId(productId);
    	String skuIdNoise = MIdNoise.enNoiseLId(skuId);
    	return Common.DOMAIN_FILE + "/ts/load/product/share?pid=" + productIdNoise + "&sid=" + skuIdNoise;
    }
    
    public static String buildFeedShareLink(long feedId) {
    	String idNoise = MIdNoise.enNoiseLId(feedId);
    	return Common.DOMAIN_FILE + "/ts/load/feed/share?fid=" + idNoise;
    }
    
//    public static String buildFeedPhotoLink(long id) {
//    	if (id <= 0) {
//    		return "";
//    	}
//    	String linkIdNoise = MIdNoise.enNoiseLId(id);
//    	return Common.DOMAIN_FILE + "/ts/load/feed/photo?photoId=" + linkIdNoise;
//    }
    
    public static String buildRoomPhotoLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/room/photo?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildBizPhotoLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/biz/photo?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildCNDLevelPhotoLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
	        link = Common.DOMAIN_FILE + "/ts/load/biz/photo?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildPromoPhotoLink(long id) {
    	String link = "";
    	if (id > 0) {
			String idNoise = MIdNoise.enNoiseLId(id);
			link = Common.DOMAIN_FILE + "/ts/load/pro/photo?p=" + idNoise;
    	}
    	return link;
	}
    
    public static String buildSkuAppLink(long productId, long skuId) {
    	String link = "";
    	if (productId > 0 && skuId > 0) {
//    		String sIdNoise = MIdNoise.enNoiseLId(skuId);
//    		String pIdNoise = MIdNoise.enNoiseLId(productId);
    		link = Common.APP_LINK + "sku/share?pId=" + productId + "&" + "sId=" + skuId;
    	}
    	return link;
    }
    
    public static void main(String[] args) {
		System.out.println(LinkBuilder.buildBannerThumbLink(4));
	}
    
    
}
