package cn.sunibas.entity;

import java.io.Serializable;

/**
 * Created by IBAS on 2017/2/15.
 */
public class TSLanguage implements Serializable {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
