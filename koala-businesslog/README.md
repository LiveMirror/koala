Koala业务日志系统
----
Koala业务日志系统分为以下几个子模块：

1. koala-businesslog-api 业务日志系统的核心api
1. koala-businesslog-impl 业务日志系统的koala的默认实现
1. koala-businesslog-web 业务日志系统WEB模块
1. koala-businesslog-acceptance-test 业务日志系统的集成测试，也是业务日志系统的**范例**


### Koala业务日志系统的目标

1. 对业务方法无侵入

1. 尽最大可能不影响业务方法的性能

1. 系统及日志模板配置简单

1. 日志导出方式灵活

1. 修改日志模板而不需要重启应用


### 目前的缺陷

1. 依赖Spring 的AOP
1. 只有受Spring IOC容器托管的bean才能被日志


### 如何使用Koala默认实现的业务日志系统

**大纲**

    1. 在类路径下加入`koala-businesslog.properties`文件
    1. 为业务方法加上别名，具体做法：在业务方法上加入`@MethodAlias`注解，并设置别名
    1. 在类路径下加入日志模板配置文件


**详细操作**
1. 在类路径下加入`koala-businesslog.properties`文件
        #指定拦截的业务方法，使用Spring的切入点写法
        pointcut=execution(* business.*Application.*(..))

        #日志开关
        kaola.businesslog.enable=true

        #指定日志导出器BusinessLogExporter接口的实现。默认有：BusinessLogConsoleExporter和BusinessLogExporterImpl
        businessLogExporter=org.openkoala.businesslog.utils.BusinessLogConsoleExporter

        #线程池配置。因为业务日志的导出借助线程池实现异步
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

        #如果使用Koala的默认日志导出器，需要配置数据库参数
        #数据库设置
        log.db.jdbc.driver=${db.jdbcDriver}
        log.db.jdbc.connection.url=${db.connectionURL}
        log.db.jdbc.username=${db.username}
        log.db.jdbc.password=${db.password}
        log.db.jdbc.dialect=${hibernate.dialect}
        log.hibernate.hbm2ddl.auto=${hibernate.hbm2ddl.auto}
        log.hibernate.show_sql=${hibernate.show_sql}
        log.db.Type=${db.Type}
        db.generateDdl=${generateDdl}
        log.maximumConnectionCount=50
        log.minimumConnectionCount=10

1. 为业务方法加上别名。这个别名必须符合Java方法名的命名规则。给业务方法加别名的目的是为了方便业务方法与日志模板之间的映射。

        @MethodAlias("业务方法别名")
        业务方法
    
1. 在类路径下加入日志模板配置文件

    日志模板实际上是groovy文件。在这个groovy文件中，你可以写Java代码，也可以写groovy代码。这样，就可以达到最大的
    灵活。同时，配置起来又不复杂。

    目前我们支持两种配置方式：单文件配置方式和多文件配置方式。

* 单文件配置
    1. 在类路径下加入`BusinessLogConfig.groovy`
    1. 文件模板为：

            class BusinesslogConfig {
                //必须
                def context

                //InvoiceApplicationImpl_addInvoice为业务方法别名
                def InvoiceApplicationImpl_addInvoice() {
                    "日志内容"
                }

                def ProjectApplicationImpl_findSomeProjects() {
                    [category:"项目操作", logs:"查找项目"]
                }

            }

    1. 配置模板说明
        配置模板实际上是一个Groovy类。你可以在类中定义任何方法。如果方法为某个业务方法的别名（使用`@MethodAlias`注解）
        那么，我们就认为它是一个业务日志方法。它的返回值（return或者放在方法最后一行的变量）将会被Set到` org.openkoala.businesslog.BusinessLog`的实例中。

        日志方法返回值有两种情况：1. 只返回一个String类型的日志文本；2. 返回一个Map，这个Map包括Key为`category`的日志分类及日志文本。

        在类中，还会使用Groovy定义变量的方法：`def context`定义一个变量。这个变量实际上是一个Map。
        Map中存储的是业务方法的`返回值`、`参数`。如果需要，你可以存储任何你需要的数据。你可以从这个context中取
        出你需要的内容，填充到你的日志中。至于如何取context中的内容，请看附录


* 多文件配置
当业务系统非常复杂的时候，一个日志配置文件是不足够的。我们提供多文件的配置方式

    1. 在类路径中加入`businessLogConfig`文件夹。
    1. 在该文件夹中加入日志配置文件，文件名任意，只要符合Groovy类文件的命名规范即可。

注： 多文件配置方式与单文件配置方式不兼容。在此业务日志系统中，单文件配置方式优先。
    `businessLogConfig`文件夹中的所有以`.groovy`结尾的文件都将被作为日志配置文件。


附录
---
#### 在日志模板中取`context`的内容

     key                     value
    _methodReturn           业务方法返回值
    _param                  业务方法的参数, _param0代表第一个参数 _param1代表第二个参数，依此类推
    _executeError           业务方法执行失败的异常信息
    _businessMethod         业务方法
    _user                   业务方法操作人
    _time                   业务方法操作时间
    _ip                     ip地址
