package items;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lyuzik.remoteimageview.RImageView;
import com.zari.matan.testapk.R;

import adapters.FeedAdapter;
import helper.ShowGifView;
import helper.Utils;

/**
 * Created by Matan on 5/27/2015
 */
public class GifItem implements FeedItem{

    Context context;
    ItemData itemData;
    ViewHolder holder;
    LinearLayout root;

    public GifItem(Context context, ItemData itemData) {
        this.context = context;
        this.itemData = itemData;
        //((Activity)context).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    @Override
    public int getViewType() {
        return FeedAdapter.types.GIF.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gif_item_layout, null, false);
            holder = new ViewHolder();
            FrameLayout gifContainer = (FrameLayout) convertView.findViewById(R.id.gif_container);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            RImageView cover = (RImageView) convertView.findViewById(R.id.gifItemCover);
            ShowGifView gif = (ShowGifView) convertView.findViewById(R.id.gif);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            TextView text = (TextView) convertView.findViewById(R.id.text);
            root = (LinearLayout) convertView.findViewById(R.id.root);
            holder.cover = cover;
            holder.root = root;
            holder.gif = gif;
            holder.name = name;
            holder.time = time;
            holder.text = text;
            holder.gifContainer = gifContainer;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int dp = Utils.getDP1();
        holder.cover.setTargetSize(35 * dp * 2, 35 * dp * 2);
        holder.cover.loadImageCircleBitmap(itemData.collData.cover);
        holder.gif.setGifURL(itemData.img);
        holder.time.setText(itemData.dateShort + " ago on " + context.getString(itemData.sourceStrId));
        holder.name.setText(itemData.collData.title);

        holder.text.setText(itemData.text);
        return convertView;
    }

    @Override
    public int getClickType() {
        return 0;


    }



    private class ViewHolder {
        TextView name;
        FrameLayout gifContainer;
        RImageView cover;
        ShowGifView gif;
        TextView time;
        TextView text;
        LinearLayout root;

    }



}
