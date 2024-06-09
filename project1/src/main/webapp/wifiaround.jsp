<%@ page import="WIFI.wifidetail, project1_DB.Database, java.util.List, java.sql.Timestamp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%

    double userLat = 0.0; 
    double userLng = 0.0; 

    String latParameter = request.getParameter("mylat");
    String lngParameter = request.getParameter("mylnt");

    if (latParameter != null && lngParameter != null) {
        userLat = Double.parseDouble(latParameter);
        userLng = Double.parseDouble(lngParameter); 

        Timestamp searchTime = new Timestamp(System.currentTimeMillis());
        Database.saveHistory(userLat, userLng, searchTime); 
    }
    List<wifidetail> nearbyWifiList = Database.getAroundWifi(userLat, userLng); 
%>
<html>
<head>
    <title> 내 근처 와이파이 정보</title>
    <style>
        table {
            width: 100%;
        }

        thead:first-child {
            background-color: wheat;
        }

        td {
            text-align: center;
        }
    </style>
    <script>
        function getLocation() {
            // 내 위치 가져오기 기능 구현
            // 예: navigator.geolocation.getCurrentPosition() 사용
        }
    </script>
</head>
<body>
<!-- HTML 내용 -->
</body>
</html>

