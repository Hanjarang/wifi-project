package project1_DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import WIFI.wifidetail;

public class Database {
	
    private static final String dbUrl = "jdbc:mariadb://localhost:3306/testdb3";
    private static final String dbUser = "testuser3";
    private static final String dbPassword = "zerobase";

    public static void insertWifiData(List<wifidetail> wifiDetails) {
        if (wifiDetails == null || wifiDetails.isEmpty()) {
            System.err.println("No Wifi details to insert.");
            return;
        }

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO wifi_detail (X_SWIFI_MGR_NO, X_SWIFI_WRDOFC, X_SWIFI_MAIN_NM, X_SWIFI_ADRES1, X_SWIFI_ADRES2, X_SWIFI_INSTL_FLOOR, X_SWIFI_INSTL_TY, X_SWIFI_INSTL_MBY, X_SWIFI_SVC_SE, X_SWIFI_CMCWR, X_SWIFI_CNSTC_YEAR, X_SWIFI_INOUT_DOOR, X_SWIFI_REMARS3, LAT, LNT, WORK_DTTM) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
             )) {

            for (wifidetail detail : wifiDetails) {
                ps.setString(1, detail.getxSwifiMgrNo());
                ps.setString(2, detail.getxSwifiWrdoFc());
                ps.setString(3, detail.getxSwifiMainNm());
                ps.setString(4, detail.getxSwifiAdres1());
                ps.setString(5, detail.getxSwifiAdres2());
                ps.setString(6, detail.getxSwifiInstlFloor());
                ps.setString(7, detail.getxSwifiInstlTy());
                ps.setString(8, detail.getxSwifiInstlMby());
                ps.setString(9, detail.getxSwifiSvcSe());
                ps.setString(10, detail.getxSwifiCmcwr());
                ps.setInt(11, detail.getxSwifiCnstcYear());
                ps.setString(12, detail.getxSwifiInoutDoor());
                ps.setString(13, detail.getxSwifiRemars3());
                ps.setDouble(14, detail.getLat());
                ps.setDouble(15, detail.getLnt());
                ps.setString(16, detail.getWorkDttm());
                ps.addBatch();
            }
            ps.executeBatch();
            System.out.println("Data insertion successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
    
    public static List<Object[]> getHistory() {
        List<Object[]> historyList = new ArrayList<>();
        String query = "SELECT X_SWIFI_MGR_NO, X_SWIFI_ADRES1, X_SWIFI_ADRES2, WORK_DTTM FROM wifi_detail"; // 적절한 컬럼명으로 수정하세요.

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] row = new Object[6]; // 컬럼 수에 맞게 조정했습니다.
                row[0] = rs.getInt("id");
                row[1] = rs.getTimestamp("request_time");
                row[2] = rs.getString("ssid");
                row[3] = rs.getString("bssid");
                row[4] = rs.getString("location");
                row[5] = rs.getInt("signal_strength");
                historyList.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }
    
    public static int wifiCount() {
        int wifiCount = 0;

        // JDBC 드라이버 로드
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return wifiCount; // 드라이버 로드 실패시 0 반환
        }

        String dbUrl = "jdbc:mariadb://localhost:3306/testdb3";
        String dbUser = "testuser3";
        String dbPassword = "zerobase";

        String sql = "SELECT COUNT(*) FROM wifi_detail";

        // 데이터베이스 연결 및 쿼리 실행
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                wifiCount = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return wifiCount;
    }

    
    public static void saveHistory(double lat, double lnt, Timestamp searchTime) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            // JDBC 드라이버 로드
            Class.forName("org.mariadb.jdbc.Driver");
            // 데이터베이스 연결
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String sql = " insert into search_history(LAT, LNT, SEARCH_TIME) values(?,?,?) " ;

            ps = connection.prepareStatement(sql);
            ps.setDouble(1,lnt);
            ps.setDouble(2,lat);
            ps.setTimestamp(3, searchTime);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("히스토리 저장 성공!");
            } else {
                System.out.println("히스토리 저장 실패!");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static List<wifidetail> getAroundWifi(double userlat, double userlnt) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<wifidetail> AroundWifiList = new ArrayList<>();

        try {
            // JDBC 드라이버 로드
            Class.forName("org.mariadb.jdbc.Driver");
            // 데이터베이스 연결
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String sql =
                    " select distinct * from wifi_detail " +
                            " order by (6371 * acos(cos(radians(?)) * cos(radians(LAT)) * cos(radians(LNT) - radians(?)) + sin(radians(?)) * sin(radians(LAT)))) " +
                            " limit 20";


            ps = connection.prepareStatement(sql);
            ps.setDouble(1, userlat);
            ps.setDouble(2, userlnt);
            ps.setDouble(3, userlat);
            rs = ps.executeQuery();

            // 결과를 리스트에 추가
            while (rs.next()) {
                wifidetail wifiDetail = new wifidetail(userlnt, userlnt);
                wifiDetail.setxSwifiMgrNo(rs.getString("X_SWIFI_MGR_NO"));
                wifiDetail.setxSwifiWrdoFc(rs.getString("X_SWIFI_WRDOFC"));
                wifiDetail.setxSwifiMainNm(rs.getString("X_SWIFI_MAIN_NM"));
                wifiDetail.setxSwifiAdres1(rs.getString("X_SWIFI_ADRES1"));
                wifiDetail.setxSwifiAdres2(rs.getString("X_SWIFI_ADRES2"));
                wifiDetail.setxSwifiInstlFloor(rs.getString("X_SWIFI_INSTL_FLOOR"));
                wifiDetail.setxSwifiInstlTy(rs.getString("X_SWIFI_INSTL_TY"));
                wifiDetail.setxSwifiInstlMby(rs.getString("X_SWIFI_INSTL_MBY"));
                wifiDetail.setxSwifiSvcSe(rs.getString("X_SWIFI_SVC_SE"));
                wifiDetail.setxSwifiCmcwr(rs.getString("X_SWIFI_CMCWR"));
                wifiDetail.setxSwifiCnstcYear(rs.getInt("X_SWIFI_CNSTC_YEAR"));
                wifiDetail.setxSwifiInoutDoor(rs.getString("X_SWIFI_INOUT_DOOR"));
                wifiDetail.setxSwifiRemars3(rs.getString("X_SWIFI_REMARS3"));
                wifiDetail.setLat(rs.getDouble("LAT"));
                wifiDetail.setLnt(rs.getDouble("LNT"));
                wifiDetail.setWorkDttm(rs.getString("WORK_DTTM"));

                AroundWifiList.add(wifiDetail);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return AroundWifiList;
    }

    
    
}
    



