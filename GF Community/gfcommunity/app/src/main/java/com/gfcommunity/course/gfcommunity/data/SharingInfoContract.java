package com.gfcommunity.course.gfcommunity.data;

import android.provider.BaseColumns;

/**
 * Sharing Information Contract : products and recipes
 */
public class SharingInfoContract {

    //Products
    public  static  class ProductsEntry implements BaseColumns {
        public static  final String TABLE_NAME = "ProductsTBL";
        public static  final String PRODUCT_NAME = "product_name";
        public static  final String STORE_NAME = "store_name";
        public static  final String STORE_URL = "store_url";
        public static  final String CITY = "city";
        public static  final String STREET = "street";
        public static  final String HOUSE_NO = "house_no";
        public static  final String COMMENT = "comment";
        public static  final String PHONE = "phone";
        public static  final String CREATED_AT = "created_at";
        public static  final String IMAGE_URI = "img_uri";
        public static  final String USER_ID = "user_id";
    }

    //Recipes
    public  static  class RecipesEntry implements BaseColumns {
        public static  final String TABLE_NAME = "RecipesTBL";
        public static  final String RECIPE_NAME = "recipe_name";
        public static  final String INGREDIENTS = "ingredients"; // Column INGREDIENTS is a list of string separated by ||.
        public static  final String INSTRUCTIONS = "instructions"; // Column INSTRUCTIONS is a list of string separated by ||.
        public static  final String RECIPE_IMAGE_URl = "recipe_img_url";
        public static  final String PREPARATION_TIME = "preparation_time";
        public static  final String DINERS_NUMBER = "diners_number";
        public static  final String DIFFICULTY_PREPARATION = "difficulty_preparation";
        public static  final String RECIPE_STORY = "recipe_story";
        public static  final String CATEGORY = "category";
        public static  final String CREATED_AT = "created_at";
        public static  final String USER_ID = "user_id";
    }
}
