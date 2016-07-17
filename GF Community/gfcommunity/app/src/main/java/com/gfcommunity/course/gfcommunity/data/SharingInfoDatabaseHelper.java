package com.gfcommunity.course.gfcommunity.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;

/**
 * Sharing Information DatabaseHelper : products, recipes and tips
 */
public class SharingInfoDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; // Database Version
    // Table Create Statements

    //Products table create statement
    private static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE IF NOT EXISTS "
            + SharingInfoContract.ProductsEntry.TABLE_NAME + "("
            + SharingInfoContract.ProductsEntry._ID + " INTEGER PRIMARY KEY, "
            + SharingInfoContract.ProductsEntry.PRODUCT_NAME + " TEXT, "
            + SharingInfoContract.ProductsEntry.STORE_NAME + " TEXT, "
            + SharingInfoContract.ProductsEntry.STORE_URL + " TEXT, "
            + SharingInfoContract.ProductsEntry.CITY + " TEXT, "
            + SharingInfoContract.ProductsEntry.STREET + " TEXT, "
            + SharingInfoContract.ProductsEntry.HOUSE_NO + " INTEGER, "
            + SharingInfoContract.ProductsEntry.COMMENT + " TEXT, "
            + SharingInfoContract.ProductsEntry.PHONE + " TEXT, "
            + SharingInfoContract.ProductsEntry.CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
            + SharingInfoContract.ProductsEntry.IMAGE_URI + " TEXT, "
            + SharingInfoContract.ProductsEntry.USER_ID + " TEXT"+");";

    //Recipes table create statement
    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE IF NOT EXISTS "
                    + SharingInfoContract.RecipesEntry.TABLE_NAME + "("
                    + SharingInfoContract.RecipesEntry._ID + " INTEGER PRIMARY KEY, "
                    + SharingInfoContract.RecipesEntry.RECIPE_NAME + " TEXT, "
                    + SharingInfoContract.RecipesEntry.INGREDIENTS + " TEXT, "
                    + SharingInfoContract.RecipesEntry.INSTRUCTIONS + " TEXT, "
                    + SharingInfoContract.RecipesEntry.RECIPE_IMAGE_URl + " TEXT, "
                    + SharingInfoContract.RecipesEntry.PREPARATION_TIME + " TEXT, "
                    + SharingInfoContract.RecipesEntry.DINERS_NUMBER + " INTEGER, "
                    + SharingInfoContract.RecipesEntry.DIFFICULTY_PREPARATION + " TEXT, "
                    + SharingInfoContract.RecipesEntry.RECIPE_STORY + " TEXT, "
                    + SharingInfoContract.RecipesEntry.CATEGORY + " TEXT, "
                    + SharingInfoContract.RecipesEntry.CREATED_AT + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + SharingInfoContract.RecipesEntry.USER_ID + " TEXT"+");";

    public SharingInfoDatabaseHelper(Context context){
        super(context,"SharingInfoDB",null,DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_RECIPES);

        insertFakeData(db);
        ContentValues values = new ContentValues();

        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Mazon min hateva");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Rye flour");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 147);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Rabi Akiva");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Tel Aviv");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME,null, values);
    }

    private void insertFakeData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Super Sal Market");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Gluten free cookies");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 25);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Herzel");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Tel Aviv");
        values.put(SharingInfoContract.ProductsEntry.PHONE, "0526984458");
        values.put(SharingInfoContract.ProductsEntry.IMAGE_URI, "http://www.lance.com/assets/img/temp/snacks/gf_pb_8pk_crtn.png?1431482040");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Teva Banachala");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Gluten free pasta");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 98);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Nakhalat Yitskhak");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Hadera");
        values.put(SharingInfoContract.ProductsEntry.PHONE, "02-696-7474");
        values.put(SharingInfoContract.ProductsEntry.IMAGE_URI, "http://makingourlifematter.com/wp-content/uploads/2014/02/gf-family-hp.png");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Nizat haduvdevan");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Gluten free crispy");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 58);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Ibn Gabirol");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Tel aviv");
        values.put(SharingInfoContract.ProductsEntry.PHONE, "03-696-5174");
        values.put(SharingInfoContract.ProductsEntry.IMAGE_URI, "http://www.miltonsbaking.com/assets/Uploads/GlutenFreeSeaSaltLARGE.jpg");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(SharingInfoContract.ProductsEntry.STORE_NAME, "Shkedia");
        values.put(SharingInfoContract.ProductsEntry.PRODUCT_NAME, "Gluten free kornfleks");
        values.put(SharingInfoContract.ProductsEntry.HOUSE_NO, 56);
        values.put(SharingInfoContract.ProductsEntry.STREET, "Katzenelson");
        values.put(SharingInfoContract.ProductsEntry.CITY, "Ramat gan");
        values.put(SharingInfoContract.ProductsEntry.PHONE, "03-731-9958");
        values.put(SharingInfoContract.ProductsEntry.IMAGE_URI, "https://nyoobserver.files.wordpress.com/2015/09/screen-shot-2015-09-22-at-11-02-05-am-e1442935619503.png?w=242&h=300");
        db.insert(SharingInfoContract.ProductsEntry.TABLE_NAME, null, values);

        values = new ContentValues();
        values.put(SharingInfoContract.RecipesEntry.CATEGORY, "Shkedia");
        values.put(SharingInfoContract.RecipesEntry.CATEGORY, "Fish Recipes");
        values.put(SharingInfoContract.RecipesEntry.INGREDIENTS, "½ pack ready roll short crust pastry ;" +
                "100 grams chicken/lamb or pork. Cooked and 1/2 inch dice ;" +
                "1/2 cooked carrot ;" +
                "1 cooked roast potatoes ;" +
                "50 grams cooked swede ;" +
                "10 grams cooked peas ;" +
                "3 tbsp. of leftover gravy ;" +
                "1 egg");
        values.put(SharingInfoContract.RecipesEntry.INSTRUCTIONS, "Pre heat your oven to 180degrees.;" +
                "Use a large mixing bowl, take the cooked carrot and crush it between your fingers into the bowl, repeat with the swede and potato, and stir in a meat of choice, peas and gravy. Stir until evenly mixed. ;" +
                "Take the ready rolled pastry and cut out a circle about 10 inches across. You can use a medium sized dinner plate to cut around. Firmly pack the filling along the centre line leaving a 1 inch margin at the edges. ;" +
                "Brush all around the edge of the pastry with the beaten egg. Carefully lift up both sides of the pastry so that they meet at the top, then pinch them together to seal, ensuring there are no gaps.;" +
                "Carefully lift onto a grease proof lined baking tray. Now brush the entire pasty with the egg wash. Bake for 45 minutes until golden. ;" +
                "Serve and enjoy.");
        values.put(SharingInfoContract.RecipesEntry.RECIPE_IMAGE_URl, "http://www.neelscorner.com/wp-content/uploads/2009/07/wholethaifish2_thumb.jpg");
        values.put(SharingInfoContract.RecipesEntry.PREPARATION_TIME, "50 minutes");
        values.put(SharingInfoContract.RecipesEntry.DIFFICULTY_PREPARATION, "Easy");
        db.insert(SharingInfoContract.RecipesEntry.TABLE_NAME, null, values);


        values = new ContentValues();
        values.put(SharingInfoContract.RecipesEntry.CATEGORY, "Cakes");
        values.put(SharingInfoContract.RecipesEntry.RECIPE_NAME, "GLUTEN-FREE CHOCOLATE CHIP CAKE");
        values.put(SharingInfoContract.RecipesEntry.INGREDIENTS, "1 box King Arthur Gluten-Free Chocolate Cake Mix;" +
                "2/3 cup vegetable oil;" +
                "2 teaspoons vanilla extract;" +
                "4 large eggs;" +
                "1 1/3 cups water;" +
                "2 cups chocolate chips;" +
                "1 cup chopped walnuts or pecans");
        values.put(SharingInfoContract.RecipesEntry.INSTRUCTIONS, "Preheat the oven to 350°F. Grease a full size (12-cup) Bundt or tube pan.;" +
                "To make the cake: Place the cake mix in a bowl. Beat in the oil, vanilla, and 1 egg.;" +
                "Add the remaining eggs one at a time, beating well after each addition.;" +
                "Add the water 1/3 at a time, beating until smooth and scraping the bowl after each addition.;" +
                "Stir in the chocolate chips and nuts.;" +
                "Bake the cake for 50 to 55 minutes, until a cake tester or toothpick inserted into the center comes out with perhaps crumbs clinging to it, but no wet batter.;" +
                "Remove the cake from the oven, and after 15 minutes turn it out onto a rack.;" +
                "To make the glaze: Melt the butter in a medium-sized saucepan set over low heat.;" +
                "Stir in the corn syrup, cocoa, water, vanilla, espresso powder, and salt. Heat and stir until the salt dissolves.;" +
                "Mix in enough confectioners' sugar to make a glaze with the consistency of thick molasses; you may not need the entire 2 cups.;" +
                "Transfer the cake to a serving plate; if it's still a bit warm, that's OK.;" +
                "Spoon the glaze over the cake, covering it as thoroughly as possible. Reheat the glaze briefly if it becomes too thick to pour");
        values.put(SharingInfoContract.RecipesEntry.RECIPE_IMAGE_URl, "http://d2gk7xgygi98cy.cloudfront.net/4033-3-large.jpg");
        values.put(SharingInfoContract.RecipesEntry.PREPARATION_TIME, "1 hrs 27 mins");
        values.put(SharingInfoContract.RecipesEntry.DIFFICULTY_PREPARATION, "hard");
        db.insert(SharingInfoContract.RecipesEntry.TABLE_NAME, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
