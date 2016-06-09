package com.beto4812.airqueue.ui;

import android.os.Bundle;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.aws.AWSDynamoDbManager;
import com.beto4812.airqueue.ui.register.BaseDrawerActivity;

public class MainActivity extends BaseDrawerActivity {

    private AWSDynamoDbManager dbManagerr= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
    }

}