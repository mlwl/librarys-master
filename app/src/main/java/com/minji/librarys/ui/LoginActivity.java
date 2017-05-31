package com.minji.librarys.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.sqlite.MySQLiteOpenHelper;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.StringUtils;
import com.minji.librarys.uitls.ToastUtil;
import com.minji.librarys.uitls.ViewsUitls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    private EditText mEtUser;
    private EditText mEtPassWord;

    private TextView mTvRegister;
    private TextView mTvForget;

    private CheckBox mRbRemember;

    private Button mBLogin;
    private MySQLiteOpenHelper mySQLiteOpenHelper;
    private SQLiteDatabase writableDatabase;
    private boolean mIsAuto;
    private String mHistoryPassWard;
    private String mHistoryUser;
    private String mPassWord;
    private String mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        // 设置Ip地址
        SharedPreferencesUtil.saveStirng(getApplicationContext(), StringsFiled.IP_ADDRESS_PREFIX,
                "http://" + "192.168.1.40:8080");

        // 创建数据库操作对象
        mySQLiteOpenHelper = new MySQLiteOpenHelper(ViewsUitls.getContext());
        writableDatabase = mySQLiteOpenHelper.getWritableDatabase();

        // 初始化CLIENTID使之为空
        SharedPreferencesUtil.saveStirng(this, StringsFiled.CLIENTID, "");
        // TODO 初始化推送
        PushManager.getInstance().initialize(this.getApplicationContext());

        getDate();

        initView();

        if (mIsAuto) {
            login();
        }
    }


    /*获取数据库中存储的登录密码以及帐号和是否自动登录*/
    private void getDate() {
        mIsAuto = SharedPreferencesUtil.getboolean(ViewsUitls.getContext(),
                StringsFiled.IS_AUTO_LOGIN, false);

        		/*
         * 参数1:表名 参数2:要查询的字段 参数3:where表达式 参数4:替换?号的真实值 参数5:分组 null
		 * 参数6:having表达式null 参数7:排序规则 c_age desc
		 */
        Cursor cursor = writableDatabase.query("t_user",
                new String[]{"c_password"}, "c_pw>?", new String[]{"0"},
                null, null, null);
        while (cursor.moveToNext()) {
            mHistoryPassWard = cursor.getString(0);
        }
        cursor.close();

        mHistoryUser = SharedPreferencesUtil.getString(ViewsUitls.getContext(),
                StringsFiled.SAVE_USERNAME, "");
    }

    private void initView() {
        mEtPassWord = (EditText) findViewById(R.id.et_login_password);
        mEtUser = (EditText) findViewById(R.id.et_login_user);
        showEditHistoryText();

        mRbRemember = (CheckBox) findViewById(R.id.cb_login_remember_password);
        mRbRemember.setChecked(mIsAuto);

        mTvRegister = (TextView) findViewById(R.id.tv_login_register_user);
        mTvRegister.setOnClickListener(this);
        mTvForget = (TextView) findViewById(R.id.tv_login_forget_password);
        mTvForget.setOnClickListener(this);
        mBLogin = (Button) findViewById(R.id.bt_login_button);
        mBLogin.setOnClickListener(this);
    }

    // 根据帐号与密码的历史记录来设置显示
    private void showEditHistoryText() {
        if (!mHistoryUser.isEmpty() && !mHistoryPassWard.isEmpty()) {
            mEtUser.setText(mHistoryUser);
            mEtPassWord.setText(mHistoryPassWard);
        }
    }


    @Override
    public void onClick(View v) {

        Intent registerAndForget = new Intent(ViewsUitls.getContext(), RegisterAndForgetActivity.class);

        switch (v.getId()) {
            case R.id.tv_login_register_user:
                registerAndForget.putExtra(StringsFiled.DISTINGUISH_REGISTER_FORGET, StringsFiled.REGISTER_FRAGMENT);
                startActivity(registerAndForget);
                break;
            case R.id.tv_login_forget_password:
                registerAndForget.putExtra(StringsFiled.DISTINGUISH_REGISTER_FORGET, StringsFiled.FORGET_FRAGMENT);
                startActivity(registerAndForget);
                break;
            case R.id.bt_login_button:
                login();
                break;
        }
    }

    private void login() {
        mUser = mEtUser.getText().toString().trim();
        mPassWord = mEtPassWord.getText().toString().trim();
        String cid = SharedPreferencesUtil.getString(this, StringsFiled.CLIENTID, "");

        if (!StringUtils.isEmpty(cid)) {

            if (!StringUtils.isEmpty(mUser) && !StringUtils.isEmpty(mPassWord)) {
                System.out.println("userName: " + mUser + "  passWord: " + mPassWord + "  cid: " + cid);

                requestIsLoginSuccess(mUser, mPassWord, cid);

            } else {
                ToastUtil.showToast(this, "账户密码不可为空");
            }
        } else {
            ToastUtil.showToast(this, "cId:0x000000");
        }

    }

    /*请求网络是否登录成功*/
    private void requestIsLoginSuccess(String userName, String passWord, String cid) {
        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("username", userName).add("password", passWord).add("cid", cid).add("rememberMe", "false")
                .build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address + IpFiled.LOGIN)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("=========================onFailure=============================");
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(LoginActivity.this, "网络异常,请稍候");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string().toString();
                System.out.println(result + "      +++++++++++++++++++++++++++++++++");
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!result.contains("<html>")) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String message = jsonObject.optString("message");
                                if (result.contains("true")) {

                                    saveSuccessPassWardUserName();

                                    SharedPreferencesUtil.saveint(getApplicationContext(), StringsFiled.ROLEID, jsonObject.optInt("roleId"));
                                    SharedPreferencesUtil.saveStirng(getApplicationContext(), StringsFiled.USERID, jsonObject.optString("userId"));
                                    Intent mainActivity = new Intent(ViewsUitls.getContext(), MainActivity.class);
                                    startActivity(mainActivity);
                                    finish();
                                }
                                ToastUtil.showToast(LoginActivity.this, message);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.showToast(LoginActivity.this, "服务器异常,请稍候");
                        }
                    }
                });

            }
        });
    }

    /*当登录成功后需要将帐号密码进行保存*/
    private void saveSuccessPassWardUserName() {
        if (StringUtils.isEmpty(mHistoryPassWard)) {// 当数据库中没有保存过密码时需要第一次插入密码数据
            ContentValues values = new ContentValues();
            values.put("c_password", mPassWord);
            values.put("c_pw", 1);
            writableDatabase.insert("t_user", null, values);
        } else {// 修改数据
            if (!mHistoryPassWard.equals(mPassWord)) {// EditText中的密码与历史密码不一样
                ContentValues values = new ContentValues();
                values.put("c_password", mPassWord);
                writableDatabase.update("t_user", values, "c_pw>?",
                        new String[]{"0"});
            }
        }
        writableDatabase.close();
        mySQLiteOpenHelper.close();

        if (!mHistoryUser.equals(mUser)) {// EditText中的帐号与历史帐号不一样
            SharedPreferencesUtil.saveStirng(ViewsUitls.getContext(),
                    StringsFiled.SAVE_USERNAME, mUser);
        }
        SharedPreferencesUtil.saveboolean(ViewsUitls.getContext(),
                StringsFiled.IS_AUTO_LOGIN, mRbRemember.isChecked());
    }

}
