package com.homechef.ProductService.model;

import java.util.Map;
import java.util.UUID;

public abstract class ProductFactory {

    //public static Product createProduct(String type, String name, UUID sellerId, Double price, int amountSold) {
//        switch (type.toLowerCase()) {
//            case "beverage":
//                return new Beverage.Builder()
//                        .setName(name)
//                        .setSellerId(sellerId)
//                        .setPrice(price)
//                        .setAmountSold(amountSold)
//                        .build();
//            case "food":
//                return new Food.Builder()
//                        .setName(name)
//                        .setSellerId(sellerId)
//                        .setPrice(price)
//                        .setAmountSold(amountSold)
//                        .build();
//            default:
//                throw new IllegalArgumentException("Unknown product type: " + type);
//        }


    //}
    public abstract Product createProduct(String name, UUID sellerId, Double price, int amountSold,String description,Double discount, Map<String, Object> request);

}
