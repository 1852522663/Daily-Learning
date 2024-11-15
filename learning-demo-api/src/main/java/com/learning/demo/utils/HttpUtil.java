package com.learning.demo.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpUtils - 一个用于发送HTTP请求的实用工具类
 * 该类支持GET、POST、PUT、DELETE请求方法，具有失败重试、日志记录和自定义请求头的功能。
 * Created by yuehewei <yuehewei@kuaishou.com> on 2023-10-19
 */
public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    private static final int MAX_RETRY = 3; // 最大重试次数
    private static final int CONNECTION_TIMEOUT = 5000; // 连接超时时间（毫秒）
    private static final int READ_TIMEOUT = 5000; // 读取超时时间（毫秒）

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    /**
     * 发送GET请求
     * @param url 请求的URL
     * @param headers 请求头
     * @return 响应内容
     */
    public static String get(String url, Map<String, String> headers) {
        return sendRequest(url, GET, null, headers);
    }

    /**
     * 发送POST请求
     * @param url 请求的URL
     * @param body 请求体
     * @param headers 请求头
     * @return 响应内容
     */
    public static String post(String url, Map<String, Object> body, Map<String, String> headers) {
        String requestBody = mapToRequestBody(body);
        return sendRequest(url, POST, requestBody, headers);
    }

    /**
     * 发送PUT请求
     * @param url 请求的URL
     * @param body 请求体
     * @param headers 请求头
     * @return 响应内容
     */
    public static String put(String url, Map<String, Object> body, Map<String, String> headers) {
        String requestBody = mapToRequestBody(body);
        return sendRequest(url, PUT, requestBody, headers);
    }

    /**
     * 发送DELETE请求
     * @param url 请求的URL
     * @param headers 请求头
     * @return 响应内容
     */
    public static String delete(String url, Map<String, String> headers) {
        return sendRequest(url, DELETE, null, headers);
    }

    // 辅助方法，根据Map<String, Object>构建请求体
    private static String mapToRequestBody(Map<String, Object> body) {
        if (body == null || body.isEmpty()) {
            return "";
        }
        StringBuilder requestBody = new StringBuilder();
        for (Map.Entry<String, Object> entry : body.entrySet()) {
            if (requestBody.length() > 0) {
                requestBody.append("&");
            }
            requestBody.append(entry.getKey()).append("=").append(entry.getValue().toString());
        }
        return requestBody.toString();
    }

    // 辅助方法，发送HTTP请求
    private static String sendRequest(String url, String method, String body, Map<String, String> headers) {
        int retryCount = 0;
        String response = null;
        while (retryCount < MAX_RETRY) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod(method);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.setReadTimeout(READ_TIMEOUT);

                if (headers != null) {
                    for (Map.Entry<String, String> entry : headers.entrySet()) {
                        connection.setRequestProperty(entry.getKey(), entry.getValue());
                    }
                }

                if (body != null) {
                    connection.setDoOutput(true);
                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(body.getBytes("UTF-8"));
                    }
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }
                        response = content.toString();
                    }
                    connection.disconnect();
                    break; // 请求成功，不再重试
                } else {
                    // 请求失败，记录日志并重试
                    logger.info("Request failed with response code: " + responseCode);
                    retryCount++;
                }
            } catch (IOException e) {
                // 请求异常，记录日志并重试
                logger.info("Request failed with exception: " + e.getMessage());
                retryCount++;
            }
        }
        return response;
    }
}
