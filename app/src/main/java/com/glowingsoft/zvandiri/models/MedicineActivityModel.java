package com.glowingsoft.zvandiri.models;

/**
 * Created by Asif on 2/8/2019.
 */

public class MedicineActivityModel {

    String id, title;
    boolean is_joined;

    public MedicineActivityModel(String id, String title, boolean is_joined) {
        this.is_joined = is_joined;
        this.title = title;
        this.id = id;
    }

    public boolean isIs_joined() {
        return is_joined;
    }

    public String getId() {
        return id;
    }

    public String getMedicineTitle() {
        return title;
    }

}
