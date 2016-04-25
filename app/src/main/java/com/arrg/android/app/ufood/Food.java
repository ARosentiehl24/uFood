package com.arrg.android.app.ufood;

import com.afollestad.inquiry.annotations.Column;

import java.io.Serializable;

public class Food implements Serializable {

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
    private String price;

    @Column
    private boolean inPromotion;

    public Food() {
    }

    public Food(String name, String description, String type, String kindOfFood, String price, boolean inPromotion) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.kindOfFood = kindOfFood;
        this.price = price;
        this.inPromotion = inPromotion;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isInPromotion() {
        return inPromotion;
    }

    public void setInPromotion(boolean inPromotion) {
        this.inPromotion = inPromotion;
    }
}
