package com.minji.librarys;

import android.support.v4.app.Fragment;

import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.fragment.FragmentHistoryIntegral;
import com.minji.librarys.fragment.FragmentInLibraryStatistics;
import com.minji.librarys.fragment.FragmentOccupancyStatistics;
import com.minji.librarys.fragment.FragmentTodayIntegral;
import com.minji.librarys.observer.MySubject;


public class FragmentFactory {

    public static BaseFragment[] fragments = new BaseFragment[2];

    public static Fragment create(int position, String differentiate) {
        BaseFragment fragment = null;

        if ("IntegralDetail".equals(differentiate)) {
            switch (position) {
                case 0:
                    fragment = new FragmentTodayIntegral();
                    break;
                case 1:
                    fragment = new FragmentHistoryIntegral();
                    break;
            }
        } else if ("StatementDetail".equals(differentiate)) {
            switch (position) {
                case 0:
                    FragmentInLibraryStatistics fragmentInLibraryStatistics = new FragmentInLibraryStatistics();
                    MySubject.getInstance().add(fragmentInLibraryStatistics);
                    fragment = fragmentInLibraryStatistics;
                    break;
                case 1:
                    fragment = new FragmentOccupancyStatistics();
                    break;
            }
        }

        fragments[position] = fragment;

        return fragment;
    }
}
