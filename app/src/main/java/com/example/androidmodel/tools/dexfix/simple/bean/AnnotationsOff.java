package com.example.androidmodel.tools.dexfix.simple.bean;

public class AnnotationsOff {
    private int class_annotation_off;
    private int fields_size;
    private int methods_size;
    private int parameters_size;
    private int annotations_off_length;

    public AnnotationsOff() {

    }

    public int getClass_annotation_off() {
        return class_annotation_off;
    }

    public void setClass_annotation_off(int class_annotation_off) {
        this.class_annotation_off = class_annotation_off;
    }

    public int getFields_size() {
        return fields_size;
    }

    public void setFields_size(int fields_size) {
        this.fields_size = fields_size;
    }

    public int getMethods_size() {
        return methods_size;
    }

    public void setMethods_size(int methods_size) {
        this.methods_size = methods_size;
    }

    public int getParameters_size() {
        return parameters_size;
    }

    public void setParameters_size(int parameters_size) {
        this.parameters_size = parameters_size;
    }

    public int getAnnotations_off_length() {
        return annotations_off_length;
    }

    public void setAnnotations_off_length(int annotations_off_length) {
        this.annotations_off_length = annotations_off_length;
    }
}
