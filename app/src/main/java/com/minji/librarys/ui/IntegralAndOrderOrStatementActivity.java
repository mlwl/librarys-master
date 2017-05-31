package com.minji.librarys.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.DatePicker;

import com.minji.librarys.R;
import com.minji.librarys.base.BaseActivity;
import com.minji.librarys.fragment.FragmentIntegralDetail;
import com.minji.librarys.fragment.FragmentMyOrder;
import com.minji.librarys.fragment.FragmentOrderBookingRules;
import com.minji.librarys.fragment.FragmentOrderVoucher;
import com.minji.librarys.fragment.FragmentStatementDetail;
import com.minji.librarys.fragment.FragmentUpdateInformation;
import com.minji.librarys.fragment.FragmentWindowInformation;
import com.minji.librarys.observer.MySubject;

import java.util.Calendar;

/**
 * Created by user on 2016/8/29.
 */
public class IntegralAndOrderOrStatementActivity extends BaseActivity {

    @Override
    public void onCreateContent() {

        setBackVisibility(View.VISIBLE);
        setSettingVisibility(View.GONE);
        setTitleVisibility(View.VISIBLE);
        setTitleImageVisibility(View.GONE);

        if (stringTitle.equals("积分明细")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base_content, new FragmentIntegralDetail()).commit();
        }
        if (stringTitle.equals("我的预约")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base_content, new FragmentMyOrder()).commit();
        }
        if (stringTitle.equals("我的资料")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base_content, new FragmentUpdateInformation()).commit();
        }
        if (stringTitle.equals("系统消息")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base_content, new FragmentWindowInformation()).commit();
        }
        if (stringTitle.equals("预约凭证")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base_content, new FragmentOrderVoucher()).commit();
        }
        if (stringTitle.equals("报表统计")) {
            initCalendar();
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base_content, new FragmentStatementDetail()).commit();
        }
        if (stringTitle.equals("预约规则")) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_base_content, new FragmentOrderBookingRules()).commit();
        }
    }

    /*========================================以下都是弹出系统自带的时间选择对话框================================================*/
    private static final int DATE_DIALOG_ID = 1;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                System.out.println("onCreateDialog");
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                System.out.println("onPrepareDialog");
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }

    private int mYear;
    private int mMonth;
    private int mDay;
    /**
     * 日期控件的事件
     */
    public DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            System.out.println("onDateSet");
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDateDisplay();
        }
    };

    /**
     * 更新日期显示
     */
    private void updateDateDisplay() {
        System.out.println("updateDateDisplay");
        compareTime();
        MySubject.getInstance().operation(mYear, mMonth, mDay);
    }

    /**
     * 比较大小，使得选择的时间不大于现在的时间
     */
    private void compareTime() {
        final Calendar c = Calendar.getInstance();
        int nowYear = c.get(Calendar.YEAR);
        int nowMonth = c.get(Calendar.MONTH);
        int nowDay = c.get(Calendar.DAY_OF_MONTH);

        if (nowYear <= mYear) {
            mYear = nowYear;
            if (nowMonth <= mMonth) {
                mMonth = nowMonth;
                if (nowDay <= mDay) {
                    mDay = nowDay;
                }
            }
        }
    }

    /**
     * 获取现在的日期,使得在弹出修改时间的对话框时，一开就显示对应的时间
     */
    private void initCalendar() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }
}
