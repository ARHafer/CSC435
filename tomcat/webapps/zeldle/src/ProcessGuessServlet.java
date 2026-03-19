import java.io.*;
import java.sql.*;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

/*
 * TODO:
 * Handle items of the same name from different games.
 * Add the handheld entries.
 * Handle the Oracle games.
 */

@WebServlet("/processGuess")
public class ProcessGuessServlet extends HttpServlet {

    // Needed for determining if an guessed item's game is within one release of the target's.
    // Just the console games for now.
    private static final List<String> GAME_ORDER = Arrays.asList(
        "The Legend of Zelda",
        "Zelda II: The Adventure of Link",
        "A Link to the Past",
        "Ocarina of Time",
        "Majora's Mask",
        "The Wind Waker",
        "Twilight Princess",
        "Skyward Sword"
    );

    private String handleFeedback(String guessedItem, String targetItem) {
        String result;

        if (guessedItem.equalsIgnoreCase(targetItem)) {
            result = "correct";
        } else {
            result = "incorrect";
        }

        return "{\"guessed\": \"" + guessedItem + "\", \"result\": \"" + result + "\"}";
    }

    private String handleGameFeedback(String guessedGame, String targetGame) {
        String result;
        String arrow = "";
        int guessedIndex = GAME_ORDER.indexOf(guessedGame);
        int targetIndex = GAME_ORDER.indexOf(targetGame);

        if (guessedGame.equalsIgnoreCase(targetGame)) {
            result = "correct";
        } else if (guessedIndex - targetIndex == 1) {
            result = "withinOne";
            arrow = "down";
        } else if (guessedIndex - targetIndex == -1) {
            result = "withinOne";
            arrow = "up";
        } else {
            result = "incorrect";
        }

        if (result.equals("withinOne")) {
            return "{\"guessed\": \"" + guessedGame + "\", \"result\": \"" + result + "\", \"arrow\": \"" + arrow + "\"}";
        } else {
            return "{\"guessed\": \"" + guessedGame + "\", \"result\": \"" + result + "\"}";
        }
    }

    private String handleValueFeedback(Integer guessedValue, Integer targetValue) {
        String result;
        String arrow = "";

        if (targetValue == null) {
            result = "N/A";
        } else if (guessedValue.equals(targetValue)) {
            result = "correct";
        } else if (guessedValue > targetValue) {
            result = "incorrect";
            arrow = "down";
        } else {
            result = "incorrect";
            arrow = "up";
        }

        if (result.equals("incorrect") && arrow != null) {
            return "{\"guessed\": " + guessedValue + ", \"result\": \"" + result + "\", \"arrow\": \"" + arrow + "\"}";
        } else {
            return "{\"guessed\": " + guessedValue + ", \"result\": \"" + result + "\"}";
        }
    }

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

        int guessesRemaining = (int) session.getAttribute("guessesRemaining");
        String guess = request.getParameter("guess");
        Item guessedItem;

        if (guess == null || guess.isBlank()) {
            response.setStatus(400);
            out.print("{\"error\": \"No guess provided.\"}");
            return;
        }

        try {
            guessedItem = DatabaseHandler.getItem(guess.trim());
        } catch (SQLException e) {
            response.setStatus(500);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
            return;
        }

        if (guessedItem == null) {
            response.setStatus(200);
            out.print("{\"validItem\": false, \"message\": \"No such item exists.\"}");
            return;
        }

        if (guessesRemaining <= 0) {
            response.setStatus(400);
            out.print("{\"error\": \"Game over; No guesses remaining.\"}");
            return;
        }

        Item targetItem = (Item) session.getAttribute("targetItem");

        guessesRemaining--;
        session.setAttribute("guessesRemaining", guessesRemaining);

        boolean correctGuess = guessedItem.getName().equalsIgnoreCase(targetItem.getName());

        out.print("{" +
            "\"guessesRemaining\": " + guessesRemaining + ", " +
            "\"correctGuess\": " + correctGuess + ", " +
            "\"feedback\": {" +
                "\"name\": " + handleFeedback(guessedItem.getName(), targetItem.getName()) + ", " +
                "\"category\": " + handleFeedback(guessedItem.getCategory(), targetItem.getCategory()) + ", " +
                "\"type\": " + handleFeedback(guessedItem.getType(), targetItem.getType()) + ", " +
                "\"location\": " + handleFeedback(guessedItem.getLocation(), targetItem.getLocation()) + ", " +
                "\"game\": " + handleGameFeedback(guessedItem.getGame(), targetItem.getGame()) + ", " +
                "\"value\": " + handleValueFeedback(guessedItem.getValue(), targetItem.getValue()) +
            "}" +
        "}");
    }
}
