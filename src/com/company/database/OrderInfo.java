package com.company.database;

import com.company.database.generic.DatabaseEntity;

import java.util.Calendar;

public class OrderInfo implements DatabaseEntity {
    private String name;
    private String producer;
    private String measureUnit;
    private Calendar acquireDate;
    private Integer count;
    private Float priceForOne;

    public OrderInfo(String name, String producer, String measureUnit, Calendar acquireDate, Integer count, Float priceForOne) {
        this.name = name;
        this.producer = producer;
        this.measureUnit = measureUnit;
        this.acquireDate = acquireDate;
        this.count = count;
        this.priceForOne = priceForOne;
    }

    public String getName() {
        return name;
    }

    public String getProducer() {
        return producer;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public Calendar getAcquireDate() {
        return acquireDate;
    }

    public Integer getCount() {
        return count;
    }

    public Float getPriceForOne() {
        return priceForOne;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public void setAcquireDate(Calendar acquireDate) {
        this.acquireDate = acquireDate;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setPriceForOne(Float priceForOne) {
        this.priceForOne = priceForOne;
    }

    @Override
    public String toString() {

        return String.format(
                "Order Info:%n" +
                        "\tproduct name: '%s'%n" +
                        "\tproducer: '%s'%n" +
                        "\tmeasure unit: '%s'%n" +
                        "\tacquire date: %tF %tT %n" +
                        "\tcount: %d%n" +
                        "\tprice for one: %.2f%n",
                this.name,
                this.producer,
                this.measureUnit,
                this.acquireDate, this.acquireDate,
                this.count,
                this.priceForOne
        );
    }
}