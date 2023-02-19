package com.example.rento;

public class Model {
    String Product_Name, Product_Price, Product_ImgUrl, Categories, Product_Descreiption, Product_brocrage, UID,id,Uid,ProId,Itemid;

    public Model(String proId) {
        ProId = proId;
    }

    public String getProId() {
        return ProId;
    }

    public void setProId(String proId) {
        ProId = proId;
    }

    public Model() {
    }

    public Model(String product_Name, String product_Price, String product_ImgUrl, String categories, String product_Descreiption, String product_brocrage, String UID) {
        Product_Name = product_Name;
        Product_Price = product_Price;
        Product_ImgUrl = product_ImgUrl;
        Categories = categories;
        Product_Descreiption = product_Descreiption;
        Product_brocrage = product_brocrage;
        this.UID = UID;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getProduct_Price() {
        return Product_Price;
    }

    public void setProduct_Price(String product_Price) {
        Product_Price = product_Price;
    }

    public String getProduct_ImgUrl() {
        return Product_ImgUrl;
    }

    public void setProduct_ImgUrl(String product_ImgUrl) {
        Product_ImgUrl = product_ImgUrl;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public String getProduct_Descreiption() {
        return Product_Descreiption;
    }

    public void setProduct_Descreiption(String product_Descreiption) {
        Product_Descreiption = product_Descreiption;
    }

    public String getProduct_brocrage() {
        return Product_brocrage;
    }

    public void setProduct_brocrage(String product_brocrage) {
        Product_brocrage = product_brocrage;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
