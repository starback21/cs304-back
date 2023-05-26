package edu.sustech.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.sustech.auth.service.SysUserService;
import edu.sustech.common.handler.SpecialException;
import edu.sustech.common.jwt.JwtHelper;
import edu.sustech.common.result.Result;
import edu.sustech.common.utils.MD5;
import edu.sustech.model.system.SysUser;
import edu.sustech.vo.system.LoginVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/user")
public class LoginController {
    /**
     * 登录
     * @return
     */
    @Autowired
    SysUserService userService;
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        String account = loginVo.getUsername();
        String password = loginVo.getPassword();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getName,account);
        SysUser sysUser = userService.getOne(wrapper)   ;
        if(null == sysUser) {
            return Result.fail(400,"用户名或密码错误");
        }
        if(!MD5.encrypt(password).equals(sysUser.getPassword())) {
            return Result.fail(400,"用户名或密码错误");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("token", JwtHelper.createToken(sysUser.getId(), sysUser.getName()));
        if(Objects.equals(account, "admin")){
//            map.put("token", "qwefqwadf");
            map.put("identity","admin");
        }else {
//            map.put("token", "asdaccas");
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
