package com.macauto.macautowarehouse.data;

import android.widget.Button;
import android.widget.EditText;

public class DividedItem {
    private int quantity;
    private EditText edit;
    private Button delete;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public EditText getEdit() {
        return edit;
    }

    public void setEdit(EditText edit) {
        this.edit = edit;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }
}
