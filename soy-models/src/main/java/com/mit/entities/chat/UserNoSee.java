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

import java.util.*;

/**
 *
 * @author nghiatc
 * @since Apr 27, 2016
 */
public class UserNoSee {
    private int id;
    private Set<Integer> setUserId;

    public UserNoSee() {
    }

    public UserNoSee(int id, Set<Integer> setUser) {
        this.id = id;
        this.setUserId = setUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Integer> getSetUserId() {
        return setUserId;
    }

    public void setSetUserId(Set<Integer> setUserId) {
        this.setUserId = setUserId;
    }

    @Override
    public String toString() {
        return "UserNoSee{" + "id=" + id + ", setUserId=" + setUserId + '}';
    }
    
    
}
