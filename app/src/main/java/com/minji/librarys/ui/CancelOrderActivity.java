package com.minji.librarys.ui;

import android.view.View;

import com.minji.librarys.R;
import com.minji.librarys.base.BaseActivity;
import com.minji.librarys.fragment.FragmentCancelOrder;
import com.minji.librarys.observer.MySubject;

/**
 * Created by user on 2016/9/9.
 */
public class CancelOrderActivity extends BaseActivity{

    private FragmentCancelOrder fragmentCancelOrder;

    @Override
    public void onCreateContent() {

        // 对标题栏进行设置
        setBackVisibility(View.VISIBLE);
        setSettingVisibility(View.GONE);
        setTitleVisibility(View.VISIBLE);
        setTitleImageVisibility(View.GONE);

        fragmentCancelOrder = new FragmentCancelOrder();
        MySubject.getInstance().add(fragmentCancelOrder);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_base_content, fragmentCancelOrder).commit();

    }

    @Override
    protected void onDestroy() {
        MySubject.getInstance().del(fragmentCancelOrder);
        super.onDestroy();
    }
}
