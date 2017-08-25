package ali.asad.expenserecorder.Status;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ali.asad.expenserecorder.MainActivity;
import ali.asad.expenserecorder.R;


/**
 * Created by AsadAli on 18-Jul-17.
 */

public class StatusTabFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static View rootView;
    TabLayout.Tab tab;

    static ArrayAdapter<String> adpYear;

    List<String> listYear;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Yearly Summmary");
         /*
         * Making lists for spinners of status and yearly summary fragments
         * These lists will be refreshed every time this fragment is created
          */

        listYear = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);

        listYear.add("" + currentYear);

        StatusDBhelper db=new StatusDBhelper(getActivity());
        db.deleteEmptyRows();
        List<String> yearList =db.getAllYears();
        for (int i = 0; i < yearList.size(); i++) {
            String year= yearList.get(i);
            if (YearNotAlready(year))
                listYear.add(year);
        }

        adpYear = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_layout_text, listYear);

        if (MainActivity.first) {
            rootView = inflater.inflate(R.layout.status_fragment, container, false);
            tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
            viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
            viewPager.setAdapter(MainActivity.viewPagerAdapter);
            /*
            * This will create yearly summary fragments and add them to the tabbed layout
            * They will need to be created only once for the first time
             */
            tabLayout.setupWithViewPager(viewPager);
            tabLayout.setScrollPosition(0,0f,true);
            viewPager.setCurrentItem(0);
        }
        else{
            /*
            * All the other times when this fragment is created we only
            * set the changed data to the fields and spinners of the tabbed fragments
             */
            YearlySummaryFragment.year.setAdapter(adpYear);
            YearlyExpenseFragment.year.setAdapter(adpYear);
            YearlyRunningFragment.year.setAdapter(adpYear);
        }
        MainActivity.first = false;
        return rootView;
    }

    private boolean YearNotAlready(String year) {
        for (int i = 0; i < listYear.size(); i++) {
            if (listYear.get(i).equals(year))
                return false;
        }
        return true;
    }

}
