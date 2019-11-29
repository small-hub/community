package life.rookie.community.controller;

import life.rookie.community.dto.AccessTokerDTO;
import life.rookie.community.dto.GithubUser;
import life.rookie.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @RequestMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state){
        AccessTokerDTO accessTokerDTO = new AccessTokerDTO();
        accessTokerDTO.setClient_id("ce21dda71a40823e5516");
        accessTokerDTO.setClient_secret("45a5d5fa0aad9fd7a0ec010bcc390d9de4e24c79");
        accessTokerDTO.setCode(code);
        accessTokerDTO.setRedirect_uri("http://localhost:8887/callback");
        accessTokerDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokerDTO);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println(user.getName());
        return "index";
    }
}
