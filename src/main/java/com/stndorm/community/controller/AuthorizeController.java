package com.stndorm.community.controller;

import com.stndorm.community.dto.AccessTokenDTO;
import com.stndorm.community.dto.GithubUser;
import com.stndorm.community.mapper.UserMapper;
import com.stndorm.community.model.User;
import com.stndorm.community.provider.GithubProvider;
import com.stndorm.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 登录后获取code和state参数
 * 获取参数后传递给AccessTokenDTO通过GitHubProvider去获取accesstoken
 * 然后获取用户信息
 */
@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String ClientID;
    @Value("${github.client.uri}")
    private String ClientUri;
    @Value("${github.client.secret}")
    private String ClientSecret;


    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletResponse response){
        //通过reponse将信息写入cookie
        //当把HttpServletRequest写在方法中时，Spring就会自动把上下文中的request放入其中
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(ClientUri);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(ClientID);
        accessTokenDTO.setClient_secret(ClientSecret);
        //获取access token
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        //获取user信息
        GithubUser user = githubProvider.getUser(accessToken);
//        System.out.println(user.getId());

        if(user != null){
            //将登录的用户存入数据库中
            User user1 = new User();
            String token = UUID.randomUUID().toString();
            user1.setToken(token);
            user1.setAccountId(String.valueOf(user.getId()));
            user1.setName(user.getName());
            user1.setAvatarUrl(user.getAvatar_url());
            //判断数据库中是否有该user，有就更新，没有就存入
            userService.createOrUpdate(user1);
//            userMapper.insert(user1);
            //登录成功，写cookie和session
            //手动写入cookie,上面插入到数据库中就相当于写session
            response.addCookie(new Cookie("token", token));
//          //获取Session并写入user,如果不指定，会自动生成一个cookie
//          request.getSession().setAttribute("user", user);
            return "redirect:/";
        }else{
            //登录失败，重新登录
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response){
        //退出登录时删除session中的user信息
        request.getSession().removeAttribute("user");
        //退出登录时删除cookie中的token信息，通过新建一个同名cookie完成
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
