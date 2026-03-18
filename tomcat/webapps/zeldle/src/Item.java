public class Item {
    private int id;
    private String name, category, type, location, game;
    private Integer value;

    public Item(int id, String name, String category, String type, String location, String game, Integer value) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.type = type;
        this.location = location;
        this.game = game;
        this.value = value;
    }

    // Getters //
    public int getID() {
        return id;
    } public String getName() {
        return name;
    } public String getCategory() {
        return category;
    } public String getType() {
        return type;
    } public String getLocation() {
        return location;
    } public String getGame() {
        return game;
    } public Integer getValue() {
        return value;
    }
}
