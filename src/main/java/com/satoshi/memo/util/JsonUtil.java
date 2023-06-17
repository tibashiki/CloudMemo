package com.satoshi.memo.util;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author:tibashiki
 * @DATE:2023/05/2115:13
 * @Description:
 */
public class JsonUtil {
    public static void toJson(HttpServletResponse resp,Object resultInfo) {
        PrintWriter out = null;
        try {
            //设置相应类型以及编码格式
            resp.setContentType("application/json;charset=UTF-8");
            String json = JSON.toJSONString(resultInfo);
            //得到字符输出流
            out = resp.getWriter();
            out.write(json);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            out.close();
        }
    }

}
