package com.minji.librarys.fragment;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.ViewsUitls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 2016/8/24.
 */
public class FragmentUpdateInformation extends BaseFragment implements View.OnClickListener {


    private String mResultString;
    private ArrayList<Object> arrayList;
    private String mEmail;
    private String mMobileNumber;
    private String mStudentId;
    private String mUserName;
    private EditText mpPoneNumber;
    private EditText mEmailAddress;
    private Button mAlterInformation;
    private int alterOrMakeSure = 0;//  0是修改资料 1是确认修改

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        View inflate = ViewsUitls.inflate(R.layout.layout_my_cneter_bottom_update_information);

        TextView mStudyNumber = (TextView) inflate.findViewById(R.id.tv_update_information_item_study_number);
        mStudyNumber.setText(mStudentId);
        TextView mStudyName = (TextView) inflate.findViewById(R.id.tv_update_information_item_study_name);
        mStudyName.setText(mUserName);
        mpPoneNumber = (EditText) inflate.findViewById(R.id.et_update_information_item_phone_number);
        mpPoneNumber.setText(mMobileNumber);
        mEmailAddress = (EditText) inflate.findViewById(R.id.et_update_information_item_email_address);
        mEmailAddress.setText(mEmail);
        mAlterInformation = (Button) inflate.findViewById(R.id.bt_update_information_information_make_sure);
        mAlterInformation.setOnClickListener(this);

        return inflate;
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        // 用此集合去chat
        arrayList = new ArrayList<>();


        String mUserId = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.USERID, "");
        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody meInformationFormBody = new FormBody.Builder()
                .add("userid", mUserId).build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request meInformationRequest = new Request.Builder()
                .url(address + IpFiled.UPDATE_INFORMATION)
                .post(meInformationFormBody)
                .build();
        try {
            Response response = okHttpClient.newCall(meInformationRequest).execute();
            if (response.isSuccessful()) {
                mResultString = response.body().string();
                Log.i("asdfgh", mResultString);
                analysisJsonDate();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("=========================onFailure=============================");
            Log.i("asdfgh", "okHttp is request error");
            arrayList = null;
        }

        return chat(arrayList);
    }

    private void analysisJsonDate() {
        try {
            JSONObject object = new JSONObject(mResultString);
            mUserName = object.optString("userName");
            mStudentId = object.optString("studentId");
            mMobileNumber = object.optString("mobileNumber");
            mEmail = object.optString("email");
            arrayList.add("");// 添加一个数据来走显示页面
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_update_information_information_make_sure:

                if (alterOrMakeSure == 0) {
                    System.out.println("变为确认修改");
                    alterViewTextColorEnable("确认修改", R.color.black, true);
                    alterOrMakeSure = 1;
                } else if (alterOrMakeSure == 1) {
                    System.out.println("变为修改资料");
                    alterViewTextColorEnable("修改资料", R.color.hint_color, false);
                    alterOrMakeSure = 0;
                }


                break;
        }
    }

    private void alterViewTextColorEnable(String text, int color, boolean enable) {
        mAlterInformation.setText(text);
        mEmailAddress.setTextColor(ContextCompat.getColor(getActivity(), color));
        mEmailAddress.setEnabled(enable);
        mpPoneNumber.setTextColor(ContextCompat.getColor(getActivity(), color));
        mpPoneNumber.setEnabled(enable);
    }
}
