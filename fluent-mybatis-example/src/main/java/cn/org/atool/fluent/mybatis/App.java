package cn.org.atool.fluent.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zzq
 * @date 2021/8/4
 */
@MapperScan({"cn.org.atool.fluent.mybatis.generate.mapper"})
@SpringBootApplication
public class App {
    
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
