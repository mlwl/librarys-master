package com.minji.librarys.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.adapter.ReadingRoomAdapter;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;
import com.minji.librarys.bean.SeatDetail;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.ui.SelectAreasActivity;
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
 * Created by user on 2016/8/22.
 */
public class FragmentReadingRoom extends BaseFragment<SeatDetail> implements View.OnClickListener {


    private Window orderSeatWindow;
    private AlertDialog mOrderAlertDialog;
    private String mFloorId;
    private String mReadingId;
    private String mUserId;
    private List<SeatDetail> mSeatDetails;
    private String mResultString;
    private int mSeatId;
    private int mPositionOrderTure;
    private ReadingRoomAdapter readingRoomAdapter;
    private SelectAreasActivity selectAreasActivity;


    /*获取传递过来的数据*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        mFloorId = bundle.getString(StringsFiled.FLOORID);
        mReadingId = bundle.getString(StringsFiled.READINGROOMID);
        mUserId = SharedPreferencesUtil.getString(getActivity(), StringsFiled.USERID, "");
        System.out.println(mUserId + "-" + mFloorId + "-" + mReadingId);

        selectAreasActivity = (SelectAreasActivity) getActivity();
    }

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        requestSeatLists();

        return chat(mSeatDetails);
    }

    @Override
    protected View onCreateSuccessView() {

        View view = ViewsUitls.inflate(R.layout.layout_reading_room_seats);

        GridView gridView = (GridView) view.findViewById(R.id.gv_raeding_room_seats);

        gridView.setOverScrollMode(View.OVER_SCROLL_NEVER);// 取消顶部阴影
        readingRoomAdapter = new ReadingRoomAdapter(mSeatDetails);
        gridView.setAdapter(readingRoomAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mSeatDetails.get(position).getSeatStatus() == 0) {
                    mPositionOrderTure = position;
                    mSeatId = mSeatDetails.get(position).getSeatId();
                    showReadingRoomOrderSeatDialog();
                }
            }
        });

        return view;
    }

    /*点击可以预约的座位编号弹出对话框*/
    private void showReadingRoomOrderSeatDialog() {
        mOrderAlertDialog = new AlertDialog.Builder(getActivity()).create();
        mOrderAlertDialog.setView(new EditText(ViewsUitls.getContext()));// 为了让对话框内的输入框可以使用？
        WindowManager.LayoutParams attributes = mOrderAlertDialog.getWindow().getAttributes();// 获取对话框的属性集
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        attributes.width = (int) (d.getWidth() * 0.9);
        mOrderAlertDialog.show();
        // 设置对话框中自定义内容
        orderSeatWindow = mOrderAlertDialog.getWindow();
        orderSeatWindow.getDecorView().setBackgroundColor(Color.TRANSPARENT);// 获取对话框的根布局，设置其背景为透明，自定义对话框就没有白色的边框了
//        window.setBackgroundDrawableResource(R.drawable.shape_reading_seat_order_dialog_background_all);
        orderSeatWindow.setContentView(R.layout.dialog_select_reading_room_seat_order);

        initDialogView();

    }

    private void initDialogView() {
        ImageView mImageDialogCancel = (ImageView) orderSeatWindow.findViewById(R.id.iv_order_seat_dialog_cancel);
        mImageDialogCancel.setOnClickListener(this);
        TextView mCancelOrder = (TextView) orderSeatWindow.findViewById(R.id.tv_order_seat_dialog_cancel_order);
        mCancelOrder.setOnClickListener(this);
        TextView mMakeSureOrder = (TextView) orderSeatWindow.findViewById(R.id.tv_order_seat_dialog_make_sure_order);
        mMakeSureOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_order_seat_dialog_cancel:
            case R.id.tv_order_seat_dialog_cancel_order:
                // 上面两个case都是取消
                mOrderAlertDialog.cancel();
                break;
            case R.id.tv_order_seat_dialog_make_sure_order:
                requestIsOrderSuccess();
                break;
        }
    }

    /*请求网络是否能进行预约*/
    private void requestIsOrderSuccess() {
        selectAreasActivity.setLoadingVisibility(View.VISIBLE);
        selectAreasActivity.setIsInterruptTouch(true);

        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("userId", mUserId).add("seatId", mSeatId + "").build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address + IpFiled.IS_OK_ORDER)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        selectAreasActivity.setLoadingVisibility(View.GONE);
                        selectAreasActivity.setIsInterruptTouch(false);
                    }
                });
                System.out.println("=========================onFailure=============================");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                analysisOrderResult(response);
            }
        });
    }

    /*解析请求预约返回的结果*/
    private void analysisOrderResult(Response response) throws IOException {
        try {
            JSONObject object = new JSONObject(response.body().string());
            final boolean result = object.optBoolean("result");
            final String message = object.optString("message");
            ViewsUitls.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    selectAreasActivity.setLoadingVisibility(View.GONE);
                    selectAreasActivity.setIsInterruptTouch(false);
                    ToastUtil.showToast(ViewsUitls.getContext(), message);
                    if (result) {
                        mOrderAlertDialog.cancel();
                        mSeatDetails.get(mPositionOrderTure).setSeatStatus(1);
                        readingRoomAdapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*请求网络获取座位列表数据*/
    private void requestSeatLists() {
        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder().add("userid", mUserId).add("floorId", mFloorId).add("areaId", mReadingId).build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address + IpFiled.SEATS_DETAIL)
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                mResultString = response.body().string();
                Log.i("-FragmentReadingRoom-", mResultString);
                analysisJsonDate();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("=========================onFailure=============================");
            Log.i("-FragmentReadingRoom-", "okHttp is request error");
            mSeatDetails = null;
        }
    }

    private void analysisJsonDate() {
        try {
            JSONObject jsonObject = new JSONObject(mResultString);
            String seats = jsonObject.optString("seats");
            JSONArray jsonArray = new JSONArray(seats);
            mSeatDetails = null;
            mSeatDetails = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String seatName = object.optString("seatNum");
                int seatStatus = object.optInt("seatStatus");
                int seatId = object.optInt("id");
                SeatDetail seatDetail = new SeatDetail(seatName, seatStatus, seatId);
                mSeatDetails.add(seatDetail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
