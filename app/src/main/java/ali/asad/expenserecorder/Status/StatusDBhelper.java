package ali.asad.expenserecorder.Status;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ali.asad.expenserecorder.Expenses.ExpenseDBhelper;

/**
 * Created by AsadAli on 18-Jul-17.
 */

public class StatusDBhelper extends SQLiteOpenHelper implements BaseColumns {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "main.db";
    public static final String TABLE_NAME = "home";
    public static final String COLUMN_NAME_MONTH = "month";
    public static final String COLUMN_NAME_YEAR = "year";
    public static final String COLUMN_NAME_STARTING = "starting";
    public static final String COLUMN_NAME_RUNNING = "running";
    public static final String COLUMN_NAME_INCOMES = "incomes";
    public static final String COLUMN_NAME_EXPENSES = "expenses";
    public static final String COLUMN_NAME_SAVINGS = "savings";
    public static final String COLUMN_NAME_BUDGET = "budget";
    static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY," +
            COLUMN_NAME_MONTH + " TEXT," +
            COLUMN_NAME_YEAR + " TEXT," +
            COLUMN_NAME_STARTING + " INT," +
            COLUMN_NAME_RUNNING + " INT," +
            COLUMN_NAME_INCOMES + " INT," +
            COLUMN_NAME_EXPENSES + " INT," +
            COLUMN_NAME_SAVINGS + " INT," +
            COLUMN_NAME_BUDGET + " INT )";
    static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public StatusDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean isTableExists() {
        boolean exists = false;
        SQLiteDatabase dbRead = getReadableDatabase();
        if (dbRead == null || !dbRead.isOpen()) {
            dbRead = getReadableDatabase();
        }

        if (!dbRead.isReadOnly()) {
            dbRead.close();
            dbRead = getReadableDatabase();
        }

        Cursor cursor = dbRead.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TABLE_NAME + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                exists = true;
            }
            cursor.close();
        }

        if (!exists) {
            SQLiteDatabase dbWrite = getWritableDatabase();
            onUpgrade(dbWrite, 1, 1);
            System.out.println("Created HomeDB again");
        }

        return exists;
    }

    public boolean isEntryExists(String month, String year) {
        boolean exists = false;
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_MONTH,
                COLUMN_NAME_YEAR,
        };
        String whereClause = COLUMN_NAME_MONTH + " = ? AND " + COLUMN_NAME_YEAR + " = ?";
        String[] whereArgs = new String[]{
                month, year
        };
        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                 // The sort order >>orderBy
        );
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                exists = true;
            }
            cursor.close();
        }
        return exists;
    }

    public void insertOrAlterEntry(String month, String year, int starting, int running,
                                   int incomes, int expenses, int savings, int budget) {
        SQLiteDatabase dbWrite = getWritableDatabase();

        if (!isEntryExists(month, year)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_MONTH, month);
            values.put(COLUMN_NAME_YEAR, year);
            values.put(COLUMN_NAME_STARTING, starting);
            values.put(COLUMN_NAME_RUNNING, running);
            values.put(COLUMN_NAME_INCOMES, incomes);
            values.put(COLUMN_NAME_EXPENSES, expenses);
            values.put(COLUMN_NAME_SAVINGS, savings);
            values.put(COLUMN_NAME_BUDGET, budget);
            // Insert the new row, returning the primary key value of the new row
            // if not inserted then return -1
            long newRowId = dbWrite.insert(TABLE_NAME, null, values);
        } else {
            //UPDATE the data
            dbWrite.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_NAME_STARTING + " = " + starting
                    + ", " + COLUMN_NAME_RUNNING + " = " + running + ", " + COLUMN_NAME_INCOMES + " = " + incomes
                    + ", " + COLUMN_NAME_EXPENSES + " = " + expenses + ", " + COLUMN_NAME_SAVINGS + " = " + savings
                    + ", " + COLUMN_NAME_BUDGET + " = " + budget + " WHERE " + COLUMN_NAME_MONTH
                    + " = '" + month + "' AND " + COLUMN_NAME_YEAR + " = '" + year + "'"
            );
        }
        changeForRunning(month,year,running);
    }

    /*
    * call this method when changing starting of current month
    * and also after inserting expenses or incomes in the table
    * simply calling it at the end of insertOrAlterEntry
     */
    public void changeForRunning(String Month, String Year, int Running) {
        boolean found = false;
        int SelectedMonthNum = Integer.parseInt(Month);
        int SelectedYearNum = Integer.parseInt(Year);

        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_MONTH,
                COLUMN_NAME_YEAR,
                COLUMN_NAME_STARTING,
                COLUMN_NAME_RUNNING,
                COLUMN_NAME_INCOMES,
                COLUMN_NAME_EXPENSES,
                COLUMN_NAME_SAVINGS,
                COLUMN_NAME_BUDGET
        };
        String orderBy = COLUMN_NAME_YEAR + ", " + COLUMN_NAME_MONTH;

        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                null,                                   // The columns for the WHERE clause >>whereClause
                null,                                   // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                orderBy                                    // The sort order >>orderBy
        );
        System.out.println("Month  Year  Starting  Running  Incomes  Expenses  Savings  Budget");
        while (cursor.moveToNext()) {
            String month = cursor.getString(0);
            String year = cursor.getString(1);
            int starting = cursor.getInt(2);
            int running = cursor.getInt(3);
            int incomes = cursor.getInt(4);
            int expenses = cursor.getInt(5);
            int savings = cursor.getInt(6);
            int budget = cursor.getInt(7);

            if (found) {
                int monthNum = Integer.parseInt(month);
                int yearNum = Integer.parseInt(year);
                if (SelectedMonthNum != 12) {
                    if (monthNum == SelectedMonthNum + 1) {
                        starting = Running;
                        running = starting + incomes - expenses;

                        SelectedMonthNum = monthNum;
                        Running=running;

                        insertOrAlterEntry(month,year,starting,running,incomes,expenses,savings,budget);

                    }
                } else {
                    if (yearNum == SelectedYearNum + 1 && monthNum == 1) {
                        starting = Running;
                        running = starting + incomes - expenses;

                        SelectedMonthNum = monthNum;
                        SelectedYearNum = yearNum;
                        Running = running;

                        insertOrAlterEntry(month,year,starting,running,incomes,expenses,savings,budget);

                    }
                }
                System.out.println(month + " " + year + " " + starting + " " + running + " "
                        + incomes + " " + expenses + " " + savings + " " + budget);
            }

            if (Month.equals(month) && Year.equals(year)) {
                found = true;
            }

        }
        cursor.close();

    }


    public List<String[]> getAllEntries() {
        List<String[]> list = new ArrayList<>();
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_MONTH,
                COLUMN_NAME_YEAR,
                COLUMN_NAME_STARTING,
                COLUMN_NAME_RUNNING,
                COLUMN_NAME_INCOMES,
                COLUMN_NAME_EXPENSES,
                COLUMN_NAME_SAVINGS,
                COLUMN_NAME_BUDGET
        };

        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                null,                                   // The columns for the WHERE clause >>whereClause
                null,                                   // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                    // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {
            String GOTvals[] = new String[8];

            String month = cursor.getString(0);
            String year = cursor.getString(1);
            String starting = cursor.getString(2);
            String running = cursor.getString(3);
            String incomes = cursor.getString(4);
            String expenses = cursor.getString(5);
            String savings = cursor.getString(6);
            String budget = cursor.getString(7);

            GOTvals[0] = month;
            GOTvals[1] = year;
            GOTvals[2] = starting;
            GOTvals[3] = running;
            GOTvals[4] = incomes;
            GOTvals[5] = expenses;
            GOTvals[6] = savings;
            GOTvals[7] = budget;

            list.add(GOTvals);
        }
        cursor.close();
        return list;
    }

    public List<String> getAllYears() {
        List<String> years = new ArrayList<>();
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_YEAR
        };
        String orderBy = COLUMN_NAME_YEAR;
        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                // The columns to return >>projection
                null,                      // The columns for the WHERE clause >>whereClause
                null,                      // The values for the WHERE clause >>whereArgs
                null,                      // group the rows
                null,                      // filter by row groups
                orderBy                    // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {
            String year = cursor.getString(0);
            years.add(year);
        }
        cursor.close();
        return years;
    }


    public int getStarting(String month, String year) {
        int n = 0;
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_STARTING
        };
        String whereClause = COLUMN_NAME_MONTH + " = ? AND " + COLUMN_NAME_YEAR + " = ?";
        String[] whereArgs = new String[]{
                month, year
        };
        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                 // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {
            n = cursor.getInt(0);
        }
        cursor.close();
        return n;
    }

    public int getRunning(String month, String year) {
        int n = 0;
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_RUNNING
        };
        String whereClause = COLUMN_NAME_MONTH + " = ? AND " + COLUMN_NAME_YEAR + " = ?";
        String[] whereArgs = new String[]{
                month, year
        };
        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                 // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {
            n = cursor.getInt(0);
        }
        cursor.close();
        return n;
    }

    public int getIncomes(String month, String year) {
        int n = 0;
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_INCOMES
        };
        String whereClause = COLUMN_NAME_MONTH + " = ? AND " + COLUMN_NAME_YEAR + " = ?";
        String[] whereArgs = new String[]{
                month, year
        };
        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                 // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {
            n = cursor.getInt(0);
        }
        cursor.close();
        return n;
    }

    public int getExpenses(String month, String year) {
        int n = 0;
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_EXPENSES
        };
        String whereClause = COLUMN_NAME_MONTH + " = ? AND " + COLUMN_NAME_YEAR + " = ?";
        String[] whereArgs = new String[]{
                month, year
        };
        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                 // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {
            n = cursor.getInt(0);
        }
        cursor.close();
        return n;
    }

    public int getSavings(String month, String year) {
        int n = 0;
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_SAVINGS
        };
        String whereClause = COLUMN_NAME_MONTH + " = ? AND " + COLUMN_NAME_YEAR + " = ?";
        String[] whereArgs = new String[]{
                month, year
        };
        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                 // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {
            n = cursor.getInt(0);
        }
        cursor.close();
        return n;
    }

    public int getBudget(String month, String year) {
        int n = 0;
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                COLUMN_NAME_BUDGET
        };
        String whereClause = COLUMN_NAME_MONTH + " = ? AND " + COLUMN_NAME_YEAR + " = ?";
        String[] whereArgs = new String[]{
                month, year
        };
        Cursor cursor = dbRead.query(
                TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                 // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {
            n = cursor.getInt(0);
        }
        cursor.close();
        return n;
    }


    public void deleteEmptyRows() {
        SQLiteDatabase dbWrite = getWritableDatabase();
        String[] whereArgs = new String[]{"0", "0", "0", "0"};
        Cursor c = dbWrite.rawQuery("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_INCOMES + " = ? AND "
                + COLUMN_NAME_EXPENSES + " = ? AND " + COLUMN_NAME_SAVINGS + " = ? AND "
                + COLUMN_NAME_BUDGET + " = ? ", whereArgs);
        while (c.moveToNext()) {
            System.out.println("Deleting..");//this line never runs
        }
        c.close();
    }
}
