package ali.asad.expenserecorder.Status;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by AsadAli on 03-Aug-17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public static ArrayList<Fragment> fragments;
    public static ArrayList<String>   titles;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments=new ArrayList<>();
        titles=new ArrayList<>();
        //make fragments and titles here and made them static so we can use Status and

    }

    public void addFragments(Fragment fragment, String title){
        fragments.add(fragment);
        titles.add(title);

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }


}
