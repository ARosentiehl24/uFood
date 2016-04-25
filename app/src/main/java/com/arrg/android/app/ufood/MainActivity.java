package com.arrg.android.app.ufood;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.inquiry.Inquiry;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Food> foodArrayList;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @OnClick({R.id.fabAddFood})
    public void OnClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.fabAddFood:
                startActivity(new Intent(this, AddFoodActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Inquiry.init(this, Constants.DATA_BASE_NAME, 1);

        Food[] foods = Inquiry.get().selectFrom(Constants.FOOD_TABLE, Food.class).sort("name ASC").all();

        foodArrayList = new ArrayList<>();

        if (foods != null) {
            Collections.addAll(foodArrayList, foods);

            FoodAdapter foodAdapter = new FoodAdapter(this, foodArrayList);

            recyclerView.setAdapter(foodAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem actionSearch = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(actionSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();

                ArrayList<Food> filteredFoods = new ArrayList<>();

                for (Food food : foodArrayList) {
                    if (food.getName().toLowerCase().contains(newText)) {
                        filteredFoods.add(food);
                    }
                }

                FoodSingleAdapter foodSingleAdapter = new FoodSingleAdapter(MainActivity.this, filteredFoods);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                recyclerView.setAdapter(foodSingleAdapter);

                foodSingleAdapter.notifyDataSetChanged();

                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_see_all:
                Food[] foods = Inquiry.get().selectFrom(Constants.FOOD_TABLE, Food.class).sort("name ASC").all();

                Collections.addAll(foodArrayList, foods);

                FoodAdapter foodAdapter = new FoodAdapter(this, foodArrayList);

                recyclerView.setAdapter(foodAdapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                foodAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_report:
                return true;
            case R.id.action_about:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
