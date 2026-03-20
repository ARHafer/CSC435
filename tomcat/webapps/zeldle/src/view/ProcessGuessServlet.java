package view;

import java.io.*;
import java.sql.*;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import model.Item;
import model.Guess;
import controller.GameController;

@WebServlet("/processGuess")
public class ProcessGuessServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("targetItem") == null) {
            response.setStatus(400);
            out.print("{\"error\": \"An item has not been selected. Call /chooseItem.\"}");
            return;
        }

        String guess = request.getParameter("guess");
        if (guess == null || guess.isBlank()) {
            response.setStatus(400);
            out.print("{\"error\": \"No guess provided.\"}");
            return;
        }

        int guessesRemaining = (int) session.getAttribute("guessesRemaining");
        if (guessesRemaining <= 0) {
            response.setStatus(400);
            out.print("{\"error\": \"Game over; No guesses remaining.\"}");
            return;
        }

        try {
        Item guessedItem = GameController.validateGuess(guess);
        if (guessedItem == null) {
            response.setStatus(200);
            out.print("{\"validItem\": false, \"message\": \"No such item exists.\"}");
            return;
        }

        Item targetItem = (Item) session.getAttribute("targetItem");

        guessesRemaining--;
        session.setAttribute("guessesRemaining", guessesRemaining);

        Guess guessResult = GameController.processGuess(session.getId(), guessedItem, targetItem, guessesRemaining);

        response.setStatus(200);
        out.print("{" +
        "\"guessesRemaining\": " + guessResult.getGuessesRemaining() + ", " +
        "\"correct\": " + guessResult.isCorrect() + ", " +
        "\"results\": {" +
        "\"name\": {\"guessed\": \"" + guessResult.getGuessedName() + "\", \"result\": \"" + guessResult.getNameResult() + "\"}, " +
        "\"classification\": {\"guessed\": \"" + guessResult.getGuessedClassification() + "\", \"result\": \"" + guessResult.getClassificationResult() + "\"}, " +
        "\"pType\": {\"guessed\": \"" + guessResult.getGuessedPType() + "\", \"result\": \"" + guessResult.getPTypeResult() + "\"}, " +
        "\"sType\": {\"guessed\": \"" + guessResult.getGuessedSType() + "\", \"result\": \"" + guessResult.getSTypeResult() + "\"}, " +
        "\"location\": {\"guessed\": \"" + guessResult.getGuessedLocation() + "\", \"result\": \"" + guessResult.getLocationResult() + "\"}, " +
        "\"game\": {\"guessed\": \"" + guessResult.getGuessedGame() + "\", \"result\": \"" + guessResult.getGameResult() + "\"" + arrowString(guessResult.getGameArrow()) + "}, " +
        "\"value\": {\"guessed\": " + guessResult.getGuessedValue() + ", \"result\": \"" + guessResult.getValueResult() + "\"" + arrowString(guessResult.getValueArrow()) + "}" +
        "}}"
        );

        } catch (SQLException e) {
            response.setStatus(500);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    private String arrowString(String arrow) {
            if (arrow == null) {
                return "";
            } else {
                return ", \"arrow\": \"" + arrow + "\"";
            }
        }
}