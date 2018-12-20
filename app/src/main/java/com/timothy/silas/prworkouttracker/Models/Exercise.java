package com.timothy.silas.prworkouttracker.Models;

import java.text.DecimalFormat;
import java.util.UUID;

public class Exercise {

    private UUID id;
    private String name;
    private double weight;
    private WtUnit wtUnit;

    public Exercise(UUID id, String name, double weight, WtUnit wtUnit) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.wtUnit = wtUnit;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public String getWeightFormatted() {
        DecimalFormat df = new DecimalFormat("#.0");
        String weightFormatted = df.format(weight);
        return weightFormatted;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public WtUnit getWtUnit() {
        return wtUnit;
    }

    public void setWtUnit(WtUnit wtUnit) {
        this.wtUnit = wtUnit;
    }

    public void addWeight(int amount) {
        this.weight += amount;
    }
}
