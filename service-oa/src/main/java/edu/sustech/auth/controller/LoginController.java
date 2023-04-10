package edu.sustech.auth.controller;

import com.alibaba.fastjson.JSONObject;
import edu.sustech.common.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Api(tags = "后台登录管理")
@RestController
@CrossOrigin
@RequestMapping("/user")
public class LoginController {
    /**
     * 登录
     * @return
     */
    @PostMapping("login")
    public Result login(@RequestBody JSONObject jsonParam) {
        String account = jsonParam.get("account").toString();
        String password = jsonParam.get("password").toString();
        Map<String, Object> map = new HashMap<>();
        if(Objects.equals(account, "admin") && Objects.equals(password, "12345")){
            map.put("token","afasfsafawsf");
            map.put("identity","admin");
        }else {
            map.put("token","sadqwrwqr");
            map.put("identity","user");
        }
        return Result.ok(map);
    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("info")
    public Result info() {
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name","admin");
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.ok(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("logout")
    public Result logout(){
        return Result.ok();
    }

}
