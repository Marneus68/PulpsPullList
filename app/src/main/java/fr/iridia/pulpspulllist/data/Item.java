package fr.iridia.pulpspulllist.data;

public class Item {
    public String image = null;
    public String title = null;
    public String content = null;

    public Item(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Item(String title, String content, String image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }
}
