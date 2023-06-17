package com.satoshi.memo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author:tibashiki
 * @DATE:2023/05/2314:27
 * @Description:
 */
@Getter
@Setter
public class NoteVo {
    private String groupName;
    private long noteCount;
    private Integer typeId;//类型id
}
