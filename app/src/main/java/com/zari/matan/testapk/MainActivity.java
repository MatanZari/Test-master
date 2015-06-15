package com.zari.matan.testapk;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.concurrent.atomic.AtomicInteger;

import adapters.MyPagerAdapter;
import tabs.SlidingTabLayout;


public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    public ViewPager pager;
    GoogleApiClient apiClient;
    public static boolean isInternetAvailable;
    String appTitle = "Wolflo";
    public final String SP_NAME = "MyPrefs";
    Uri appUri = Uri.parse("android-app://com.zari.matan.testapk/http/zari.com");
    Uri webUri = Uri.parse("http://www.wolflo.com");
    SlidingTabLayout tabs;
    public static MediaPlayer mMediaPlayer;

    public SharedPreferences sp;
    public MyPagerAdapter adapter;
    public static YouTubePlayer mYoutubePlayer;
    private int[] imageResId = {
            R.drawable.home_tab_icon,
            R.drawable.search_tab_icon,
            R.drawable.notification_tab_icon

    };
    int numbOfTabs = 3;


    static AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
        Action viewAction = Action.newAction(Action.TYPE_VIEW, appTitle, webUri, appUri);
        AppIndex.AppIndexApi.start(apiClient, viewAction);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Action viewAction = Action.newAction(Action.TYPE_VIEW, appTitle, webUri, appUri);
        AppIndex.AppIndexApi.end(apiClient, viewAction);
        apiClient.disconnect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isInternetAvailable = isInternetConnected();
        apiClient = new GoogleApiClient.Builder(this).addApi(AppIndex.APP_INDEX_API).build();
        sp = getSharedPreferences(SP_NAME,MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new MyPagerAdapter(this, getSupportFragmentManager(), imageResId, numbOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);




        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        //tabs.setCustomTabView(R.layout.custom_tab, R.id.textPageTitle);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimaryDark);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
        pager.setOffscreenPageLimit(numbOfTabs-1);
        //pager.setOnPageChangeListener(this);





    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        FragmentUiLifeCycleHelper fragmentToShow = (FragmentUiLifeCycleHelper)adapter.instantiateItem(null, position);
//        fragmentToShow.onFragmentResumed();
//
//        FragmentUiLifeCycleHelper fragmentToHide = (FragmentUiLifeCycleHelper)adapter.instantiateItem(null,currentPosition);
//        fragmentToHide.onPauseFragment();
//
//        currentPosition = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        
    }

    public  boolean isInternetConnected(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }
}