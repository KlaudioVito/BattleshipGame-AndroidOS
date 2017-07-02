/**
 * CSC 221 - Final Project
 * Android App Development
 * by Klaudio Vito
 * 12/15/2015
 */

package com.example.klaudio.battleship_final;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Klaudio on 12/15/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(5, 5, 5, 5);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    public Integer[] mThumbIds = {
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,
            R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell, R.drawable.cell,

    };
}