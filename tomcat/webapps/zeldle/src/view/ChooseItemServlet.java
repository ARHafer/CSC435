package view;

import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import model.Item;
import data.DatabaseHandler;

@WebServlet("/chooseItem")
public class ChooseItemServlet extends HttpServlet {
    private static final int MAX_GUESSES = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession prevSession = request.getSession(false);

        if (prevSession != null) {
            try {
                DatabaseHandler.clearGuesses(prevSession.getId());
            } catch (SQLException e) {
                response.setStatus(500);
                out.print("{\"error\": \"" + e.getMessage() + "\"}");
                return;
            }

            prevSession.invalidate();
        }

        try {
            Item targetItem = DatabaseHandler.getRandomItem();

            if (targetItem == null) {
                response.setStatus(500);
                out.print("{\"error\": \"Database is empty!\"}");
                return;
            }

            HttpSession session = request.getSession(true);

            session.setAttribute("targetItem", targetItem);
            session.setAttribute("guessesRemaining", MAX_GUESSES);

            response.setStatus(200);
            out.print("{\"status\": \"ok\"}");
        } catch (SQLException e) {
            response.setStatus(500);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

}
