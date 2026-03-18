import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/chooseItem")
public class ChooseItemServlet extends HttpServlet {
    private static final int MAX_GUESSES = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Item chosenItem = DatabaseHandler.getRandomItem();

            if (chosenItem == null) {
                response.setStatus(500);
                out.print("{\"error\": \"Database is empty!\"}");
                return;
            }

            HttpSession session = request.getSession(true);

            session.setAttribute("chosenItem", chosenItem);
            session.setAttribute("guessesRemaining", MAX_GUESSES);

            response.setStatus(200);
            out.print("{\"status\": \"ok\"}");
        } catch (SQLException e) {
            response.setStatus(500);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

}
