package com.gfcommunity.course.gfcommunity.activities.recipes;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gfcommunity.course.gfcommunity.R;
import com.gfcommunity.course.gfcommunity.activities.MainActivity;
import com.gfcommunity.course.gfcommunity.data.SharingInfoContract;
import com.gfcommunity.course.gfcommunity.data.recipes.RecipesContentProvider;
import com.gfcommunity.course.gfcommunity.firebase.storage.UploadFile;
import com.gfcommunity.course.gfcommunity.loaders.UpdateLoader;
import com.gfcommunity.course.gfcommunity.loaders.recipe.InsertRecipeLoader;
import com.gfcommunity.course.gfcommunity.model.Recipe;
import com.gfcommunity.course.gfcommunity.recyclerView.recipes.RecipesAdapter;
import com.gfcommunity.course.gfcommunity.utils.SpinnerAdapter;

import java.io.File;
import java.util.ArrayList;

public class AddRecipeActivity extends AppCompatActivity  implements View.OnClickListener, AdapterView.OnItemSelectedListener, UploadFile.OnuploadCompletedListener{
    private Button addRecipetBtn;
    private EditText recipeNameEditTxt;
    private EditText ingredientsEditTxt;
    private EditText instructionsEditTxt;
    private EditText preparationTimeEditTxt;
    private EditText dinersNumberEditTxt;
    private EditText recipeStoryEditTxt;
    private Spinner recipesCategoriesSpinner;
    private ImageView recipeImg;
    private ImageView addIngredientImg;
    private ImageView addInstructionImg;
    private FloatingActionButton addRecipeImgBtn;
    private int loaderID = 1; //Insert recipes loader ID
    private int updateLoaderID = 2;//Update recipes loader ID
    private String logTag = AddRecipeActivity.class.getName();
    private String selectedRecipeCategory;
    private String selectedDifficultyPreparation;
    private Uri selectedImage;
    private String recipeName;
    private LinearLayout addIngredientsLayout;
    private LinearLayout addInstructionsLayout;
    private ArrayList<EditText> ingredientsEditTextsArray;
    private ArrayList<EditText> instructionsEditTextsArray;
	private int selectedRecipeId;
    private boolean updateActivity = false;
    private Context context;
    private Toolbar toolbar;
    private  String[] recipeCategoriesArray;
    private String[] difficultyPreparationArray;

    private LoaderManager.LoaderCallbacks<Uri> insertRecipeLoaderListener = new LoaderManager.LoaderCallbacks<Uri>() {
        @Override
        public Loader<Uri> onCreateLoader(int id, Bundle args) {
            return new InsertRecipeLoader(context, setRecipeValues(args));
        }


        @Override
        public void onLoadFinished(Loader<Uri> loader, Uri data) {
            Log.i(logTag, getString(R.string.recipe_added_msg)+ recipeName);
            Toast.makeText(context, String.format(getString(R.string.recipe_added_msg), recipeName),Toast.LENGTH_SHORT).show();//TODO: Show inserted successfully popup
            sendNotification(data); //Send notification
            finish(); //Close this activity and go back to Main Activity
        }

        @Override
        public void onLoaderReset(Loader<Uri> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Integer> updateRecipeLoaderListener = new LoaderManager.LoaderCallbacks<Integer>() {

        @Override
        public Loader<Integer> onCreateLoader(int id, Bundle args) {
            Uri uri = ContentUris.withAppendedId(RecipesContentProvider.RECIPES_CONTENT_URI, Integer.valueOf(selectedRecipeId));
            return new UpdateLoader(context, setRecipeValues(args), uri);
        }

        @Override
        public void onLoadFinished(Loader<Integer> loader, Integer data) {
            RecipesAdapter.ViewHolder.recipesMap.delete(selectedRecipeId);
            Log.i(logTag, getString(R.string.recipe_saved_msg)  + recipeName);
            Toast.makeText(context, String.format(getString(R.string.recipe_saved_msg), recipeName),Toast.LENGTH_SHORT).show();//TODO: Show inserted successfully popup
            handleOnBackPress(); //Close this activity and go back to Main Activity
        }

        @Override
        public void onLoaderReset(Loader<Integer> loader) {

        }
    };

    private ContentValues setRecipeValues(Bundle args) {
        String downloadUrlPath = args != null ? args.getString("downloadUrlPath") : "";
        ContentValues values = new ContentValues();
        recipeName = recipeNameEditTxt.getText().toString();
        values.put(SharingInfoContract.RecipesEntry.RECIPE_NAME, !TextUtils.isEmpty(recipeName) ? recipeName : "");
        String ingredients = ingredientsEditTxt.getText().toString();
        values.put(SharingInfoContract.RecipesEntry.INGREDIENTS, concatEditTextsArray(ingredientsEditTextsArray)); //Concat ingredientsEditTextsArray array to string separated by ';'
        String instructions = instructionsEditTxt.getText().toString();
        values.put(SharingInfoContract.RecipesEntry.INSTRUCTIONS, concatEditTextsArray(instructionsEditTextsArray)); //Concat instructionsEditTextsArray array to string separated by ';'
        values.put(SharingInfoContract.RecipesEntry.CREATED_AT, DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
        String preparationTime = preparationTimeEditTxt.getText().toString();
        values.put(SharingInfoContract.RecipesEntry.PREPARATION_TIME, !TextUtils.isEmpty(preparationTime) ? preparationTime : "");
        String dinersNumberStr = dinersNumberEditTxt.getText().toString();
        int dinersNumber = !TextUtils.isEmpty(dinersNumberStr) ? Integer.parseInt(dinersNumberStr) : 0;
        values.put(SharingInfoContract.RecipesEntry.DINERS_NUMBER, dinersNumber);
        String recipeStory = recipeStoryEditTxt.getText().toString();
        values.put(SharingInfoContract.RecipesEntry.RECIPE_STORY, !TextUtils.isEmpty(recipeStory) ? recipeStory : "");
        values.put(SharingInfoContract.RecipesEntry.DIFFICULTY_PREPARATION, !TextUtils.isEmpty(selectedDifficultyPreparation) && !(selectedDifficultyPreparation.equals(getString(R.string.select_difficulty_preparation))) ? selectedDifficultyPreparation : "");
        values.put(SharingInfoContract.RecipesEntry.CATEGORY, !TextUtils.isEmpty(selectedRecipeCategory) && !(selectedRecipeCategory.equals(getString(R.string.select_recipe_category))) ? selectedRecipeCategory : "");
        values.put(SharingInfoContract.RecipesEntry.RECIPE_IMAGE_URl, !TextUtils.isEmpty(downloadUrlPath) ? downloadUrlPath : "");
        return values;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        context = getBaseContext();
        initializeViews();//Define views and bind them to events

        Intent intent = getIntent();
        selectedRecipeId = intent.getIntExtra("selectedRecipeId" , -1);
        if (selectedRecipeId != -1) {
            initialEditedRecipe();
        }
    }

    private void initialEditedRecipe() {
        updateActivity = true;
        Uri _uri = ContentUris.withAppendedId(RecipesContentProvider.RECIPES_CONTENT_URI, Long.valueOf(selectedRecipeId));
        Cursor cursor = this.getContentResolver().query(_uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Recipe recipe = RecipesAdapter.setRecipeValues(cursor);

            addRecipetBtn.setText(getString(R.string.save_recipe));
            recipeNameEditTxt.setText(recipe.getRecipeName());
            preparationTimeEditTxt.setText(recipe.getPreparationTime());
            dinersNumberEditTxt.setText(recipe.getDinersNumber()+"");
            recipeStoryEditTxt.setText(recipe.getRecipeStory());

            String imgUrl =  recipe.getRecipeImgUri();
            if (!TextUtils.isEmpty(imgUrl)) {
                Glide.with(this).load(imgUrl)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.recipes)
                        .error(R.drawable.recipes)
                        .centerCrop()
                        .into(recipeImg);
            }

            recipesCategoriesSpinner = (Spinner) findViewById(R.id.recipes_categories_spinner);
            String category = recipe.getCategory();
            int indexCategory = 0;
            for (; indexCategory < recipeCategoriesArray.length - 1; indexCategory++) {
                if (recipeCategoriesArray[indexCategory].equals(category.trim()))
                    break;
            }
            recipesCategoriesSpinner.setSelection(indexCategory);

            Spinner difficultyPreparationSpinner = (Spinner) findViewById(R.id.difficulty_preparation_spinner);
            String difficultyPreparation = recipe.getDifficultyPreparation();
            int indexDifficultyPreparation = 0;
            for (; indexDifficultyPreparation < difficultyPreparationArray.length - 1; indexDifficultyPreparation++) {
                if (difficultyPreparationArray[indexDifficultyPreparation].equals(difficultyPreparation.trim()))
                    break;
            }
            difficultyPreparationSpinner.setSelection(indexDifficultyPreparation);

            EditText dynamicEdtText;
            String []ingredients = recipe.getIngredients().split(";");
            ingredientsEditTxt.setText(ingredients[0]);
            addIngredientImg.setVisibility(View.GONE);
            for(int i = 1; i < ingredients.length ; i++) {
                dynamicEdtText = addDynamicEditText(getString(R.string.ingredient), addIngredientsLayout, ingredientsEditTextsArray);
                dynamicEdtText.setText(ingredients[i]);
            }

            String []instructions = recipe.getInstructions().split(";");
            instructionsEditTxt.setText(instructions[0]);
            addInstructionImg.setVisibility(View.GONE);
            for(int i = 1; i < instructions.length ; i++) {
                dynamicEdtText = addDynamicEditText(getString(R.string.step), addInstructionsLayout, instructionsEditTextsArray);
                dynamicEdtText.setText(instructions[i]);
            }
        }
    }

    /**
     * Define views and bind them to events
     */
    private void initializeViews() {
        //Define views
        addRecipetBtn  = (Button) findViewById(R.id.add_recipe_btn);
        addRecipetBtn.setTransformationMethod(null);  //Ignore automatic upper case
        recipeNameEditTxt  = (EditText) findViewById(R.id.recipe_name_edit_text);
        ingredientsEditTxt  = (EditText) findViewById(R.id.ingredients);
        instructionsEditTxt  = (EditText) findViewById(R.id.instructions);
        preparationTimeEditTxt  = (EditText) findViewById(R.id.preparation_time_edit_txt);
        dinersNumberEditTxt  = (EditText) findViewById(R.id.diners_number_edit_txt);
        recipeStoryEditTxt  = (EditText) findViewById(R.id.recipe_story_edit_txt);
        recipesCategoriesSpinner = (Spinner) findViewById(R.id.recipes_categories_spinner);
        Spinner difficultyPreparationSpinner = (Spinner) findViewById(R.id.difficulty_preparation_spinner);
        addRecipeImgBtn = (FloatingActionButton)findViewById(R.id.select_recipe_img_btn);
        recipeImg = (ImageView)findViewById(R.id.recipe_img);
        addIngredientImg = (ImageView)findViewById(R.id.add_ingredient_img);
        addIngredientsLayout = (LinearLayout) findViewById(R.id.ingredients_layout);
        addInstructionImg = (ImageView)findViewById(R.id. add_instruction_img);
        addInstructionsLayout = (LinearLayout) findViewById(R.id.instructions_layout);

        //Array of edit text for ingredients
        ingredientsEditTextsArray = new ArrayList<EditText>();
        ingredientsEditTextsArray.add(ingredientsEditTxt);

        //Array of edit text for instructions
        instructionsEditTextsArray = new ArrayList<EditText>();
        instructionsEditTextsArray.add(instructionsEditTxt);

        //Bind views to Listener
        addRecipetBtn.setOnClickListener(this);
        addRecipeImgBtn.setOnClickListener(this);
        addIngredientImg.setOnClickListener(this);
        addInstructionImg.setOnClickListener(this);
        recipesCategoriesSpinner.setOnItemSelectedListener(this);
        difficultyPreparationSpinner.setOnItemSelectedListener(this);
        recipeNameEditTxt.addTextChangedListener(new RecipeTextWatcher(recipeNameEditTxt));
        ingredientsEditTxt.addTextChangedListener(new RecipeTextWatcher(ingredientsEditTxt));
        instructionsEditTxt.addTextChangedListener(new RecipeTextWatcher(instructionsEditTxt));

        //Categories spinner
        String[] recipeCategoriesArrayTemp = getResources().getStringArray(R.array.recipe_categories_array);
        recipeCategoriesArray = new String[(recipeCategoriesArrayTemp.length)+1];
        System.arraycopy(recipeCategoriesArrayTemp, 0, recipeCategoriesArray, 0, recipeCategoriesArrayTemp.length);
        recipeCategoriesArray[recipeCategoriesArrayTemp.length] = getResources().getString(R.string.select_recipe_category);
        SpinnerAdapter dataAdapter = new SpinnerAdapter(this, recipeCategoriesArray, android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// Drop down layout style - list view with radio button
        recipesCategoriesSpinner.setAdapter(dataAdapter);
        recipesCategoriesSpinner.setSelection(dataAdapter.getCount());// show hint

        //Difficulty Preparation spinner
        String[] difficultyPreparationArrayTemp = getResources().getStringArray(R.array.difficulty_recipe_preparation_array);
        difficultyPreparationArray = new String[(difficultyPreparationArrayTemp.length)+1];
        System.arraycopy(difficultyPreparationArrayTemp, 0, difficultyPreparationArray, 0, difficultyPreparationArrayTemp.length);
        difficultyPreparationArray[difficultyPreparationArrayTemp.length] = getResources().getString(R.string.select_difficulty_preparation);
        SpinnerAdapter difficultyPreparationDataAdapter = new SpinnerAdapter(this, difficultyPreparationArray, android.R.layout.simple_spinner_item);
        difficultyPreparationDataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);// Drop down layout style - list view with radio button
        difficultyPreparationSpinner.setAdapter(difficultyPreparationDataAdapter);
        difficultyPreparationSpinner.setSelection(difficultyPreparationDataAdapter.getCount());// show hint
        toolbar = (Toolbar) findViewById(R.id.toolbar_basic);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(toolbar!= null){
            if(updateActivity){
                toolbar.setTitle(getResources().getString(R.string.edit_recipe));
            }
            else{
                toolbar.setTitle(getResources().getString(R.string.add_recipe));
            }
            toolbar.setTitleTextColor(Color.WHITE);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_menu_left_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleOnBackPress();
                }
            });
        }
    }

    private void handleOnBackPress() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragmentPosition", 2);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        handleOnBackPress();
    }

    /**
     * Concat EditText array to string separated by ';'
     * @return concatString
     */
    private String concatEditTextsArray(ArrayList<EditText> editTextsArray) {
        String concatString = "";
        String ingredient;
        for (EditText editText:editTextsArray) {
            ingredient = (editText.getText() != null) ?  editText.getText().toString() : "";
            if(!TextUtils.isEmpty(ingredient)) {
                concatString += ingredient + ";";
            }
        }
        return concatString;
    }

    /*
     * Create pending intent for details activity which be opened from notification
     * @param recipeUri: relevant recipe uri to get recipe details
     * @return PendingIntent
     */
    public PendingIntent createPendingIntent(Uri recipeUri){
        //Add notification action
        Intent recipeDetailsIntent = new Intent(this, RecipeDetailsActivity.class);
        Cursor cursor = this.getContentResolver().query(recipeUri, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            recipeDetailsIntent.putExtra("selected_item", RecipesAdapter.setRecipeValues(cursor)); //Pass relevant recipe to RecipeDetailsActivity
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntentWithParentStack(recipeDetailsIntent);
        PendingIntent pi = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    /**
     * Send notification about new recipe
     */
    public void sendNotification(Uri recipeUri){
        Notification n = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.recipe_notification_title))
                .setContentText(recipeName)
                .setSmallIcon(R.mipmap.ic_app)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setAutoCancel(true)
                .setContentIntent(createPendingIntent(recipeUri))
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
        notificationManager.notify(1, n);
    }

    /**
     * Add editText to the layout param and editTextsArray param
     * @param hint
     * @param layout
     * @param editTextsArray
     */
    public EditText addDynamicEditText(String hint, LinearLayout layout, ArrayList<EditText> editTextsArray) {
        EditText edtView = new EditText(this);
        LayoutParams lParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        edtView.setHint(hint);
        edtView.setLayoutParams(lParams);
        edtView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        edtView.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.greenAppColor), PorterDuff.Mode.SRC_ATOP);
        layout.addView(edtView);
        editTextsArray.add(edtView);
        return edtView;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.add_recipe_btn: //Start Add new recipe activity
                if(isValidatedForm()) { //Validate inputs
                    if(selectedImage != null) {
                        UploadFile.uploadFile(this, selectedImage, this, "recipe"); //Upload recipe image to firebase
                    } else {
                        if (updateActivity) {
                            getSupportLoaderManager().initLoader(loaderID, null, updateRecipeLoaderListener).forceLoad();//Initializes the Insert Loader
                        } else {
                            getSupportLoaderManager().initLoader(updateLoaderID, null, insertRecipeLoaderListener).forceLoad();//Initializes the update Loader
                        }
                    }
                }
                break;
            case R.id.select_recipe_img_btn:
                selectImage(); //select image from camera or gallery
                break;
            case R.id.add_ingredient_img:
                try{
                    addDynamicEditText(getString(R.string.ingredient), addIngredientsLayout, ingredientsEditTextsArray);
                    break;
                }catch(Exception e){
                    Log.d(logTag, "Failed to create new ingredients edit text");
                }

            case R.id.add_instruction_img:
                try{
                    addDynamicEditText(getString(R.string.step), addInstructionsLayout, instructionsEditTextsArray);
                    break;
                }catch(Exception e){
                    Log.d(logTag, "Failed to create new instructions edit text");
                }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.recipes_categories_spinner:
                selectedRecipeCategory = parent.getItemAtPosition(position).toString();
                break;
            case R.id.difficulty_preparation_spinner:
                selectedDifficultyPreparation = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Create alert dialog with options to add image from gallery or camera
     */
    private void selectImage() {
        Uri selectedImage;
        final CharSequence[] options = { getResources().getString(R.string.take_photo_option),getResources().getString(R.string.gallery_option),getResources().getString(R.string.cancel_option)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppAlertDialogStyle);
        builder.setTitle(getString(R.string.add_recipe_image_title));
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getResources().getString(R.string.take_photo_option))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "recipe.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }

                else if (options[item].equals(getResources().getString(R.string.gallery_option))) {
                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }

                else if (options[item].equals(getResources().getString(R.string.cancel_option))) {
                    dialog.dismiss();
                }

            }

        });

        builder.show();

    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        selectedImage = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File recipe : f.listFiles()) {
                    if (recipe.getName().equals("recipe.jpg")) {
                        f = recipe;
                        break;
                    }
                }
                selectedImage = Uri.fromFile(f);

            } else if (requestCode == 2) {
                selectedImage = data.getData();
            }

            if(!TextUtils.isEmpty(selectedImage.toString())){
                Glide.with(this).load(selectedImage)
                        .dontAnimate()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.recipes)
                        .error(R.drawable.recipes)
                        .into(recipeImg);
            }

        }

    }



    /**
     * Validating form
     */
    private boolean isValidatedForm() {
        boolean isValid = false;
        boolean isRecipeCategorySelected = checkRequiredSpinnerItem(recipesCategoriesSpinner);
        boolean isRecipeNameFilled = validateRequiredText(recipeNameEditTxt); //validate recipe name
        boolean isIngredientsFilled = validateRequiredText(ingredientsEditTxt); //validate ingredients
        boolean isInstructionsFilled = validateRequiredText(instructionsEditTxt); //validate ingredients

        if(isRecipeNameFilled && isIngredientsFilled && isInstructionsFilled && isRecipeCategorySelected ) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Set view error message by view id and set view focus
     * @param view
     */
    private void setErrorMsg(EditText view){
        String errMessage = getString(R.string.required_err_msg_default);
        switch (view.getId()) {
            case R.id.recipe_name_edit_text:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg),getString(R.string.recipe_name)));
                break;
            case R.id.ingredients:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg),getString(R.string.ingredients)));
                break;
            case R.id.instructions:
                errMessage = (String.format(getResources().getString(R.string.required_err_msg),getString(R.string.instructions)));
                break;
        }
        view.setError(errMessage);
        requestFocus(view);
    }

    /**
     * Validate required text
     * @param editTextView
     * @return isValid
     */
    private boolean validateRequiredText(EditText editTextView) {
        if (TextUtils.isEmpty(editTextView.getText().toString().trim())) {
            setErrorMsg(editTextView);
            return false;
        }
        return true;
    }

    /**
     * Validate required spinner item text
     * @param spinner
     * @return isValid
     */
    private boolean checkRequiredSpinnerItem(Spinner spinner) {
        if(TextUtils.isEmpty(selectedRecipeCategory) || selectedRecipeCategory.equals(getString(R.string.select_recipe_category))) {
            TextView categoryItem = (TextView) recipesCategoriesSpinner.getSelectedView();
            String errMessage = (String.format(getResources().getString(R.string.required_err_msg), getString(R.string.recipes_category)));
            categoryItem.setError(errMessage);
            categoryItem.setText(errMessage);//changes the selected item error text
            requestFocus(categoryItem);
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onUrlReceived(Uri uri) {
        Bundle bundle= new Bundle();
        bundle.putString("downloadUrlPath",uri.toString());
        getSupportLoaderManager().initLoader(loaderID,bundle , insertRecipeLoaderListener).forceLoad();//Initializes the Insert Loader
    }

    private class RecipeTextWatcher implements TextWatcher {

        private View view;

        private RecipeTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.recipe_name_edit_text:
                    validateRequiredText((EditText)view);
                    break;
                case R.id.ingredients:
                    validateRequiredText((EditText)view);
                    break;
                case R.id.instructions:
                    validateRequiredText((EditText)view);
                    break;
            }
        }
    }
}
