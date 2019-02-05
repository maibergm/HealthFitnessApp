package com.example.maxim.diabetesireland;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private String [] titles ={"Profile","Exercise","Home","Diet","Graphs"};
    private TriangleFragment mFragTriangle;
    private FoodIntakeFragment mFragFood;
    private ProfileFragment mProfile;
    private ExerciseFragment mExer;
    private WeeklyUpdateFragment mGraphs;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.mFragTriangle = new TriangleFragment();
        this.mFragFood = new FoodIntakeFragment();
        this.mExer = new ExerciseFragment();
        this.mProfile =new ProfileFragment();
        this.mGraphs = new WeeklyUpdateFragment();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
     switch(position){
         case 0:
             return mProfile;
         case 1:
             return mExer;
         case 2:
             return mFragTriangle;
         case 3:
             return mFragFood;
         case 4:
             return mGraphs;
         default:
             return mFragTriangle;
     }
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

}