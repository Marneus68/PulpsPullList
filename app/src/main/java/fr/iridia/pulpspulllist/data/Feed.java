package fr.iridia.pulpspulllist.data;

import java.util.ArrayList;

public class Feed {
    public String name = null;
    public String link = null;
    public String lastEdited = null;
    public ArrayList<Item> items = null;

    public Feed() {
        items = new ArrayList<>();
    }

    public void addItem(String title, String content) {
        items.add(new Item(title, content));
    }

    public void addItem(String title, String content, String image) {
        items.add(new Item(title, content, image));
    }
}
