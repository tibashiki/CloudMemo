package com.satoshi.memo.util;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/238:35
 * @Description:分页工具类
 */
@Getter
@Setter
public class Page<T> {
    private Integer pageNum;//pageNum前台传回的，默认1
    private Integer pageSize;//pageSize每页显示的数量,前台或后台设定
    private long totalCount;//总记录数，后台数据库查询到count
    private Integer totalPages;//总页数（总记录数/每页显示数量）转换为浮点，执行除法，向上取整
    private  Integer prePage;//上一页（当前页-1,如果当前页就是第一页，则同样为1）
    private  Integer nextPage;//下一页(当前页+1，如果是最后一页，则同样为max页数)
    private Integer startNavPage;//导航开始(当前页-5,如果不够，导航开始为1)
    private Integer endNavPage;//导航结束(当前页+4，如果超过，导航结束为max)
    private List<T> dataList;//当前页的数据集合

    public Page(Integer pageNum, Integer pageSize, long totalCount) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPages = (int)Math.ceil(totalCount/(pageSize*1.0));
        this.prePage = pageNum-1 <1 ? 1 :pageNum-1;
        this.nextPage = pageNum+1 <totalPages ? 1 :pageNum+1;
        this.startNavPage = pageNum-5;
        this.endNavPage = pageNum+4;
        if(this.startNavPage<1){
            this.startNavPage=1;
            this.endNavPage = this.startNavPage+9>totalPages ? totalPages:this.startNavPage+9;
        }
        if(this.endNavPage<totalPages){
            this.endNavPage=totalPages;
            this.startNavPage = this.endNavPage-9 < 1 ? 1:this.endNavPage-9;
        }
    }
}
