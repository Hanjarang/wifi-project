<%@ page import="WIFI.wifidetail, project1_DB.Database, java.util.List, java.sql.Timestamp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // 사용자 위치 정보 초기화
    double userLat = 0.0; // 기본값 설정
    double userLnt = 0.0; // 기본값 설정

    // 사용자의 위치 정보 받아오기
    String latParameter = request.getParameter("mylat");
    String lntParameter = request.getParameter("mylnt");

    // 받아온 파라미터 값이 null이 아닌 경우에만
    if (latParameter != null && lntParameter != null) {
        userLat = Double.parseDouble(latParameter);
        userLnt = Double.parseDouble(lntParameter);

        Timestamp searchTime = new Timestamp(System.currentTimeMillis());
        Database.saveHistory(userLat, userLnt, searchTime);
    }
    List<wifidetail> nearbyWifiList = Database.getAroundWifi(userLat, userLnt);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 위치기반 공공 와이파이</title>
    <style>
        table {
            width: 100%;
        }

        thead:first-child {
            background-color: #90EE90 ; /* 배경색을 초록색으로 변경 */
        }

        td, th {
            text-align: center;
            font-size: 12px; /* 원하는 폰트 크기로 변경 */
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
    <form id="locationForm" method="get" action="wifiaround.jsp">
        <label for="mylat">LAT</label>
        <input id="mylat" name="mylat"/>
        <label for="mylnt">LNT</label>
        <input id="mylnt" name="mylnt"/>
        <button type="button" onclick="getLocation()">내 위치 정보</button>
        <button type="submit">근처 WIFI 정보 보기 (20개)</button>
    </form>
    <table>
        <thead>
        <tr>
            <th>거리(km)</th>
            <th>관리번호</th>
            <th>자치구</th>
            <th>와이파이명</th>
            <th>도로명 주소</th>
            <th>상세주소</th>
            <th>설치위치(층)</th>
            <th>설치유형</th>
            <th>설치기관</th>
            <th>서비스구분</th>
            <th>망종류</th>
            <th>설치연도</th>
            <th>실내외구분</th>
            <th>WIFI 접속환경</th>
            <th>X좌표</th>
            <th>Y좌표</th>
            <th>작업일자</th>
        </tr>
        </thead>
        <tbody>
        <% if (nearbyWifiList != null && !nearbyWifiList.isEmpty()) { 
            for (wifidetail wifiDetail : nearbyWifiList) { %>
        <tr>
    <td><%= wifiDetail.formatDistance(userLat, userLnt) %></td>
    <td><%= wifiDetail.getxSwifiMgrNo() %></td>
    <td><%= wifiDetail.getxSwifiWrdoFc() %></td>
    <td><%= wifiDetail.getxSwifiMainNm() %></td>
    <td><%= wifiDetail.getxSwifiAdres1() %></td>
    <td><%= wifiDetail.getxSwifiAdres2() %></td>
    <td><%= wifiDetail.getxSwifiInstlFloor() %></td>
    <td><%= wifiDetail.getxSwifiInstlTy() %></td>
    <td><%= wifiDetail.getxSwifiInstlMby() %></td>
    <td><%= wifiDetail.getxSwifiSvcSe() %></td>
    <td><%= wifiDetail.getxSwifiCmcwr() %></td>
    <td><%= wifiDetail.getxSwifiCnstcYear() %></td>
    <td><%= wifiDetail.getxSwifiInoutDoor() %></td>
    <td><%= wifiDetail.getxSwifiRemars3() %></td>
    <td><%= wifiDetail.getLat() %></td>
    <td><%= wifiDetail.getLnt() %></td>
    <td><%= wifiDetail.getWorkDttm() %></td>
</tr>
        
        <% } 
        } else { %>
        <tr>
            <td colspan="17">위치를 입력한 후 조회해 주세요</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</main>
<script>
    function getLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(showPosition);
    }

    function showPosition(position) {
        document.getElementById('mylat').value = position.coords.latitude;
        document.getElementById('mylnt').value = position.coords.longitude;
    }
</script>
</body>
</html>
