业务日志系统原理
----------

业务日志系统底层AOP技术拦截业务方法。

当业务方法执行，业务日志引擎`BusinessLogEngine通过配置适配器`BusinessLogConfigAdapter`拿到业务日志配置项`BusinessLogConfig`。

并从`BusinessLogConfig`取出日志模板，接着业务日志引擎使用日志渲染器`BusinessLogRender`对模板进行渲染，
并返回渲染后的日志文本，如张三向李四转了40元。

最后，业务日志引擎使用日志导出器`BusinessLogExporter`将日志导出。

业务日志引擎的工作原理大致如上述。下面，我们再来解释一些细节上的问题。

* 配置适配器`BusinessLogConfigAdapter`与业务日志配置项`BusinessLogConfig`之间的关系

人们常常喜欢使用XML作为配置媒介，但是并不是每个人都喜欢。所以，我们将日志配置的设计为一个`配置适配器`的接口，
这样，人们就可以选择自己喜欢的配置媒介了。为方便，我们默认提供了XML配置适配器。

**配置适配器**接口，只有一个方法：`BusinessLogConfig findConfigBy(String businessOperation);`适配器根据
业务操作找到相应的`BusinessLogConfig`业务日志配置项。事实上，一个业务方法对应一个业务日志配置项。

* 业务日志配置项`BusinessLogConfig`包括哪些内容呢?

    1. 日志模板。如{who}转了{money}元到{to}的帐户。
    1. 日志类型。有时我们会需要对日志进行分类。
    1. 一批日志上下文查询`BusinessLogContextQuery`。

* 日志上下文查询`BusinessLogContextQuery`是什么？

在业务日志引擎的内部有一个业务日志上下文查询器`BusinessLogContextQueryExecutor`，查询器执行一批的日志上下文查询
最终返回一个`Map<Object, String>`。事实上，渲染器`BusinessLogRender`就是使用这个Map与模板合并，形成日志的最
终效果。如张三转了300元到李四的帐户。

以下是业务日志引擎核心的伪码：

        // 从配置适配器拿到业务日志配置项
        BusinessLogConfig config = businessLogConfigAdapter.findConfigBy(businessOperation);

        //从配置项中拿到日志模板
        String template = config.getTemplate();

        //从配置项中拿到一批日志上下文查询
        List<BusinessLogContextQuery> queries = config.getQueries();

        //日志上下文查询器执行查询，返回查询结果
        Map<String,Object> map =  queryExecutor.startQuery(queries);

        //渲染器渲染，并返回渲染结果
        String renderResult = businessLogRender.render(map, template);

        BusinessLog businessLog = new BusinessLog();

        businessLog.setLog(renderResult);

        businessLog.setCategory(config.getCategory());

        //导出
        exporter.export(businessLog);








