package model;

public class Item {
    private int id;
    private String name, classification, pType, sType, location, game;
    private Integer value;

    public Item(int id, String name, String classification, String pType, String sType, String location, String game, Integer value) {
        this.id = id;
        this.name = name;
        this.classification = classification;
        this.pType = pType;
        this.sType = sType;
        this.location = location;
        this.game = game;
        this.value = value;
    }

    // Getters //
    public int getID() {
        return id;
    } public String getName() {
        return name;
    } public String getClassification() {
        return classification;
    } public String getPType() {
        return pType;
    } public String getSType() {
        return sType;
    } public String getLocation() {
        return location;
    } public String getGame() {
        return game;
    } public Integer getValue() {
        return value;
    }
}