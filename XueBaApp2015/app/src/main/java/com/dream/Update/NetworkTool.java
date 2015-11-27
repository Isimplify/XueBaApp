package com.dream.Update;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by 夏目 on 2015/11/8.
 */
public class NetworkTool {
    public static String getContent(String url) throws Exception {
        StringBuilder sb = new StringBuilder();

        HttpClient client = new DefaultHttpClient();
        HttpParams httpParams = client.getParams();
        // 设置网络超时参数
        HttpConnectionParams.setConnectionTimeout(httpParams, 5000);//3000
        HttpConnectionParams.setSoTimeout(httpParams, 50000);//5000
        HttpResponse response = client.execute(new HttpGet(url));
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    entity.getContent(), "UTF-8"), 8192);

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            reader.close();
        }
        return sb.toString();
    }
}
