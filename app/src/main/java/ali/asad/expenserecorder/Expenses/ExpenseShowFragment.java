package ali.asad.expenserecorder.Expenses;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import ali.asad.expenserecorder.OnSwipeTouchListener;
import ali.asad.expenserecorder.R;
import ali.asad.expenserecorder.Status.StatusDBhelper;

/**
 * Created by AsadAli on 15-Jul-17.
 */

public class ExpenseShowFragment extends Fragment {
    ArrayList<HashMap<String, String>> list;
    RecyclerView recyclerView;
    public static Spinner month, year;
    View rootView;

    String months[] = {"All", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    List<String> listMonth;
    List<String> listYear;

    static OnSwipeTouchListener expenseSwipeListener;
    static List<String> deleteListID;
    public static int currentYear = 0;
    public static int currentMonth;

    /*
    * onResume is called after onCreate View in fragments
     */
    @Override
    public void onResume() {
        super.onResume();
        month.setSelection(currentMonth);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.show_expense_fragment, container, false);
        getActivity().setTitle("Expenses");

        month = (Spinner) rootView.findViewById(R.id.monthSpinnerExpense);
        year = (Spinner) rootView.findViewById(R.id.yearSpinnerExpense);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.listViewExpense);
        System.out.println("Expense Show: month>" + currentMonth + " year>" + currentYear);

        /*
        * Listener for swipe event.
         */
        expenseSwipeListener = new OnSwipeTouchListener(getActivity()) {
            public void onSwipeLeft() {
                /*LinearLayoutManager lm =(LinearLayoutManager) recyclerView.getLayoutManager();
                System.out.println(lm.findLastCompletelyVisibleItemPosition());
                if (recyclerView.getChildCount() == 0 ||
                        (lm.findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1 &&
                                recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight())) {
                    //It is scrolled all the way down*/
                recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.swipe_to_left));
                if (month.getSelectedItemPosition() + 1 < months.length) {
                    int newPosition = month.getSelectedItemPosition() + 1;
                    month.setSelection(newPosition);
                } else
                    month.setSelection(0);

                //}
            }

            public void onSwipeRight() {
                /*LinearLayoutManager lm =(LinearLayoutManager) recyclerView.getLayoutManager();
                System.out.println(lm.findFirstCompletelyVisibleItemPosition());
                if (recyclerView.getChildCount() == 0 ||
                        (lm.findFirstCompletelyVisibleItemPosition() == 0 &&
                                recyclerView.getChildAt(0).getTop() >= 0)) {
                    //It is scrolled all the way up*/
                recyclerView.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.swipe_to_right));
                if (month.getSelectedItemPosition() - 1 >= 0) {
                    int newPosition = month.getSelectedItemPosition() - 1;
                    month.setSelection(newPosition);
                } else
                    month.setSelection(months.length - 1);

                //}
            }
        };

        /*
        * Listener for selection of spinners.
         */
        AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (getActivity().getTitle().equals("Expenses")) {
                    MakeList();
                }
                if (getActivity().getTitle().equals("Expenses By Category")) {
                    MakeListByCategories();
                }
                if (getActivity().getTitle().equals("Expenses By Date")) {
                    MakeListByDates();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        listMonth = new ArrayList<String>();
        listYear = new ArrayList<String>();

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
        month.setOnItemSelectedListener(selectedListener);

        year.setAdapter(adpYear);
        year.setOnItemSelectedListener(selectedListener);

        MakeList();
        return rootView;
    }

    public void MakeList() {
        getActivity().setTitle("Expenses");

        list = new ArrayList<HashMap<String, String>>();
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

            HashMap<String, String> temp = new HashMap<String, String>();//temp for recyclerView
            temp.put(ExpenseRecyclerViewAdapter.FIRST_COLUMN, months[Integer.parseInt(GetMonthNumber(date))] + " " + GetDayNumber(date));
            temp.put(ExpenseRecyclerViewAdapter.SECOND_COLUMN, detail);
            temp.put(ExpenseRecyclerViewAdapter.THIRD_COLUMN, amount);
            temp.put(ExpenseRecyclerViewAdapter.FOURTH_COLUMN, category);
            temp.put(ExpenseRecyclerViewAdapter.FIFTH_COLUMN, id);

            list.add(temp);
            //System.out.println(id + " " + date + " " + detail + " " + amount + " " + category);
        }

        ExpenseRecyclerViewAdapter adapter = new ExpenseRecyclerViewAdapter(getActivity(), list);
        recyclerView.setOnTouchListener(expenseSwipeListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        deleteListID = new ArrayList<String>();

    }

    public void MakeListByCategories() {
        getActivity().setTitle("Expenses By Category");

        list = new ArrayList<HashMap<String, String>>();
        ExpenseDBhelper db = new ExpenseDBhelper(getActivity());

        if (!month.getSelectedItem().toString().equals("All")) {//"All" is NOT selected
            List<String> categoriesList = db.getCategoriesOf(GetMonthNumberFromMonth(month.getSelectedItem().toString()),
                    year.getSelectedItem().toString());
            for (int i = 0; i < categoriesList.size(); i++) {
                String category = categoriesList.get(i);
                int expenses = db.getExpensesOf(category,
                        GetMonthNumberFromMonth(month.getSelectedItem().toString()),
                        year.getSelectedItem().toString());
                HashMap<String, String> temp = new HashMap<String, String>();//temp for recyclerView
                temp.put(ExpenseRecyclerViewAdapterCat.FIRST_COLUMN,category);
                temp.put(ExpenseRecyclerViewAdapterCat.SECOND_COLUMN,""+expenses);
                list.add(temp);
            }


        } else {//"All" is selected
            List<String> categoriesList = db.getCategoriesOfAllEntries();
            for (int i = 0; i < categoriesList.size(); i++) {
                String category = categoriesList.get(i);
                int expenses = db.getExpensesOf(category,
                        year.getSelectedItem().toString());
                HashMap<String, String> temp = new HashMap<String, String>();//temp for recyclerView
                temp.put(ExpenseRecyclerViewAdapterCat.FIRST_COLUMN,category);
                temp.put(ExpenseRecyclerViewAdapterCat.SECOND_COLUMN,""+expenses);
                list.add(temp);
            }

        }

        ExpenseRecyclerViewAdapterCat adapter = new ExpenseRecyclerViewAdapterCat(getActivity(), list);
        recyclerView.setOnTouchListener(expenseSwipeListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    public void MakeListByDates() {
        getActivity().setTitle("Expenses By Date");

        list = new ArrayList<HashMap<String, String>>();
        ExpenseDBhelper db = new ExpenseDBhelper(getActivity());

        if (!month.getSelectedItem().toString().equals("All")) {//"All" is NOT selected
            List<String> dateList = db.getDateOf(GetMonthNumberFromMonth(month.getSelectedItem().toString()),
                    year.getSelectedItem().toString());
            for (int i = 0; i < dateList.size(); i++) {
                String date = dateList.get(i);
                int expenses = db.getExpensesOf(date);
                HashMap<String, String> temp = new HashMap<String, String>();//temp for recyclerView
                temp.put(ExpenseRecyclerViewAdapterDate.FIRST_COLUMN,months[Integer.parseInt(GetMonthNumber(date))] + " " + GetDayNumber(date));
                temp.put(ExpenseRecyclerViewAdapterDate.SECOND_COLUMN,""+expenses);
                list.add(temp);
            }


        } else {//"All" is selected
            List<String> dateList = db.getDateOfAllEntries();
            for (int i = 0; i < dateList.size(); i++) {
                String date = dateList.get(i);
                int expenses = db.getExpensesOf(date);
                HashMap<String, String> temp = new HashMap<String, String>();//temp for recyclerView
                temp.put(ExpenseRecyclerViewAdapterDate.FIRST_COLUMN, months[Integer.parseInt(GetMonthNumber(date))] + " " + GetDayNumber(date));
                temp.put(ExpenseRecyclerViewAdapterDate.SECOND_COLUMN,""+expenses);
                list.add(temp);
            }

        }

        ExpenseRecyclerViewAdapterDate adapter = new ExpenseRecyclerViewAdapterDate(getActivity(), list);
        recyclerView.setOnTouchListener(expenseSwipeListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
