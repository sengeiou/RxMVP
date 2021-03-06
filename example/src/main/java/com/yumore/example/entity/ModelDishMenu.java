package com.yumore.example.entity;

import java.util.ArrayList;

/**
 * @author yumore
 * @date 16-11-10
 */
public class ModelDishMenu {
    private String menuName;
    private ArrayList<ModelDish> mModelDishList;

    public ModelDishMenu() {

    }

    public ModelDishMenu(String menuName, ArrayList dishList) {
        this.menuName = menuName;
        this.mModelDishList = dishList;
    }

    public ArrayList<ModelDish> getModelDishList() {
        return mModelDishList;
    }

    public void setModelDishList(ArrayList<ModelDish> modelDishList) {
        this.mModelDishList = modelDishList;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
