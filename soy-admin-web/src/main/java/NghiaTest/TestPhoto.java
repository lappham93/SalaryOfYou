/*
 * Copyright 2016 nghiatc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package NghiaTest;

import com.mit.augusta.admin.client.ProductPhotoClient;
import com.mit.dao.mid.MIdGenLongDAO;
import com.mit.dao.photo.BannerNewsPhotoClient;
import com.mit.dao.photo.PhotoCommon;
import com.mit.mphoto.thrift.TMPhoto;
import com.mit.mphoto.thrift.TMPhotoResult;
import com.mit.utils.MIMETypeUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author nghiatc
 * @since Jan 12, 2016
 */
public class TestPhoto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
//        long idPri = MIdGenLongDAO.getInstance(PhotoCommon.productPhotoIdGen).getNext();
//        if(idPri >= 0){
//            TMPhoto tmp = new TMPhoto();
//            tmp.setId(idPri);
//            String filename = "cam.jpg";
//            tmp.setFilename(filename);
//            //File file = new File("/home/nghiatc/cyogel2/cyogel-admin-web/public/common/imgs/cam.jpg");
//            Path path = Paths.get("/home/nghiatc/cyogel2/cyogel-admin-web/public/common/imgs/cam.jpg");
//            byte[] data = Files.readAllBytes(path);
//            tmp.setData(data);
//            String ext = FilenameUtils.getExtension(filename);
//            String contentType = MIMETypeUtil.getInstance().getMIMETypeImage(ext);
//            tmp.setContentType(contentType);
//            int err = ProductPhotoClient.getInstance().putMPhoto(tmp);
//            System.out.println("err: " + err);
//            System.out.println(tmp);
//        }
        
        ///Put
        
//        TMPhoto tmp = new TMPhoto();
//        tmp.setId(2L);
//        String filename = "cam.jpg";
//        tmp.setFilename(filename);
//        //File file = new File("/home/nghiatc/cyogel2/cyogel-admin-web/public/common/imgs/cam.jpg");
//        Path path = Paths.get("/home/nghiatc/cyogel2/cyogel-admin-web/public/common/imgs/cam.jpg");
//        byte[] data = Files.readAllBytes(path);
//        tmp.setData(data);
//        String ext = FilenameUtils.getExtension(filename);
//        String contentType = MIMETypeUtil.getInstance().getMIMETypeImage(ext);
//        tmp.setContentType(contentType);
//        int err = ProductPhotoClient.getInstance().putMPhoto(tmp);
//        System.out.println("err: " + err);
//        System.out.println(tmp);
        
        
        //Get.
//        TMPhotoResult tmpGet = ProductPhotoClient.getInstance().getMPhoto(2L);
//        System.out.println("tmpGet: " + tmpGet);
        
        //Get.
        TMPhotoResult tmpThumb = BannerNewsPhotoClient.getInstance().getMPhoto(31L);
        System.out.println("tmpThumb: " + tmpThumb);
    }

}
