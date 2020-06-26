# Fluent-Mybatis介绍

## Fluent-Mybatis能干吗
fluent-mybatis是mybatis的增强版，既有改变，又有增强，简化开发、提高效率。

- 无需xml文件，直接用java所见即所得的写出sql语句
    1. 干掉了生成xml文件步骤，避免了维护xml文件，同时也避免要在xml文件中设置一堆&lt;if test="...">
    2. 避免了dao类里面if else方式的参数设置
    3. 避免参数设置不当，导致全表扫描
    
- 智能化的，语义化的流式接口，让你一气呵成写完sql操作
    1. 流式动态接口，结合IDE的智能提示，最大限度的避免书写错误
    2. 对不可空的参数会自动判断，避免粗心的程序员没有做前置检验导致的错误结果
    3. 支持嵌套查询，99%的单表操作使用fluent-mybatis语法就可以直接完成，无需再自定义mapper操作
    4. 对聚合函数的支持，包括select 聚合函数 和 having 聚合函数判断
    ![-w930](https://gitee.com/tryternity/fluent-mybatis-docs/raw/master/00-introduce/test_count_gt_10_groupByGrade.png)
    
- 增强功能
    1. 可以自动帮忙进行传统的分页操作, 只需要传入一个查询条件, 自动完成count查询
    总数，和limit查询分页列表的操作。并且在查询总数的时候，自动去除了order by的部分，大大简化了分页查询
    2. 支持按标识进行分页的操作，每次查询会自动多查一条数据作为下一次查询的marker标识
    3. 结合test4j单元测试工具，可以自动化的进行内存数据库方式测试，
    并且无需提供脚本，框架会根据实体类自动生成数据库脚本，真正做到实时随地跑测试。
    可以选择h2,速度快，但有少量语法和mysql不一致；也可以选择mariadb数据库，语法和mysql高度一致;
    当然更可以支持实体数据库，方便查看测试过程中的数据。
    
    ***重要：不论什么数据库，你都无需维护测试数据库的脚本***
    
## 使用fluent-mybatis

   [使用fluent-mybatis](./fluent-mybatis-tutorial/index.md)