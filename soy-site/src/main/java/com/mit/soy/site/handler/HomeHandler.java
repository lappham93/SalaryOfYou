/*
 * Copyright 2015 nghiatc.
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

package com.mit.soy.site.handler;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mit.entities.salary.JobShare;
import com.mit.models.SalaryModel;
import com.mit.models.StatisticsModel;
import com.mit.utils.JsonUtils;

import hapax.TemplateDataDictionary;

/**
 * @author nghiatc
 * @since Dec 18, 2015
 */
public class HomeHandler extends BaseHandler {
    private static Logger logger = LoggerFactory.getLogger(HomeHandler.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            TemplateDataDictionary dic = getDictionary();
            renderHome(dic, req, resp);
            dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "home.xtm", req));
            print(applyTemplateLayoutMain(dic, req, resp), resp);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void renderHome(TemplateDataDictionary dic, HttpServletRequest req, HttpServletResponse resp) {
    }
    
    public static void shareSalary() {
    	Map<String, Object> sj = SalaryModel.Instance.shareJob(1L, 1L, 2, "", "Ho Chi Minh", "Viet Nam", "US", 15);
    	JobShare jobShare = (JobShare)sj.get("jobShare");
    	Map<Integer, Double> salaryStat = StatisticsModel.Instance.getMeanSal(jobShare);
    	System.out.println(JsonUtils.Instance.toJson(salaryStat));
    }
    
    public static void main(String[] args) {
    	shareSalary();
    }
}
