package com.example.androidmodel.tools.dexfix.simple.bean;

public class Methods{
    private int method_idx_diff;
    private int access_flags;
    private int code_off;//也是code_item_start;
    private CodeItem codeItem;

    private int method_index;
    private int method_start;
    private int method_length;
    private int code_off_start;
    private int code_item_length;




    public int getMethod_idx_diff() {
        return method_idx_diff;
    }

    public void setMethod_idx_diff(int method_idx_diff) {
        this.method_idx_diff = method_idx_diff;
    }

    public int getAccess_flags() {
        return access_flags;
    }

    public void setAccess_flags(int access_flags) {
        this.access_flags = access_flags;
    }

    public int getCode_off() {
        return code_off;
    }

    public void setCode_off(int code_off) {
        this.code_off = code_off;
    }

    public CodeItem getCodeItem() {
        return codeItem;
    }

    public void setCodeItem(CodeItem codeItem) {
        this.codeItem = codeItem;
    }

    public int getMethod_index() {
        return method_index;
    }

    public void setMethod_index(int method_index) {
        this.method_index = method_index;
    }

    public int getMethod_start() {
        return method_start;
    }

    public void setMethod_start(int method_start) {
        this.method_start = method_start;
    }

    public int getMethod_length() {
        return method_length;
    }

    public void setMethod_length(int method_length) {
        this.method_length = method_length;
    }

    public int getCode_off_start() {
        return code_off_start;
    }

    public void setCode_off_start(int code_off_start) {
        this.code_off_start = code_off_start;
    }
}