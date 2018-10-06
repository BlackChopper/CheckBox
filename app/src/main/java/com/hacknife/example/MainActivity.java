package com.hacknife.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hacknife.checkbox.CheckBox;

public class MainActivity extends AppCompatActivity implements CheckBox.OnCheckedChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckBox checkBox=findViewById(R.id.chb);
        checkBox.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onChange(View view, boolean isChecked) {
        return true;
    }
}
