package com.minji.librarys.base;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.minji.librarys.R;

/**
 * Created by user on 2016/9/29.
 */
public abstract class MyBaseMarkerView extends MarkerView {

    public final ImageView markerImage;
    public final TextView markerBottomText;
    public final TextView markerTopText;
    public final String[] xValues;

    public MyBaseMarkerView(Context context, int layoutResource, String[] xValues) {
        super(context, layoutResource);
        markerTopText = (TextView) findViewById(R.id.tv_marker_top_text);
        markerBottomText = (TextView) findViewById(R.id.tv_marker_bottom_text);
        markerImage = (ImageView) findViewById(R.id.iv_marker_image);
        this.xValues = xValues;
    }

    @Override
    public int getXOffset(float xpos) {
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        return -(getHeight() + getHeight() / 6);
    }
}
