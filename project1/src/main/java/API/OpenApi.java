package API;

import project1_DB.Database;
import WIFI.wifidetail;
import WIFI.wifiinfo;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OpenApi {
    private static final String API_KEY = "756c476155676b733831596a797877";
    private static final String BASE_URL = "http://openapi.seoul.go.kr:8088/" + API_KEY + "/json/TbPublicWifiInfo/";

    public static void main(String[] args) {
        try {
            List<wifidetail> allWifiDetails = fetchAllWifiDetails();
            Database.insertWifiData(allWifiDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<wifidetail> fetchAllWifiDetails() throws IOException {
        int totalCount = getTotalCount();
        int pageSize = 1000;
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        List<wifidetail> allWifiDetails = new ArrayList<>();

        for (int page = 1; page <= totalPages; page++) {
            String urlString = String.format("%s%d/%d/", BASE_URL, (page - 1) * pageSize + 1, page * pageSize);
            allWifiDetails.addAll(fetchWifiDetails(urlString));
        }

        return allWifiDetails;
    }

    private static int getTotalCount() throws IOException {
        String urlString = BASE_URL + "1/1/";
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            Gson gson = new Gson();
            wifiinfo wifiInfo = gson.fromJson(sb.toString(), wifiinfo.class);

            if (wifiInfo != null) {
                return wifiInfo.getTbPublicWifiInfo().getList_total_count();
            } else {
                throw new IOException("API에서 전체 데이터 개수 가져오기 실패");
            }
        } finally {
            if (rd != null) {
                rd.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static List<wifidetail> fetchWifiDetails(String urlString) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader rd = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                throw new IOException("HTTP error code : " + responseCode);
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }

            Gson gson = new Gson();
            wifiinfo wifiInfo = gson.fromJson(sb.toString(), wifiinfo.class);

            if (wifiInfo != null && wifiInfo.getTbPublicWifiInfo().getRow() != null) {
                return wifiInfo.getTbPublicWifiInfo().getRow();
            } else {
                System.err.println("API에서 정보 가져오기 실패");
                return new ArrayList<>();
            }
        } finally {
            if (rd != null) {
                rd.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}

