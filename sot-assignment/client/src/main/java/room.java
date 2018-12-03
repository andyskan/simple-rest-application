public class room {
    private int id;
    private int width;
    private String location;
    private int price;
    private String occupier;

    public room() {
    }

    public String getOccupier() {
        return occupier;
    }

    public void setOccupier(String occupier) {
        this.occupier = occupier;
    }

    public room(int id, int width, String location, int price) {
        this.id = id;
        this.width = width;
        this.location = location;
        this.price = price;
        this.occupier = "None";
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public String getLocation() {
        return location;
    }

    public int getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
