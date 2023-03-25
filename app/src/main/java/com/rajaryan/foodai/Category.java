package com.rajaryan.foodai;

public class Category {
    String Name,Image,
            Ingredient;

    public Category(String name, String image, String ingredient) {
        Name = name;
        Image = image;
        Ingredient = ingredient;
    }

    public Category() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getIngredient() {
        return Ingredient;
    }

    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
    }
}
