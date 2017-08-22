package ali.asad.expenserecorder.Expenses;

import android.app.Fragment;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import ali.asad.expenserecorder.Status.StatusDBhelper;
import ali.asad.expenserecorder.OnSwipeTouchListener;
import ali.asad.expenserecorder.R;

/**
 * Created by AsadAli on 15-Jul-17.
 */

public class ExpenseShowFragment extends Fragment {
    ArrayList<HashMap<String, String>> list;
    ListView listView;
    Spinner month, year;
    View rootView;

    String months[] = {"All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    List<String> listMonth;
    List<String> listYear;

    List<String> deleteListID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.show_expense_fragment, container, false);
        getActivity().setTitle("Expenses");

        month = (Spinner) rootView.findViewById(R.id.monthSpinnerExpense);
        year = (Spinner) rootView.findViewById(R.id.yearSpinnerExpense);
        listMonth = new ArrayList<String>();
        listYear = new ArrayList<String>();
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH) + 1;//as jan=0 feb=1 mar=2 ...
        int currentYear = cal.get(Calendar.YEAR);


        listYear.add("" + currentYear);
        for (int i = 0; i < months.length; i++) {
            listMonth.add(months[i]);
        }

        ExpenseDBhelper db = new ExpenseDBhelper(getActivity());
        List<String> expenseList = db.getDateOfAllEntries();
        for (int i = 0; i < expenseList.size(); i++) {
            String date = expenseList.get(i);
            if (YearNotAlready(date))
                listYear.add(GetYearNumber(date));
        }

        ArrayAdapter<String> adpMonth = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.spinner_layout_text, listMonth);
        ArrayAdapter<String> adpYear = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.spinner_layout_text, listYear);

        month.setAdapter(adpMonth);
        month.setSelection(currentMonth);
        month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MakeList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        year.setAdapter(adpYear);
        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MakeList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        MakeList();
        listView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {

            public void onSwipeTop() {
                if (listView.getChildCount() == 0 ||
                        (listView.getLastVisiblePosition() == listView.getAdapter().getCount() - 1 &&
                                listView.getChildAt(listView.getChildCount() - 1).getBottom() <= listView.getHeight())) {
                    //It is scrolled all the way down
                    if (month.getSelectedItemPosition() + 1 < months.length) {
                        int newPosition = month.getSelectedItemPosition() + 1;
                        month.setSelection(newPosition);
                    } else
                        month.setSelection(0);

                }

            }

            public void onSwipeBottom() {
                if (listView.getChildCount() == 0 ||
                        (listView.getFirstVisiblePosition() == 0 &&
                                listView.getChildAt(0).getTop() >= 0)) {

                    if (month.getSelectedItemPosition() - 1 >= 0) {
                        int newPosition = month.getSelectedItemPosition() - 1;
                        month.setSelection(newPosition);
                    } else
                        month.setSelection(months.length - 1);
                }
            }


        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                System.out.println("Item # " + pos + " pressed");
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBoxExpense);
                TextView hidden = (TextView) view.findViewById(R.id.hiddenExpense);
                if (checkBox.isChecked()) {
                    LinearLayout.LayoutParams pram = new LinearLayout.LayoutParams(0, 0);
                    checkBox.setLayoutParams(pram);
                    checkBox.setChecked(false);
                    deleteListID.remove(hidden.getText().toString());//Removing clicked item from deleteArray
                } else {
                    LinearLayout.LayoutParams pram = new LinearLayout.LayoutParams(100, 100);
                    checkBox.setLayoutParams(pram);
                    checkBox.setChecked(true);
                    deleteListID.add(hidden.getText().toString());//Adding clicked item's id to deleteArray

                }
            }
        });
        return rootView;
    }

    public void MakeList() {
        listView = (ListView) rootView.findViewById(R.id.listViewExpense);
        list = new ArrayList<HashMap<String, String>>();

        System.out.println("ID Date Detail Amount Category");
        ExpenseDBhelper db = new ExpenseDBhelper(getActivity());
        List<String[]> expenseList;
        if (!month.getSelectedItem().toString().equals("All"))
            expenseList = db.getAllEntriesOf(GetMonthNumberFromMonth(month.getSelectedItem().toString()), year.getSelectedItem().toString());
        else
            expenseList = db.getAllEntriesOf(year.getSelectedItem().toString());

        for (int i = 0; i < expenseList.size(); i++) {
            String id = expenseList.get(i)[0];
            String date = expenseList.get(i)[1];
            String detail = expenseList.get(i)[2];
            String amount = expenseList.get(i)[3];
            String category = expenseList.get(i)[4];

            HashMap<String, String> temp = new HashMap<String, String>();//temp for listview
            temp.put(ExpenseListViewAdapter.FIRST_COLUMN, months[Integer.parseInt(GetMonthNumber(date))] + " " + GetDayNumber(date));
            temp.put(ExpenseListViewAdapter.SECOND_COLUMN, detail);
            temp.put(ExpenseListViewAdapter.THIRD_COLUMN, amount);
            temp.put(ExpenseListViewAdapter.FOURTH_COLUMN, category);
            temp.put(ExpenseListViewAdapter.FIFTH_COLUMN, id);

            list.add(temp);
            System.out.println(id + " " + date + " " + detail + " " + amount + " " + category);
        }

        ExpenseListViewAdapter adapter = new ExpenseListViewAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        deleteListID = new ArrayList<String>();
    }

    public void onClearExpenses() {
        ExpenseDBhelper db = new ExpenseDBhelper(getActivity());
        SQLiteDatabase dbWrite = db.getWritableDatabase();
        db.onUpgrade(dbWrite, 1, 1);

        StatusDBhelper dbHome = new StatusDBhelper(getActivity());
        List<String[]> homeList = dbHome.getAllEntries();
        for (int i = 0; i < homeList.size(); i++) {
            String month = homeList.get(i)[0];
            String year = homeList.get(i)[1];
            int starting = Integer.parseInt(homeList.get(i)[2]);
            int incomes = Integer.parseInt(homeList.get(i)[4]);
            int expenses = 0;
            int running = starting + incomes - expenses;
            int savings = incomes - expenses;
            int budget = Integer.parseInt(homeList.get(i)[7]);
            dbHome.insertOrAlterEntry(month, year, starting, running, incomes, expenses, savings, budget);
        }
        MakeList();
    }

    public void onClearSelectedExpenses() {
        int deleted = 0;

        for (int i = 0; i < deleteListID.size(); i++) {
            ExpenseDBhelper db = new ExpenseDBhelper(getActivity());
            int amount = Integer.parseInt(db.getAmountOf(Integer.parseInt(deleteListID.get(i))));
            String date = db.getDateOf(Integer.parseInt(deleteListID.get(i)));

            StatusDBhelper dbHome = new StatusDBhelper(getActivity());
            String month = GetMonthNumber(date);
            String year = GetYearNumber(date);
            int starting = dbHome.getStarting(month, year);
            int incomes = dbHome.getIncomes(month, year);
            int expenses = dbHome.getExpenses(month, year) - amount;
            int running = starting + incomes - expenses;
            int savings = incomes - expenses;
            int budget = dbHome.getBudget(month, year);
            dbHome.insertOrAlterEntry(month, year, starting, running, incomes, expenses, savings, budget);

            db.deleteEntry(Integer.parseInt(deleteListID.get(i)));
            deleted++;
        }
        System.out.println("Deleted Items: " + deleted);
        MakeList();
    }

    private boolean YearNotAlready(String date) {
        for (int i = 0; i < listYear.size(); i++) {
            if (listYear.get(i).equals(GetYearNumber(date)))
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

    public String GetMonthNumber(String fullDate) {
        String month = "";
        for (int i = 0; i < fullDate.length(); i++) {
            if (fullDate.charAt(i) == '-') {
                break;
            }
            month += fullDate.charAt(i);
        }
        return month;
    }

    public String GetDayNumber(String fullDate) {
        int count = 0;
        String day = "";
        for (int i = 0; i < fullDate.length(); i++) {
            if (fullDate.charAt(i) == '-') {
                count++;
                continue;
            }
            if (count == 1) {
                day += fullDate.charAt(i);
            }
        }
        return day;

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