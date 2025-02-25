package com.example.androidmodel.tools.dexfix.simple.bean;

/**
 * map_item_list 中 相关数据
 */
public class MapItem {
    private short type;
    private short unused;
    private int size;
    private int offset;
    private int this_type_start;
    private int this_type_offset_start;
    private int index_of_map_item_list;
    // 修复 map_item_list中的 offset 和 对应位置的offset
    //只要在code_item后的item,都暂时考虑需要修复 bin.ins.length与实际code_item.length的差值总长度
    //0不需要;1是code_item;2需要;
    private int need_fix_offset;
    public MapItem() {

    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public short getUnused() {
        return unused;
    }

    public void setUnused(short unused) {
        this.unused = unused;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getThis_type_start() {
        return this_type_start;
    }

    public void setThis_type_start(int this_type_start) {
        this.this_type_start = this_type_start;
    }

    public int getThis_type_offset_start() {
        return this_type_offset_start;
    }

    public void setThis_type_offset_start(int this_type_offset_start) {
        this.this_type_offset_start = this_type_offset_start;
    }

    public int getIndex_of_map_item_list() {
        return index_of_map_item_list;
    }

    public void setIndex_of_map_item_list(int index_of_map_item_list) {
        this.index_of_map_item_list = index_of_map_item_list;
    }

    public int getNeed_fix_offset() {
        return need_fix_offset;
    }

    public void setNeed_fix_offset(int need_fix_offset) {
        this.need_fix_offset = need_fix_offset;
    }
}
