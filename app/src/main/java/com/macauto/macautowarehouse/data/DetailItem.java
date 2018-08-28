package com.macauto.macautowarehouse.data;


import android.widget.CheckBox;

import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailItem {
    private String title;
    private String name;
    private TextView textView;
    private LinearLayout linearLayout;
    private CheckBox checkBox;
    //private EditText edit;
    //private Button button;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

}
