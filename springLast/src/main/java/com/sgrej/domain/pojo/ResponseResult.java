
package com.sgrej.domain.pojo;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class ResponseResult<T> implements Serializable {

    private Integer code; //编码：200成功，0和其它数字为失败
    private String msg; //错误信息
    private T data; //数据

    public static <T> ResponseResult<T> success() {
        ResponseResult<T> result = new ResponseResult<T>();
        result.code = 200;
        return result;
    }

    public static <T> ResponseResult<T> success(T object) {
        ResponseResult<T> result = new ResponseResult<T>();
        result.data = object;
        result.code = 200;
        return result;
    }

    public static <T> ResponseResult<T> error(String msg) {
        ResponseResult result = new ResponseResult();
        result.msg = msg;
        result.code = 0;
        return result;
    }


    public static <T> ResponseResult<T> error(Integer code,String msg) {
        ResponseResult result = new ResponseResult();
        result.msg = msg;
        result.code = code;
        return result;
    }

}
