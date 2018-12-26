package com.example.administrator.app44drink;

import java.io.Serializable;

public class MenuInfo implements Serializable {
    String name;
    int price;
    public MenuInfo(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
