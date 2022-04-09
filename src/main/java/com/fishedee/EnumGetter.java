package com.fishedee;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class EnumGetter {

    private OkHttpClient okHttpClient;

    public void setOkHttpClient(OkHttpClient okHttpClient){
        this.okHttpClient = okHttpClient;
    }

    private ObjectMapper objectMapper;

    public void setObjectMapper(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public EnumDTO get(String url){
        try {
            Request request = new Request.Builder().url(url).get().build();
            Response response = okHttpClient.newCall(request).execute();
            byte[] responseData = response.body().bytes();

            EnumDTO enumDTO = objectMapper.readValue(responseData,EnumDTO.class);
            return enumDTO;
        }catch(IOException e){
            throw new CrashException("网络错误",e);
        }
    }
}
