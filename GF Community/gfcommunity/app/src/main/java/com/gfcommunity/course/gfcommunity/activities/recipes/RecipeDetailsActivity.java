package com.gfcommunity.course.gfcommunity.activities.recipes;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.model.Recipe;
import com.gfcommunity.course.gfcommunity.recyclerView.DividerItemDecoration;
import com.gfcommunity.course.gfcommunity.recyclerView.recipes.InstructionsAndIngredientsAdapter;


public class RecipeDetailsActivity extends AppCompatActivity{
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        recipe = (Recipe) getIntent().getSerializableExtra("selected_item"); //get current recipe passed from RecipesAdapter

        setRecipeValues(); //Set recipe details in the textViews
    }

    /**
     * Set recipe details in the textViews
     */
    private void setRecipeValues() {
        TextView recipeNameTxt = (TextView) findViewById(R.id.recipe_name_txt);
        String recipeName = recipe.getRecipeName();
        recipeNameTxt.setText(!TextUtils.isEmpty(recipeName) ? recipeName : "");

        ImageView recipeImg = (ImageView) findViewById(R.id.recipe_img);
        String imgUrl = recipe.getRecipeImgUri();
        if(!TextUtils.isEmpty(imgUrl)){
            Glide.with(this).load(imgUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.recipes)
                    .error(R.drawable.recipes)
                    .into(recipeImg);
        }

        //Ingredients RecyclerView
        RecyclerView ingredientsRecyclerView = (RecyclerView)findViewById(R.id.ingredients_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        ingredientsRecyclerView.setLayoutManager(mLayoutManager);
        ingredientsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        String [] ingredientsArray = recipe.getIngredients().split(";");
        InstructionsAndIngredientsAdapter ingredientsAdapter= new InstructionsAndIngredientsAdapter(this, ingredientsArray);
        ingredientsRecyclerView.setAdapter(ingredientsAdapter);

        TextView instructionsTxt = (TextView) findViewById(R.id.instructions_txt);
        String instructions = recipe.getInstructions();
        instructionsTxt.setText(!TextUtils.isEmpty(instructions) ? instructions : "");

        TextView preparationTimeTxt = (TextView) findViewById(R.id.preparation_time_txt);
        String preparationTime = recipe.getPreparationTime();
        if(!TextUtils.isEmpty(preparationTime)) {
            preparationTimeTxt.setText(String.format(getString(R.string.preparation_time_details),preparationTime));
        } else {
            preparationTimeTxt.setVisibility(View.GONE);
        }

        TextView dinersNumberTxt = (TextView) findViewById(R.id.diners_number_txt);
        int dinersNumber = recipe.getDinersNumber();
        if(dinersNumber > 0) {
            dinersNumberTxt.setText(String.format(getString(R.string.diners_number_details),dinersNumber));
        } else {
            dinersNumberTxt.setVisibility(View.GONE);
        }

        TextView difficultyPreparationTxt = (TextView) findViewById(R.id.difficulty_preparation_txt);
        String difficultyPreparation = recipe.getDifficultyPreparation();
        if(!TextUtils.isEmpty(difficultyPreparation)) {
            difficultyPreparationTxt.setText(String.format(getString(R.string.difficulty_preparation_details),difficultyPreparation));
        } else {
            difficultyPreparationTxt.setVisibility(View.GONE);
        }

        TextView recipeStoryTxt = (TextView) findViewById(R.id.recipe_story_txt);
        String recipeStory = recipe.getRecipeStory();
        if(!TextUtils.isEmpty(recipeStory)) {
            recipeStoryTxt.setText(recipeStory);
        } else {
            recipeStoryTxt.setVisibility(View.GONE);
        }

        TextView recipetUserUploadedTxt = (TextView) findViewById(R.id.added_by_txt);
        //TODO: set user name
        String recipetUserUploaded = String.format(getResources().getString(R.string.user_uploaded_text), !TextUtils.isEmpty(recipe.getUserID()) ? "user name" : getString(R.string.app_name));
        recipetUserUploadedTxt.setText(!TextUtils.isEmpty(recipetUserUploaded) ? recipetUserUploaded : "");

    }

}
