package com.minji.librarys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;

/**
 * Created by user on 2016/8/1.
 */
public class FragmentPhoneOrEmailFind extends Fragment {


    private String emailOrForget;
    private EditText mRegisterPhoneOrEmail;
    private Button mAffirmFindPassWord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            emailOrForget = bundle.getString(StringsFiled.EMAILORFORGET);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_forget_email_or_phone_find, null);
        mRegisterPhoneOrEmail = (EditText) view.findViewById(R.id.et_forget_password_find);
        mAffirmFindPassWord = (Button) view.findViewById(R.id.bt_forget_password_find_affirm);
        if (StringsFiled.EMAILORFORGETVALUE_PHONE.equals(emailOrForget)) {
            mRegisterPhoneOrEmail.setHint("请输入注册手机号");
        } else if (StringsFiled.EMAILORFORGETVALUE_EMAIL.equals(emailOrForget)) {
            mRegisterPhoneOrEmail.setHint("请输入注册邮箱");
        }
        return view;
    }
}
