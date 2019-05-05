package com.ajin.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应对象
 * @Auther: ajin
 * @Date: 2019/4/4 20:59
 */
// get set
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T>  implements Serializable {

    private Integer code;
    private String message;
    private T data;

    public CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
