package com.demo.view;

/**
 */
public class MainModel implements MainContract.IMainModel {


    @Override
    public String CreateFragment(String name) {
        return name;
    }
}
