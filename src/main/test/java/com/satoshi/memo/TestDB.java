package com.satoshi.memo;
import org.junit.Test;
import com.satoshi.memo.util.DBUtil;
import org.slf4j.*;

/**
 * @Author:tibashiki
 * @DATE:2023/05/178:21
 * @Description:
 * 单元测试方法
 *  1，方法的返回值一般为void
 *  2,梣属列表，一般没有参数（空参）
 *  3,方法上需要@Test注解
 *  4,每个方法能独立运行
 */

public class TestDB {
    @Test
    public void testDB(){
        System.out.println("获取数据库连接"+DBUtil.getConnection());
        Logger logger = LoggerFactory.getLogger(TestDB.class);
        logger.info("获取{}数据库连接" , DBUtil.getConnection());
    }

}
