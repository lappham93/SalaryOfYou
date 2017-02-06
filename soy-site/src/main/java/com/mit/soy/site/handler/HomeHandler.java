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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.mit.entities.salary.JobShare;
import com.mit.entities.salary.SalaryDistributor;
import com.mit.entities.salary.SalaryStatisticsType;
import com.mit.midutil.MIdNoise;
import com.mit.models.SalaryModel;
import com.mit.models.StatisticsModel;
import com.mit.soy.site.utils.HttpHelper;
import com.mit.soy.site.utils.UploadFormUtil;
import com.mit.utils.JsonUtils;

import hapax.TemplateDataDictionary;

/**
 * @author nghiatc
 * @since Dec 18, 2015
 */
public class HomeHandler extends BaseHandler {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3107244131461635259L;
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
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    	try {
            JsonObject result = new JsonObject();
            result.set("err", -1);
            result.set("msg", "Execute fail. Please try again.");

            String action = "";
            String callback = "";
            if (HttpHelper.isMultipartRequest(req)) { // process request multipart form data.
                Map<String, FileItem> mapFile = new HashMap<String, FileItem>();
                Map<String, String> params = new HashMap<String, String>();
                UploadFormUtil.getInstance().getMapFormUpload(req, mapFile, params);
                if (params != null && !params.isEmpty()) {
                    System.out.println("multipart params: " + params);
                    System.out.println("multipart mapFile: " + mapFile);
                    callback = params.containsKey("callback") ? params.get("callback") : "";
                    action = params.containsKey("action") ? params.get("action") : "";
                    if (action != null && !action.isEmpty()) {
                    }
                }
            } else { // process request nomal.
                action = req.getParameter("action");
                callback = req.getParameter("callback");
                if(action != null && !action.isEmpty()) {
                	if ("sharesalary".equalsIgnoreCase(action)) {
                		shareSalary(req, resp, result);
                	}
                } 
            }

            //render JsonObject.
            if (action != null && !action.isEmpty()) {
                if (HttpHelper.isAjaxRequest(req)) {
                    if (callback != null && !callback.isEmpty()) {
                        printStrJSON(callback + "(" + result.toString() + ")", resp);
                    } else {
                        printStrJSON(result.toString(), resp);
                    }
                } else {
                    TemplateDataDictionary dic = getDictionary();
                    dic.setVariable("callback", callback);
                    dic.setVariable("data", result.toString());
                    print(applyTemplate(dic, "iframe_callback", req), resp);
                }
            } else {
                printStrJSON(result.toString(), resp);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    private void renderHome(TemplateDataDictionary dic, HttpServletRequest req, HttpServletResponse resp) {
    	
    }
    
    private void shareSalary(HttpServletRequest req, HttpServletResponse resp, JsonObject result) {
    	List<String> reParams = Arrays.asList("cate", "job", "yearExperiences", "skill", "city", "country", "companyCountry", "salary");
    	if (validParams(req, reParams, result)) {
    		long categoryId = MIdNoise.deNoiseLId(req.getParameter("cate"));
    		long jobId = MIdNoise.deNoiseLId("job");
    		int yexperiences = NumberUtils.toInt(req.getParameter("yearExperiences"));
    		String skill = req.getParameter("skill");
    		String city = req.getParameter("city");
    		String country = req.getParameter("country");
    		String companyCountry = req.getParameter("companyCountry");
    		double salary = NumberUtils.toDouble(req.getParameter("salary"));
    		
	    	Map<String, Object> sj = SalaryModel.Instance.shareJob(categoryId, jobId, yexperiences, skill, city, country, companyCountry, salary);
	    	int err = (int)sj.get("err");
	    	if (err >= 0) {
		    	JobShare jobShare = (JobShare)sj.get("jobShare");
		    	Map<Integer, Double> salaryStat = StatisticsModel.Instance.getMeanSal(jobShare);
		    	Map<Integer, Map<Integer, SalaryDistributor>> salaryDis = StatisticsModel.Instance.getDistributeSal(jobShare);
		    	System.out.println(JsonUtils.Instance.toJson(salaryStat));
		    	System.out.println(JsonUtils.Instance.toJson(salaryDis));
		    	//render to view
		    	JsonArray salariesJ = new JsonArray();
		    	for (int type : salaryDis.keySet()) {
		    		JsonObject salaryJ = new JsonObject();
		    		JsonArray data = new JsonArray();
		    		Map<Integer, SalaryDistributor> dis = salaryDis.get(type);
		    		for (int i : dis.keySet()) {
		    			JsonObject ele = new JsonObject();
		    			SalaryDistributor aDis = dis.get(i);
		    			ele.set("label", aDis.getMinRange() + "-" + aDis.getMaxRange());
		    			ele.set("value", aDis.getEleCount());
		    			data.add(ele);
		    		}
		    		salaryJ.add("data", data);
		    		String chartName = "";
		    		if (type == SalaryStatisticsType.ALL.getValue()) {
		    			chartName = "DATA1";
		    		} else if (type == SalaryStatisticsType.EXPERIENCE.getValue()) {
		    			chartName = "DATA2";
		    		} else if (type == SalaryStatisticsType.JOB.getValue()) {
		    			chartName = "DATA3";
		    		} else if (type == SalaryStatisticsType.PLACE.getValue()) {
		    			chartName = "DATA4";
		    		}
		    		salaryJ.add("name", chartName);
//		    		dic.setVariable(chartName, data.toString());
		    		salariesJ.add(salaryJ);
		    	}
		    	result.set("err", 0);
		    	result.set("salaries", salariesJ);
	    	} else {
	    		result.set("err", -1);
	    		result.set("msg", "Server error. ");
	    	}
    	}
    }
    
    public static void shareSalary() {
    	Map<String, Object> sj = SalaryModel.Instance.shareJob(1L, 1L, 1, "", "Ho Chi Minh", "Viet Nam", "Viet Nam", 12);
    	int err = (int)sj.get("err");
    	if (err >= 0) {
	    	JobShare jobShare = (JobShare)sj.get("jobShare");
	    	Map<Integer, Double> salaryStat = StatisticsModel.Instance.getMeanSal(jobShare);
	    	Map<Integer, Map<Integer, SalaryDistributor>> salaryDis = StatisticsModel.Instance.getDistributeSal(jobShare);
	    	System.out.println(JsonUtils.Instance.toJson(salaryStat));
	    	System.out.println(JsonUtils.Instance.toJson(salaryDis));
    	}
    }
    
    public static void main(String[] args) {
    	shareSalary();
    }
}
