package com.minji.librarys.ui;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.base.BaseActivity;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.SystemTime;
import com.minji.librarys.uitls.ViewsUitls;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTimeNumber;
    private TextView mWeek;
    private Timer timer;
    private TimerTask timerTask;
    private int mRoleId;

    @Override
    public void onCreateContent() {

        setBackVisibility(View.GONE);
        setSettingVisibility(View.GONE);
        setTitleVisibility(View.GONE);
        setTitleImageVisibility(View.VISIBLE);

        mRoleId = SharedPreferencesUtil.getint(getApplicationContext(), StringsFiled.ROLEID, -1);

        View view = setContent(R.layout.activity_main);

        initChildView(view);

    }

    private void initChildView(View view) {
        view.findViewById(R.id.mi_main_outside_reservation).setOnClickListener(this);
        view.findViewById(R.id.mi_main_booking_rules).setOnClickListener(this);
        view.findViewById(R.id.mi_main_me_center).setOnClickListener(this);
        view.findViewById(R.id.mi_main_serach_seat).setOnClickListener(this);
        view.findViewById(R.id.mi_main_cancel_order).setOnClickListener(this);
        view.findViewById(R.id.mi_title_setting).setOnClickListener(this);

        showRunTimeOrStatementStatistics(view);

    }

    private void showRunTimeOrStatementStatistics(View view) {
        View runTime = view.findViewById(R.id.ll_main_run_time);
        View statementStatistics = view.findViewById(R.id.mi_main_statement_statistics);
        statementStatistics.setOnClickListener(this);
        if (mRoleId == 1) {// 显示统计报表
            runTime.setVisibility(View.GONE);
            statementStatistics.setVisibility(View.VISIBLE);
        } else if (mRoleId == 2) {// 显示时间
            runTime.setVisibility(View.VISIBLE);
            statementStatistics.setVisibility(View.GONE);

            mTimeNumber = (TextView) view.findViewById(R.id.tv_main_item_tiem_number);
            mTimeNumber.setText(SystemTime.getTimer());
            mWeek = (TextView) view.findViewById(R.id.tv_main_item_tiem_week);
            mWeek.setText(SystemTime.getTimerWeek());
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mi_main_outside_reservation:
                skipToActivity(SelectAreasActivity.class, "选择阅览室");
                break;
            case R.id.mi_main_me_center:
                skipToActivity(MyCentersActivity.class, "个人中心");
                break;
            case R.id.mi_main_serach_seat:
                skipToActivity(SearchSeatActivity.class, "查询座位");
                break;
            case R.id.mi_main_booking_rules:
                skipToActivity(IntegralAndOrderOrStatementActivity.class, "预约规则");
                break;
            case R.id.mi_main_cancel_order:
                skipToActivity(CancelOrderActivity.class, "取消预约");
                break;
            case R.id.mi_title_setting:
                break;
            case R.id.mi_main_statement_statistics:
                skipToActivity(IntegralAndOrderOrStatementActivity.class, "报表统计");
                break;
        }

    }

    private void skipToActivity(Class<?> cls, String title) {
        Intent intent = new Intent(ViewsUitls.getContext(), cls);
        intent.putExtra(StringsFiled.ACTIVITY_TITLE, title);
        startActivity(intent);
    }


    @Override
    protected void onStop() {
        stopTimer();
        super.onStop();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
        timerTask = null;
    }

    @Override
    protected void onStart() {
        // TODO 开启计时器前先判断是否要显示时间
        if (mRoleId == 2) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    ViewsUitls.runInMainThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("更新时间");
                            mTimeNumber.setText(SystemTime.getTimer());
                            mWeek.setText(SystemTime.getTimerWeek());
                        }
                    });
                }
            };
            timer.schedule(timerTask, 0, 45000);
        }
        super.onStart();
    }
}
