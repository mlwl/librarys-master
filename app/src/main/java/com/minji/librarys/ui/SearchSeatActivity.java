package com.minji.librarys.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.minji.librarys.FragmentTag;
import com.minji.librarys.IpFiled;
import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.base.BaseActivity;
import com.minji.librarys.bean.SearchHistory;
import com.minji.librarys.fragment.FragmentSearchHistory;
import com.minji.librarys.fragment.FragmentSearchResult;
import com.minji.librarys.http.OkHttpManger;
import com.minji.librarys.uitls.GsonTools;
import com.minji.librarys.uitls.SharedPreferencesUtil;
import com.minji.librarys.uitls.StringUtils;
import com.minji.librarys.uitls.ToastUtil;
import com.minji.librarys.uitls.ViewsUitls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchSeatActivity extends BaseActivity implements View.OnClickListener {

    private View view;

    private EditText mInputSearchSeat;
    private ImageView mCleanInputSearch;
    private TextView mSearchButton;

    private boolean mHistoryIsEqual;
    private FragmentSearchHistory mFragmentSearchHistory;
    private FragmentSearchResult mFragmentSearchResult;

    @Override
    public void onCreateContent() {

        setBackVisibility(View.VISIBLE);
        setSettingVisibility(View.GONE);
        setTitleVisibility(View.VISIBLE);
        setTitleImageVisibility(View.GONE);

        view = setContent(R.layout.layout_serach_seat);
        initSetContentView();

        // 设置清除输入与输入显示清除的监听
        setCleanAndInputListener();

        showFragmentWhenFirstOrSecond();

        mSearchButton.setOnClickListener(this);

    }

    private void showFragmentWhenFirstOrSecond() {
        if (savedInstanceState == null) {// 第一次进入该界面，需要通过add来进行展示Fragment
            System.out.println("第一次进入该界面，需要通过add来进行展示Fragment");
            showHistorySearchContentFirst();
        } else {
            System.out.println("第二次+++++++++++++++++++++++++++++");
            mFragmentSearchHistory = (FragmentSearchHistory) getSupportFragmentManager().findFragmentByTag(FragmentTag.SEARCH_HISTORY);
            mFragmentSearchResult = (FragmentSearchResult) getSupportFragmentManager().findFragmentByTag(FragmentTag.SEARCH_SEAT_DETAIL);
            /*
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (mFragmentSearchHistory != null && mFragmentSearchResult != null) {
                fragmentTransaction.hide(mFragmentSearchResult).show(mFragmentSearchHistory);
                mNowShowFragment = mFragmentSearchHistory;// 说明此时显示的Fragment是哪个
            } else if (mFragmentSearchHistory != null && mFragmentSearchResult == null) {
                fragmentTransaction.show(mFragmentSearchHistory);
                mNowShowFragment = mFragmentSearchHistory;// 说明此时显示的Fragment是哪个
            } else if (mFragmentSearchResult != null && mFragmentSearchHistory == null) {
                fragmentTransaction.show(mFragmentSearchResult);
                mNowShowFragment = mFragmentSearchResult;// 说明此时显示的Fragment是哪个
            }
            fragmentTransaction.commit();*/
        }
    }

    /*显示历史记录界面，即使是第一次显示也可以(第一次显示会自动实例化一个对象)*/
    private void showHistorySearchContentFirst() {
        String searchHistory = SharedPreferencesUtil.getString(this, StringsFiled.SEARCH_HISTORY, "");
        if (!StringUtils.isEmpty(searchHistory)) {
            if (mFragmentSearchHistory == null) {
                mFragmentSearchHistory = new FragmentSearchHistory();
            }
            nowShowSkipToWantFragment(R.id.fl_search_seat_bottom_history_or_search_result, mFragmentSearchHistory, FragmentTag.SEARCH_HISTORY);
        }
    }

    private void setCleanAndInputListener() {
        mCleanInputSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 当点击了清空输入，设置输入内容为""
                if (mCleanInputSearch.getVisibility() == View.VISIBLE) {
                    mInputSearchSeat.setText("");
                }
                // 当点击了清空输入后，且查询座位详情正在展示，则隐藏查询座位详情，而显示历史记录
                if (mFragmentSearchResult != null && !mFragmentSearchResult.isHidden()) {
                    showHistorySearchContentFirst();
                }
            }
        });
        mInputSearchSeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            // 根据输入内容是否为空来展示清空键
            @Override
            public void afterTextChanged(Editable s) {
                if (StringUtils.isEmpty(s.toString())) {// 输入为空那么要隐藏清除按钮，显示语音按钮
                    if (mCleanInputSearch.getVisibility() != View.INVISIBLE) {
                        mCleanInputSearch.setVisibility(View.INVISIBLE);
                    }
                } else {// 输入不为空那么要隐藏语音按钮，显示清除按钮
                    if (mCleanInputSearch.getVisibility() == View.INVISIBLE) {
                        mCleanInputSearch.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void initSetContentView() {

        mInputSearchSeat = (EditText) view.findViewById(R.id.et_input_search_seat);
        mCleanInputSearch = (ImageView) view.findViewById(R.id.iv_clean_input_search);
        mSearchButton = (TextView) view.findViewById(R.id.tv_search_seat_search_button);

    }

    public EditText getInputSearchSeat() {
        return mInputSearchSeat;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_seat_search_button:
                String mSearchContent = mInputSearchSeat.getText().toString().trim();
                if (!StringUtils.isEmpty(mSearchContent)) {
                    if (judgeFormat(mSearchContent)) {
                        requestSearchSeat(mSearchContent);
                    } else {
                        ToastUtil.showToast(this, "查询内容格式有误,请重新输入");
                        mInputSearchSeat.setText("");
                    }
                } else {
                    ToastUtil.showToast(this, "查询内容不可为空");
                    mInputSearchSeat.setText("");
                }
                break;
        }

    }

    /*查询座位请求网络*/
    public void requestSearchSeat(final String searchContent) {
        setIsInterruptTouch(true);
        setLoadingVisibility(View.VISIBLE);

        OkHttpClient okHttpClient = OkHttpManger.getInstance().getOkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("seat", searchContent).build();

        String address = SharedPreferencesUtil.getString(
                ViewsUitls.getContext(), StringsFiled.IP_ADDRESS_PREFIX, "");

        Request request = new Request.Builder()
                .url(address+ IpFiled.SEARCH_SEAT_ORDER_DETAIL)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        setIsInterruptTouch(false);
                        setLoadingVisibility(View.GONE);
                        ToastUtil.showToast(SearchSeatActivity.this, "查询座位信息失败!");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                ViewsUitls.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        analysisRequestSeatResult(result, searchContent);
                    }
                });
            }
        });
    }

    /*对查询座位请求网络获得数据进行解析*/
    private void analysisRequestSeatResult(String result, String searchContent) {
        try {
            System.out.println(result);
            JSONObject object = new JSONObject(result);
            if (object.has("result")) {
                if (object.optBoolean("result")) {
                    // 查询到具体信息
                    SharedPreferencesUtil.saveStirng(this, StringsFiled.SEARCH_JSON_RESULT, result);
                    SharedPreferencesUtil.saveStirng(this, StringsFiled.SEARCH_RESULT_SEAT_NUMBER, searchContent);
                    if (mFragmentSearchResult == null) {
                        mFragmentSearchResult = new FragmentSearchResult();
                    }
                    nowShowSkipToWantFragment(R.id.fl_search_seat_bottom_history_or_search_result, mFragmentSearchResult, FragmentTag.SEARCH_SEAT_DETAIL);
                    // 查询成功后将查询记录进行保存
                    updateSearchNote(searchContent);

                } else {
                    ToastUtil.showToast(SearchSeatActivity.this, object.optString("message"));
                }
            } else {
                ToastUtil.showToast(SearchSeatActivity.this, "查询座位信息失败!!");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setIsInterruptTouch(false);
        setLoadingVisibility(View.GONE);
    }

    /*当查询座位成功后将SharedPreferences中存储的json数据取出添加进去新的数据*/
    private void updateSearchNote(String searchContent) {
        String searchHistory = SharedPreferencesUtil.getString(this, StringsFiled.SEARCH_HISTORY, "");
        if (StringUtils.isEmpty(searchHistory)) {// 第一次查询成功
            List<SearchHistory> list = new ArrayList<>();
            list.add(new SearchHistory(searchContent));
            String gsonString = GsonTools.createGsonString(list);
            SharedPreferencesUtil.saveStirng(this, StringsFiled.SEARCH_HISTORY, gsonString);
        } else {// 已经有查询记录了
            List<SearchHistory> searchHistories = analysisGSONDate(searchHistory);
            // 先声明本次查询在查询记录里没有重复
            mHistoryIsEqual = false;
            for (SearchHistory history : searchHistories) {
                if (searchContent.equals(history.getSearchSeat())) {
                    mHistoryIsEqual = true;// 有重复了
                    return;
                }
            }
            if (!mHistoryIsEqual) {// 没有重复，需要进行写入记录
                searchHistories.add(new SearchHistory(searchContent));
                String gsonString = GsonTools.createGsonString(searchHistories);
                SharedPreferencesUtil.saveStirng(this, StringsFiled.SEARCH_HISTORY, gsonString);
            }
        }
    }

    /*解析存储在SharedPreferences中的json数据*/
    private List<SearchHistory> analysisGSONDate(String searchHistory) {
        List<SearchHistory> middleList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(searchHistory);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.optJSONObject(i);
                middleList.add(new SearchHistory(object.optString("searchSeat")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return middleList;
    }

    /*正则表达式匹配*/
    private boolean judgeFormat(String searchContent) {
        Pattern pattern = Pattern.compile("^[0-9]*$");
        Matcher isNum = pattern.matcher(searchContent);
        return isNum.matches();
    }


    /*只有当按下清空历史记录时调用*/
    public void hideSearchHistory() {
        getSupportFragmentManager().beginTransaction().hide(mFragmentSearchHistory).commit();
    }


    private Fragment mNowShowFragment;

    /*
    * containerViewId ：新add开启Fragment时填充的布局Id
    * wantSkipTo ：将要add开启或show显示的Fragment实例
    * wantSkipToTag ： 将要add开启或show显示的Fragment实例的Tag标志
    * */
    private void nowShowSkipToWantFragment(int containerViewId, Fragment wantSkipTo, String wantSkipToTag) {

        if (mNowShowFragment != wantSkipTo) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (!wantSkipTo.isAdded()) {    // 先判断是否被add过
                if (mNowShowFragment == null) {
                    fragmentTransaction.add(containerViewId, wantSkipTo, wantSkipToTag).commit();
                } else {
                    fragmentTransaction.hide(mNowShowFragment).add(containerViewId, wantSkipTo, wantSkipToTag).commit();
                }
            } else {
                if (mNowShowFragment == null) {
                    fragmentTransaction.show(wantSkipTo).commit();
                } else {
                    fragmentTransaction.hide(mNowShowFragment).show(wantSkipTo).commit();
                }// 隐藏当前的fragment，显示下一个
            }
            mNowShowFragment = wantSkipTo;
        } else {
            getSupportFragmentManager().beginTransaction().detach(mNowShowFragment).attach(mNowShowFragment).commit();
            mNowShowFragment = wantSkipTo;
        }
    }
}
