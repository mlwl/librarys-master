package com.minji.librarys.chart;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.minji.librarys.base.MyBaseMarkerView;

/**
 * Created by user on 2016/9/29.
 */
public class OccupancyMarkerView extends MyBaseMarkerView {


    public OccupancyMarkerView(Context context, int layoutResource, String[] xValues) {
        super(context, layoutResource, xValues);
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {

        markerBottomText.setText("占有率: " + entry.getVal() + "");
        markerTopText.setText(xValues[entry.getXIndex()]);


        System.out.println(entry.getXIndex() + "---------" + highlight.getDataSetIndex());


    }
}
