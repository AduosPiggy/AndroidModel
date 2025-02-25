package com.example.androidmodel.tools.dexfix.simple.bean;

import java.util.List;

public class ClassDataItem {
    private int staticFieldsSize;
    private int instanceFieldsSize;
    private int directMethodsSize;
    private int virtualMethodsSize;
    private List<Fields> staticFieldsList;
    private List<Fields> instanceFieldsList;
    private List<Methods> directMethodList;
    private List<Methods> virtualMethodList;

    //读取完4个size后的start;
    private int staticFieldsSizeStart;
    private int instanceFieldsSizeStart;
    private int directMethodsSizeStart;
    private int virtualMethodsSizeStart;
    //用来修复,直接将一整段长度全部写入;
    private int staticFieldsSizeLength;
    private int instanceFieldsSizeLength;
    //所有code_item的总长度;
    private int directMethodsSizeLength;
    private int virtualMethodsSizeLength;



    public int getStaticFieldsSize() {
        return staticFieldsSize;
    }

    public void setStaticFieldsSize(int staticFieldsSize) {
        this.staticFieldsSize = staticFieldsSize;
    }

    public int getInstanceFieldsSize() {
        return instanceFieldsSize;
    }

    public void setInstanceFieldsSize(int instanceFieldsSize) {
        this.instanceFieldsSize = instanceFieldsSize;
    }

    public int getDirectMethodsSize() {
        return directMethodsSize;
    }

    public void setDirectMethodsSize(int directMethodsSize) {
        this.directMethodsSize = directMethodsSize;
    }

    public int getVirtualMethodsSize() {
        return virtualMethodsSize;
    }

    public void setVirtualMethodsSize(int virtualMethodsSize) {
        this.virtualMethodsSize = virtualMethodsSize;
    }

    public List<Fields> getStaticFieldsList() {
        return staticFieldsList;
    }

    public void setStaticFieldsList(List<Fields> staticFieldsList) {
        this.staticFieldsList = staticFieldsList;
    }

    public List<Fields> getInstanceFieldsList() {
        return instanceFieldsList;
    }

    public void setInstanceFieldsList(List<Fields> instanceFieldsList) {
        this.instanceFieldsList = instanceFieldsList;
    }

    public List<Methods> getDirectMethodList() {
        return directMethodList;
    }

    public void setDirectMethodList(List<Methods> directMethodList) {
        this.directMethodList = directMethodList;
    }

    public List<Methods> getVirtualMethodList() {
        return virtualMethodList;
    }

    public void setVirtualMethodList(List<Methods> virtualMethodList) {
        this.virtualMethodList = virtualMethodList;
    }

    public int getStaticFieldsSizeStart() {
        return staticFieldsSizeStart;
    }

    public void setStaticFieldsSizeStart(int staticFieldsSizeStart) {
        this.staticFieldsSizeStart = staticFieldsSizeStart;
    }

    public int getInstanceFieldsSizeStart() {
        return instanceFieldsSizeStart;
    }

    public void setInstanceFieldsSizeStart(int instanceFieldsSizeStart) {
        this.instanceFieldsSizeStart = instanceFieldsSizeStart;
    }

    public int getDirectMethodsSizeStart() {
        return directMethodsSizeStart;
    }

    public void setDirectMethodsSizeStart(int directMethodsSizeStart) {
        this.directMethodsSizeStart = directMethodsSizeStart;
    }

    public int getVirtualMethodsSizeStart() {
        return virtualMethodsSizeStart;
    }

    public void setVirtualMethodsSizeStart(int virtualMethodsSizeStart) {
        this.virtualMethodsSizeStart = virtualMethodsSizeStart;
    }

    public int getStaticFieldsSizeLength() {
        return staticFieldsSizeLength;
    }

    public void setStaticFieldsSizeLength(int staticFieldsSizeLength) {
        this.staticFieldsSizeLength = staticFieldsSizeLength;
    }

    public int getInstanceFieldsSizeLength() {
        return instanceFieldsSizeLength;
    }

    public void setInstanceFieldsSizeLength(int instanceFieldsSizeLength) {
        this.instanceFieldsSizeLength = instanceFieldsSizeLength;
    }

    public int getDirectMethodsSizeLength() {
        return directMethodsSizeLength;
    }

    public void setDirectMethodsSizeLength(int directMethodsSizeLength) {
        this.directMethodsSizeLength = directMethodsSizeLength;
    }

    public int getVirtualMethodsSizeLength() {
        return virtualMethodsSizeLength;
    }

    public void setVirtualMethodsSizeLength(int virtualMethodsSizeLength) {
        this.virtualMethodsSizeLength = virtualMethodsSizeLength;
    }
}
