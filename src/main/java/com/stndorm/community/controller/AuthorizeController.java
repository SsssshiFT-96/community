package com.stndorm.community.controller;

import com.stndorm.community.dto.AccessTokenDTO;
import com.stndorm.community.dto.GithubUser;
import com.stndorm.community.mapper.UserMapper;
import com.stndorm.community.model.User;
import com.stndorm.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
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
    private UserMapper userMapper;

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
        System.out.println(user.getId());

        if(user != null){
            //将登录的用户存入数据库中
            User user1 = new User();
            String token = UUID.randomUUID().toString();
            user1.setToken(token);
            user1.setAccountId(String.valueOf(user.getId()));
            user1.setName(user.getName());
            user1.setGmtCreate(System.currentTimeMillis());
            user1.setGmtModified(user1.getGmtCreate());
            user1.setAvatarUrl(user.getAvatar_url());
            userMapper.insert(user1);
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
}
