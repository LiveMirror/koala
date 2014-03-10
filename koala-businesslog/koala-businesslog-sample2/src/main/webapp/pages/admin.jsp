<%@ page import="org.openkoala.businesslog.BusinessLog" %>
<%@ page import="org.openkoala.businesslog.application.BusinessLogApplication" %>
<%@ page import="org.dayatang.domain.InstanceFactory" %>
<%@ page import="org.openkoala.businesslog.dto.DefaultBusinessLogDTO" %>
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

    out.println("<br/>done!");
%>

</body>
</html>