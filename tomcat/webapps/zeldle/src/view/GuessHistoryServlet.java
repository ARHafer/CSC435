package view;

import java.util.*;
import java.io.*;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import model.Guess;
import controller.GameController;

@WebServlet("/guessHistory")
public class GuessHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("targetItem") == null) {
            response.setStatus(400);
            out.print("{\"error\": \"An item has not been selected. Call /chooseItem.\"}");
            return;
        }

        try {
            String sessionID = session.getId();
            List<Guess> guessHistory = GameController.getGuessHistory(sessionID);

            boolean first = true;
            out.print("{\"guesses\": [");

            for (Guess guess : guessHistory) {

                if (first) {
                first = false;
            } else {
                out.print(",");
            }

                out.print(" {" + 
                    "\"guessedName\": \"" + guess.getGuessedName() + "\", " +
                    "\"guessedClassification\": \"" + guess.getGuessedClassification() + "\", " +
                    "\"guessedPType\": \"" + guess.getGuessedPType() + "\", " +
                    "\"guessedSType\": \"" + guess.getGuessedSType() + "\", " +
                    "\"guessedLocation\": \"" + guess.getGuessedLocation() + "\", " +
                    "\"guessedGame\": \"" + guess.getGuessedGame() + "\", " +
                    "\"guessedValue\": " + guess.getGuessedValue() + ", " +
                    "\"nameResult\": \"" + guess.getNameResult() + "\", " +
                    "\"classificationResult\": \"" + guess.getClassificationResult() + "\", " +
                    "\"pTypeResult\": \"" + guess.getPTypeResult() + "\", " +
                    "\"sTypeResult\": \"" + guess.getSTypeResult() + "\", " +
                    "\"locationResult\": \"" + guess.getLocationResult() + "\", " +
                    "\"gameResult\": \"" + guess.getGameResult() + "\", " +
                    "\"gameArrow\": \"" + guess.getGameArrow() + "\", " +
                    "\"valueResult\": \"" + guess.getValueResult() + "\", " +
                    "\"valueArrow\": \"" + guess.getValueArrow() + "\"" +
                    "}"
                );
            }
            out.print(" ]}");

            response.setStatus(200);

        } catch (Exception e) {
            response.setStatus(500);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}