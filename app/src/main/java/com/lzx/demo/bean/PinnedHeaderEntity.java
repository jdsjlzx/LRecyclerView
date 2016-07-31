package com.lzx.demo.bean;


import com.lzx.demo.base.Entity;

/**
 * <p>
 * 大标签实体类，可以将自己想要填充的数据包装进去，同时附带这个数据对应的类型
 */
public class PinnedHeaderEntity<T> extends Entity {

    private T data;
    private String pinnedHeaderName;

    public PinnedHeaderEntity(T data, int type, String pinnedHeaderName) {
        this.data = data;
        super.type = type;
        this.pinnedHeaderName = pinnedHeaderName;
    }

    public void setData(T data) {
        this.data = data;
    }


    public void setPinnedHeaderName(String pinnedHeaderName) {
        this.pinnedHeaderName = pinnedHeaderName;
    }

    public T getData() {
        return data;
    }

    public String getPinnedHeaderName() {
        return pinnedHeaderName;
    }
}
