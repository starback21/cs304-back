package edu.sustech.common.result;

import lombok.Getter;

@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限")
    ;

    private Integer code;

    private String msg;

    private ResultCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
