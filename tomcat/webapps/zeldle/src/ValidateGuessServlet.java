import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/validateGuess")
public class ValidateGuessServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        String guess = request.getParameter("guess");

        if (guess == null || guess.isBlank()) {
            response.setStatus(400);
            out.print("{\"error\": \"No guess provided\"}");
            return;
        }

        try {
            Item item = DatabaseHandler.getItem(guess.trim());

            if (item == null) {
                response.setStatus(200);
                out.print("{\"validItem\": false, \"message\": \"No such item exists.\"}");
            } else {
                response.setStatus(200);
                out.print("{" +
                    "\"validItem\": true, " +
                    "\"item\": {" +
                    "\"id\": " + item.getID() + ", " +
                    "\"name\": " + item.getName() + ", " +
                    "\"category\": " + item.getCategory() + ", " +
                    "\"type\": " + item.getType() + ", " +
                    "\"location\": " + item.getLocation() + ", " +
                    "\"game\": " + item.getGame() + ", " +
                    "\"value\": " + item.getValue() + 
                    "}" + "}"
                );
            }
        } catch (SQLException e) {
            response.setStatus(500);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
