package com.glowingsoft.zvandiri.models;

/**
 * Created by Tausif ur Rehman on 2/10/2019.
 */

public class QuizCategoryModel {
    String id, CategoryName;

    public String getId() {
        return id;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public QuizCategoryModel(String id, String categoryName) {

        this.id = id;
        CategoryName = categoryName;
    }
}
