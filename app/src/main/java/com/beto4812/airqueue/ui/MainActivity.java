package com.beto4812.airqueue.ui;

import android.os.Bundle;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.ui.register.BaseDrawerActivity;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.*;

public class MainActivity extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
    }

}