package edu.sustech.common.handler;


import edu.sustech.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * 自定义全局异常类
 *
 */
@Data
public class SpecialException extends RuntimeException {

    private Integer code;

    private String msg;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param code
     * @param msg
     */
    public SpecialException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public SpecialException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMsg());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMsg();
    }

    @Override
    public String toString() {
        return "Exception{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
