package org.gdkm.springbootxiangmushizhan.web.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class LoginController {
    @GetMapping(value = "/login")
    public String login(HttpServletRequest request, Map map){
        String referer = request.getHeader("Referer");
        String url = request.getParameter("url");
        if (url != null && !url.equals("")){
            map.put("url",url);
        }else if (referer != null && referer.contains("/login")){
            map.put("url","");
        }else {
            map.put("url",referer);
        }
        return "comm/login";
    }

    @GetMapping(value = "/errorPage/{page}/{code}")
    public String AccessExceptionHandler(@PathVariable("page") String page,@PathVariable("code") String code){
        return page+"/"+code;
    }
}
