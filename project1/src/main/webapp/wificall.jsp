<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.ArrayList, java.util.List, project1_DB.Database"%>
<!DOCTYPE html>

<html>
<head>
    <title>서울시 공공와이파이 개수</title>
    <style>
        body {
            display : flex;
            flex-direction : column;
            justify-content: center;
            align-items: center;
        }
    </style>
</head>

<body>
<header>
    <%
        // 와이파이 개수를 가져오는 코드
        int wifiCount = Database.wifiCount();
        // 와이파이 개수 출력
        out.println("<h1>"+ wifiCount + "개의 WIFI 정보를 정상적으로 저장하였습니다.</h1>");
    %>


</header>
<main>
    <a href="index.jsp">
        <span>홈으로 가기</span>
    </a>
</main>
</body>
</html>