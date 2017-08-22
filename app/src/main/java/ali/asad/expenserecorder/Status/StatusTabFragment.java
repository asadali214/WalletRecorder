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

import ali.asad.expenserecorder.Expenses.ExpenseDBhelper;
import ali.asad.expenserecorder.Incomes.IncomeDBhelper;
import ali.asad.expenserecorder.MainActivity;
import ali.asad.expenserecorder.R;


/**
 * Created by AsadAli on 18-Jul-17.
 */

public class StatusTabFragment extends Fragment {
    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static View rootView;

    public static ArrayAdapter<String> adpMonth;
    public static ArrayAdapter<String> adpYear;

    String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    List<String> listMonth;
    List<String> listYear;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Wallet Status");
         /*
         * Making lists for spinners of status and yearly summary fragments
         * These lists will be refreshed every time this fragment is created
          */
        listMonth = new ArrayList<String>();
        listYear = new ArrayList<String>();

        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);

        listYear.add("" + currentYear);
        for (int i = 0; i < months.length; i++) {
            listMonth.add(months[i]);
        }

        StatusDBhelper db=new StatusDBhelper(getActivity());
        db.deleteEmptyRows();
        List<String> yearList =db.getAllYears();
        for (int i = 0; i < yearList.size(); i++) {
            String year= yearList.get(i);

            if (YearNotAlready(year))
                listYear.add(year);
        }

        adpMonth = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_layout_text, listMonth);
        adpYear = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_layout_text, listYear);

        if (MainActivity.first) {
            rootView = inflater.inflate(R.layout.status_fragment, container, false);
            tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
            viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
            viewPager.setAdapter(MainActivity.viewPagerAdapter);
            /*
            * This will create the acount status fragment and yearly summary fragment and add them to the tabbed layout
            * They will need to be created only once for the first time
             */
            tabLayout.setupWithViewPager(viewPager);
        }
        else{
            /*
            * All the other times when this fragment is created we only
            * set the changed data to the fields and spinners of the tabbed fragments
             */
            AccountStatusFragment.month.setAdapter(adpMonth);
            AccountStatusFragment.month.setSelection(currentMonth);

            AccountStatusFragment.year.setAdapter(adpYear);
            YearlySummaryFragment.year.setAdapter(adpYear);

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
    public String GetMonthNumberFromMonth(String month) {
        String monthNum = "";
        for (int i = 0; i < months.length; i++) {
            if (month == months[i]) {
                if (i < 10)
                    monthNum = "0" + i;
                else
                    monthNum = "" + i;
            }
        }
        return monthNum;
    }
    public String GetYearNumber(String fullDate) {
        int count = 0;
        String year = "";
        for (int i = 0; i < fullDate.length(); i++) {
            if (fullDate.charAt(i) == '-') {
                count++;
                continue;
            }
            if (count == 2) {
                year += fullDate.charAt(i);
            }
        }
        return year;
    }

}
