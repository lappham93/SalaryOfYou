package com.mit.dao.photo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.mphoto.thrift.TMPhoto;
import com.mit.mphoto.thrift.TMPhotoResult;
import com.mit.mphoto.thrift.TMapMPhotoResult;
import com.mit.utils.MIMETypeUtil;

public class PhotoUtils {
	public static long genPhotoId(int photoType) {
		long id = -1;
		if (photoType == PhotoType.BANNER.getValue()) {
			id = MIdGenLongDAO.getInstance(PhotoCommon.bannerNewsPhotoIdGen).getNext();
		} else if (photoType == PhotoType.EVENT.getValue()) {
			id = MIdGenLongDAO.getInstance(PhotoCommon.eventPhotoIdGen).getNext();
		} else if (photoType == PhotoType.BOOTH.getValue()) {
			id = MIdGenLongDAO.getInstance(PhotoCommon.boothPhotoIdGen).getNext();
		} else if (photoType == PhotoType.PRODUCT.getValue()) {
			id = MIdGenLongDAO.getInstance(PhotoCommon.productPhotoIdGen).getNext();
		} else if (photoType == PhotoType.MAP.getValue()) {
			id = MIdGenLongDAO.getInstance(PhotoCommon.mapPhotoIdGen).getNext();
		} else if (photoType == PhotoType.COMMENT.getValue()) {
			id = MIdGenLongDAO.getInstance(PhotoCommon.commentPhotoIdGen).getNext();
		} else if (photoType == PhotoType.MODEL3D.getValue()) {
			id = MIdGenLongDAO.getInstance(PhotoCommon.model3dIdGen).getNext();
		}
		
		return id;
	}
	
	public static int putPhoto(TMPhoto tmp, int photoType) {
		int err = -1;
		if (photoType == PhotoType.BANNER.getValue()) {
			err = BannerNewsPhotoClient.getInstance().putMPhoto(tmp);
		} else if (photoType == PhotoType.EVENT.getValue()) {
			err = EventPhotoClient.getInstance().putMPhoto(tmp);
		} else if (photoType == PhotoType.BOOTH.getValue()) {
			err = BoothPhotoClient.getInstance().putMPhoto(tmp);
		} else if(photoType == PhotoType.PRODUCT.getValue()) {
			err = ProductPhotoClient.getInstance().putMPhoto(tmp);
		} else if (photoType == PhotoType.MAP.getValue()) {
			err = MapPhotoClient.getInstance().putMPhoto(tmp);
		} else if (photoType == PhotoType.COMMENT.getValue()) {
			err = CommentPhotoClient.getInstance().putMPhoto(tmp);
		} else if(photoType == PhotoType.MODEL3D.getValue()) {
			err = Model3DClient.getInstance().putMPhoto(tmp);
		}
		
		return err;
	}
	
	public static TMPhotoResult getPhoto(long pid, int photoType) {
		TMPhotoResult tmp = null;
		if (photoType == PhotoType.BANNER.getValue()) {
			tmp = BannerNewsPhotoClient.getInstance().getMPhoto(pid);
		} else if (photoType == PhotoType.EVENT.getValue()) {
			tmp = EventPhotoClient.getInstance().getMPhoto(pid);
		} else if (photoType == PhotoType.BOOTH.getValue()) {
			tmp = BoothPhotoClient.getInstance().getMPhoto(pid);
		} else if (photoType == PhotoType.PRODUCT.getValue()) {
			tmp = ProductPhotoClient.getInstance().getMPhoto(pid);
		} else if (photoType == PhotoType.MAP.getValue()) {
			tmp = MapPhotoClient.getInstance().getMPhoto(pid);
		} else if (photoType == PhotoType.COMMENT.getValue()) {
			tmp = CommentPhotoClient.getInstance().getMPhoto(pid);
		} else if (photoType == PhotoType.MODEL3D.getValue()) {
			tmp = Model3DClient.getInstance().getMPhoto(pid);
		}
		
		return tmp;
	}
    
    public static TMapMPhotoResult getMapPhoto(List<Long> pids, int photoType) {
		TMapMPhotoResult tmp = null;
		if (photoType == PhotoType.BANNER.getValue()) {
			tmp = BannerNewsPhotoClient.getInstance().multiGetMPhoto(pids);
		} else if (photoType == PhotoType.EVENT.getValue()) {
			tmp = EventPhotoClient.getInstance().multiGetMPhoto(pids);
		} else if (photoType == PhotoType.BOOTH.getValue()) {
			tmp = BoothPhotoClient.getInstance().multiGetMPhoto(pids);
		} else if (photoType == PhotoType.PRODUCT.getValue()) {
			tmp = ProductPhotoClient.getInstance().multiGetMPhoto(pids);
		} else if (photoType == PhotoType.MAP.getValue()) {
			tmp = MapPhotoClient.getInstance().multiGetMPhoto(pids);
		} else if (photoType == PhotoType.COMMENT.getValue()) {
			tmp = CommentPhotoClient.getInstance().multiGetMPhoto(pids);
		} else if (photoType == PhotoType.MODEL3D.getValue()) {
			tmp = Model3DClient.getInstance().multiGetMPhoto(pids);
		}
		
		return tmp;
	}
	
    public static long saveFilePhoto(File file, int photoType) throws IOException {
        long id = genPhotoId(photoType);
        if(id > 0){
            TMPhoto tmp = new TMPhoto();
            tmp.setId(id);
            String filename = file.getName();
            tmp.setFilename(filename);
            tmp.setData(Files.readAllBytes(file.toPath()));
            String ext = FilenameUtils.getExtension(filename);
            String contentType = MIMETypeUtil.getInstance().getMIMETypeImage(ext);
            tmp.setContentType(contentType);
            int err = putPhoto(tmp, photoType);
            if (err < 0) {
            	id = err;
            }
        }
        
        return id;
	}
    
    public static long saveDataPhoto(byte[] data, String fileName, int photoType) throws IOException {
        long id = genPhotoId(photoType);
        if(id > 0){
            TMPhoto tmp = new TMPhoto();
            tmp.setId(id);
            tmp.setFilename(fileName);
            tmp.setData(data);
            String ext = FilenameUtils.getExtension(fileName);
            String contentType = MIMETypeUtil.getInstance().getMIMETypeImage(ext);
            tmp.setContentType(contentType);
            int err = putPhoto(tmp, photoType);
            if (err < 0) {
            	id = err;
            }
        }
        
        return id;
	}
}
