package com.stndorm.community.controller;

import com.stndorm.community.dto.AccessTokenDTO;
import com.stndorm.community.dto.GithubUser;
import com.stndorm.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登录后获取code和state参数
 * 获取参数后传递给AccessTokenDTO通过GitHubProvider去获取accesstoken
 * 然后获取用户信息
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri("http://localhost:8081/callback");
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id("486218ff86ab2b6a8825");
        accessTokenDTO.setClient_secret("1ca0f7e7d70b339dd483ba6e7d999e9fabfbc4dd");
        //获取access token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //获取user信息
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getId());
        return "index";
    }
}
