package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lyuzik.remoteimageview.RImageView;
import com.zari.matan.testapk.Page;
import com.zari.matan.testapk.R;

import java.util.ArrayList;

import helper.Utils;

/**
 * Created by Matan on 5/18/2015
 */
public class SearchResultsAdapter extends ArrayAdapter<Page> {

    Context context;
    ArrayList<Page> data;
    ViewHolder holder;
    public SearchResultsAdapter(Context context, int resource, ArrayList<Page> data) {
        super(context, resource, data);
        this.context = context;
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        if(convertView == null){
           holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.search_result_item,null,false);
            TextView title = (TextView) convertView.findViewById(R.id.search_item_title);
            holder.cover = (RImageView) convertView.findViewById(R.id.pageCover);
            holder.title = title;
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Page item = getItem(position);
        holder.title.setText(item.getName());
        int width = Utils.getDP1()*50;
        int height = Utils.getDP1()*50;
        holder.cover.setTargetSize(width,height);
        holder.cover.loadImageCircleBitmap(item.getCover());



        return convertView;
    }


    class ViewHolder{
        TextView title;
        RImageView cover;
    }
}
