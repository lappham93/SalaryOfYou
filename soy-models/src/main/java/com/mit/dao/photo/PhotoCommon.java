/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mit.dao.photo;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;

import com.mit.common.conts.Common;
import com.mit.dao.mlist.MSortType;
import com.mit.dao.mlist.MStrSortSetI64DAO;
import com.mit.midutil.MIdNoise;
import com.mit.mphoto.thrift.TMPhotoResult;

/**
 *
 * @author nghiatc
 * @since Jan 12, 2016
 */
public class PhotoCommon {
    //Photo Product.
    public static final String productPhotoIdGen = "productphotoid";
    public static final String PRODUCT_PHOTO_TABLE = "set_product_photo";
    public static MStrSortSetI64DAO SET_PRODUCT_PHOTO = new MStrSortSetI64DAO(PRODUCT_PHOTO_TABLE, MSortType.DESC);
    
    //Photo User.
    public static final String userPhotoIdGen = "userphotoid";
    public static final String USER_PHOTO_TABLE = "set_user_photo";
    public static MStrSortSetI64DAO SET_USER_PHOTO = new MStrSortSetI64DAO(USER_PHOTO_TABLE, MSortType.DESC);
    
    //Banner News User.
    public static final String bannerNewsPhotoIdGen = "bannerphotoid";
    public static final String BANNER_NEWS_PHOTO_TABLE = "set_banner_photo";
    public static MStrSortSetI64DAO SET_BANNER_NEWS_PHOTO = new MStrSortSetI64DAO(BANNER_NEWS_PHOTO_TABLE, MSortType.DESC);
    
//    Photo Feed.
    public static final String commentPhotoIdGen = "feed";
    public static final String COMMENT_PHOTO_TABLE = "set_comment_photo";
    public static MStrSortSetI64DAO SET_COMMENT_PHOTO = new MStrSortSetI64DAO(COMMENT_PHOTO_TABLE, MSortType.DESC);
    
    //Photo Event
    public static final String eventPhotoIdGen = "eventphotoid";
    public static final String EVENT_PHOTO_TABLE = "set_event_photo";
    public static MStrSortSetI64DAO SET_EVENT_PHOTO = new MStrSortSetI64DAO(EVENT_PHOTO_TABLE, MSortType.DESC);
    
    //Photo Booth
    public static final String boothPhotoIdGen = "boothphotoid";
    public static final String BOOTH_PHOTO_TABLE = "set_booth_photo";
    public static MStrSortSetI64DAO SET_BOOTH_PHOTO = new MStrSortSetI64DAO(BOOTH_PHOTO_TABLE, MSortType.DESC);
    
    //Photo Map
    public static final String mapPhotoIdGen = "mapphotoid";
    public static final String MAP_PHOTO_TABLE = "set_map_photo";
    public static MStrSortSetI64DAO SET_MAP_PHOTO = new MStrSortSetI64DAO(MAP_PHOTO_TABLE, MSortType.DESC);
    
    //Model 3D
    public static final String model3dIdGen = "model3did";
    public static final String MODEL3D_PHOTO_TABLE = "set_model3d_photo";
    public static MStrSortSetI64DAO SET_MODEL3D_PHOTO = new MStrSortSetI64DAO(MODEL3D_PHOTO_TABLE, MSortType.DESC);
    
    public static String buildURIImgProduct(long pid){
        try {
            TMPhotoResult tmprs = ProductPhotoClient.getInstance().getMPhoto(pid);
            if(tmprs != null && tmprs.value != null){
                byte[] dataImg = tmprs.value.getData();
                String filename = tmprs.value.getFilename();
                String ext = FilenameUtils.getExtension(filename);
                String pidn = MIdNoise.enNoiseLId(pid);
                String pathSaveImg = Common.PRODUCT_PHOTO_LOAD_DIR_PATH + File.separator + pidn + "." + ext;
                String uriImg = Common.PRODUCT_PHOTO_LOAD_SERV_PATH + pidn + "." + ext;
                File img = new File(pathSaveImg);
                if(!img.exists()){
                    FileOutputStream fos = new FileOutputStream(img);
                    fos.write(dataImg, 0, dataImg.length);
                    fos.close();
                }
                return uriImg;
            }
        } catch (Exception e) {
        }
        return "";
    }
}
