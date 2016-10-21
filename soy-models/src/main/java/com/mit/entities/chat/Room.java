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

package com.mit.entities.chat;

import com.mit.utils.LinkBuilder;

/**
 *
 * @author nghiatc
 * @since Mar 7, 2016
 */
public class Room {
    private int id;
    private String name;
    private int order;
    private int size;
    private long photoId;
    private String desc;
    private int status;
	private long createTime;
	private long updateTime;
    
    public Room(){
        super();
    }

    public Room(int id, String name, int order, int size, long photoId, String desc, int status, long createTime, long updateTime) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.size = size;
        this.photoId = photoId;
        this.desc = desc;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(long photoId) {
        this.photoId = photoId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", name=" + name + ", order=" + order + ", size=" + size + ", photoId=" + photoId + ", desc=" + desc + ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime + '}';
    }
    
    public JsonBuilder buildJson() {
		return new JsonBuilder(this);
	}
    
    public static class JsonBuilder {
        private int id;
        private String name;
        private int order;
        private int size;
        private String avtRoom;
        private String desc;
        private int status;
        private long createTime;
        private long updateTime;
        private String url;
        private String urlUpload;
        private int numCurUser;

        public JsonBuilder(Room r) {
            this.id = r.id;
            this.name = r.name;
            this.order = r.order;
            this.size = r.size;
            this.avtRoom = LinkBuilder.buildAvtRoomLink(r.photoId);
            this.desc = r.desc;
            this.status = r.status;
            this.createTime = r.createTime;
            this.updateTime = r.updateTime;
            this.url = LinkBuilder.buildChatRoomLink(r.id);
            this.urlUpload = LinkBuilder.buildUploadRoomLink(r.id);
            this.numCurUser = (int) RoomSizeRedis.instance.getCurSize(r.id);
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getAvtRoom() {
            return avtRoom;
        }

        public void setAvtRoom(String avtRoom) {
            this.avtRoom = avtRoom;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrlUpload() {
            return urlUpload;
        }

        public void setUrlUpload(String urlUpload) {
            this.urlUpload = urlUpload;
        }

        public int getNumCurUser() {
            return numCurUser;
        }

        public void setNumCurUser(int numCurUser) {
            this.numCurUser = numCurUser;
        }

        @Override
        public String toString() {
            return "JsonBuilder{" + "id=" + id + ", name=" + name + ", order=" + order + ", size=" + size + ", avtRoom=" + avtRoom + ", desc=" + desc + ", status=" + status + ", createTime=" + createTime + ", updateTime=" + updateTime + ", url=" + url + ", urlUpload=" + urlUpload + ", numCurUser=" + numCurUser + '}';
        }
        
        
        
        
    }
    
    
    
}
