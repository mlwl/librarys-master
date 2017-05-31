package com.minji.librarys.fragment;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.adapter.WindowInformationAdapter;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;
import com.minji.librarys.bean.InformationDetail;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.ui.IntegralAndOrderOrStatementActivity;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.ToastUtil;
import com.minji.librarys.uitls.ViewsUitls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 2016/9/9.
 */
public class FragmentWindowInformation extends BaseFragment {

    private String mResultString;
    private List<InformationDetail> informationList;
    private String mUserId;
    private IntegralAndOrderOrStatementActivity activity;
    private String address;
    private OkHttpClient okHttpClient;
    private String mMessageTitle;
    private String mMessageContent;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {

        activity = (IntegralAndOrderOrStatementActivity) getActivity();

        View view = ViewsUitls.inflate(R.layout.layout_normal_list);
        ListView listView = (ListView) view.findViewById(R.id.lv_normal_list);

        WindowInformationAdapter windowInformationAdapter = new WindowInformationAdapter(informationList);
        listView.setAdapter(windowInformationAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                requestMessageContent(informationList.get(position).getId(), informationList.get(position).getIsread());
            }
        });

        return view;
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        requestInformationList();

        return chat(informationList);
    }

    private void requestInformationList() {

        mUserId = SharedPreferencesUtil.getString(getActivity(), StringsFiled.USERID, "");

        okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("userid", mUserId).build();

        address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address + IpFiled.WINDOW_INFORMATION_LIST)
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                mResultString = response.body().string();
                Log.i("asdfgh", mResultString);
                analysisJsonDate();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("=========================onFailure=============================");
            Log.i("asdfgh", "okHttp is request error");
            informationList = null;
        }

    }

    private void analysisJsonDate() {
        try {
            JSONArray jsonArray = new JSONArray(mResultString);
            informationList = null;
            informationList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                informationList.add(new InformationDetail(jsonObject.optInt("id"), jsonObject.optInt("isread"), jsonObject.optString("title"), jsonObject.optString("updateTime")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void requestMessageContent(int id, int isRead) {
        activity.setLoadingVisibility(View.VISIBLE);
        activity.setIsInterruptTouch(true);

        RequestBody formBody = new FormBody.Builder().add("id", id + "").add("isread", isRead + "").build();
        Request request = new Request.Builder()
                .url(address + IpFiled.WINDOW_INFORMATION_CONTENT)
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
                            analysisMessageContent(result);
                            showWindowMessageDialog();
                        } else {
                            ToastUtil.showToast(getActivity(), "服务器异常，请稍候");
                        }
                    }
                });
            }
        });
    }

    private void analysisMessageContent(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has("title")) {
                mMessageTitle = jsonObject.optString("title");
            }
            if (jsonObject.has("notice")) {
                mMessageContent = jsonObject.optString("notice");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void showWindowMessageDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();// 获取对话框的属性集
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        attributes.width = (int) (d.getWidth() * 0.9);
        dialog.show();
        // 设置对话框中自定义内容
        Window window = dialog.getWindow();
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);// 获取对话框的根布局，设置其背景为透明，自定义对话框就没有白色的边框了
        window.setContentView(R.layout.dialog_window_message_content);

        TextView dialogTitle = (TextView) window.findViewById(R.id.tv_dialog_top_title);
        dialogTitle.setText(mMessageTitle);
        TextView dialogContent = (TextView) window.findViewById(R.id.tv_window_message_content);
        dialogContent.setText(mMessageContent);
        ImageView cancel = (ImageView) window.findViewById(R.id.iv_dialog_right_top_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }
}
