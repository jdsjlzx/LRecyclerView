package com.lzx.demo.bean;

import java.io.Serializable;

/**
 * Created by 杜坤鹏 on 2019/5/27.
 */

public class HolderOrientedModel implements Serializable {
    private int id;
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
