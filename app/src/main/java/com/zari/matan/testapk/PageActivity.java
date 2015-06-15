package com.zari.matan.testapk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import adapters.FeedAdapter;
import items.FeedItem;
import items.ImageItem;
import items.ItemData;
import items.MP4VideoItem;
import items.YouTubeVideo;

/**
 * Created by Matan on 5/18/2015.
 */
public class PageActivity extends AppCompatActivity implements Task.DoIt, AbsListView.OnScrollListener {
    ImageView cover;
    TextView title;
    ListView collectionItems;
    Task task;
    int mScrollState;
    String pageName;
    public static MediaPlayer mPlayer;
    FeedAdapter adapter;
    ArrayList<FeedItem> data;
    private boolean alreadyReset = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        cover = (ImageView) findViewById(R.id.pageCover);
        title = (TextView) findViewById(R.id.pageTitle);
        collectionItems = (ListView) findViewById(R.id.collection_items);
        Intent intent = getIntent();
        pageName = intent.getStringExtra("page_name");
        task = new Task(this);
        String url = "http://wolflo.com/walls/system/" + pageName + "/?skip=0&limit=30";
        task.execute(url);




    }


    @Override
    public void send(String result) {
        if (result != null) {
            try {
                JSONObject collection = new JSONObject(result);
                JSONObject collectionInfo = collection.getJSONObject("collectionInfo");
                String page_cover = collectionInfo.getString("cover");
                String page_title = collectionInfo.getString("title");
                String bgColor = collectionInfo.getString("bgColor");
                bgColor = bgColor.replace("0x", "#");
                title.setBackgroundColor(Color.parseColor(bgColor));
                title.setText(page_title);
                Picasso.with(this).load(page_cover).into(cover);
                cover.setAdjustViewBounds(true);
                JSONArray items = collection.getJSONArray("items");
                if (data == null) {
                    data = new ArrayList<>();
                }
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    ItemData itemData = ItemData.parsUrl(this, item);
                    assert itemData != null;
                    if (itemData.type.equals("image")){
//                        if (itemData.img.contains(".gif"))
//                           data.add(new GifItem(this,itemData));
//                        else
                            data.add(new ImageItem( itemData,this));
                    }

                    if (itemData.type.equals("video") && !itemData.source.equals("youtube") && !itemData.source.equals("vimeo")) {
                        data.add(new MP4VideoItem(this, itemData));
                    }
                    if (itemData.type.equals("video") && itemData.source.equals("youtube")){
                        data.add(new YouTubeVideo(this,itemData));
                    }

                }
                if (adapter == null) {
                    adapter = new FeedAdapter(this, 0, data);
                    collectionItems.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();

                }
                collectionItems.setOnScrollListener(this);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        mScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (mScrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
            return;
        if (firstVisibleItem == totalItemCount - 10) {
            if (task != null && task.getStatus() != AsyncTask.Status.RUNNING && task.isCancelled()) {
                String url = "http://wolflo.com/walls/system/" + pageName + "/?skip=" + totalItemCount + "&limit=30";
                task = null;
                task = new Task(this);
                task.execute(url);
            }
        }
        for (int i = 0; i < visibleItemCount; i++) {
            View listItem = collectionItems.getChildAt(i);
            int itemViewType = adapter.getItemViewType(firstVisibleItem);
            if (i == 0) {
                if (itemViewType == FeedAdapter.types.MP4VIDEO.ordinal()) {
                    if (mPlayer != null) {
                        if (listItem.getTop() < (int) (-listItem.getHeight() * 0.75)) {
                            if (alreadyReset) {
                                alreadyReset = false;
                                mPlayer = null;
                                break;
                            }

                            try {

                                mPlayer.reset();
                                alreadyReset = true;
                                break;
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }


        if (listItem == null)
            break;
    }
//            LinearLayout title = (LinearLayout) listItem.findViewById(R.id.header);
//
//            int topMargin = 0;
//            if (i == 0) {
//                int top = listItem.getTop();
//                int height = listItem.getHeight();
//
//                if (top < 0) {
//                    topMargin = title.getHeight() < (top + height) ? -top : (height - title.getHeight());
//                }
//            }
//
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                title.setTranslationY(topMargin);
//                title.bringToFront();
//            } else {
//                title.setTranslationY(topMargin);
//                title.setTranslationZ(100);
//            }
        }

    }




