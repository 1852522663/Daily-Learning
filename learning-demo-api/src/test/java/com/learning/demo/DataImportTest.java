package com.learning.demo;

import java.io.IOException;
import java.io.InputStream;

import com.learning.demo.model.MyCustomMultipartFile;
import com.learning.demo.plugin.DataImport;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuehewei <yuehewei@kuaishou.com>
 * Created on 2023-07-11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataImportTest {
    @Autowired
    private DataImport dataImport;

    @Test
    void DataImportTest() throws IOException {
        // 创建模拟的MultipartFile对象
        Resource resource = new ClassPathResource("页面对应biz列表（不完整）.xlsx");
        InputStream inputStream = resource.getInputStream();
        MultipartFile file = new MyCustomMultipartFile("页面对应biz列表（不完整）.xlsx", inputStream);
        // 调用方法进行数据导入
        dataImport.importDataFromFile2(file);
    }
}
