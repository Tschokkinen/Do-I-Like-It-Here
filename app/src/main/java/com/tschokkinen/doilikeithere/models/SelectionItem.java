package com.tschokkinen.doilikeithere.models;

public class SelectionItem {
    private int id;
    private String name;
    private int weight;
    private Boolean hasBeenSelected;

    public SelectionItem(int id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.hasBeenSelected = false;
    }

    public String getName() {
        return name;
    }

    public int getWeight() { return weight; }

    public void setHasBeenSelected() {
        if (!hasBeenSelected) {
            hasBeenSelected = true;
        } else {
            hasBeenSelected = false;
        }
    }

    public Boolean getHasBeenSelected() {
        return hasBeenSelected;
    }

}
