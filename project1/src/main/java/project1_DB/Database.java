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
                ps.setString(1, detail.getX_SWIFI_MGR_NO());
                ps.setString(2, detail.getX_SWIFI_WRDOFC());
                ps.setString(3, detail.getX_SWIFI_MAIN_NM());
                ps.setString(4, detail.getX_SWIFI_ADRES1());
                ps.setString(5, detail.getX_SWIFI_ADRES2());
                ps.setString(6, detail.getX_SWIFI_INSTL_FLOOR());
                ps.setString(7, detail.getX_SWIFI_INSTL_TY());
                ps.setString(8, detail.getX_SWIFI_INSTL_MBY());
                ps.setString(9, detail.getX_SWIFI_SVC_SE());
                ps.setString(10, detail.getX_SWIFI_CMCWR());
                ps.setInt(11, detail.getX_SWIFI_CNSTC_YEAR());
                ps.setString(12, detail.getX_SWIFI_INOUT_DOOR());
                ps.setString(13, detail.getX_SWIFI_REMARS3());
                ps.setDouble(14, detail.getLAT());
                ps.setDouble(15, detail.getLNT());
                ps.setString(16, detail.getWORK_DTTM());
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
                Object[] row = new Object[4]; // 컬럼 수에 맞게 조정하세요.
                row[0] = rs.getString("X_SWIFI_MGR_NO");
                row[1] = rs.getString("X_SWIFI_ADRES1");
                row[2] = rs.getString("X_SWIFI_ADRES2");
                row[3] = rs.getString("WORK_DTTM");
                historyList.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }
    
    public static int wifiCount() {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int wifiCount = 0;

        try {
            // JDBC 드라이버 로드
            Class.forName("org.mariadb.jdbc.Driver");
            // 데이터베이스 연결
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String sql = " select count(*) from wifi_detail ";

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                wifiCount = rs.getInt(1);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // 리소스 해제
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
                wifidetail wifiDetail = new wifidetail();
                wifiDetail.setX_SWIFI_MGR_NO(rs.getString("X_SWIFI_MGR_NO"));
                wifiDetail.setX_SWIFI_WRDOFC(rs.getString("X_SWIFI_WRDOFC"));
                wifiDetail.setX_SWIFI_MAIN_NM(rs.getString("X_SWIFI_MAIN_NM"));
                wifiDetail.setX_SWIFI_ADRES1(rs.getString("X_SWIFI_ADRES1"));
                wifiDetail.setX_SWIFI_ADRES2(rs.getString("X_SWIFI_ADRES2"));
                wifiDetail.setX_SWIFI_INSTL_FLOOR(rs.getString("X_SWIFI_INSTL_FLOOR"));
                wifiDetail.setX_SWIFI_INSTL_TY(rs.getString("X_SWIFI_INSTL_TY"));
                wifiDetail.setX_SWIFI_INSTL_MBY(rs.getString("X_SWIFI_INSTL_MBY"));
                wifiDetail.setX_SWIFI_SVC_SE(rs.getString("X_SWIFI_SVC_SE"));
                wifiDetail.setX_SWIFI_CMCWR(rs.getString("X_SWIFI_CMCWR"));
                wifiDetail.setX_SWIFI_CNSTC_YEAR(rs.getInt("X_SWIFI_CNSTC_YEAR"));
                wifiDetail.setX_SWIFI_INOUT_DOOR(rs.getString("X_SWIFI_INOUT_DOOR"));
                wifiDetail.setX_SWIFI_REMARS3(rs.getString("X_SWIFI_REMARS3"));
                wifiDetail.setLAT(rs.getDouble("LAT"));
                wifiDetail.setLNT(rs.getDouble("LNT"));
                wifiDetail.setWORK_DTTM(rs.getString("WORK_DTTM"));

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
    



