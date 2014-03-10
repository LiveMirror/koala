<%@ page import="org.openkoala.businesslog.model.DefaultBusinessLogDTO" %>
<%@ page import="org.openkoala.businesslog.application.BusinessLogApplication" %>
<%@ page import="org.dayatang.domain.InstanceFactory" %>
<%--
  Created by IntelliJ IDEA.
  User: zjzhai
  Date: 3/7/14
  Time: 2:23 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    BusinessLogApplication businessLogApplication = InstanceFactory.getInstance(BusinessLogApplication.class);
    out.println("size:" + businessLogApplication.findAllDefaultBusinessLog().size() + "<br/>");
    for (DefaultBusinessLogDTO log : businessLogApplication.findAllDefaultBusinessLog()) {
        out.println(log.getCategory() + ":" + log.getLog());
    }
%>
</body>
</html>