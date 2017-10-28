package com.ecnu.leon.jarvis.model.account;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LeonDu on 28/10/2017.
 */

public class AccountItem implements Serializable {

    private static final long serialVersionUID = 6386186293823567672L;

    private long id;
    private float cost;
    private String remark = "";

    private long ts;


    public AccountItem(long id, float cost) {
        this.id = id;
        this.cost = cost;

        this.ts = System.currentTimeMillis();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public long getId() {
        return id;
    }

    public long getTs() {
        return ts;
    }
}
