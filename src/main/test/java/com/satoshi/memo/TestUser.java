package java.com.satoshi.memo;
import com.satoshi.memo.dao.BaseDao;
import com.satoshi.memo.dao.UserDao;
import com.satoshi.memo.po.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:tibashiki
 * @DATE:2023/05/1711:52
 * @Description:
 */
public class TestUser {
    @Test
    public void testQueryUserByName(){
        UserDao userDao = new UserDao();
        User user = userDao.queryUserByName("admin");
        System.out.println(user.getUpwd());
    }
    @Test
    public void testAdd(){
        String sql = "insert into tb_user(uname,upwd,nick,head,mood) values (?,?,?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add("Bob");
        params.add("e10adc3949ba59abbe56e057f20f883e");
        params.add("Error");
        params.add("cache.jpg");
        params.add("cool");
        int row = BaseDao.executeUpdate1(sql,params);
        System.out.println(row);
    }
}
