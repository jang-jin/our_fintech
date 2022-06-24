package com.example.david0926.templete;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter{

    int num;

    public TabAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Tab_People tab1 = new Tab_People();
                return tab1;
            case 1:
                Tab_Chat tab2 = new Tab_Chat();
                return tab2;
            case 2:
                Tab_Setting tab3 = new Tab_Setting();
                return tab3;

            default: return null;

        }
    }

    @Override
    public int getCount() {
        return num;
    }
}
