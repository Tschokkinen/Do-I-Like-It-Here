package com.tschokkinen.doilikeithere.models;

public class SelectionItem {
    private int id;
    private String name;
    private int weight;
    private Boolean selected;

    public SelectionItem(int id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.selected = false;
    }

    public String getName() {
        return name;
    }

    public void Selected() {
        if (!selected) {
            selected = true;
        } else {
            selected = false;
        }
    }

    public Boolean getSelected() {
        return selected;
    }

}
