package com.macauto.macautowarehouse.data;

public class AllocationMsgItem {
    private String work_order;
    //private String date;
    private boolean delete;
    private boolean selected;

    public String getWork_order() {
        return work_order;
    }

    public void setWork_order(String work_order) {
        this.work_order = work_order;
    }

    /*public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }*/

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
