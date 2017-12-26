package com.lz.swiperefreshablelayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    SwipeRefreshableLayout mSwipeRefreshableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] items = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M"};
        ListView lv = findViewById(R.id.list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lv.setAdapter(adapter);

        mSwipeRefreshableLayout = findViewById(R.id.swipe_refreshable_layout);
        mSwipeRefreshableLayout.setSwipeRefreshableListener(new SwipeRefreshableLayout.SwipeRefreshableListener() {
            @Override
            public void onRefreshing() {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshableLayout.refreshCompleted();
                    }
                }, 3000);
            }
        });
    }
}
