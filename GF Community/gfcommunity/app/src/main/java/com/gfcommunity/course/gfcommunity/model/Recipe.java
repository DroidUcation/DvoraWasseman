package com.gfcommunity.course.gfcommunity.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Recipe to present row in recipe list
 */
public class Recipe implements Serializable{
    private String recipeName;
    private String ingredients;
    private String instructions;
    private String recipeImgUri;
    private String preparationTime;
    private int dinersNumber;
    private String difficultyPreparation;
    private String recipeStory;
    private String category;
    private String userID;
    private Timestamp createdAt;

    public Recipe(){}

    public Recipe(String recipeName, String ingredients, String instructions, String recipeImgUri, String preparationTime, int dinersNumber, String difficultyPreparation, String recipeStory, String category, Timestamp createdAt, String userID) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.recipeImgUri = recipeImgUri;
        this.preparationTime = preparationTime;
        this.dinersNumber = dinersNumber;
        this.difficultyPreparation = difficultyPreparation;
        this.recipeStory = recipeStory;
        this.category = category;
        this.createdAt = createdAt;
        this.userID = userID;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getRecipeImgUri() {
        return recipeImgUri;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public int getDinersNumber() {
        return dinersNumber;
    }

    public String getDifficultyPreparation() {
        return difficultyPreparation;
    }

    public String getCategory() {
        return category;
    }

    public String getRecipeStory() {
        return recipeStory;
    }

    public String getUserID() {
        return userID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setRecipeImgUri(String recipeImgUri) {
        this.recipeImgUri = recipeImgUri;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public void setDinersNumber(int dinersNumber) {
        this.dinersNumber = dinersNumber;
    }

    public void setDifficultyPreparation(String difficultyPreparation) {
        this.difficultyPreparation = difficultyPreparation;
    }

    public void setRecipeStory(String recipeStory) {
        this.recipeStory = recipeStory;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "recipeName='" + recipeName + '\'' +
                ", ingredients=" + ingredients +
                ", instructions='" + instructions + '\'' +
                ", recipeImgUri='" + recipeImgUri + '\'' +
                ", preparationTime='" + preparationTime + '\'' +
                ", dinersNumber='" + dinersNumber + '\'' +
                ", difficultyPreparation='" + difficultyPreparation + '\'' +
                ", recipeStory='" + recipeStory + '\'' +
                ", category='" + category + '\'' +
                ", userID='" + userID + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
