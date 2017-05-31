package com.minji.librarys.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.adapter.MyOrderListAdapter;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;
import com.minji.librarys.bean.MyOrderListDetail;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.ViewsUitls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by user on 2016/8/29.
 */
public class FragmentMyOrder extends BaseFragment<MyOrderListDetail> {

    private String mResultString;
    private List<MyOrderListDetail> myOrderLists;

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {

        View view = ViewsUitls.inflate(R.layout.layout_normal_list);
        ListView listView = (ListView) view.findViewById(R.id.lv_normal_list);

        MyOrderListAdapter myOrderListAdapter = new MyOrderListAdapter(myOrderLists);
        listView.setAdapter(myOrderListAdapter);

        return view;
    }

    @Override
    protected ContentPage.ResultState onLoad() {
        String mUserId = SharedPreferencesUtil.getString(ViewsUitls.getContext(), StringsFiled.USERID, "");
        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("userid", mUserId).build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address + IpFiled.ME_CENTER_MY_ORDER)
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
            myOrderLists = null;
        }


        return chat(myOrderLists);
    }

    private void analysisJsonDate() {
        try {
            JSONObject object = new JSONObject(mResultString);
            myOrderLists = new ArrayList<>();
            if (object.has("maps")) {
                JSONArray maps = object.optJSONArray("maps");
                for (int i = 0; i < maps.length(); i++) {
                    JSONObject jsonObject = maps.optJSONObject(i);
                    myOrderLists.add(new MyOrderListDetail(jsonObject.optString("name"), jsonObject.optString("time"), jsonObject.optString("status")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
