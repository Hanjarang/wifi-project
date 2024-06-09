package project1_Servlet;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "TestServlet", value = "/Test-Servlet")
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String message;

    public void init() {
        message = "Hello SSIBAL World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    public void destroy() {
        // cleanup code if needed
    }
}
