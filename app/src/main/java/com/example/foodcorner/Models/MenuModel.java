package com.example.foodcorner.Models;

public class MenuModel {
    String ItemName,ItemDescription,ItemImage,ItemPrice,MenuId;

    public MenuModel(String itemName, String itemDescription, String itemImage, String itemPrice) {
        ItemName = itemName;
        ItemDescription = itemDescription;
        ItemImage = itemImage;
        ItemPrice = itemPrice;
    }

    public MenuModel() {
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
