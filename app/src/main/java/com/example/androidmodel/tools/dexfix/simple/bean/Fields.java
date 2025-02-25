package com.example.androidmodel.tools.dexfix.simple.bean;

public class Fields{
    private int field_idx_diff;
    private int access_flags;
    private int field_index;

    private int field_start;
    private int field_length;

    public int getField_idx_diff() {
        return field_idx_diff;
    }

    public void setField_idx_diff(int field_idx_diff) {
        this.field_idx_diff = field_idx_diff;
    }

    public int getAccess_flags() {
        return access_flags;
    }

    public void setAccess_flags(int access_flags) {
        this.access_flags = access_flags;
    }

    public int getField_index() {
        return field_index;
    }

    public void setField_index(int field_index) {
        this.field_index = field_index;
    }

    public int getField_start() {
        return field_start;
    }

    public void setField_start(int field_start) {
        this.field_start = field_start;
    }

    public int getField_length() {
        return field_length;
    }

    public void setField_length(int field_length) {
        this.field_length = field_length;
    }
}