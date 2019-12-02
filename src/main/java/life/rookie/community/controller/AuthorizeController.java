package life.rookie.community.controller;

import life.rookie.community.dto.AccessTokerDTO;
import life.rookie.community.dto.GithubUser;
import life.rookie.community.mapper.UserMapper;
import life.rookie.community.model.User;
import life.rookie.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String cliendId;
    @Value("${github.client.secret}")
    private String cliendSecret;
    @Value("${github.client.uri}")
    private String redirectUri;
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokerDTO accessTokerDTO = new AccessTokerDTO();
        accessTokerDTO.setClient_id(cliendId);
        accessTokerDTO.setClient_secret(cliendSecret);
        accessTokerDTO.setCode(code);
        accessTokerDTO.setRedirect_uri(redirectUri);
        accessTokerDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokerDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser !=null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            //登录失败,重新登录
            return "redirect:/";
        }
    }
}
