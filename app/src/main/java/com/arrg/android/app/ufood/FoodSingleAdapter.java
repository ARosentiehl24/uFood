package com.arrg.android.app.ufood;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FoodSingleAdapter extends RecyclerView.Adapter<FoodSingleAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<Food> foodArrayList;

    public FoodSingleAdapter(Activity context, ArrayList<Food> foodArrayList) {
        this.context = context;
        this.foodArrayList = foodArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View foods = layoutInflater.inflate(R.layout.food_list_simple_item_row, parent, false);

        return new ViewHolder(foods);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Food food = foodArrayList.get(position);

        holder.tvFoodName.setText(food.getName());
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvFoodName)
        TextView tvFoodName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
