package com.learning.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-07-10
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskSQLTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
/*
在任务SQL测试检查功能的示例代码中，assertEquals用于验证数据库操作的结果是否符合预期。如果预期的受影响行数与实际的受影响行数相等，则测试通过；否则，测试失败并抛出断言错误。
除了assertEquals，JUnit还提供了其他常用的断言方法，例如assertTrue、assertFalse、assertNotNull、assertNull等，可以根据不同的测试场景选择合适的断言方法来进行验证。
 */
    @Test
    public void testInsertTask() {
        String sql = "INSERT INTO user (name, sex, pwd, email, status) VALUES (?, ?,?,?,?)";
        int affectedRows = jdbcTemplate.update(sql, "John", "Male", "123456", "john@example.com", 1);
        assertEquals(1, affectedRows);
    }

    @Test
    public void testUpdateTaskStatus() {
        String sql = "UPDATE user SET status = ? WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, 0, 1);
        assertEquals(1, affectedRows);
    }

    // 其他测试方法...

}

