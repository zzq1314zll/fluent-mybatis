package cn.org.atool.fluent.mybatis;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.Column;
import cn.org.atool.generator.annotation.Table;
import cn.org.atool.generator.annotation.Tables;

/**
 * @author zzq
 * @date 2021/8/4
 */
public class GeneratorDemo {
    
    static final String SrcDir = "fluent-mybatis-example/src/main/java";
    
    static final String BasePack = "cn.org.atool.fluent.mybatis.generate";
    
    // 数据源 url
    static final String url = "jdbc:mysql://localhost:3306/fluent_mybatis?useUnicode=true&characterEncoding=utf8";
    // 数据库用户名
    static final String username = "root";
    // 数据库密码
    static final String password = "root";
    
    public static void main(String[] args) {
        // 引用配置类，build方法允许有多个配置类
        FileGenerator.build(En.class);
    }
    
    @Tables(url = url, username = username, password = password,
            srcDir = SrcDir, basePack = BasePack,
            tables = {
                    @Table(value = "hello_world",
                            columns = {@Column(value = "is_deleted", javaType = Long.class)})
            }, logicDeleted = "is_deleted", version = "version")
    static class En {
    }
}
