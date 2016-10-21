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

package com.mit.models;

import com.mit.dao.chat.RoomDAO;
import com.mit.entities.chat.Room;
import com.mit.utils.JsonUtils;
import java.util.*;

/**
 *
 * @author nghiatc
 * @since Mar 7, 2016
 */
public class ChatModel {
    public static ChatModel instance = new ChatModel();
    
    // Room
    public Map<String, Object> getListRoom(){
        int err = 0;
        Map<String, Object> rs = new HashMap<String, Object>();
        List<Room> rooms = RoomDAO.getInstance().listAllActive();
        List<Room.JsonBuilder> jrooms = new LinkedList<Room.JsonBuilder>();
        if(rooms != null && !rooms.isEmpty()){
            for(Room r : rooms){
                Room.JsonBuilder rb = r.buildJson();
                jrooms.add(rb);
            }
        }
        rs.put("rooms", jrooms);
        rs.put("err", err);
        return rs;
    }
    
    public Map<String, Object> getListSliceRoom(int offset, int count, int status){
        int err = 0;
        Map<String, Object> rs = new HashMap<String, Object>();
        List<Room> rooms = RoomDAO.getInstance().getSlideByStatus(offset, count, status, false);
        List<Room.JsonBuilder> jrooms = new LinkedList<Room.JsonBuilder>();
        if(rooms != null && !rooms.isEmpty()){
            for(Room r : rooms){
                Room.JsonBuilder rb = r.buildJson();
                jrooms.add(rb);
            }
        }
        rs.put("rooms", jrooms);
        rs.put("err", err);
        return rs;
    }
    
    public static void main(String[] args) {
        
        System.out.println(JsonUtils.Instance.toJson(ChatModel.instance.getListRoom()));
        
    }
    
    
}
