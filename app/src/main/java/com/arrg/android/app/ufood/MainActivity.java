package com.arrg.android.app.ufood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.afollestad.inquiry.Inquiry;
import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.afollestad.materialdialogs.MaterialDialog;
import com.easyandroidanimations.library.FadeInAnimation;
import com.easyandroidanimations.library.FadeOutAnimation;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fabAddFood)
    FloatingActionButton fabAddFood;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @OnClick({R.id.fabAddFood})
    public void OnClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.fabAddFood:
                startActivity(new Intent(this, HandleFoodActivity.class));
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

        FoodAdapter foodAdapter = new FoodAdapter(this, loadFoods());

        recyclerView.setAdapter(foodAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    fabAddFood.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabAddFood.isShown()) {
                    fabAddFood.hide();
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        foodAdapter.notifyDataSetChanged();
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
                if (newText.length() > 0) {
                    new FadeOutAnimation(findViewById(R.id.fabAddFood)).animate();

                    newText = newText.toLowerCase();

                    ArrayList<Food> filteredFoods = new ArrayList<>();

                    for (Food food : loadFoods()) {
                        if (food.getName().toLowerCase().contains(newText)) {
                            filteredFoods.add(food);
                        }
                    }

                    FoodSingleAdapter foodSingleAdapter = new FoodSingleAdapter(MainActivity.this, filteredFoods);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(foodSingleAdapter);

                    foodSingleAdapter.notifyDataSetChanged();
                } else {
                    new FadeInAnimation(findViewById(R.id.fabAddFood)).animate();

                    addFoodsToRecycler(loadFoods());
                }
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
                addFoodsToRecycler(loadFoods());
                return true;
            case R.id.action_on_sale:
                Food[] foods = Inquiry.get().selectFrom(Constants.FOOD_TABLE, Food.class).where("inPromotion = ?", 1).sort("name ASC").all();

                if (foods != null) {
                    ArrayList<Food> filteredFoods = new ArrayList<>();

                    Collections.addAll(filteredFoods, foods);

                    addFoodsToRecycler(filteredFoods);
                }
                return true;
            case R.id.action_kind_of_food:
                new MaterialDialog.Builder(this)
                        .items(R.array.kindOfFood)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                Food[] foods = Inquiry.get().selectFrom(Constants.FOOD_TABLE, Food.class).where("kindOfFood = ?", text.toString()).sort("name ASC").all();

                                if (foods != null) {
                                    ArrayList<Food> filteredFoods = new ArrayList<>();

                                    Collections.addAll(filteredFoods, foods);

                                    addFoodsToRecycler(filteredFoods);
                                } else {
                                    ArrayList<Food> filteredFoods = new ArrayList<>();

                                    addFoodsToRecycler(filteredFoods);
                                }
                            }
                        })
                        .show();
                return true;
            case R.id.action_about:
                new AlertDialogWrapper.Builder(this)
                        .setTitle(R.string.action_about)
                        .setMessage(getString(R.string.app_name) + " was developed by:\n\n" +
                                " » Alberto Rosentiehl - 2012114114\n" +
                                " » Cristian Sarmiento - \n\n" +
                                " Programming for Android phones\n\n" +
                                " App Version: v1.0")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
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

    public ArrayList<Food> loadFoods() {
        ArrayList<Food> foodArrayList = new ArrayList<>();

        Food[] foods = Inquiry.get().selectFrom(Constants.FOOD_TABLE, Food.class).sort("name ASC").all();

        if (foods != null) {
            Collections.addAll(foodArrayList, foods);
        }

        return foodArrayList;
    }

    public void addFoodsToRecycler(ArrayList<Food> foodArrayList) {
        FoodAdapter foodAdapter = new FoodAdapter(this, foodArrayList);

        if (foodAdapter.getItemCount() > 0) {
            recyclerView.setAdapter(foodAdapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            foodAdapter.notifyDataSetChanged();
        }
    }
}
