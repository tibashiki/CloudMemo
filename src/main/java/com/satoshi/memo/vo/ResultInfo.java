package com.satoshi.memo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author:tibashiki
 * @DATE:2023/05/1711:33
 * @Description:封装返回的结果类
 * 状态码
 *  成功1失败0
 *  提示信息
 *  返回对象（字符串，javaBean，集合，Map）
 */
@Getter
@Setter
public class ResultInfo<T> {
    private Integer code;//状态码 成功1失败0
    private String msg;//提示信息
    private T result;//返回对象，可以是任意类型
}
