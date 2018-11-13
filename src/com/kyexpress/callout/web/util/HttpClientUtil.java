package com.kyexpress.callout.web.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.apache.log4j.Logger;
import com.kyexpress.callout.web.config.Configuration;


public class HttpClientUtil {

    private static Logger logger = Logger.getLogger(Configuration.class);

    public static String charset = "UTF-8";

    public static String getURLContent(String urlPath, String requestData, Map<String, String> requestHeaderMap) {
        HttpURLConnection httpURLConnection = null;
        StringBuilder strBuffer = new StringBuilder();
        OutputStreamWriter output = null;
        try {
            URL url = new URL(urlPath);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDefaultUseCaches(false);
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(60000);

            // 设置请求头
            for (String key : requestHeaderMap.keySet()) {
                httpURLConnection.setRequestProperty(key, requestHeaderMap.get(key));
            }

            output = new OutputStreamWriter(httpURLConnection.getOutputStream(), charset);
            output.write(requestData);
            output.flush();

            int repCode = httpURLConnection.getResponseCode();
            if (repCode == 200) {
                int count = 0;
                char[] chBuffer = new char[1024];
                BufferedReader input = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), charset));
                while ((count = input.read(chBuffer)) != -1) {
                    strBuffer.append(chBuffer, 0, count);
                }
            }
        } catch (Exception ex) {
            logger.error("获取内容失败", ex);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            } catch (Exception ex) {
                logger.error("关闭连接失败", ex);
            }
        }
        return strBuffer.toString();
    }
}
