package ali.asad.expenserecorder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import ali.asad.expenserecorder.Categories.CategoryDBhelper;
import ali.asad.expenserecorder.Categories.CategoryFragment;
import ali.asad.expenserecorder.Categories.CategoryShowFragment;
import ali.asad.expenserecorder.Expenses.ExpenseDBhelper;
import ali.asad.expenserecorder.Expenses.ExpenseFragment;
import ali.asad.expenserecorder.Expenses.ExpenseShowFragment;
import ali.asad.expenserecorder.Home.HomeFragment;
import ali.asad.expenserecorder.Incomes.IncomeDBhelper;
import ali.asad.expenserecorder.Incomes.IncomeFragment;
import ali.asad.expenserecorder.Incomes.IncomeShowFragment;
import ali.asad.expenserecorder.Status.AccountStatusFragment;
import ali.asad.expenserecorder.Status.StatusDBhelper;
import ali.asad.expenserecorder.Status.StatusTabFragment;
import ali.asad.expenserecorder.Status.ViewPagerAdapter;
import ali.asad.expenserecorder.Status.YearlyExpenseFragment;
import ali.asad.expenserecorder.Status.YearlyRunningFragment;
import ali.asad.expenserecorder.Status.YearlySummaryFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    HomeFragment homeFragment = new HomeFragment();
    ExpenseFragment expenseFragment = new ExpenseFragment();
    IncomeFragment incomeFragment = new IncomeFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    CategoryShowFragment showCategoryFragment = new CategoryShowFragment();
    IncomeShowFragment showIncomeFragment = new IncomeShowFragment();
    ExpenseShowFragment showExpenseFragment = new ExpenseShowFragment();
    StatusTabFragment statusTabFragment = new StatusTabFragment();
    AccountStatusFragment accountStatusFragment = new AccountStatusFragment();
    YearlySummaryFragment yearlySummaryFragment = new YearlySummaryFragment();
    YearlyExpenseFragment yearlyExpenseFragment = new YearlyExpenseFragment();
    YearlyRunningFragment yearlyRunningFragment = new YearlyRunningFragment();

    public static ViewPagerAdapter viewPagerAdapter;
    public static Boolean first = true;

    public static Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    /*
    * This method will be called whenever we start the application Wallet Recorder
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        first = true;

        /*
        *For Status Tab Fragment
         */
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addFragments(yearlySummaryFragment, "Income & Expenses");
        viewPagerAdapter.addFragments(yearlyExpenseFragment, "Overall Expenses");
        viewPagerAdapter.addFragments(yearlyRunningFragment, "Running Balances");

        /*
        * Following Code handles the navigation drawer and its touch event.
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);//touch event of navigation drawer

        /*
        * Checking all databases if they are present or not
         */
        ExpenseDBhelper dbExpense = new ExpenseDBhelper(this);
        if (dbExpense.isTableExists())//if doesn't exists then it create one.
            System.out.println("Expense Table is already present");
        IncomeDBhelper dbIncome = new IncomeDBhelper(this);
        if (dbIncome.isTableExists())//if doesn't exists then it create one.
            System.out.println("Income Table is already present");
        CategoryDBhelper dbCategory = new CategoryDBhelper(this);
        if (dbCategory.isTableExists())//if doesn't exists then it create one.
            System.out.println("Category Table is already present");
        StatusDBhelper dbStatus = new StatusDBhelper(this);
        if (dbStatus.isTableExists())//if doesn't exists then it create one.
            System.out.println("Home Table is already present");

        /*
        * Finally Opening the HomeFragment for the first time.
         */
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, homeFragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();

        //RandomEntriesAdder("2017");
    }

    /*
    * ****ONLY FOR TESTING PURPOSES****
    * This method add random income and expense entries into
    * the database in a given year
     */
    public void RandomEntriesAdder(String year) {
        Random random = new Random();

        ExpenseDBhelper dbExpense = new ExpenseDBhelper(this);
        dbExpense.onUpgrade(dbExpense.getWritableDatabase(), 1, 1);

        IncomeDBhelper dbIncome = new IncomeDBhelper(this);
        dbIncome.onUpgrade(dbIncome.getWritableDatabase(), 1, 1);

        StatusDBhelper dbHome = new StatusDBhelper(this);
        dbHome.onUpgrade(dbHome.getWritableDatabase(), 1, 1);

        CategoryDBhelper dbCat = new CategoryDBhelper(this);


        for (int i = 1; i <= 12; i++) {//months loop
            String month;
            if (i < 10)
                month = "0" + i;
            else
                month = "" + i;
            if (i == 1)
                dbHome.insertOrAlterEntry(month, year, 90000, 0, 0, 0, 0, 0);
            List<String[]> list = dbCat.getAllEntries();
            for (int j = 0; j < list.size(); j++) {//Categories Loop
                int day = 10 + random.nextInt(20);
                String Date = month + "-" + day + "-" + year;
                int n;

                //Adding expenses
                n = 1000 + random.nextInt(8999);
                String Category = list.get(j)[1];
                dbExpense.insertEntry(Date, "" + n, "" + n, Category);
                int starting = dbHome.getStarting(month, year);
                int incomes = dbHome.getIncomes(month, year);
                int expenses = dbHome.getExpenses(month, year) + n;
                int running = starting + incomes - expenses;
                int savings = incomes - expenses;
                int budget = dbHome.getBudget(month, year);
                dbHome.insertOrAlterEntry(month, year, starting, running, incomes, expenses, savings, budget);

                //Adding incomes
                n = 1000 + random.nextInt(8999);
                dbIncome.insertEntry(Date, "" + n, "" + n);
                starting = dbHome.getStarting(month, year);
                incomes = dbHome.getIncomes(month, year) + n;
                expenses = dbHome.getExpenses(month, year);
                running = starting + incomes - expenses;
                savings = incomes - expenses;
                budget = dbHome.getBudget(month, year);
                dbHome.insertOrAlterEntry(month, year, starting, running, incomes, expenses, savings, budget);
            }

        }

    }

    /*
    * The following method is called whenever we choose an item
    * in the navigation drawer
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        switch (item.getItemId()) {
            case R.id.home_menu:
                if (!homeFragment.isVisible()) {
                    removeTillHome();
                }
                break;
            case R.id.status_menu:
                if (!accountStatusFragment.isVisible()) {
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentPlace, accountStatusFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.summary_menu:
                if (!statusTabFragment.isVisible()) {
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentPlace, statusTabFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.Income_menu:
                if (!showIncomeFragment.isVisible()) {
                    Calendar cal = Calendar.getInstance();
                    int currentMonth = cal.get(Calendar.MONTH) + 1;//as jan=0 feb=1 mar=2 ...
                    int currentYear = cal.get(Calendar.YEAR);
                    IncomeShowFragment.currentMonth = currentMonth;
                    IncomeShowFragment.currentYear = currentYear;
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentPlace, showIncomeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.Expense_menu:
                if (!showExpenseFragment.isVisible()) {
                    Calendar cal = Calendar.getInstance();
                    int currentMonth = cal.get(Calendar.MONTH) + 1;//as jan=0 feb=1 mar=2 ...
                    int currentYear = cal.get(Calendar.YEAR);
                    ExpenseShowFragment.currentMonth = currentMonth;
                    ExpenseShowFragment.currentYear = currentYear;
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentPlace, showExpenseFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            /*
            * This opens the CategoryShowFragment.
             */
            case R.id.Category_menu:
                if (!showCategoryFragment.isVisible()) {
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentPlace, showCategoryFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;

        }
        drawerLayout.closeDrawers();
        return false;
    }

    /*
    * on opening the home fragment the following code pops all the back stack
    * entries until the "first" entry is on top of the stack
    */
    public void removeTillHome() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
        fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /*
    * The following method is used to create a navigation drawer icon
    * on the top left corner of the screen, this is called in onPostCreate
    * to refresh the screen after its creation
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    /*
    * The following method is used to create a upper right corner menu
    * in the application, works by getting the title name and change the
    * menu items accordingly.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getTitle().equals("Expense Categories")) {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_show_category, menu);
        } else if (getTitle().equals("Expenses")) {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_show_expense, menu);
        } else if (getTitle().equals("Incomes")) {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_show_income, menu);
        } else if (getTitle().equals("Add Expenses") || getTitle().equals("Add Incomes") || getTitle().equals("Add Categories")) {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_adding, menu);
        } else {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    /*
    * This method is called whenever we click on an option menu item
    * from the upper right corner menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear_selected) {
            if (showExpenseFragment.isVisible())
                showExpenseFragment.onClearSelectedExpenses();
            if (showIncomeFragment.isVisible())
                showIncomeFragment.onClearSelectedIncomes();
            if (showCategoryFragment.isVisible())
                showCategoryFragment.onClearSelectedCategories();

        } else if (id == R.id.clear_all) {

            if (showExpenseFragment.isVisible())
                showExpenseFragment.onClearExpenses();
            if (showIncomeFragment.isVisible())
                showIncomeFragment.onClearIncomes();
            if (showCategoryFragment.isVisible())
                showCategoryFragment.onClearCategories();

        } else if (id == R.id.adding) {

            if (expenseFragment.isVisible())
                onGoExpense();
            if (incomeFragment.isVisible())
                onGoIncome();
            if (categoryFragment.isVisible())
                categoryFragment.onGoCatgory();

        } else if (id == R.id.arrange_expense_category) {
            showExpenseFragment.MakeListByCategories();
        } else if (id == R.id.arrange_expense_day) {
            showExpenseFragment.MakeListByDates();
        } else if (id == R.id.arrange_income_day) {
            showIncomeFragment.MakeListByDates();
        } else if (id == R.id.about) {
            Toast.makeText(this, "Make a info dialog", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    * This method is called whenever we change the title of the application
    * I am using this call onCreateOptionsMenu to change the Menu items
    * whenever title's changed
     */
    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (toolbar != null)
            onCreateOptionsMenu(toolbar.getMenu());
    }

    /*
    * This method is called when we click the date picker in add expense and
    * add income fragments and it open a date picker dialog
     */
    public void onDatePicker(View view) {
        Calendar cal = Calendar.getInstance();
        int dayS = cal.get(Calendar.DAY_OF_MONTH);
        int monthS = cal.get(Calendar.MONTH);
        int yearS = cal.get(Calendar.YEAR);

        DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month++;
                        String date = "";
                        String Day = "";
                        String Month = "";
                        if (day < 10)
                            Day = "0" + day;
                        else
                            Day = "" + day;
                        if (month < 10)
                            Month = "0" + month;
                        else
                            Month = "" + month;
                        date = Month + "-" + Day + "-" + year;
                        if (expenseFragment.isVisible())
                            ExpenseFragment.date.setText(date);
                        if (incomeFragment.isVisible())
                            IncomeFragment.date.setText(date);
                    }
                }, yearS, monthS, dayS);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /*
    * This method is called when we press the WalletStatus button in the home
    * fragment and it opens up the AccountStatusFragment
     */
    public void onStatus(View view) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, accountStatusFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*
    * This method is called when we press the OverallStatus button in the home
    * fragment and it opens up the StatusTabFragment
     */
    public void onSummary(View view) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentPlace, statusTabFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*
    * This method is called when we press the AddExpense button in the home fragment
    * and it opens up the ExpenseFragment
     */
    public void onAddExpense(View view) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //RemoveAllFragments(fragmentTransaction);
        fragmentTransaction.replace(R.id.fragmentPlace, expenseFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*
    * This method is called when we press the Expenses button in the home fragment
    * and it opens up the ExpenseShowFragment
     */
    public void onShowExpense(View view) {
        if (view != null) {
            Calendar cal = Calendar.getInstance();
            int currentMonth = cal.get(Calendar.MONTH) + 1;//as jan=0 feb=1 mar=2 ...
            int currentYear = cal.get(Calendar.YEAR);
            ExpenseShowFragment.currentMonth = currentMonth;
            ExpenseShowFragment.currentYear = currentYear;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //RemoveAllFragments(fragmentTransaction);
        fragmentTransaction.replace(R.id.fragmentPlace, showExpenseFragment);//move to show expense fragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        //System.out.println(">"+showExpenseFragment.isVisible());
        //ExpenseShowFragment.month.setSelection(ExpenseShowFragment.currentMonth);
    }

    /*
    * This method is called when we press the AddIncome button in the home fragment
    * and it opens up the IncomeFragment
     */
    public void onAddIncome(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //RemoveAllFragments(fragmentTransaction);
        fragmentTransaction.replace(R.id.fragmentPlace, incomeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*
   * This method is called when we press the Incomes button in the home fragment
   * and it opens up the IncomeShowFragment
    */
    public void onShowIncome(View view) {
        if (view != null) {
            Calendar cal = Calendar.getInstance();
            int currentMonth = cal.get(Calendar.MONTH) + 1;//as jan=0 feb=1 mar=2 ...
            int currentYear = cal.get(Calendar.YEAR);
            IncomeShowFragment.currentMonth = currentMonth;
            IncomeShowFragment.currentYear = currentYear;
        }
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //RemoveAllFragments(fragmentTransaction);
        fragmentTransaction.replace(R.id.fragmentPlace, showIncomeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*
    * This method is called when we press the addCategory button in the ExpenseFragment
    * and it opens the CategoryFragment.
     */
    public void onAddCategory(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //RemoveAllFragments(fragmentTransaction);
        fragmentTransaction.replace(R.id.fragmentPlace, categoryFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*
    * Called when we press Budget image icon used to change the budget
    * of the current month.
     */
    public void onBudget(View view) {
        //it should open up a dialog to change the value of budget
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_budget);
        dialog.setTitle("Enter new budget!");

        Button ok = (Button) dialog.findViewById(R.id.btnOk);
        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
        final EditText input = (EditText) dialog.findViewById(R.id.ETbudget);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ValueBudget = (input.getText().toString());
                if (ValueBudget.equals(""))
                    dialog.dismiss();
                else {
                    Calendar cal = Calendar.getInstance();
                    int currentMonth = cal.get(Calendar.MONTH) + 1;//as jan=0 feb=1 mar=2 ...
                    int currentYear = cal.get(Calendar.YEAR);
                    String month;
                    if (currentMonth < 10)
                        month = "0" + currentMonth;
                    else
                        month = "" + currentMonth;
                    String year = "" + currentYear;
                    StatusDBhelper dbStatus = new StatusDBhelper(MainActivity.this);
                    int starting = dbStatus.getStarting(month, year);
                    int running = dbStatus.getRunning(month, year);
                    int incomes = dbStatus.getIncomes(month, year);
                    int expenses = dbStatus.getExpenses(month, year);
                    int savings = dbStatus.getSavings(month, year);
                    int budget = Integer.parseInt(ValueBudget);
                    dbStatus.insertOrAlterEntry(month, year, starting, running, incomes, expenses, savings, budget);
                    homeFragment.Refresh();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /*
    * Called when we press Edit Starting Balance image icon
    * used to change the starting balance
    * of the current month.
     */
    public void onStartingBalance(View view) {
        //it should open up a dialog to change the value of budget
        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_starting_balance);
        dialog.setTitle("Enter new starting balance!");
        Button ok = (Button) dialog.findViewById(R.id.btnOk);
        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
        final EditText input = (EditText) dialog.findViewById(R.id.ETstarting);

        ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String ValueStartingBalance = (input.getText().toString());
                if (ValueStartingBalance.equals(""))
                    dialog.dismiss();
                else {
                    Calendar cal = Calendar.getInstance();
                    int currentMonth = cal.get(Calendar.MONTH) + 1;//as jan=0 feb=1 mar=2 ...
                    int currentYear = cal.get(Calendar.YEAR);
                    String month;
                    if (currentMonth < 10)
                        month = "0" + currentMonth;
                    else
                        month = "" + currentMonth;
                    String year = "" + currentYear;
                    StatusDBhelper dbStatus = new StatusDBhelper(MainActivity.this);
                    int starting = Integer.parseInt(ValueStartingBalance);
                    int incomes = dbStatus.getIncomes(month, year);
                    int expenses = dbStatus.getExpenses(month, year);
                    int running = starting + incomes - expenses;
                    int savings = dbStatus.getSavings(month, year);
                    int budget = dbStatus.getBudget(month, year);

                    dbStatus.insertOrAlterEntry(month, year, starting, running, incomes, expenses, savings, budget);

                    homeFragment.Refresh();
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    /*
    * When we press the more or less button in home fragment then this is called
    * and this will expand the layout to view it components.
     */
    public void onMoreOrLess(View view) {
        homeFragment.onMoreOrLess();
    }

    /*
    * First it enters the data in database then calls the show Expense Fragment
     */
    public void onGoExpense() {
        if(expenseFragment.onGoExpense()) {
            getFragmentManager().popBackStack();
            if (getFragmentManager().getBackStackEntryCount() == 1) {
                onShowExpense(null);

            }
        }
    }

    /*
    * First it enters the data in database then calls the show Income Fragment
     */
    public void onGoIncome() {
        if(incomeFragment.onGoIncome()) {
            getFragmentManager().popBackStack();
            if (getFragmentManager().getBackStackEntryCount() == 1) {
                onShowIncome(null);
            }
        }
    }

    /*
    * Called every time we press back button, it will pop the back stack until its
    * empty, after the back stack is empty it will close the application.
     */
    @Override
    public void onBackPressed() {
        if (statusTabFragment.isVisible()) {
            StatusTabFragment.tabLayout.setScrollPosition(1, 0f, true);
            StatusTabFragment.viewPager.setCurrentItem(1);
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
