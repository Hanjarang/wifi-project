package project1_Servlet;

import project1_DB.Database;
import WIFI.wifidetail;
import API.OpenApi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/fetchData")
public class FetchDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	Class.forName("org.mariadb.jdbc.Driver");
            // Open API에서 데이터 가져오기
            List<wifidetail> wifiDetails = OpenApi.fetchAllWifiDetails();

            // DB에 데이터 저장
            Database.insertWifiData(wifiDetails);

            // 성공 메시지 설정
            request.setAttribute("message", "WiFi Data fetched and saved successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            // 실패 메시지 설정
            request.setAttribute("message", "Failed to fetch and save WiFi Data.");
        }

        // 결과 페이지로 포워드
        request.getRequestDispatcher("/result.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

