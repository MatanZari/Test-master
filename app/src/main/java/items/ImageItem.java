package items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lyuzik.remoteimageview.RImageView;
import com.zari.matan.testapk.MainActivity;
import com.zari.matan.testapk.R;

import adapters.FeedAdapter;
import fragments.HomeFragment;
import helper.BackgroundContainer;
import helper.ExpendImage;
import helper.ListViewItemAnimation;
import helper.Utils;

/**
 * Created by Matan on 5/18/2015
 */
public class ImageItem implements FeedItem, View.OnClickListener, ListViewItemAnimation.SwipeCallback {
    private ExpendImage zoom;
    ItemData itemData;
    Context context;
    ViewHolder holder = null;
    MainActivity activity;
    HomeFragment homeFragment;
    BackgroundContainer backgroundContainer;
    ListViewItemAnimation itemAnimation;
    ListView listView;
    RelativeLayout container;


    public ImageItem(Context context, ItemData itemData, ExpendImage zoom) {
        this.context = context;
        this.itemData = itemData;
        this.zoom = zoom;

        activity = (MainActivity) context;
        homeFragment = (HomeFragment) activity.adapter.instantiateItem(null, 0);

    }

    public ImageItem(ItemData itemData, Context context) {
        this.itemData = itemData;
        this.context = context;

        activity = (MainActivity) context;
        homeFragment = (HomeFragment) activity.adapter.instantiateItem(null, 0);
    }

    @Override
    public int getViewType() {
        return FeedAdapter.types.IMAGE.ordinal();
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(LayoutInflater inflater, View convertView) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.image_item_layout, null, false);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            RImageView cover = (RImageView) convertView.findViewById(R.id.imageItemCover);
            RImageView image = (RImageView) convertView.findViewById(R.id.item_image);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView text = (TextView) convertView.findViewById(R.id.text);
            backgroundContainer = (BackgroundContainer) homeFragment.getView().findViewById(R.id.listBackgroundContainer);
            container = (RelativeLayout) homeFragment.getView().findViewById(R.id.webViewContainer);
            listView = (ListView) convertView.getParent();
            holder = new ViewHolder();
            holder.container = container;
            holder.cover = cover;
            holder.name = name;
            holder.backgroundContainer = backgroundContainer;
            holder.image = image;
            holder.time = time;
            holder.text = text;
            convertView.setTag(holder);
            Log.e("getView","newView");
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int dp = Utils.getDP1();
        holder.cover.setTargetSize(35 * dp * 2, 35 * dp * 2);
        holder.cover.loadImageCircleBitmap(itemData.collData.cover);
        holder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // holder.image.setAdjustViewBounds(true);
        int fff = dp * 300;
        holder.image.setTargetSize(fff, fff);
        holder.image.loadImageBitmap(itemData.img);
        holder.time.setText(itemData.dateShort + " ago on " + context.getString(itemData.sourceStrId));
        holder.name.setText(itemData.collData.title);
        holder.text.setText(itemData.text);
        holder.image.setOnClickListener(this);
        if (itemData.externalUrl != null) {
            itemAnimation = new ListViewItemAnimation(context, holder.image, (ListView) homeFragment.getView().findViewById(R.id.home_feed), this);
            itemAnimation.setListItem(holder.image);
            itemAnimation.setBackgroundContainer(holder.backgroundContainer);
            holder.image.setOnTouchListener(itemAnimation.mTouchListener);
        }

        return convertView;
    }

    @Override
    public int getClickType() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (zoom != null)
            zoom.openZoom(itemData);
    }

    @Override
    public void onSwipe() {
        openWeb(itemData.externalUrl);
    }


    private class ViewHolder {
        TextView time;
        TextView name;
        RImageView cover;
        RImageView image;
        TextView text;
        BackgroundContainer backgroundContainer;
        RelativeLayout container;
    }


    @SuppressLint("SetJavaScriptEnabled")
    public void openWeb(String url) {


        final WebView webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        RImageView x = new RImageView(context);
        LinearLayout linearLayout = new LinearLayout(context);


        x.setImageResource(R.drawable.x);
        x.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        x.layout(linearLayout.getLeft(), linearLayout.getTop(), 0, 0);

        holder.container.addView(webView);
        RelativeLayout.LayoutParams webViewLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        webView.setId(MainActivity.generateViewId());
        RelativeLayout.LayoutParams linearLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150);
        linearLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        holder.container.addView(linearLayout);
        linearLayout.setId(MainActivity.generateViewId());
        webViewLayoutParams.addRule(RelativeLayout.BELOW, linearLayout.getId());
        webView.setLayoutParams(webViewLayoutParams);
        linearLayout.setLayoutParams(linearLayoutParams);
        linearLayout.addView(x);
        linearLayout.setBackgroundColor(Color.parseColor("#80000000"));
        holder.container.setVisibility(View.VISIBLE);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.clearHistory();
                holder.container.removeAllViews();
                holder.container.setVisibility(View.GONE);


            }
        });
        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });
    }
}
