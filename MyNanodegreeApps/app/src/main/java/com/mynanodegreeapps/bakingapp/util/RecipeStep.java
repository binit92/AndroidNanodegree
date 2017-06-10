package com.mynanodegreeapps.bakingapp.util;

public class RecipeStep {

    public int id;
    public String shortDesc;

    public RecipeStep(int id, String shortDesc){
        this.id = id;
        this.shortDesc = shortDesc;
    }

    public int getId(){return id;}
    public String getShortDesc() {return shortDesc;}
}
