package com.minji.librarys.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.base.BaseActivity;
import com.minji.librarys.fragment.FragmentPhoneOrEmailFind;
import com.minji.librarys.uitls.StringUtils;
import com.minji.librarys.uitls.ToastUtil;
import com.minji.librarys.uitls.ViewsUitls;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterAndForgetActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_provisions_of_height;
    private Button mGetIdentifyingCode;
    private TextView mEmailFind;
    private TextView mPhoneFind;

    private int isClickEmailFind = 0;
    private EditText mRegisterPhone;

    private int mSMSSDKInterval = 60;
    private Timer mSMSSDKIntervalTimer;

    @Override
    public void onCreateContent() {

        setBackVisibility(View.GONE);
        setSettingVisibility(View.GONE);
        setTitleVisibility(View.GONE);
        setTitleImageVisibility(View.VISIBLE);


        Intent intent = getIntent();
        // 具体加载哪个界面
        String distinguishRegisterForget = intent.getStringExtra(StringsFiled.DISTINGUISH_REGISTER_FORGET);
        if (StringsFiled.REGISTER_FRAGMENT.equals(distinguishRegisterForget)) {
            // 注册前需要初始化下短信验证SDK
            SMSSDK.initSDK(this, StringsFiled.APPKEY, StringsFiled.APPSECRET);
            SMSSDK.registerEventHandler(eventHandler); //注册短信回调
            // 注册界面
            setRegisterContent();
        } else if (StringsFiled.FORGET_FRAGMENT.equals(distinguishRegisterForget)) {
            // 密码找回界面
            setForgetContent();
            startForgetContent(StringsFiled.EMAILORFORGETVALUE_PHONE);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            // 密码找回
            case R.id.tv_froget_tab_email_find:
                isClickEmailFind++;
                if (isClickEmailFind == 1) {
                    clickTabs(R.color.thin_green, R.drawable.tabs_background_press, R.color.white, R.drawable.tabs_background_default);
                    startForgetContent(StringsFiled.EMAILORFORGETVALUE_EMAIL);
                }
                break;
            case R.id.tv_froget_tab_phone_find:
                if (isClickEmailFind != 0) {
                    clickTabs(R.color.white, R.drawable.tabs_background_default, R.color.thin_green, R.drawable.tabs_background_press);
                    startForgetContent(StringsFiled.EMAILORFORGETVALUE_PHONE);
                    isClickEmailFind = 0;
                }
                break;
            // 注册
            case R.id.bt_register_identifying_code_button:
                String phoneNum = mRegisterPhone.getText().toString().trim();
                //发送短信，传入国家号和电话---使用SMSSDK核心类之前一定要在MyApplication中初始化，否侧不能使用
                if (StringUtils.isEmpty(phoneNum)) {
                    ToastUtil.showToast(this, "号码不能为空！");
                } else {
                    SMSSDK.getVerificationCode("+86", phoneNum);
                    ToastUtil.showToast(this, "发送成功:" + phoneNum);
                    setSMSSDKInterval();
                }
                break;
        }
    }

    // 开启找回密码具体的Fragment
    private void startForgetContent(String emailOrForget) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString(StringsFiled.EMAILORFORGET, emailOrForget);

        FragmentPhoneOrEmailFind fragmentPhoneOrEmailFind = new FragmentPhoneOrEmailFind();
        fragmentPhoneOrEmailFind.setArguments(bundle);

        fragmentTransaction.replace(R.id.fl_forget_email_or_phone, fragmentPhoneOrEmailFind);
        fragmentTransaction.commit();
    }

    // 密码找回界面
    private void setForgetContent() {
        View mViewForget = setContent(R.layout.layout_forget);
        mEmailFind = (TextView) mViewForget.findViewById(R.id.tv_froget_tab_email_find);
        mPhoneFind = (TextView) mViewForget.findViewById(R.id.tv_froget_tab_phone_find);
        mEmailFind.setOnClickListener(this);
        mPhoneFind.setOnClickListener(this);
    }

    /*
     *  alpha: email文字颜色
     *  alpha2：phone文字颜色
     *  tabs_background：email的tab背景
     *  thin_green：phone的tab背景
     * */
    private void clickTabs(int alpha, int tabs_background, int alpha2, int thin_green) {
        mEmailFind.setTextColor(ContextCompat.getColor(RegisterAndForgetActivity.this, alpha));
        mEmailFind.setBackgroundResource(tabs_background);
        mPhoneFind.setTextColor(ContextCompat.getColor(RegisterAndForgetActivity.this, alpha2));
        mPhoneFind.setBackgroundResource(thin_green);
    }

    //  TODO 注册与密码找回分界线--------------------------------------------------------------------------------------------------------------------------------------

    // 注册界面
    private void setRegisterContent() {
        View mViewRegister = setContent(R.layout.layout_register);
        mRegisterPhone = (EditText) mViewRegister.findViewById(R.id.et_register_phone);
        ll_provisions_of_height = (LinearLayout) mViewRegister.findViewById(R.id.ll_provisions_of_height);
        mGetIdentifyingCode = (Button) mViewRegister.findViewById(R.id.bt_register_identifying_code_button);
        mGetIdentifyingCode.setOnClickListener(this);
        amendButtonHeight(mViewRegister);
    }

    /**
     * 使用代码修改控件的高度
     */
    private void amendButtonHeight(View mViewRegister) {
        if (ll_provisions_of_height != null && mGetIdentifyingCode != null) {
            ViewTreeObserver viewTreeObserver = ll_provisions_of_height.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ll_provisions_of_height.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    ViewGroup.LayoutParams layoutParams = mGetIdentifyingCode.getLayoutParams();
                    layoutParams.height = ll_provisions_of_height.getHeight();
                    mGetIdentifyingCode.setLayoutParams(layoutParams);
                }
            });
        }
    }

    // 获取短信验证码后动态设置按钮的变化
    private void setSMSSDKInterval() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mSMSSDKInterval != 0) {
                            mGetIdentifyingCode.setClickable(false);
                            mGetIdentifyingCode.setBackgroundResource(R.color.thin_gray);
                            mGetIdentifyingCode.setText(mSMSSDKInterval + "秒");
                            mSMSSDKInterval--;
                        } else {
                            mSMSSDKInterval = 60;
                            mGetIdentifyingCode.setClickable(true);
                            mGetIdentifyingCode.setBackgroundResource(R.color.thin_green);
                            mGetIdentifyingCode.setText("获取验证码");
                            mSMSSDKIntervalTimer.cancel();
                        }
                    }
                });
            }
        };
        mSMSSDKIntervalTimer = new Timer();
        mSMSSDKIntervalTimer.schedule(timerTask, 0, 1000);
    }

    // 短信SDK操作回调
    EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    System.out.println("获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
//                        ToastUtil.showToast(RegisterAndForgetActivity.this, "验证失败");
                        System.out.println("验证失败");
                    }
                });
                ((Throwable) data).printStackTrace();
            }
        }
    };


}
