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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eclipsesource.json.JsonObject;
import com.mit.dao.address.CountryDAO;
import com.mit.dao.address.StateDAO;
import com.mit.dao.photo.UserPhotoClient;
import com.mit.entities.address.Country;
import com.mit.entities.address.State;
import com.mit.midutil.MIdNoise;
import com.mit.mphoto.thrift.TMPhotoResult;
import com.mit.soy.site.client.ProductPhotoClient;
import com.mit.soy.site.common.Common;
import com.mit.soy.site.common.Configuration;
import com.mit.soy.site.entities.LoginSession;

import hapax.Template;
import hapax.TemplateCache;
import hapax.TemplateDataDictionary;
import hapax.TemplateDictionary;
import hapax.TemplateException;
import hapax.TemplateLoader;

/**
 * @author nghiatc
 * @since Dec 18, 2015
 */
public class BaseHandler extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(BaseHandler.class);

    public static final TemplateLoader Loader = TemplateCache.create("./views");

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        try {
            //Nếu truy suất vào url public thì không cần authentication.
//            String subPath = req.getServletPath();
//            if (!Common.publicUrl.contains(subPath)) {
//                // Kiểm tra login.
//                if (!isLoggedIn(req, resp)) {
//                    resp.sendRedirect(Configuration.APP_DOMAIN + "/soy/login");
//                    return;
//                }
//            }
            
            /*
             * forward resquest
             */
            if ("GET".equals(req.getMethod())) {
                this.doGet(req, resp);
            } else if ("POST".equals(req.getMethod())) {
                this.doPost(req, resp);
            }
        } catch (Exception ex) {
            logger.error("BaseHandler: " + ex, ex);
        }
    }

    public boolean isLoggedIn(HttpServletRequest req, HttpServletResponse resp) {
        LoginSession lgss = getSessionLoginSS(req);
        if (lgss != null && lgss.isIsLogin() && lgss.getRole() >= 0 && lgss.getUserId() >= 0) {
            return true;
        }
        return false;
    }

    public HttpSession createSessionLoginSS(HttpServletRequest req, HttpServletResponse resp, LoginSession lgss) {
        HttpSession session = req.getSession();
        session.setAttribute("loginss", lgss);
        //setting session to expiry in 24h.
        session.setMaxInactiveInterval(24 * 60 * 60);
//        Cookie sessionCookie = new Cookie("barssk", info.getSessionKey());
//        sessionCookie.setMaxAge(24*60*60);
//        resp.addCookie(sessionCookie);
        return session;
    }

    public LoginSession getSessionLoginSS(HttpServletRequest req) {
        return (LoginSession) req.getSession().getAttribute("loginss");
    }

    protected void renderCountryList(TemplateDataDictionary dic, String currentCountryCode) {
        List<Country> countries = CountryDAO.getInstance().getList();
        for (Country each : countries) {
            TemplateDataDictionary loopOption = dic.addSection("loop_country");
            loopOption.setVariable("ISO_CODE", each.getIsoCode());
            loopOption.setVariable("NAME", each.getName());
            if (each.getIsoCode().equalsIgnoreCase(currentCountryCode)) {
                loopOption.setVariable("selected", "selected");
            }
        }
    }

    protected void renderStatesOfCountry(TemplateDataDictionary dic, String countryCode, String currentState) {
        List<State> states = StateDAO.getInstance().getListByCountry(countryCode);
        System.out.println(currentState);
        for (State each : states) {
            TemplateDataDictionary loopState = dic.addSection("loop_state");
            loopState.setVariable("VALUE", each.getIsoCode());
            loopState.setVariable("NAME", each.getName());
            if (currentState.equalsIgnoreCase(each.getIsoCode())) {
                loopState.setVariable("selected", "selected");
            }
        }
    }

    protected void print(Object obj, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Connection", "Close");
            response.setStatus(HttpServletResponse.SC_OK);
            out = response.getWriter();
            out.print(obj);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void printJS(Object obj, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Connection", "Close");
            response.setStatus(HttpServletResponse.SC_OK);
            out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.print(obj);
            out.println("</script>");
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void printJSON(JsonObject json, HttpServletResponse resp) {
        PrintWriter out = null;
        try {
            resp.setContentType("application/json;charset=UTF-8");
            resp.setHeader("Connection", "Close");
            resp.setStatus(HttpServletResponse.SC_OK);
            out = resp.getWriter();
            out.print(json.toString());
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void printStrJSON(Object json, HttpServletResponse resp) {
        PrintWriter out = null;
        try {
            resp.setContentType("application/json;charset=UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            out = resp.getWriter();
            out.print(json);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void printXML(Object obj, HttpServletResponse response) {
        PrintWriter out = null;
        try {
            response.setContentType("text/xml;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Connection", "Close");
            response.setStatus(HttpServletResponse.SC_OK);
            out = response.getWriter();
            out.print(obj);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void printText(String str, HttpServletResponse resp) {
        PrintWriter out = null;
        try {
            resp.setContentType("text/plain; charset=utf-8");
            resp.setHeader("Connection", "Close");
            resp.setStatus(HttpServletResponse.SC_OK);
            out = resp.getWriter();
            out.println(str);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    protected void printFile(HttpServletResponse response, ByteArrayOutputStream bigfile, String fileName, String mimeType) throws IOException {
        ServletOutputStream stream = null;
        BufferedInputStream buf = null;
        try {
            stream = response.getOutputStream();

            //set response headers
            response.setBufferSize(1024 * 1024 * 6); // 6M
            response.setContentType(mimeType + ";charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            response.setHeader("Content-Length", String.valueOf(bigfile.size()));
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            // the contentlength
            response.setContentLength(bigfile.size());

            InputStream input = new ByteArrayInputStream(bigfile.toByteArray());
            buf = new BufferedInputStream(input);

            //read from the file; write to the ServletOutputStream
            byte[] bb = new byte[1024 * 1024 * 5]; // 5M
            int readByte;
            while ((readByte = buf.read(bb, 0, bb.length)) != -1) {
                stream.write(bb, 0, readByte);
            }
            //System.out.println("Complete send file media. Done!!!...");
        } catch (Exception e) {
            logger.error("printFile: ", e);
        } finally {
            if (stream != null) {
                stream.flush();
                stream.close();
            }
            if (buf != null) {
                buf.close();
            }
            if (bigfile != null) {
                bigfile.close();
            }
        }
    }

    protected String getClientIP(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    protected TemplateDataDictionary getDictionary() {
        TemplateDataDictionary dic = TemplateDictionary.create();
        return dic;
    }

    protected String getContentTemplate(String tplName, HttpServletRequest req) {
        try {
            TemplateDataDictionary dic = getDictionary();
            Template template = Loader.getTemplate(tplName);

            dic.setVariable("domain", Configuration.APP_DOMAIN);
            dic.setVariable("static-domain", Configuration.APP_STATIC_DOMAIN);

            if (template != null) {
                return template.renderToString(dic);
            }

        } catch (TemplateException ex) {
            logger.error("getContentTemplate: " + ex);
        }
        return "";
    }

    protected String applyTemplate(TemplateDataDictionary dic, String tplName, HttpServletRequest req) {
        try {
            Template template = Loader.getTemplate(tplName);
            dic.setVariable("domain", Configuration.APP_DOMAIN);
            dic.setVariable("static-domain", Configuration.APP_STATIC_DOMAIN);

            if (template != null) {
                return template.renderToString(dic);
            }

        } catch (TemplateException ex) {
            logger.error("applyTemplate: " + ex);
        }
        return "";
    }

    protected String applyLayoutWithAccInfo(TemplateDataDictionary dic, HttpServletRequest req, HttpServletResponse resp) {
        try {
            Template template = Loader.getTemplate("layout_with_acc_info.xtm");
            dic.setVariable("Project-Title", Configuration.APP_TITLE);
            dic.setVariable("domain", Configuration.APP_DOMAIN);
            dic.setVariable("static-domain", Configuration.APP_STATIC_DOMAIN);

//            AdminAccount account = (AdminAccount) req.getSession().getAttribute("accountInfo");
//            if(account != null && account.getId() > 0){
//                dic.setVariable("NAME_ADMIN", "Admin");
//                dic.setVariable("AVATAR_ADMIN", Configuration.AVATAR_DEFAULT);
//                dic.addSection("ISLOGIN");
//            } else{
//                dic.setVariable("NAME_ADMIN", "Guest");
//                dic.setVariable("AVATAR_ADMIN", Configuration.AVATAR_DEFAULT);
//                dic.addSection("NOLOGIN");
//            }

            if (isLoggedIn(req, resp)) {
                dic.addSection("IS_LOGIN");
            } else {
                dic.addSection("NO_LOGIN");
            }

            String svl = req.getServletPath();
            switch (svl.toLowerCase()) {
                case "/":
                case "/soy":
                case "/soy/home":
                    break;

                case "/soy/product":
                    dic.addSection("BC_PRODUCTS");
                    break;

                case "/soy/cart":
                    dic.addSection("BC_CART");
                    break;

                case "/soy/login":
                    dic.addSection("BC_LOGIN");
                    dic.setVariable("ACC_LOGIN", "active");
                    break;

                case "/soy/register":
                    dic.addSection("BC_REGISTER");
                    dic.setVariable("ACC_REGISTER", "active");
                    break;

                case "/soy/login/forgotpassword":
                    dic.addSection("BC_FORGOT_PASS");
                    dic.setVariable("ACC_FORGOT_PASS", "active");
                    break;

                case "/soy/account":
                    dic.addSection("BC_MY_ACC");
                    dic.setVariable("MY_ACC", "active");
                    break;

                case "/soy/account/password":
                    dic.addSection("BC_CHANGE_PASS");
                    dic.setVariable("ACC_CHANGE_PASS", "active");
                    break;
                case "/soy/account/shippingaddress":
                    dic.addSection("BC_SHIPPING_ADDRESS");
                    dic.setVariable("ACC_SHIPPING_ADDRESS", "active");
                    break;
                case "/soy/account/billingaddress":
                    dic.addSection("BC_BILLING_ADDRESS");
                    dic.setVariable("ACC_BILLING_ADDRESS", "active");
                    break;

                case "/soy/account/wishlist":
                    dic.addSection("BC_WISH_LIST");
                    dic.setVariable("ACC_WISH_LIST", "active");
                    break;

                case "/soy/account/orders":
                    dic.addSection("BC_ORDERS");
                    dic.setVariable("ACC_ORDERS", "active");
                    break;

                default:
                    break;
            }

            if (template != null) {
                return template.renderToString(dic);
            }
        } catch (TemplateException ex) {
            logger.error("applyLayoutWithAccInfo: ", ex);
        }
        return "";
    }

    protected String applyTemplateLayoutMain(TemplateDataDictionary dic, HttpServletRequest req, HttpServletResponse resp) {
        try {
            Template template = Loader.getTemplate("layout.xtm");
            dic.setVariable("Project-Title", Configuration.APP_TITLE);
            dic.setVariable("domain", Configuration.APP_DOMAIN);
            dic.setVariable("static-domain", Configuration.APP_STATIC_DOMAIN);

            String svl = req.getServletPath();
            if ("/".equalsIgnoreCase(svl) || "/soy".equalsIgnoreCase(svl) || "/soy/home".equalsIgnoreCase(svl)) {
                dic.setVariable("MN_HOME", "active");
            } else if ("/soy/product".equalsIgnoreCase(svl)) {
                dic.setVariable("MN_PRODUCT", "active");
            } else if ("/soy/cart".equalsIgnoreCase(svl)) {
                dic.setVariable("MN_CART", "active");
            } else if ("/soy/wishlist".equalsIgnoreCase(svl)) {
                dic.setVariable("MN_WISH_LIST", "active");
            } else if ("/soy/sale-representatives".equalsIgnoreCase(svl)) {
                dic.setVariable("MN_SALE_REPRESENTATIVES", "active");
            } else if ("/soy/distributor".equalsIgnoreCase(svl)) {
                dic.setVariable("MN_DIS", "active");
            } else if ("/soy/customer-testimonials".equalsIgnoreCase(svl)) {
                dic.setVariable("MN_CUSTOMER_TESTIMONIALS", "active");
            } else if ("/soy/faq".equalsIgnoreCase(svl)) {
                dic.setVariable("MN_FAQ", "active");
            } else if ("/soy/contactus".equalsIgnoreCase(svl)) {
                dic.setVariable("MN_CONTACT_US", "active");
            }

            int numberItem = 0;
            long wishListSize = 0;
            if (isLoggedIn(req, resp)) {
                TemplateDataDictionary userInfo = dic.addSection("IS_LOGIN");
                LoginSession userSession = getSessionLoginSS(req);
                if (userSession != null) {
                    userInfo.setVariable("USER_NAME", userSession.getName());
                }
            } else {
                dic.addSection("NO_LOGIN");
            }
            dic.setVariable("NUMBER_ITEM", String.valueOf(numberItem));
            dic.setVariable("WISH_LIST_SIZE", String.valueOf(wishListSize));

            if (template != null) {
                return template.renderToString(dic);
            }
        } catch (TemplateException ex) {
            logger.error("applyTemplateLayoutMain: " + ex);
        }
        return "";
    }

    public void renderErrorPage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            TemplateDataDictionary dic = getDictionary();

            dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "error500.xtm", req));
            print(applyTemplateLayoutMain(dic, req, resp), resp);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void renderPageNotFound(HttpServletRequest req, HttpServletResponse resp) {
        try {
            TemplateDataDictionary dic = getDictionary();

            dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "error404.xtm", req));
            print(applyTemplateLayoutMain(dic, req, resp), resp);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public void renderDenyPage(HttpServletRequest req, HttpServletResponse resp) {
        try {
            TemplateDataDictionary dic = getDictionary();
            TemplateLoader templateLoader = TemplateCache.create("./views");
            Template template = templateLoader.getTemplate("deny.xtm");

            dic.setVariable("Project-Title", Configuration.APP_TITLE);
            dic.setVariable("domain", Configuration.APP_DOMAIN);
            dic.setVariable("domain-home", Configuration.APP_DOMAIN_HOME);
            dic.setVariable("static-domain", Configuration.APP_STATIC_DOMAIN);

            String content = "";
            if (template != null) {
                content = template.renderToString(dic);
            }
            print(content, resp);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public String buildURIImg(long pid) {
        try {
            TMPhotoResult tmprs = ProductPhotoClient.getInstance().getMPhoto(pid);
            if (tmprs != null && tmprs.value != null) {
                byte[] dataImg = tmprs.value.getData();
                String filename = tmprs.value.getFilename();
                String ext = FilenameUtils.getExtension(filename);
                String pidn = MIdNoise.enNoiseLId(pid);
                String pathSaveImg = Common.swdirPDB + File.separator + "pt" + File.separator + pidn + "." + ext;
                String uriImg = Configuration.APP_STATIC_DOMAIN + "/pdb/pt/" + pidn + "." + ext;
                File img = new File(pathSaveImg);
                if (!img.exists()) {
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

    public String buildURIUserImg(long pid) {
        try {
            TMPhotoResult tmprs = UserPhotoClient.getInstance().getMPhoto(pid);
            if (tmprs != null && tmprs.value != null) {
                byte[] dataImg = tmprs.value.getData();
                String filename = tmprs.value.getFilename();
                String ext = FilenameUtils.getExtension(filename);
                String pidn = MIdNoise.enNoiseLId(pid);
                String pathSaveImg = Common.swdirPDB + File.separator + "avatar" + File.separator + pidn + "." + ext;
                String uriImg = Configuration.APP_STATIC_DOMAIN + "/pdb/avatar/" + pidn + "." + ext;
                File img = new File(pathSaveImg);
                if (!img.exists()) {
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

    protected String getCurrentDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
    
    public boolean validParams(HttpServletRequest req, List<String> params, JsonObject result) {
    	boolean rs = true;
    	if (params != null && !params.isEmpty()) {
    		for (String param : params) {
    			String value = req.getParameter(param);
    			if (value == null || value.isEmpty()) {
    				result.set("err", -1);
    				result.set("msg", "Params invalid");
    				rs = false;
    				break;
    			}
    		}
    	}
    	return rs;
    }

}
