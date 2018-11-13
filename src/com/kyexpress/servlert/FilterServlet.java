package com.kyexpress.servlert;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;
import com.kyexpress.callout.web.config.Configuration;
import com.kyexpress.callout.web.util.HttpClientUtil;
import com.kyexpress.callout.web.util.RequestBodyUtil;
import com.kyexpress.callout.web.util.StringUtil;


/**
 * @描述: Servlet拦截器转发请求到接口服务器
 * @版权: Copyright (c) 2018 
 * @公司: 跨越速运
 * @作者: Chasel
 * @版本: 1.0 
 * @创建日期: 2018年11月13日 
 * @创建时间: 上午11:28:24
 */
public class FilterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String prefix = "servlet";

    public FilterServlet() {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ignore
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            String requestURI = request.getRequestURI();
            if (!StringUtil.isEmpty(requestURI) && requestURI.length() > (prefix.length() + 1)) {
                requestURI = requestURI.substring(prefix.length()+1, requestURI.length());
                requestURI = Configuration.getString("system.ServerHost") + requestURI;

                BufferedReader bufferedReader = request.getReader();
                String requestBody = RequestBodyUtil.read(bufferedReader);

                Enumeration<String> enumer = request.getHeaderNames();
                Map<String, String> requestHeaderMap = new HashMap<String, String>();
                while (enumer.hasMoreElements()) {
                    String key = enumer.nextElement();
                    requestHeaderMap.put(key, request.getHeader(key));
                }

                String data = HttpClientUtil.getURLContent(requestURI, requestBody, requestHeaderMap);
                response.getWriter().println(data);
            }
        } catch (Exception e) {
            JSONObject result = new JSONObject();
            result.put("code", "-1");
            result.put("msg", "请求异常");
            response.getWriter().println(result.toJSONString());
        }

    }

}
