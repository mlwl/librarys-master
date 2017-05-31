package com.minji.librarys.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minji.librarys.R;

/**
 * Created by user on 2016/7/28.
 */
public class MainItem extends LinearLayout {
    private View view;
    private String mItemChinese;
    private String mItemEnglish;
    private int mItemImage;
    private int mItemBackGround;

    public MainItem(Context context) {
        super(context);
    }

    public MainItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 获取布局总控件
        view = View.inflate(context, R.layout.itme_main, this);
        // 获取资源属性集合
        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.MainItem);
        // 获取设置的资源
        mItemChinese = attributes.getString(R.styleable.MainItem_itemChinese);
        mItemEnglish = attributes.getString(R.styleable.MainItem_itemEnglish);
        mItemImage = attributes.getResourceId(R.styleable.MainItem_itmeIamge, 0);
        mItemBackGround = attributes.getResourceId(R.styleable.MainItem_itemmainbackground, 0);
        attributes.recycle();// 最后提交

        initView();
    }


    public MainItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView() {

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_main_item_top_iamge);
        imageView.setImageResource(mItemImage);
        TextView textChinese = (TextView) view.findViewById(R.id.tv_main_item_middle_chinese_text);
        textChinese.setText(mItemChinese);
        TextView textEnglish = (TextView) view.findViewById(R.id.tv_main_item_buttom_english_text);
        textEnglish.setText(mItemEnglish);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_main_item);
        linearLayout.setBackgroundResource(mItemBackGround);

    }


}
