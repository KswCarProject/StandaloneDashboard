package com.android.kswxdashboard.witsparser;

import com.google.gson.Gson;

public class WitsStatus {
    public String jsonArg;
    public int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public String getJsonArg() {
        return this.jsonArg;
    }

    public void setJsonArg(String jsonArg2) {
        this.jsonArg = jsonArg2;
    }

    public static WitsStatus getWitsStatusFormJson(String jsonArg2) {
        return (WitsStatus) new Gson().fromJson(jsonArg2, WitsStatus.class);
    }

    public WitsStatus(int type2, String jsonArg2) {
        this.type = type2;
        this.jsonArg = jsonArg2;
    }
}