package com.arrg.android.app.ufood;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.inquiry.Inquiry;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFoodActivity extends AppCompatActivity {

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

    @OnClick({R.id.bAddFood})
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
}
