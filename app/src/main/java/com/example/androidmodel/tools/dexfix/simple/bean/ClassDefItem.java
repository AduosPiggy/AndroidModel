package com.example.androidmodel.tools.dexfix.simple.bean;

public class ClassDefItem {
    private int class_idx;
    private int access_flags;
    private int superclass_idx;
    private int interfaces_off;
    private int source_file_idx;
    private int annotations_off;
    private int class_data_off;
    private int static_values_off;
    private ClassDataItem classDataItem;

    private int class_def_item_start;
    private int interfaces_off_start;
    private int annotations_off_start;
    private int class_data_off_start;
    private int static_values_off_start;


    public ClassDefItem() {
    }

    public int getClass_idx() {
        return class_idx;
    }

    public void setClass_idx(int class_idx) {
        this.class_idx = class_idx;
    }

    public int getAccess_flags() {
        return access_flags;
    }

    public void setAccess_flags(int access_flags) {
        this.access_flags = access_flags;
    }

    public int getSuperclass_idx() {
        return superclass_idx;
    }

    public void setSuperclass_idx(int superclass_idx) {
        this.superclass_idx = superclass_idx;
    }

    public int getInterfaces_off() {
        return interfaces_off;
    }

    public void setInterfaces_off(int interfaces_off) {
        this.interfaces_off = interfaces_off;
    }

    public int getSource_file_idx() {
        return source_file_idx;
    }

    public void setSource_file_idx(int source_file_idx) {
        this.source_file_idx = source_file_idx;
    }

    public int getAnnotations_off() {
        return annotations_off;
    }

    public void setAnnotations_off(int annotations_off) {
        this.annotations_off = annotations_off;
    }

    public int getClass_data_off() {
        return class_data_off;
    }

    public void setClass_data_off(int class_data_off) {
        this.class_data_off = class_data_off;
    }

    public int getStatic_values_off() {
        return static_values_off;
    }

    public void setStatic_values_off(int static_values_off) {
        this.static_values_off = static_values_off;
    }

    public int getClass_def_item_start() {
        return class_def_item_start;
    }

    public void setClass_def_item_start(int class_def_item_start) {
        this.class_def_item_start = class_def_item_start;
    }

    public int getInterfaces_off_start() {
        return interfaces_off_start;
    }

    public void setInterfaces_off_start(int interfaces_off_start) {
        this.interfaces_off_start = interfaces_off_start;
    }

    public int getAnnotations_off_start() {
        return annotations_off_start;
    }

    public void setAnnotations_off_start(int annotations_off_start) {
        this.annotations_off_start = annotations_off_start;
    }

    public int getClass_data_off_start() {
        return class_data_off_start;
    }

    public void setClass_data_off_start(int class_data_off_start) {
        this.class_data_off_start = class_data_off_start;
    }

    public int getStatic_values_off_start() {
        return static_values_off_start;
    }

    public void setStatic_values_off_start(int static_values_off_start) {
        this.static_values_off_start = static_values_off_start;
    }

    public ClassDataItem getClassDataItem() {
        return classDataItem;
    }

    public void setClassDataItem(ClassDataItem classDataItem) {
        this.classDataItem = classDataItem;
    }
}
