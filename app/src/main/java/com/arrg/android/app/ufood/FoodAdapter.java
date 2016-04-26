package com.arrg.android.app.ufood;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.inquiry.Inquiry;
import com.afollestad.materialdialogs.AlertDialogWrapper;

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
        holder.tvTypeOfFood.setText(String.format("%s: %s", context.getString(R.string.type_of_food), food.getType()));
        holder.tvKindOfFood.setText(String.format("%s: %s", context.getString(R.string.kind_of_food), food.getKindOfFood()));
        holder.tvDescription.setText(String.format("%s: %s", context.getString(R.string.description), food.getDescription()));

        String price = NumberFormat.getCurrencyInstance(new Locale("es", "CO")).format(Long.parseLong(food.getPrice()));

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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        @Bind(R.id.onSaleBackground)
        TextView onSaleBackground;

        @Bind(R.id.tvFoodName)
        TextView tvFoodName;

        @Bind(R.id.tvTypeOfFood)
        TextView tvTypeOfFood;

        @Bind(R.id.tvKindOfFood)
        TextView tvKindOfFood;

        @Bind(R.id.tvDescription)
        TextView tvDescription;

        @Bind(R.id.tvPrice)
        TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            new AlertDialogWrapper.Builder(context)
                    .setTitle(R.string.delete)
                    .setMessage("Are you sure you want to delete this item?")
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Food food = foodArrayList.get(getLayoutPosition());

                            deleteFood(getLayoutPosition());

                            Inquiry.get().deleteFrom(Constants.FOOD_TABLE, Food.class).where("name = ?", food.getName()).run();

                            dialog.dismiss();
                        }
                    }).show();
            return false;
        }
    }
}
