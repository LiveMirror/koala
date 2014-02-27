业务日志系统Koala默认实现
----------

## 说明

1. 对业务系统无侵入
1. 目前此版本依赖了Spring
1. 使用XML配置
1. 内部使用了多线程，避免影响业务系统



## 如何使用

1. 加入依赖

            <dependency>
                <groupId>org.openkoala.businesslog</groupId>
                <artifactId>koala-businesslog-impl</artifactId>
                <version></version>
            </dependency>
            <!--写此文档版本为3.0.0-SNAPSHOT-->

1. 在类路径下加入`koala-businesslog.properties`。properties中的内容，详见附录

1. 在类路径下新建`koala-log-conf`文件夹。

1. 在`koala-log-conf`中加入业务日志配置项文件，文件为xml格式，文件名任意。业务日志配置项文件内容，详见附录

1. 把`<import resource="classpath*:koala-businesslog-aop.xml"/>`加入到你的spring的配置文件中。

1. 如果在web环境下使用业务日志子系统，你还需要做：

    1. 写一个filter类并`extends BusinessLogServletFilter`。
    1. `web.xml`加入

        <filter>
            <filter-name>LogFilter</filter-name>
            <filter-class>你的filter</filter-class>
        </filter>
        <filter-mapping>
            <filter-name>LogFilter</filter-name>
            <url-pattern>/</url-pattern>
        </filter-mapping>






## FAQ
1. 日志模板未被渲染，或者日志模板出现了不知哪来的字符

   考虑是不是maven的resource插件将你的xml文件中的字符过滤了




附录
---

## 业务日志配置项

    <?xml version="1.0" encoding="UTF-8"?>
    <businessLogConfigs>
        <businessLogConfig>
            <!--日志类型-->
            <category>项目操作</category>
            <!--业务方法，注意要写全包名-->
            <method>Project[] business.ProjectApplicationImpl.findSomeProjects(List)</method>
            <!--日志模板-->
            <template><![CDATA[
            查到项目
            <#list names as name>
              ${name},
            </#list>
            ]]></template>
            <!--设置一批的上下文查询，查询的结果将用于替换模板中的占位符 -->
            <queries>
                <!--查询结果为一个map  names:查询结果 -->
                <query key="names">
                    <target class="business.Project"></target>
                    <method>getProjectsName(business.Project[])</method>
                    <args>
                        <arg>${_methodReturn}</arg>
                    </args>
                </query>
            </queries>
        </businessLogConfig>
        <businessLogConfig>
            <category>项目操作</category>
            <!--对于多维数组，待支持-->
            <method>List business.ProjectApplicationImpl.addProject()</method>
            <template><![CDATA[
            ererererer         addProject
            ]]></template>
            <queries>
                <query key="ij">
                    <target class="business.InvoiceApplicationImpl">invoiceApplication</target>
                    <method>addInvoice(String,long)</method>
                    <args>
                        <arg>xxx</arg>
                        <arg>122</arg>
                    </args>
                </query>
            </queries>
        </businessLogConfig>
    </businessLogConfigs>





## koala-businesslog.properties

            # 需要拦截的方法，使用spring的切入点表达式
            pointcut=execution(* business.*Application.*(..))

            #日志开关
            kaola.businesslog.enable=true

            #日志配置适配器
            businessLogConfigAdapter=org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter

            #关联查询执行器
            businessLogQueryExecutor=org.openkoala.businesslog.impl.BusinessLogDefaultContextQueryExecutor

            #日志模板渲染器
            businessLogRender=org.openkoala.businesslog.impl.BusinessLogFreemarkerDefaultRender

            #日志导出器
            businessLogExporter=org.openkoala.businesslog.impl.BusinessLogConsoleExporter


            #数据库设置
            log.db.jdbc.driver=${db.jdbcDriver}
            log.db.jdbc.connection.url=${db.connectionURL}
            log.db.jdbc.username=${db.username}
            log.db.jdbc.password=${db.password}
            log.db.jdbc.dialect=${hibernate.dialect}
            log.hibernate.hbm2ddl.auto=${hibernate.hbm2ddl.auto}
            log.hibernate.show_sql=${hibernate.show_sql}
            log.db.Type=${db.Type}
            db.generateDdl=${db.generateDdl}
            log.maximumConnectionCount=50
            log.minimumConnectionCount=10

            #线程池配置
            #核心线程数
            log.threadPool.corePoolSize=10
            #最大线程数
            log.threadPool.maxPoolSize=50
            #队列最大长度
            log.threadPool.queueCapacity=1000
            #线程池维护线程所允许的空闲时间
            log.threadPool.keepAliveSeconds=300
            #线程池对拒绝任务(无线程可用)的处理策略
            log.threadPool.rejectedExecutionHandler=java.util.concurrent.ThreadPoolExecutor$CallerRunsPolicy

