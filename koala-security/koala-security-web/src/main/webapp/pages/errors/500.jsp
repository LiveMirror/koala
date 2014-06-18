<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>无标题文档</title>
		<style>
			.error-page {
				text-align: center;
				margin-top: 10%;
			}
			.error-code {
				color: #BF1E4B;
				font-family: Arial, sans-serif;
				line-height: 1em;
				font-weight: bold;
				font-size: 6em;
				text-shadow: 0px 0px 0 rgb(153,-8,38), 1px 1px 0 rgb(116,-45,1), 2px 2px 0 rgb(78,-83,-37), 3px 3px 0 rgb(40,-121,-75), 4px 4px 3px rgba(0,0,0,0.45), 4px 4px 1px rgba(0,0,0,0.5), 0px 0px 3px rgba(0,0,0,.2);
			}
			.error-description {
				color: #999999;
				font-family: 'Aclonica', serif;
				font-size: 1.2em;
				line-height: 2em;
				font-weight: normal;
				text-shadow: 0px 1px 1px #4d4d4d;
			}
		</style>
	</head>
	<body>
		<div class="error-page">
			<h1 class="error-code">Error 500</h1>
			<p class="error-description">
				A generic error message, given when no more specific message is suitable
			</p>
		</div>
	</body>
</html>