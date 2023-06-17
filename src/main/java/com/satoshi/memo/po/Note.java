package com.satoshi.memo.po;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Author:tibashiki
 * @DATE:2023/05/235:13
 * @Description:
 */
@Getter
@Setter
public class Note {
    private Integer noteId,typeId;
    private String title,content;
    private Date pubTime;
    private String typeName;
}

