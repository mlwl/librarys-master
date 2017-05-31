package com.minji.librarys.chart;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by user on 2016/10/9.
 */
public class PersonNumberFormatter implements ValueFormatter, YAxisValueFormatter {
    protected DecimalFormat mFormat;

    public PersonNumberFormatter() {
        this.mFormat = new DecimalFormat("###,###,##0");
    }

    public PersonNumberFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return this.mFormat.format((double)value) + " 人";
    }

    public String getFormattedValue(float value, YAxis yAxis) {
        return this.mFormat.format((double)value) + " 人";
    }
}