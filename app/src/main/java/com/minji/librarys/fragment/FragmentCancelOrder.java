package com.minji.librarys.fragment;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.minji.librarys.IpFiled;
import com.minji.librarys.ObserverTag;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.adapter.CancelOrderAdapter;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;
import com.minji.librarys.bean.CancelOrderDetail;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.observer.Observers;
import com.minji.librarys.ui.CancelOrderActivity;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.ToastUtil;
import com.minji.librarys.uitls.ViewsUitls;

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
public class FragmentCancelOrder extends BaseFragment<CancelOrderDetail> implements Observers {

    private String mResultString;
    private List<CancelOrderDetail> cancelOrderList;
    private CancelOrderAdapter cancelOrderAdapter;
    private String mUserId;
    private int mCancelOrderBid;
    private int mCancelOrderPosition;
    private AlertDialog alertDialog;
    private CancelOrderActivity cancelOrderActivity;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {

        View view = ViewsUitls.inflate(R.layout.layout_normal_list);
        ListView listView = (ListView) view.findViewById(R.id.lv_normal_list);


        cancelOrderAdapter = new CancelOrderAdapter(cancelOrderList);
        listView.setAdapter(cancelOrderAdapter);


        return view;
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        cancelOrderActivity = (CancelOrderActivity) getActivity();

        requestOrderLists();
        return chat(cancelOrderList);
    }

    private void requestOrderLists() {


        mUserId = SharedPreferencesUtil.getString(getActivity(), StringsFiled.USERID, "");

        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("userid", mUserId).build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address + IpFiled.CANCEL_ORDER_LIST)
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
            cancelOrderList = null;
        }
    }

    private void analysisJsonDate() {

        try {
            JSONObject object = new JSONObject(mResultString);
            cancelOrderList = null;
            cancelOrderList = new ArrayList<>();
            if (!object.optString("message").equals("无可用的信息")) {
                cancelOrderList.add(new CancelOrderDetail(object.optString("name"), object.optInt("bid"), object.optString("time"), object.optString("status")));
            }
            System.out.println();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void update(int mPosition, int distinguishBeNotified, int cancelOrderBid) {

        if (distinguishBeNotified == ObserverTag.ItemCancelOrderButton) {

            mCancelOrderPosition = mPosition;
            mCancelOrderBid = cancelOrderBid;
            System.out.println("mPosition: " + mPosition);
            System.out.println("cancelOrderBid: " + cancelOrderBid);

            showCancelOrderDialog();
        }

    }

    private void showCancelOrderDialog() {
        alertDialog = new AlertDialog.Builder(cancelOrderActivity).create();
        alertDialog.setView(new EditText(ViewsUitls.getContext()));// 为了让对话框内的输入框可以使用？
        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();// 获取对话框的属性集
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        attributes.width = (int) (d.getWidth() * 0.9);
        alertDialog.show();
        // 设置对话框中自定义内容
        Window window = alertDialog.getWindow();
        window.getDecorView().setBackgroundColor(Color.TRANSPARENT);// 获取对话框的根布局，设置其背景为透明，自定义对话框就没有白色的边框了
        window.setContentView(R.layout.dialog_select_reading_room_seat_order);
        TextView dialogTitle = (TextView) window.findViewById(R.id.tv_dialog_top_title);
        dialogTitle.setText("操作提示");
        TextView dialogContetn = (TextView) window.findViewById(R.id.tv_order_seat_dialog_people);
        dialogContetn.setText("您确认要取消吗？");

        window.findViewById(R.id.iv_order_seat_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        TextView cancel = (TextView) window.findViewById(R.id.tv_order_seat_dialog_cancel_order);
        cancel.setText("再看看");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        TextView makeSure = (TextView) window.findViewById(R.id.tv_order_seat_dialog_make_sure_order);
        makeSure.setText("取消预约");
        makeSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCancelOrder();
            }
        });
    }

    private void requestCancelOrder() {

        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("userid", mUserId).add("bid", mCancelOrderBid + "").build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address + IpFiled.IS_CENCEL_ORDER)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
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
                        if (!result.contains("html")) {
                            try {
                                final JSONObject object = new JSONObject(result);

                                if (object.optBoolean("result")) {// 取消成功
                                    ToastUtil.showToast(ViewsUitls.getContext(), object.optString("message"));
                                    cancelOrderList.remove(mCancelOrderPosition);
                                    cancelOrderAdapter.notifyDataSetChanged();
                                    alertDialog.cancel();
                                } else {// 取消失败
                                    ToastUtil.showToast(ViewsUitls.getContext(), "取消失败");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtil.showToast(getActivity(), "服务器异常，请稍候");
                        }
                    }
                });
            }
        });
    }

}
