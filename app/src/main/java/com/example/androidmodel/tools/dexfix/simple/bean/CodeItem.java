package com.example.androidmodel.tools.dexfix.simple.bean;

public class CodeItem{
    private int register_size;
    private int ins_size;
    private int outs_size;
    private int tries_size;
    private int debug_info_off;
    private int instructions_size;
    private short[] instructions;

    private int debug_info_off_start;
    private int code_item_start;
    private int code_item_length;

    public int getRegister_size() {
        return register_size;
    }

    public void setRegister_size(int register_size) {
        this.register_size = register_size;
    }

    public int getIns_size() {
        return ins_size;
    }

    public void setIns_size(int ins_size) {
        this.ins_size = ins_size;
    }

    public int getOuts_size() {
        return outs_size;
    }

    public void setOuts_size(int outs_size) {
        this.outs_size = outs_size;
    }

    public int getTries_size() {
        return tries_size;
    }

    public void setTries_size(int tries_size) {
        this.tries_size = tries_size;
    }

    public int getDebug_info_off() {
        return debug_info_off;
    }

    public void setDebug_info_off(int debug_info_off) {
        this.debug_info_off = debug_info_off;
    }

    public int getInstructions_size() {
        return instructions_size;
    }

    public void setInstructions_size(int instructions_size) {
        this.instructions_size = instructions_size;
    }

    public short[] getInstructions() {
        return instructions;
    }

    public void setInstructions(short[] instructions) {
        this.instructions = instructions;
    }

    public int getDebug_info_off_start() {
        return debug_info_off_start;
    }

    public void setDebug_info_off_start(int debug_info_off_start) {
        this.debug_info_off_start = debug_info_off_start;
    }

    public int getCode_item_start() {
        return code_item_start;
    }

    public void setCode_item_start(int code_item_start) {
        this.code_item_start = code_item_start;
    }

    public int getCode_item_length() {
        return code_item_length;
    }

    public void setCode_item_length(int code_item_length) {
        this.code_item_length = code_item_length;
    }
}