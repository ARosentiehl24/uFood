package com.arrg.android.app.ufood;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Activity context;
    private ArrayList<Food> foodArrayList;

    public FoodAdapter(Activity context, ArrayList<Food> foodArrayList) {
        this.context = context;
        this.foodArrayList = foodArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View foods = layoutInflater.inflate(R.layout.food_list_item_row, parent, false);

        return new ViewHolder(foods);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Food food = foodArrayList.get(position);

        holder.onSaleBackground.setVisibility(food.isInPromotion() ? View.VISIBLE : View.INVISIBLE);
        holder.tvFoodName.setText(food.getName());
        holder.tvDescription.setText(food.getDescription());

        String price = NumberFormat.getCurrencyInstance(new Locale("es", "CO")).format(food.getPrice());

        holder.tvPrice.setText(String.format("%s COP", price));
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public void addFood(Food food, int position) {
        foodArrayList.add(food);
        notifyItemInserted(position);
    }

    public void deleteFood(int position) {
        foodArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.onSaleBackground)
        CardView onSaleBackground;

        @Bind(R.id.tvFoodName)
        TextView tvFoodName;

        @Bind(R.id.tvDescription)
        TextView tvDescription;

        @Bind(R.id.tvPrice)
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
