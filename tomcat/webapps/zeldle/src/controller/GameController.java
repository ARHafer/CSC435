package controller;

import java.sql.*;
import java.util.*;

import model.Item;
import model.Guess;
import data.DatabaseHandler;

/*
 * TODO:
 * Handle items of the same name from different games.
 * Add the handheld entries.
 * Handle the Oracle games.
 */

public class GameController {
    public static Item chooseItem() throws SQLException {
        return DatabaseHandler.getRandomItem();
    }

    public static Item validateGuess(String guess) throws SQLException {
        return DatabaseHandler.getItem(guess);
    }

    // Needed for determining if an guessed item's game is within one release of the target's.
    // Just the console games for now.
    private static final List<String> GAME_ORDER = Arrays.asList(
        "The Legend of Zelda",
        // No Zelda II because it's weird.
        "A Link to the Past",
        "Ocarina of Time",
        "Majora's Mask",
        "The Wind Waker",
        "Twilight Princess",
        "Skyward Sword"
    );

    public static Guess processGuess(String sessionID, Item guessedItem, Item targetItem, int guessesRemaining) throws SQLException {
        String nameResult = handleFeedback(guessedItem.getName(), targetItem.getName());
        String classificationResult = handleFeedback(guessedItem.getClassification(), targetItem.getClassification());
        String pTypeResult = handleFeedback(guessedItem.getPType(), targetItem.getPType());
        String sTypeResult = handleFeedback(guessedItem.getSType(), targetItem.getSType());
        String locationResult = handleFeedback(guessedItem.getLocation(), targetItem.getLocation());
        String gameResult = handleGameFeedback(guessedItem.getGame(), targetItem.getGame());
        String gameArrow = getGameArrow(guessedItem.getGame(), targetItem.getGame());
        String valueResult = handleValueFeedback(guessedItem.getValue(), targetItem.getValue());
        String valueArrow = getValueArrow(guessedItem.getValue(), targetItem.getValue());

        boolean correct = nameResult.equals("correct") && classificationResult.equals("correct") && pTypeResult.equals("correct") &&
                    sTypeResult.equals("correct") && locationResult.equals("correct") && gameResult.equals("correct") && valueResult.equals("correct");
                    
        Guess guess = new Guess(
            sessionID,
            guessedItem.getName(),
            guessedItem.getClassification(),
            guessedItem.getPType(),
            guessedItem.getSType(),
            guessedItem.getLocation(),
            guessedItem.getGame(),
            guessedItem.getValue(),
            nameResult,
            classificationResult,
            pTypeResult,
            sTypeResult,
            locationResult,
            gameResult,
            gameArrow,
            valueResult,
            valueArrow,
            correct,
            guessesRemaining
        );

        DatabaseHandler.saveGuess(guess);
        return guess;
    }

    private static String handleFeedback(String guessedItem, String targetItem) {
        if (guessedItem == null && targetItem == null) {
            return "correct";
        } else if (guessedItem == null || targetItem == null) {
            return "incorrect";
        } else if (guessedItem.equalsIgnoreCase(targetItem)) {
            return "correct";
        } else {
            return "incorrect";
        }
    }

    private static String handleGameFeedback(String guessedGame, String targetGame) {
        int guessedIndex = GAME_ORDER.indexOf(guessedGame);
        int targetIndex = GAME_ORDER.indexOf(targetGame);

        if (guessedGame.equalsIgnoreCase(targetGame)) {
            return "correct";
        } else if (guessedIndex - targetIndex == 1 || guessedIndex - targetIndex == -1) {
            return "withinOne";
        } else {
            return "incorrect";
        }
    }

    private static String getGameArrow(String guessedGame, String targetGame) {
        int guessedIndex = GAME_ORDER.indexOf(guessedGame);
        int targetIndex = GAME_ORDER.indexOf(targetGame);

        if (guessedIndex > targetIndex) {
            return "down";
        } else if (guessedIndex < targetIndex) {
            return "up";
        } else {
            return null;
        }
    }

    private static String handleValueFeedback(Integer guessedValue, Integer targetValue) {
        if (guessedValue == null && targetValue != null) {
            return "incorrect";
        } else if ((guessedValue == null && targetValue == null) || guessedValue.equals(targetValue)) {
            return "correct";
        } else if (targetValue == null) {
            return null;
        } else {
            return "incorrect";
        }
    }

    private static String getValueArrow(Integer guessedValue, Integer targetValue) {
        if (guessedValue == null || targetValue == null) {
            return null;
        } else if ((guessedValue == null && targetValue != null) || guessedValue < targetValue) {
            return "up";
        } else if (guessedValue > targetValue) {
            return "down";
        } else {
            return null;
        }
    }

    public static List<Guess> getGuessHistory(String sessionID) throws SQLException {
        return DatabaseHandler.getGuessHistory(sessionID);
    }
}