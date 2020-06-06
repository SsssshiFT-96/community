package com.stndorm.community.provider;

import com.alibaba.fastjson.JSON;
import com.stndorm.community.dto.AccessTokenDTO;
import com.stndorm.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * provider:提供对第三方支持的能力
 *
 */
@Component
public class GithubProvider {
    /**
     * GithubProvider通过post返回accesstoken
     * 这里导入Okhttp中的代码，还需要导入相应的jar包。
     */

    public String getAccessToken(AccessTokenDTO accessTokenDTO){

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        //将java信息转换成JSON形式的String信息
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            //获取access token,并将得到的字符串切割获取想要的那一段
            String string = response.body().string();
            String[] split = string.split("&");
            String token = split[0].split("=")[1];
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回user信息
     */
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            //获取user信息
            String string = response.body().string();
            //将string的JSON对象转换成java的类对象
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
