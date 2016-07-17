package com.gfcommunity.course.gfcommunity.activities.recipes;


import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.MainActivity;
import com.gfcommunity.course.gfcommunity.data.recipes.RecipesContentProvider;
import com.gfcommunity.course.gfcommunity.fragments.RecipesFragment;
import com.gfcommunity.course.gfcommunity.loaders.DeleteLoader;
import com.gfcommunity.course.gfcommunity.model.Recipe;
import com.gfcommunity.course.gfcommunity.recyclerView.recipes.InstructionsAndIngredientsAdapter;
import com.gfcommunity.course.gfcommunity.utils.NetworkConnectedUtil;


public class RecipeDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Integer>{
    private Recipe recipe;
    private Menu mMenu;
    private Toolbar toolbar;
    private int selectedRecipeId;
    private int loaderID = 1;//Delete recipe loader ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnBackPress();
            }
        });
        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("selected_item"); //get current recipe passed from RecipesAdapter
        selectedRecipeId = intent.getIntExtra("selectedItemId", -1);
        setRecipeValues(); //Set recipe details in the textViews
    }
    private void handleOnBackPress() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mMenu == null) {
            mMenu = menu;
        }
        getMenuInflater().inflate(R.menu.toolbar, mMenu); // Inflate the menu; this adds items to the action bar if it is present.
        setToolbar();
        return true;
    }

    private void setToolbar() {
        toolbar.setTitle(getString(R.string.recipe_details));
        mMenu.findItem(R.id.action_search).setVisible(false);
        mMenu.findItem(R.id.action_share).setVisible(true);
        mMenu.findItem(R.id.action_favorites).setVisible(true);
        mMenu.findItem(R.id.action_edit).setVisible(true);
        mMenu.findItem(R.id.action_delete).setVisible(true);
        mMenu.findItem(R.id.action_navigate).setVisible(true);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.action_edit:
                item.setChecked(true);
                if (NetworkConnectedUtil.isNetworkAvailable(this)) {
                    intent = new Intent(this, AddRecipeActivity.class);
                    intent.putExtra("selectedRecipeId", selectedRecipeId);//send product id to init
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_delete:
                item.setChecked(true);
                if (NetworkConnectedUtil.isNetworkAvailable(this)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppAlertDialogStyle);
                    builder.setTitle(getResources().getString(R.string.delete_recipe));
                    builder.setMessage(getResources().getString(R.string.dialog_msg_delete_recipe));
                    builder.setPositiveButton(getResources().getString(R.string.confirm_option), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            //clicked on ok button
                            deleteItem();// perform delete item action
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.cancel_option), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            //clicked on cancel button
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.show();
                    // Must call show() prior to fetching text view
                    TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
                    messageView.setGravity(Gravity.CENTER);

                } else {
                    Toast.makeText(this, getString(R.string.no_internet_connection_msg), Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * perform delete item action
     */
    private void deleteItem() {
        Bundle b = new Bundle();
        b.putCharSequence("itemIdToDelete", selectedRecipeId +"");
        getSupportLoaderManager().restartLoader(loaderID, b, this).forceLoad();//Initializes delete Loader

    }


    @Override
    public Loader<Integer> onCreateLoader(int id, Bundle args) {
        Uri uri = ContentUris.withAppendedId(RecipesContentProvider.RECIPES_CONTENT_URI, selectedRecipeId);
        return new DeleteLoader(this, uri);

    }

    @Override
    public void onLoadFinished(Loader<Integer> loader, Integer data) {
        RecipesFragment.recipesAdapter.toggleSelection(selectedRecipeId);
        finish(); //Close this activity and go back to Main Activity
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Integer> loader) {

    }
}
