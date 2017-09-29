package ali.asad.expenserecorder.Home;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import ali.asad.expenserecorder.Expenses.ExpenseDBhelper;
import ali.asad.expenserecorder.Incomes.IncomeDBhelper;
import ali.asad.expenserecorder.R;
import ali.asad.expenserecorder.Status.StatusDBhelper;

/**
 * Created by AsadAli on 12-Jul-17.
 */

public class HomeFragment extends Fragment {

    TextView month, progressStatus, expenses;
    TextView budget;
    ProgressBar bar, barStarting, barRunning;
    Button ShowExpense, ShowIncome;
    TextView moreOrless, Starting, Running;
    ImageView moreOrlessImg;
    LinearLayout expandableLayout;

    int dp = 0;
    boolean loopOn = false;

    float progressStarting;
    float progressRunning;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        getActivity().setTitle("Wallet Recorder");
        month = (TextView) rootView.findViewById(R.id.TVmonth);
        progressStatus = (TextView) rootView.findViewById(R.id.TVprogressStatus);
        expenses = (TextView) rootView.findViewById(R.id.TVexpenses);
        budget = (TextView) rootView.findViewById(R.id.TVbudget);
        bar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        barStarting = (ProgressBar) rootView.findViewById(R.id.progressBarStarting);
        barRunning = (ProgressBar) rootView.findViewById(R.id.progressBarRunning);
        ShowExpense = (Button) rootView.findViewById(R.id.BShowexpense);
        ShowIncome = (Button) rootView.findViewById(R.id.BShowincome);
        Starting = (TextView) rootView.findViewById(R.id.TvStarting);
        Running = (TextView) rootView.findViewById(R.id.TvRunning);
        moreOrless = (TextView) rootView.findViewById(R.id.moreAndlessText);
        moreOrlessImg = (ImageView) rootView.findViewById(R.id.moreAndlessImage);
        moreOrlessImg.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
        expandableLayout = (LinearLayout) rootView.findViewById(R.id.Expandable_Layout);

        System.out.println(">DP = " + dp + " & LoopOn: " + loopOn);
        loopOn=false;//every time when fragment recreates itself we should stop the loop of ui thread
        if (dp >= 0 && dp < 75) {//if more slider is less than half way
            dp = 0;
            moreOrless.setText("More");
            moreOrlessImg.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
        }
        if (dp >= 75 && dp <= 150) {//if more slider is more than half way
            dp = 150;
            moreOrless.setText("Less");
            moreOrlessImg.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
        }
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px);
        param.gravity = Gravity.CENTER;
        expandableLayout.setLayoutParams(param);
        Refresh();


        return rootView;
    }

    public void Refresh() {
        Calendar cal = Calendar.getInstance();
        String monthName = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int currentMonth = cal.get(Calendar.MONTH) + 1;//as jan=0 feb=1 mar=2 ...
        String Month = "";
        if (currentMonth < 10)
            Month = "0" + currentMonth;
        else
            Month = "" + currentMonth;
        int currentYear = cal.get(Calendar.YEAR);
        String year = "" + currentYear;

        StatusDBhelper dbHome = new StatusDBhelper(getActivity());

        ShowExpense.setText("EXPENSES\n" + dbHome.getExpenses(Month, year));
        ShowIncome.setText("INCOMES\n" + dbHome.getIncomes(Month, year));

        month.setText(monthName);
        expenses.setText("" + dbHome.getExpenses(Month, year));
        budget.setText("" + dbHome.getBudget(Month, year));

        int starting = dbHome.getStarting(Month, year);
        int running = dbHome.getRunning(Month, year);
        Starting.setText("" + starting);
        Running.setText("" + running);

        MakeStartingAndRunningProgresses(starting, running);
        barStarting.post(new Runnable() {
            @Override
            public void run() {
                barStarting.setProgress((int) progressStarting);
            }
        });
        barRunning.post(new Runnable() {
            @Override
            public void run() {
                barRunning.setProgress((int) progressRunning);
            }
        });

        final int progress = (int) (((float) dbHome.getExpenses(Month, year) / (float) dbHome.getBudget(Month, year)) * 100);
        bar.post(new Runnable() {
            @Override
            public void run() {
                bar.setProgress(progress);
            }
        });

        if (progress >= 100) {
            progressStatus.setText("Over Budget!");
            progressStatus.setTextColor(Color.RED);
        } else {
            progressStatus.setText(progress + "%");
            progressStatus.setTextColor(Color.BLUE);
        }
    }

    /*
    * Method to set starting and running progresses w.r.t
    * each other's values to progressStarting & progressRunning variables
     */
    public void MakeStartingAndRunningProgresses(int starting, int running) {
        float divider;
        float eachBlock;
        if (running > starting) {
            if (starting > 0)
                divider = (float) running / (float) starting;
            else {
                starting = 0;
                divider = (float) running / (float) starting;
            }
            if (divider > 50)
                divider = 50;
            progressStarting = 50;
            eachBlock = (float) starting / progressStarting;
            progressRunning = (float) running / eachBlock;
            if (progressRunning > 99) {
                eachBlock = eachBlock * divider;
                progressStarting = (float) starting / eachBlock;
            }
        } else if (running == starting || running < 0) {
            progressRunning = 50;
            progressStarting = 50;
            if (running < 0) {
                progressRunning = 0;
                progressStarting = 100;
            }
        } else {
            if (running > 0)
                divider = (float) starting / (float) running;
            else {
                running = 0;
                divider = (float) starting / (float) running;
            }
            if (divider > 50)
                divider = 50;
            progressRunning = 50;
            eachBlock = (float) running / progressRunning;
            progressStarting = (float) starting / eachBlock;
            if (progressStarting > 99) {
                eachBlock = eachBlock * divider;
                progressRunning = (float) running / eachBlock;
            }
        }
    }

    /*
    * This is called when we press more or less buttons
     */
    public void onMoreOrLess() {
        loopOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (loopOn) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (moreOrless.getText().toString().equals("More")) {
                                dp += 2;
                                //converting dp into pixels
                                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                                        getResources().getDisplayMetrics());
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                                        , px);
                                param.gravity = Gravity.CENTER;
                                expandableLayout.setLayoutParams(param);
                            } else {
                                dp -= 2;
                                //converting dp into pixels
                                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                                        getResources().getDisplayMetrics());
                                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, px);
                                param.gravity = Gravity.CENTER;
                                expandableLayout.setLayoutParams(param);
                            }
                            if (dp == 0) {
                                moreOrless.setText("More");
                                moreOrlessImg.setImageResource(R.drawable.ic_keyboard_arrow_down_black_48dp);
                            }
                            if (dp == 150) {
                                moreOrless.setText("Less");
                                moreOrlessImg.setImageResource(R.drawable.ic_keyboard_arrow_up_black_48dp);
                            }
                            if (dp == 0 || dp == 150) {
                                loopOn = false;
                            }
                        }
                    });
                    try {
                        Thread.sleep(4);
                    } catch (InterruptedException e) {
                    }

                }

            }
        }).start();
    }
}
