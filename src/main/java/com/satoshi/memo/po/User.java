package com.satoshi.memo.po;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Author:tibashiki
 * @DATE:2023/05/1711:25
 * @Description:
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer userId;
    private String uname,upwd,head,nick,mood;


}
