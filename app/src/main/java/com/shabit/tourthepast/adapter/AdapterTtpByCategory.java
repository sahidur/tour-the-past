package com.shabit.tourthepast.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;
import com.shabit.tourthepast.MainConfig;
import com.shabit.tourthepast.R;
import com.shabit.tourthepast.item.ItemTtp;
import com.shabit.tourthepast.utility.ImageLoader;

import java.util.List;
import java.util.Random;

public class AdapterTtpByCategory extends ArrayAdapter<ItemTtp> {

    private static final String TAG = "AdapterRecipes";
    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
    private final Random mRandom;
    public ImageLoader imageLoader;
    ItemTtp object;
    private Activity activity;
    private List<ItemTtp> item;
    private int row;

    public AdapterTtpByCategory(Activity act, int resource, List<ItemTtp> arrayList) {
        super(act, resource, arrayList);
        this.activity = act;
        this.row = resource;
        this.item = arrayList;
        imageLoader = new ImageLoader(activity);
        mRandom = new Random();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(row, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if ((item == null) || ((position + 1) > item.size()))
            return view;

        object = item.get(position);

        double positionHeight = getPositionRatio(position);

        holder.title = (TextView) view.findViewById(R.id.ttp_title);
        holder.description = (TextView) view.findViewById(R.id.ttp_desc);
        holder.time = (TextView) view.findViewById(R.id.ttp_time);
        holder.image = (DynamicHeightImageView) view.findViewById(R.id.ttp_image);

        holder.image.setHeightRatio(positionHeight);
        holder.title.setText(object.getTtpHeading());
        holder.description.setText(object.getTtpDescription());
        holder.time.setText(object.getTtpTime());
        imageLoader.DisplayImage(MainConfig.SERVER_URL + "/upload/" + object.getTtpImage(), holder.image);

        return view;

    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

    public class ViewHolder {

        public TextView title;
        public TextView description;
        public TextView time;
        public DynamicHeightImageView image;

    }
}
