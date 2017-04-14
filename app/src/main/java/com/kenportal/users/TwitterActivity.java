package com.kenportal.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kenportal.users.utils.ConnectionDetector;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;


public class TwitterActivity extends AppCompatActivity {

    ListView tweet_list;
    private TweetViewAdapter adapter;
    Toolbar toolbar;
    LinearLayout networkUnavailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_twitter);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        networkUnavailable = (LinearLayout) findViewById(R.id.networkUnavailable);
        setSupportActionBar(toolbar);
        //Toolbar customization
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (ConnectionDetector.staticisConnectingToInternet(this)) {
            setProgressBarIndeterminateVisibility(true);
            adapter = new TweetViewAdapter(TwitterActivity.this);
            tweet_list = (ListView) findViewById(R.id.tweet_list);
//            tweet_list.setScrollingEnabled(true);
            tweet_list.setAdapter(adapter);
            tweet_list.setEmptyView(findViewById(R.id.loading));
            networkUnavailable.setVisibility(View.GONE);
            final UserTimeline userTimeline = new UserTimeline
                    .Builder()
                    .screenName("KENPortal")
                    .build();
            final TweetTimelineListAdapter tweet_adapter = new TweetTimelineListAdapter.Builder(this)
                    .setTimeline(userTimeline)
                    .build();
            tweet_list.setAdapter(tweet_adapter);
            adapter.notifyDataSetChanged();
            setProgressBarIndeterminateVisibility(false);

        } else {
//            setProgressBarIndeterminateVisibility(false);
            networkUnavailable.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
