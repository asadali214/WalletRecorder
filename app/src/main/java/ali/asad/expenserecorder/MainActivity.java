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

    public static ViewPagerAdapter viewPagerAdapter;
    public static Boolean first = true;

    public static Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        first = true;

        /*
        *For Status Tab Fragment
         */
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(accountStatusFragment, "Account Status");
        viewPagerAdapter.addFragments(yearlySummaryFragment, "Yearly Summary");

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
                    fragmentManager = getFragmentManager();
                    FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
                    /*
                    * on opening the home fragment the following code pops all the back stack
                    * entries until the "first" entry is on top of the stack
                    */
                    fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                break;
            case R.id.status_menu:
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
                    fragmentManager = getFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentPlace, showIncomeFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.Expense_menu:
                if (!showExpenseFragment.isVisible()) {
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
        if (getTitle().equals("Expense Categories")
                || getTitle().equals("Expenses")
                || getTitle().equals("Incomes")) {
            menu.clear();
            getMenuInflater().inflate(R.menu.menu_show, menu);
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
    * This method is called when we press the OverallStatus button in the home
    * fragment and it opens up the StatusTabFragment
     */
    public void onStatus(View view) {

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

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //RemoveAllFragments(fragmentTransaction);
        fragmentTransaction.replace(R.id.fragmentPlace, showExpenseFragment);//move to show expense fragment
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
                    int running = starting+incomes-expenses;
                    int savings = dbStatus.getSavings(month, year);
                    int budget = dbStatus.getBudget(month,year);

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
    * This will call the onGoExpense method of expense fragment which is used to add
    * the current expense in the database
     */
    public void onGoExpense(View view) {
        expenseFragment.onGoExpense(view);
    }

    /*
    * This will call the onGoIncome method of income fragment which is used to add
    * the current income in the database
     */
    public void onGoIncome(View view) {
        incomeFragment.onGoIncome(view);
    }

    /*
    * This will call the onGoCategory method of category fragment which is used to add
    * the current expense category in the database.
     */
    public void onGoCategory(View view) {
        categoryFragment.onGoCatgory(view);
    }

    /*
    * Called every time we press back button, it will pop the back stack until its
    * empty, after the back stack is empty it will close the application.
     */
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
