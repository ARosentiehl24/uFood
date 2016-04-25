package com.arrg.android.app.ufood;

import com.afollestad.inquiry.annotations.Column;

public class Food {

    @Column(name = "_id", primaryKey = true, notNull = true, autoIncrement = true)
    private long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String type;

    @Column
    private String kindOfFood;

    @Column
    private long price;

    @Column
    private boolean inPromotion;

    public Food() {
    }

    public Food(String name, String description, String type, String kindOfFood, long price, boolean inPromotion) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.kindOfFood = kindOfFood;
        this.price = price;
        this.inPromotion = inPromotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKindOfFood() {
        return kindOfFood;
    }

    public void setKindOfFood(String kindOfFood) {
        this.kindOfFood = kindOfFood;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean isInPromotion() {
        return inPromotion;
    }

    public void setInPromotion(boolean inPromotion) {
        this.inPromotion = inPromotion;
    }
}
