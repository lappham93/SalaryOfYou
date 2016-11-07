package com.mit.soy.site.handler;

import hapax.TemplateDataDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by truong on 22/03/2016.
 */
public class StaticPageHandler extends BaseHandler {
    private static Logger logger = LoggerFactory.getLogger(StaticPageHandler.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String path = req.getServletPath();
            TemplateDataDictionary dic = getDictionary();
            switch (path.toLowerCase()) {
                case "/soy/sale-representatives":
                    dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "sale_represent", req));
                    break;
                case "/soy/customer-testimonials":
                    dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "testimonial", req));
                    break;
                case "/soy/faq":
                    dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "faq", req));
                    break;
                case "/soy/terms-of-use":
                    dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "term-of-use", req));
                    break;
                case "/soy/privacy-policy":
                    dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "privacy-policy", req));
                    break;
                case "/soy/delivery-policy":
                    dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "delivery-policy", req));
                    break;
                case "/soy/account/refund-policy":
                    dic.setVariable("MAIN_CONTENT", applyTemplate(dic, "refund-policy", req));
                    break;
                default:
                    break;
            }
            print(applyTemplateLayoutMain(dic, req, resp), resp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
