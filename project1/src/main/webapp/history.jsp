<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.ArrayList, java.util.List, project1_DB.Database"%>

<!DOCTYPE html>
<html>
<head>
    <title>조회 히스토리</title>
    <style>
        table {
            width: 100%;
        }

        thead:first-child {
            background-color: #006400; 
        }

        td, th {
            text-align: center;
            font-size: 12px;
            color: white;
            height: 50px;
        }
    </style>
</head>
<body>
<header>
    <h1>와이파이 정보 구하기</h1>
</header>
<main>
    <div class="option">
        <a href="index.jsp"><span>홈</span></a>
        <span> | </span>
        <a href="history.jsp"> <span>위치 히스토리 목록</span> </a>
        <span> | </span>
        <a href="wificall.jsp">
            <span>Open API 와이파이 정보 가져오기</span></a>
    </div>

    <table>
        <thead>
        <tr>
            <th>번호</th>
            <th>X 좌표</th>
            <th>Y 좌표</th>
            <th>조회일자</th>
            <th>비고</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Object[]> historyList = Database.getHistory();
            int num = 1;
            for (Object[] history : historyList) {
                String xCoord = (history[0] != null && !history[0].toString().isEmpty()) ? history[0].toString() : "0.0";
                String yCoord = (history[1] != null && !history[1].toString().isEmpty()) ? history[1].toString() : "0.0";
        %>
        
        <tr>
            <td><%= num++ %></td>
            <td><%= xCoord %></td>
            <td><%= yCoord %></td>
            <td><%= history[2] %></td>
            <td><button>delete</button></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</main>

</body>
</html>
