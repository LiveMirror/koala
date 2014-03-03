<%@ page import="org.openkoala.businesslog.dto.DefaultBusinessLogDTO" %>
<%@ page import="org.openkoala.businesslog.application.BusinessLogApplication" %>
<%@ page import="org.dayatang.domain.InstanceFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    BusinessLogApplication businessLogApplication = InstanceFactory.getInstance(BusinessLogApplication.class, "businessLogApplication");

    for (DefaultBusinessLogDTO log : businessLogApplication.findAllDefaultBusinessLog()) {
        out.println(log.getLog() + "<br/>");
    }
%>

<h3 id="size">${logs.size()}</h3>
</body>
</html>