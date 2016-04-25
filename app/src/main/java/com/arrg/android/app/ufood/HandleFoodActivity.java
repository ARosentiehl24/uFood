package com.arrg.android.app.ufood;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.inquiry.Inquiry;

import java.text.NumberFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HandleFoodActivity extends AppCompatActivity {

    @Bind(R.id.etName)
    TextInputEditText etName;

    @Bind(R.id.etDescription)
    TextInputEditText etDescription;

    @Bind(R.id.spinnerTypeOfFood)
    AppCompatSpinner spinnerTypeOfFood;

    @Bind(R.id.spinnerKindOfFood)
    AppCompatSpinner spinnerKindOfFood;

    @Bind(R.id.etPrice)
    TextInputEditText etPrice;

    @Bind(R.id.cbOnSale)
    AppCompatCheckBox cbOnSale;

    @Bind(R.id.bAddFood)
    AppCompatButton bAddFood;

    @Bind(R.id.bUpdateFood)
    AppCompatButton bUpdateFood;

    @OnClick({R.id.bAddFood, R.id.bUpdateFood})
    public void OnClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.bAddFood:
                if (!haveText(etName) || !haveText(etDescription) || !haveText(etPrice)) {
                    toast(getString(R.string.there_are_invalid_fields_message));
                } else {
                    Inquiry.get().insertInto(Constants.FOOD_TABLE, Food.class).values(new Food(getTextFrom(etName), getTextFrom(etDescription), getTextFrom(spinnerTypeOfFood), getTextFrom(spinnerKindOfFood), getTextFrom(etPrice), cbOnSale.isChecked())).run();
                    onBackPressed();
                }
                break;
            case R.id.bUpdateFood:
                if (!haveText(etName) || !haveText(etDescription) || !haveText(etPrice)) {
                    toast(getString(R.string.there_are_invalid_fields_message));
                } else {
                    Food food = new Food(getTextFrom(etName), getTextFrom(etDescription), getTextFrom(spinnerTypeOfFood), getTextFrom(spinnerKindOfFood), getTextFrom(etPrice), cbOnSale.isChecked());

                    Inquiry.get().update(Constants.FOOD_TABLE, Food.class).values(food).where("name = ?", getTextFrom(etName)).run();
                    onBackPressed();
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        Boolean isInEditMode = intent.getBooleanExtra(Constants.EXTRA_IS_IN_EDIT_MODE, false);

        if (isInEditMode) {
            Food food = (Food) intent.getSerializableExtra(Constants.FOOD_TABLE);

            etName.setText(food.getName());
            etDescription.setText(food.getDescription());
            spinnerKindOfFood.setSelection(getIndexFor(food.getKindOfFood(), R.array.kindOfFood));
            spinnerTypeOfFood.setSelection(getIndexFor(food.getType(), R.array.typeOfFood));

            String price = NumberFormat.getCurrencyInstance(new Locale("es", "CO")).format(Long.parseLong(food.getPrice()));
            etPrice.setText(String.format("%s COP", price));

            cbOnSale.setChecked(food.isInPromotion());

            bAddFood.setVisibility(View.INVISIBLE);
            bUpdateFood.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Inquiry.init(this, Constants.DATA_BASE_NAME, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Inquiry.deinit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean haveText(TextInputEditText textInputEditText) {
        return textInputEditText.getText().length() != 0;
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public String getTextFrom(TextInputEditText textInputEditText) {
        return textInputEditText.getText().toString();
    }

    public String getTextFrom(AppCompatSpinner appCompatSpinner) {
        return appCompatSpinner.getSelectedItem().toString();
    }

    public int getIndexFor(String item, int resId) {
        int i = 0;

        String[] androidStrings = getResources().getStringArray(resId);

        for (String string : androidStrings) {
            if (item.equals(string)) {
                break;
            }

            i++;
        }

        return i;
    }
}
