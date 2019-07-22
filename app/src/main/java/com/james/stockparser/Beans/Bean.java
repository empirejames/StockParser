package com.james.stockparser.Beans;

/**
 * Created by 101716 on 2019/5/15.
 */

public class Bean {
    private String data;
    private Double order;//用于排序

    public Bean(String data, Double order) {
        super();
        this.data = data;
        this.order = order;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public Double getOrder() {
        return order;
    }
}
