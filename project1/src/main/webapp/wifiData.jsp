<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.ArrayList, project1_DB.Database, WIFI.wifidetail, java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.SQLException" %>
<!DOCTYPE html>
<html>
<head>
    <title>Wi-Fi Data</title>
</head>
<body>
    <h1>Wi-Fi Data</h1>
    <button onclick="location.href='wifiData.jsp?action=fetch'">Load Wi-Fi Data</button>
    <%
        if ("fetch".equals(request.getParameter("action"))) {
            try {
                // JDBC 드라이버 로드
                Class.forName("org.mariadb.jdbc.Driver");
                
                // DB 커넥션
                Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/testdb3", "testuser3", "zerobase");
                
                // 와이파이 데이터 가져오기 (예제 더미 데이터)
                List<wifidetail> wifiDetails = new ArrayList<>();
                wifidetail detail = new wifidetail();
                detail.setX_SWIFI_MGR_NO("1");
                detail.setX_SWIFI_MAIN_NM("Free Wi-Fi");
                detail.setX_SWIFI_ADRES1("123 Main St");
                wifiDetails.add(detail);

                // 가져온 데이터를 DB에 저장
                Database.insertWifiData(wifiDetails);

                // 데이터 표시
                if (wifiDetails != null && !wifiDetails.isEmpty()) {
                    for (wifidetail wifi : wifiDetails) {
                        out.println("<p>관리번호: " + wifi.getX_SWIFI_MGR_NO() + "</p>");
                        out.println("<p>와이파이명: " + wifi.getX_SWIFI_MAIN_NM() + "</p>");
                        out.println("<p>주소: " + wifi.getX_SWIFI_ADRES1() + "</p>");
                        out.println("<hr/>");
                    }
                } else {
                    out.println("<p>No Wi-Fi data found.</p>");
                }

                // 히스토리 저장
                try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO history_table (query, timestamp) VALUES (?, ?)")) {
                    String query = "SELECT * FROM wifi_detail LIMIT 20";
                    pstmt.setString(1, query);
                    pstmt.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    %>
</body>
</html>



