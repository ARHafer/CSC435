package model;

public class Guess {
    private String sessionID, guessedName, guessedClassification, guessedPType, guessedSType, guessedLocation, guessedGame;
    private Integer guessedValue;
    private String nameResult, classificationResult, pTypeResult, sTypeResult, locationResult, gameResult, gameArrow, valueResult, valueArrow;
    private boolean correct;
    private Integer guessesRemaining;

    public Guess(String sessionID, String guessedName, String guessedClassification, String guessedPType, String guessedSType, String guessedLocation, String guessedGame, Integer guessedValue,
            String nameResult, String classificationResult, String pTypeResult, String sTypeResult, String locationResult, String gameResult, String gameArrow, String valueResult, String valueArrow,
            boolean correct, Integer guessesRemaining) {
        this.sessionID = sessionID;
        this.guessedName = guessedName;
        this.guessedClassification = guessedClassification;
        this.guessedPType = guessedPType;
        this.guessedSType = guessedSType;
        this.guessedLocation = guessedLocation;
        this.guessedGame = guessedGame;
        this.guessedValue = guessedValue;
        this.nameResult = nameResult;
        this.classificationResult = classificationResult;
        this.pTypeResult = pTypeResult;
        this.sTypeResult = sTypeResult;
        this.locationResult = locationResult;
        this.gameResult = gameResult;
        this.gameArrow = gameArrow;
        this.valueResult = valueResult;
        this.valueArrow = valueArrow;
        this.correct = correct;
        this.guessesRemaining = guessesRemaining;
    }

    // Getters //
    public String getSessionID() {
        return sessionID;
    } public String getGuessedName() {
        return guessedName;
    } public String getGuessedClassification() {
        return guessedClassification;
    } public String getGuessedPType() {
        return guessedPType;
    } public String getGuessedSType() {
        return guessedSType;
    } public String getGuessedLocation() {
        return guessedLocation;
    } public String getGuessedGame() {
        return guessedGame;
    } public Integer getGuessedValue() {
        return guessedValue;
    } public String getNameResult() {
        return nameResult;
    } public String getClassificationResult() {
        return classificationResult;
    } public String getPTypeResult() {
        return pTypeResult;
    } public String getSTypeResult() {
        return sTypeResult;
    } public String getLocationResult() {
        return locationResult;
    } public String getGameResult() {
        return gameResult;
    } public String getGameArrow() {
        return gameArrow;
    } public String getValueResult() {
        return valueResult;
    } public String getValueArrow() {
        return valueArrow;
    } public boolean isCorrect() {
        return correct;
    } public Integer getGuessesRemaining() {
        return guessesRemaining;
    }

}