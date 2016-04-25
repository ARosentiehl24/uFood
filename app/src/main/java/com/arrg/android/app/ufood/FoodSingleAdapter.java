package com.arrg.android.app.ufood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tvFoodName)
        TextView tvFoodName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Food food = foodArrayList.get(getLayoutPosition());

            Intent intent = new Intent(context, HandleFoodActivity.class);
            intent.putExtra(Constants.EXTRA_IS_IN_EDIT_MODE, true);
            intent.putExtra(Constants.FOOD_TABLE, food);
            context.startActivity(intent);
            context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}
