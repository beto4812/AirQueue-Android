package com.beto4812.airqueue.ui.main.pollutantsCircular;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.beto4812.airqueue.R;
import com.beto4812.airqueue.model.PollutantCategoryInfo;
import com.squareup.picasso.Picasso;

public class PollutantCategoryDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = "PollutantCategoryDetail";

    public PollutantCategoryInfo pollutantCategoryInfo;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private WebView webView;

    private View rootView;

    public static void navigate(Context context, View transitionImage, PollutantCategoryInfo pollutantCategoryInfo) {
        Intent intent = new Intent(context, PollutantCategoryDetailActivity.class);
        Log.v(LOG_TAG, "IMAGE: " + pollutantCategoryInfo.getImage());
        intent.putExtra("IMAGE", pollutantCategoryInfo.getImage());
        Log.v(LOG_TAG, "TITLE: " + pollutantCategoryInfo.getPollutantCategoryString());
        intent.putExtra("TITLE", pollutantCategoryInfo.getPollutantCategoryString());
        intent.putExtra("URL", pollutantCategoryInfo.getUrl());
        context.startActivity(intent);
    }

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_pollutant_detail);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), "Detail");
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("What is it?");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        final ImageView image = (ImageView) findViewById(R.id.image);
        Log.v(LOG_TAG, "onCreate: getIntExtra: " + getIntent().getIntExtra("IMAGE", -1));
        Picasso.with(this).load(getIntent().getIntExtra("IMAGE", -1)).into(image);

        Log.v(LOG_TAG, "TITLE: " + getIntent().getStringExtra("TITLE"));
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("TITLE"));

        webView = (WebView)  findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }});

        webView.getSettings().setJavaScriptEnabled(true);
        Log.v(LOG_TAG, "navigating: " + getIntent().getStringExtra("URL"));
        webView.loadUrl(getIntent().getStringExtra("URL"));
    }

    @Override public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
