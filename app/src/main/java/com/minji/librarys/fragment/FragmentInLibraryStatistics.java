package com.minji.librarys.fragment;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;
import com.minji.librarys.chart.InLibraryMarkerView;
import com.minji.librarys.chart.PersonNumberFormatter;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.observer.MySubject;
import com.minji.librarys.observer.Observers;
import com.minji.librarys.ui.IntegralAndOrderOrStatementActivity;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.StringUtils;
import com.minji.librarys.uitls.ToastUtil;
import com.minji.librarys.uitls.ViewsUitls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 2016/9/20.
 */
public class FragmentInLibraryStatistics extends BaseFragment implements View.OnClickListener, Observers {

    private View inflate;
    private EditText mInputStartDay;
    private Button mStartDaySearch;
    private LineChart mLineChart;
    private BarChart mBarChart;
    private ImageView mSelectLineChart;
    private ImageView mSelectBarChart;

    private int WHAT_CHART_NO_PRESSED = 2;// 一进入本页面就是柱状没被选中
    private int LINE_CHART_NO_PRESSED = 1;// 此为折线没被选中
    private int BAR_CHART_NO_PRESSED = 2;//  此为柱状没被选中

    // 分别存储折线或者柱状数据的对象
    private ArrayList<Entry> mOrderDatesLineList = new ArrayList<>();
    private ArrayList<Entry> mInLibraryDatesLineList = new ArrayList<>();
    private ArrayList<BarEntry> mOrderDatesBarList = new ArrayList<>();
    private ArrayList<BarEntry> mInLibraryDatesBarList = new ArrayList<>();

    private String[] mInLibraryTime = {};
    private float[] mInLibraryPerson = {0.0f, 1.0f, 2.0f, 4.0f, 2.0f, 7.0f, 4.0f, 5.0f, 6.0f, 2.0f,};
    private float[] mOrderPerson = {2.0f, 3.0f, 3.0f, 1.0f, 8.0f, 3.0f, 4.0f, 0.0f, 1.0f, 2.0f};

    private LinearLayout mLinearBarChart;
    private LinearLayout mLinearLineChart;

    private BarData mBarData;
    private LineData mLineData;

    private LineDataSet mOrderDataSetLine;
    private LineDataSet mInLibraryDataSetLine;
    private BarDataSet mOrderDataSetBar;
    private BarDataSet mInLibraryDataSetBar;
    private IntegralAndOrderOrStatementActivity activity;

    private int mInLibraryPersonColor = Color.RED;
    private int mOrderPersonColor = Color.BLACK;
    private View mLineLegend1;
    private View mLineLegend2;
    private View mBarLegend1;
    private View mBarLegend2;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {

        activity = (IntegralAndOrderOrStatementActivity) getActivity();

        inflate = ViewsUitls.inflate(R.layout.layout_in_library_statistics);

        initInflateView();

        return inflate;
    }

    private void initInflateView() {
        mInputStartDay = (EditText) inflate.findViewById(R.id.et_in_library_statistics_start_day_input);
        mInputStartDay.setOnClickListener(this);
        mStartDaySearch = (Button) inflate.findViewById(R.id.bt_in_library_statistics_start_search);
        mStartDaySearch.setOnClickListener(this);

        mLinearLineChart = (LinearLayout) inflate.findViewById(R.id.ll_line_chart_in_library_statistics);
        mLinearBarChart = (LinearLayout) inflate.findViewById(R.id.ll_bar_chart_in_library_statistics);

        mLineChart = (LineChart) inflate.findViewById(R.id.line_chart_in_library_statistics);
        mBarChart = (BarChart) inflate.findViewById(R.id.bar_chart_in_library_statistics);
        setBarLineChartStyle(mLineChart);
        setBarLineChartStyle(mBarChart);

        mSelectLineChart = (ImageView) inflate.findViewById(R.id.iv_in_library_statistics_skip_broken_line);
        mSelectLineChart.setOnClickListener(this);
        mSelectBarChart = (ImageView) inflate.findViewById(R.id.iv_in_library_statistics_skip_columnar);
        mSelectBarChart.setOnClickListener(this);

        mLineLegend1 = inflate.findViewById(R.id.tv_line_chart_in_library_legend_1);
        mLineLegend2 = inflate.findViewById(R.id.tv_line_chart_in_library_legend_2);
        mBarLegend1 = inflate.findViewById(R.id.tv_bar_chart_in_library_legend_1);
        mBarLegend2 = inflate.findViewById(R.id.tv_bar_chart_in_library_legend_2);
    }

    private void setBarLineChartStyle(BarLineChartBase barLineChartBase) {

        setNoDataTextPaint(barLineChartBase);

        barLineChartBase.setNoDataText("请先在上方选择起始日期");

        // 图表描述字的设置
        barLineChartBase.setDescription("");//描述文字默认在图表右下角
        barLineChartBase.setDescriptionColor(0xffffffff);// 图表右下角的描述文字的颜色
        // mLineChart.setDescriptionPosition(0.5f, 0.5f);// 同过XY参数手动去设置描述文字所在位置，参数是像素
        // mLineChart.setDescriptionTypeface(Typeface.DEFAULT_BOLD);// 设置描述文字的 Typeface。文本样式
        barLineChartBase.setDescriptionTextSize(6f); // 设置描述文字大小，以像素为单位，最小6f，最大16f。

        // 网格的背景颜色(这个只设置网格的背景，不是整个图表的背景)
        barLineChartBase.setDrawGridBackground(false);// 这个是设置是对下面这个设置是否生效而言的
        barLineChartBase.setGridBackgroundColor(0xffffffff);// 设置图表中网格的背景颜色只用setDrawGridBackground为true该设置才能生效

        // 网格的边框
        barLineChartBase.setDrawBorders(false);  // 这个是设置是对下面两个设置是否生效而言的
        barLineChartBase.setBorderColor(0xff00ff00); //设置网格边框线的颜色。
        barLineChartBase.setBorderWidth(2f); // 设置网格边界线的宽度，单位 dp。


        // 图列设置
        Legend legend = barLineChartBase.getLegend();// 获取图列对象
        legend.setEnabled(false);// 设置Legend启用或禁用
        legend.setTextColor(Color.BLACK);// 设置图例标签文字的颜色。
        legend.setTextSize(10f);// 设置图例标签文字大小。
        legend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);//设置图列位置
        legend.setForm(Legend.LegendForm.CIRCLE);// 设置图列前图案样式

        // X轴的设置
        XAxis xAxis = barLineChartBase.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置X轴的位置
        xAxis.setEnabled(true);// 设置是否禁用X轴(包括一些相关的竖线)
        // 上面xAxis.setEnabled(false)代码设置了false,所以下面第一行即使设置为true也不会绘制AxisLine
//        xAxis.setDrawAxisLine(true);
        // 前面xAxis.setEnabled(false);则下面绘制的Grid不会有"竖的线"（与X轴有关）
        xAxis.setDrawGridLines(false); // 设置X轴上的相关竖线是否绘制
        xAxis.setDrawLabels(true); // 设置X轴上的标签文字是否绘制
        xAxis.setTypeface(Typeface.DEFAULT_BOLD); // 设置X轴上标签的字体
        xAxis.setTextColor(Color.BLACK);//设置X轴标签的颜色。
//        xAxis.setTextSize(14f);//设置X轴标签的文字大小。
//        xAxis.setGridLineWidth(10f);//设置X轴网格线的宽度。
        xAxis.setGridColor(ContextCompat.getColor(getActivity(), R.color.my_darker_gray));//设置X轴上网格线(竖线)颜色。
        xAxis.setAxisLineColor(Color.BLACK);//设置 X轴 轴线的颜色。
        xAxis.setAxisLineWidth(1f);// 设置 X轴 轴线的宽度。
        xAxis.enableGridDashedLine(10f, 10f, 0f);// 设置X轴上的网格线为虚线模式
//        xAxis.setSpaceBetweenLabels(1);//设置标签字符间的空隙，默认characters间隔是4 。(有可能根据屏幕宽度有一定关系)

        // Y轴的设置
        YAxis leftAxis = barLineChartBase.getAxisLeft();
        YAxis rightAxis = barLineChartBase.getAxisRight();
        rightAxis.setEnabled(false);// 设置最右边的Y轴不启用

//        leftAxis. setAxisMaxValue(float max) ;// 设置该轴的最大值。 如果设置了，这个值将不会是根据提供的数据计算出来的。
//        leftAxis. resetAxisMaxValue();// 调用此方法撤销先前设置的最大值。 通过这样做，你将再次允许轴自动计算出它的最大值。
        leftAxis.setAxisMinValue(0.0f);// 设置该轴的自定义最小值。 如果设置了，这个值将不会是根据提供的数据计算出来的。
//        leftAxis.resetAxisMinValue();// 调用此撤销先前设置的最小值。 通过这样做，你将再次允许轴自动计算它的最小值。
        leftAxis.setDrawGridLines(false);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setAxisLineColor(Color.BLACK);
        leftAxis.setTypeface(Typeface.DEFAULT_BOLD);
        leftAxis.setDrawGridLines(true);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisLineWidth(1f);
//        leftAxis.setTextSize(14f);
        leftAxis.setValueFormatter(new PersonNumberFormatter());// 设置Y轴的标签值的格式(此处是%格式)

        // 触摸监听交互
        barLineChartBase.setTouchEnabled(true);   //启用/禁用与图表的所有可能的触摸交互。
        barLineChartBase.setDragEnabled(true);    // 缩放后是否可以拖动（平移）图表。
        barLineChartBase.setScaleEnabled(true);   // 图表上的两个轴是否可以进行缩放
        barLineChartBase.setScaleXEnabled(true);  //在x轴上是否可以进行缩放
        barLineChartBase.setScaleYEnabled(true);  // 在y轴是否可以进行缩放
        barLineChartBase.setPinchZoom(false);     // 设置图表x轴和y轴是否可以一起缩放(true)，还是可分别缩放(falses)。
        barLineChartBase.setDoubleTapToZoomEnabled(false);    // 设置为false以禁止通过在其上双击缩放图表。
        barLineChartBase.setHighlightPerDragEnabled(true); // 设置为true时,且图表已经缩小到最小时，在图表上进行拖动，吐出显示的值会随着拖动而改变。 默认值：true
        barLineChartBase.setHighlightPerTapEnabled(true);  // 设置Values(节点)被点击时是否会凸出显示(进行交互反应)   设置为false后值仍然可以通过拖动或编程方式突出显示。 默认值：true

    }

    /*自定义设置没有数据时显示文本的画笔对象*/
    private void setNoDataTextPaint(BarLineChartBase barLineChartBase) {
        Paint paint = new Paint(1);
        paint.setColor(Color.rgb(000, 000, 000));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Utils.convertDpToPixel(16.0F));
        barLineChartBase.setPaint(paint, 7);
    }

    private void setChartDate(String[] timeXValue, float[] orderDate, float[] inLibraryDate) {

        // 每次设置数据时都重新创建mLineData和mBarData对象
        mLineData = new LineData();
        mBarData = new BarData();

        // 清空四个ArrayList数据并添加新数据
        setDateToListOrXValue(timeXValue, orderDate, inLibraryDate);

        // 使用这个方法的前提，如果四个DateSet对象有一个不为空则，要使用mLineData或者mBarData 移除掉DateSet对象
        // 使用四个ArrayList数据对象重新创建LineDataSet或者BarDataSet然后赋值给四个DateSet对象
        getLineBarDateSetAndAddDateSet();

        mLineChart.setData(mLineData);
        mBarChart.setData(mBarData);

        mLineChart.invalidate();
        mBarChart.invalidate();

        mLineChart.animateXY(1000, 1000);
        mBarChart.animateXY(1000, 1000);
    }

    /*
     *使用四个ArrayList数据对象重新创建LineDataSet或者BarDataSet然后赋值给四个DateSet对象*/
    private void getLineBarDateSetAndAddDateSet() {

        if (mOrderDatesLineList.size() > 0) {
            mOrderDataSetLine = getOrderLineDataSet(mOrderDatesLineList);
            mLineData.addDataSet(mOrderDataSetLine);
        }

        if (mInLibraryDatesLineList.size() > 0) {
            mInLibraryDataSetLine = getInLibraryLineDataSet(mInLibraryDatesLineList);
            mLineData.addDataSet(mInLibraryDataSetLine);
        }

        if (mOrderDatesBarList.size() > 0) {
            mOrderDataSetBar = getOrderBarDataSet(mOrderDatesBarList);
            mBarData.addDataSet(mOrderDataSetBar);
        }
        if (mInLibraryDatesBarList.size() > 0) {
            mInLibraryDataSetBar = getInLibraryBarDataSet(mInLibraryDatesBarList);
            mBarData.addDataSet(mInLibraryDataSetBar);
        }
    }

    /*
    * timeXValue：X轴上的数据
    * orderDate：预约数据
    * inLibraryDate：入馆数据
    * */
    private void setDateToListOrXValue(String[] timeXValue, float[] orderDate, float[] inLibraryDate) {
        mOrderDatesLineList.clear();
        mOrderDatesBarList.clear();
        mInLibraryDatesLineList.clear();
        mInLibraryDatesBarList.clear();
        if (timeXValue != null) {
            for (int i = 0; i < timeXValue.length; i++) {
                // 添加X轴的标签文本
                mLineData.addXValue(timeXValue[i]);
                mBarData.addXValue(timeXValue[i]);
            }
        }
        if (orderDate != null) {
            for (int i = 0; i < orderDate.length; i++) {
                // 从textOrder数据源中获取数据并创建Entry对象并添加进各自的集合中
                mOrderDatesLineList.add(new Entry(orderDate[i], i));
                mOrderDatesBarList.add(new BarEntry(orderDate[i], i));
            }
        }
        if (inLibraryDate != null) {
            for (int i = 0; i < inLibraryDate.length; i++) {
                // 从textInLibrary数据源中获取数据并创建Entry对象并添加进各自的集合中
                mInLibraryDatesLineList.add(new Entry(inLibraryDate[i], i));
                mInLibraryDatesBarList.add(new BarEntry(inLibraryDate[i], i));
            }
        }
    }

    @NonNull
    private BarDataSet getInLibraryBarDataSet(ArrayList<BarEntry> inLibraryDatesBar) {
        BarDataSet inLibraryDataSetBar = new BarDataSet(inLibraryDatesBar, "入馆人数");
        inLibraryDataSetBar.setAxisDependency(YAxis.AxisDependency.LEFT);
        inLibraryDataSetBar.setDrawValues(false); // 设置是否在点上绘制Value
        inLibraryDataSetBar.setColor(mInLibraryPersonColor);
        return inLibraryDataSetBar;
    }

    @NonNull
    private BarDataSet getOrderBarDataSet(ArrayList<BarEntry> orderDatesBar) {
        BarDataSet orderDataSetBar = new BarDataSet(orderDatesBar, "预约人数");
        orderDataSetBar.setAxisDependency(YAxis.AxisDependency.LEFT);
        orderDataSetBar.setDrawValues(false); // 设置是否在点上绘制Value
        orderDataSetBar.setColor(mOrderPersonColor);
        return orderDataSetBar;
    }

    @NonNull
    private LineDataSet getInLibraryLineDataSet(ArrayList<Entry> inLibraryDatesLine) {
        LineDataSet inLibraryDataSetLine = new LineDataSet(inLibraryDatesLine, "入馆人数");
        inLibraryDataSetLine.setAxisDependency(YAxis.AxisDependency.LEFT);
        inLibraryDataSetLine.setLineWidth(2f);// 设置该组数据折线的线的宽度
        inLibraryDataSetLine.setCircleRadius(3f);// 设置该组数据折线上的节点圆圈的大小
        inLibraryDataSetLine.setDrawValues(false); // 设置是否在点上绘制Value
        inLibraryDataSetLine.setHighLightColor(Color.TRANSPARENT); // 设置点击某个点时，横竖两条线的颜色
        inLibraryDataSetLine.setColor(mInLibraryPersonColor);
        inLibraryDataSetLine.setCircleColor(mInLibraryPersonColor);
        return inLibraryDataSetLine;
    }

    @NonNull
    private LineDataSet getOrderLineDataSet(ArrayList<Entry> orderDatesLine) {
        LineDataSet orderDataSetLine = new LineDataSet(orderDatesLine, "预约人数");
        orderDataSetLine.setAxisDependency(YAxis.AxisDependency.LEFT);
        orderDataSetLine.setLineWidth(2f);// 设置该组数据折线的线的宽度
        orderDataSetLine.setCircleRadius(3f);// 设置该组数据折线上的节点圆圈的大小
        orderDataSetLine.setDrawValues(false); // 设置是否在点上绘制Value
        orderDataSetLine.setHighLightColor(Color.TRANSPARENT); // 设置点击某个点时，横竖两条线的颜色
        orderDataSetLine.setColor(mOrderPersonColor);
        orderDataSetLine.setCircleColor(mOrderPersonColor);
        return orderDataSetLine;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        // 为了进入onCreateSuccessView()方法
        ArrayList<String> list = new ArrayList<>();
        list.add("list");
        return chat(list);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_in_library_statistics_skip_broken_line:
                if (WHAT_CHART_NO_PRESSED == LINE_CHART_NO_PRESSED) {// 当折线没被选中时点击图标才有用
                    System.out.println("折线被选中");

                    mSelectBarChart.setImageResource(R.mipmap.select_columnar_default);
                    mSelectLineChart.setImageResource(R.mipmap.select_broken_line_pressed);
                    setLineBarVisibility(View.VISIBLE, View.GONE);

                    mLineChart.animateXY(1000, 1000);

                    WHAT_CHART_NO_PRESSED = BAR_CHART_NO_PRESSED;
                }
                break;
            case R.id.iv_in_library_statistics_skip_columnar:
                if (WHAT_CHART_NO_PRESSED == BAR_CHART_NO_PRESSED) {// 当柱状没被选中时点击图标才有用
                    System.out.println("柱状被选中");

                    mSelectBarChart.setImageResource(R.mipmap.select_columnar_pressed);
                    mSelectLineChart.setImageResource(R.mipmap.select_broken_line_default);
                    setLineBarVisibility(View.GONE, View.VISIBLE);

                    mBarChart.animateXY(1000, 1000);

                    WHAT_CHART_NO_PRESSED = LINE_CHART_NO_PRESSED;
                }
                break;
            case R.id.bt_in_library_statistics_start_search:
                requestGetStatisticsDate();
                break;
            case R.id.et_in_library_statistics_start_day_input:
                // TODO 备选 选择时间
                activity.showDialog(1);
                break;
        }

    }

    private void requestGetStatisticsDate() {

        final String startDay = mInputStartDay.getText().toString();
        if (StringUtils.isEmpty(startDay)) {
            ToastUtil.showToast(getActivity(), "请选择起始日期");
            return;
        }

        activity.setLoadingVisibility(View.VISIBLE);
        activity.setIsInterruptTouch(true);

        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("sdate", startDay).add("type", "1").build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");
        System.out.println(address + IpFiled.IN_LIBRARY_STATISTICS);
        Request request = new Request.Builder()
                .url(address + IpFiled.IN_LIBRARY_STATISTICS)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.setLoadingVisibility(View.GONE);
                        activity.setIsInterruptTouch(false);
                        ToastUtil.showToast(getActivity(), "网络异常，请稍候");
                    }
                });
                System.out.println("========================onFailure========================");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                System.out.println(result);
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        activity.setLoadingVisibility(View.GONE);
                        activity.setIsInterruptTouch(false);
                        if (!result.contains("html")) {
                            analysisStatisticDate(result);
                            if (mOrderPerson.length == 0 && mInLibraryPerson.length == 0) {
                                ToastUtil.showToast(getActivity(), "此时间段暂无数据");
                            } else {
                                mLineLegend1.setVisibility(View.VISIBLE);
                                mLineLegend2.setVisibility(View.VISIBLE);
                                mBarLegend1.setVisibility(View.VISIBLE);
                                mBarLegend2.setVisibility(View.VISIBLE);

                                setChartDate(mInLibraryTime, mOrderPerson, mInLibraryPerson);
                                mLineChart.setMarkerView(new InLibraryMarkerView(getActivity(), R.layout.item_markerview, mInLibraryTime, startDay));
                                mBarChart.setMarkerView(new InLibraryMarkerView(getActivity(), R.layout.item_markerview, mInLibraryTime, startDay));
                            }
                        } else {
                            ToastUtil.showToast(getActivity(), "服务器异常，请稍候");
                        }
                    }
                });
            }
        });
    }

    private void analysisStatisticDate(String result) {
        try {
            final JSONObject object = new JSONObject(result);
            if (object.has("data_date")) {
                JSONArray timeDate = object.optJSONArray("data_date");
                mInLibraryTime = new String[timeDate.length() + 1];
                for (int i = 0; i < timeDate.length(); i++) {
                    String string = timeDate.optString(i);
                    mInLibraryTime[i] = string.substring(5, string.length());
                }
                mInLibraryTime[timeDate.length()] = "  ";
            }
            if (object.has("data_bespeak")) {
                JSONArray dataBespeak = object.optJSONArray("data_bespeak");
                mOrderPerson = new float[dataBespeak.length()];
                for (int i = 0; i < dataBespeak.length(); i++) {
                    mOrderPerson[i] = dataBespeak.optInt(i);
                }
            }
            if (object.has("data_enter")) {
                JSONArray dataEnter = object.optJSONArray("data_enter");
                mInLibraryPerson = new float[dataEnter.length()];
                for (int i = 0; i < dataEnter.length(); i++) {
                    mInLibraryPerson[i] = dataEnter.optInt(i);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setLineBarVisibility(int isShow1, int isShow2) {
        mLinearLineChart.setVisibility(isShow1);
        mLinearBarChart.setVisibility(isShow2);
    }

    @Override
    public void update(int mYear, int mMonth, int mDay) {

        StringBuilder stringBuilder = new StringBuilder().append(mYear)
                .append("-")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
                .append("-").append((mDay < 10) ? "0" + mDay : mDay);

        mInputStartDay.setText(stringBuilder.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MySubject.getInstance().del(this);
    }
}
