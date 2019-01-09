package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mayi.mayisports.R;

public class CustomBarActivity extends AppCompatActivity {

    public static void start(Activity activity){
        Intent intent = new Intent(activity,CustomBarActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_bar);
    }
}
