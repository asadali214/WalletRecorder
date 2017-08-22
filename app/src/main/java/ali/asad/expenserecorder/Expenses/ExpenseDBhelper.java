package ali.asad.expenserecorder.Expenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AsadAli on 13-Jul-17.
 */

public class ExpenseDBhelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "main.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExpenseEntry.TABLE_NAME + " (" +
                    ExpenseEntry._ID + " INTEGER PRIMARY KEY," +
                    ExpenseEntry.COLUMN_NAME_DATE + " DATE," +
                    ExpenseEntry.COLUMN_NAME_DETAIL + " TEXT," +
                    ExpenseEntry.COLUMN_NAME_AMOUNT + " INT," +
                    ExpenseEntry.COLUMN_NAME_CATEGORY + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExpenseEntry.TABLE_NAME;

    public ExpenseDBhelper(Context context) {
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

    /*
    * The following method will return if table exists or not
    * if table doesn't exists then it create one.
    */
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

        Cursor cursor = dbRead.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + ExpenseEntry.TABLE_NAME + "'", null);
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
            System.out.println("Created ExpenseDB again");
        }
        return exists;
    }

    public List<String> getDateOfAllEntries() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                "DISTINCT " + ExpenseEntry.COLUMN_NAME_DATE,
        };

        Cursor cursor = dbRead.query(
                ExpenseEntry.TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                null,                                   // The columns for the WHERE clause >>whereClause
                null,                                   // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                    // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {
            String date = cursor.getString(0);
            list.add(date);
        }
        cursor.close();
        return list;
    }

    public List<String[]> getAllEntriesOf(String Month, String Year) {
        List<String[]> list = new ArrayList<>();

        //Month =Month+"-";
        //Year = "-"+Year;
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                ExpenseEntry._ID,
                ExpenseEntry.COLUMN_NAME_DATE,
                ExpenseEntry.COLUMN_NAME_DETAIL,
                ExpenseEntry.COLUMN_NAME_AMOUNT,
                ExpenseEntry.COLUMN_NAME_CATEGORY
        };
        String whereClause = ExpenseEntry.COLUMN_NAME_DATE + " LIKE ?";
        String[] whereArgs = new String[]{
                Month + "-%-" + Year
        };
        String orderBy = ExpenseEntry.COLUMN_NAME_DATE;

        Cursor cursor = dbRead.query(
                ExpenseEntry.TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                orderBy                                 // The sort order >>orderBy
        );
        while (cursor.moveToNext()) {

            String GOTvals[] = new String[5];

            int id = cursor.getInt(0);
            String date = cursor.getString(1);
            String detail = cursor.getString(2);
            String amount = cursor.getString(3);
            String category = cursor.getString(4);

            GOTvals[0] = "" + id;
            GOTvals[1] = date;
            GOTvals[2] = detail;
            GOTvals[3] = amount;
            GOTvals[4] = category;
            list.add(GOTvals);
        }
        cursor.close();
        return list;
    }

    public List<String[]> getAllEntriesOf(String Year) {
        List<String[]> list = new ArrayList<>();

        String Date = "-" + Year;
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {
                ExpenseEntry._ID,
                ExpenseEntry.COLUMN_NAME_DATE,
                ExpenseEntry.COLUMN_NAME_DETAIL,
                ExpenseEntry.COLUMN_NAME_AMOUNT,
                ExpenseEntry.COLUMN_NAME_CATEGORY
        };
        String whereClause = ExpenseEntry.COLUMN_NAME_DATE + " LIKE ?";
        String[] whereArgs = new String[]{"%" + Date};
        String orderBy = ExpenseEntry.COLUMN_NAME_DATE;

        Cursor cursor = dbRead.query(
                ExpenseEntry.TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                orderBy                                 // The sort order >>orderBy
        );

        while (cursor.moveToNext()) {

            String GOTvals[] = new String[5];

            int id = cursor.getInt(0);
            String date = cursor.getString(1);
            String detail = cursor.getString(2);
            String amount = cursor.getString(3);
            String category = cursor.getString(4);

            GOTvals[0] = "" + id;
            GOTvals[1] = date;
            GOTvals[2] = detail;
            GOTvals[3] = amount;
            GOTvals[4] = category;
            list.add(GOTvals);
        }
        cursor.close();
        return list;
    }

    public String getAmountOf(int id) {
        String amount = "";
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {ExpenseEntry.COLUMN_NAME_AMOUNT};
        String whereClause = ExpenseEntry._ID + " = ?";
        String[] whereArgs = new String[]{"" + id};
        Cursor cursor = dbRead.query(
                ExpenseEntry.TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                    // The sort order >>orderBy
        );

        while (cursor.moveToNext()) {
            amount = cursor.getString(0);
        }

        return amount;
    }

    public String getDateOf(int id) {
        String date = "";
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {ExpenseEntry.COLUMN_NAME_DATE};
        String whereClause = ExpenseEntry._ID + " = ?";
        String[] whereArgs = new String[]{"" + id};
        Cursor cursor = dbRead.query(
                ExpenseEntry.TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                    // The sort order >>orderBy
        );

        while (cursor.moveToNext()) {
            date = cursor.getString(0);
        }

        return date;
    }

    public void insertEntry(String date, String detail, String amount, String category) {
        SQLiteDatabase dbWrite = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExpenseEntry.COLUMN_NAME_DATE, date);
        values.put(ExpenseEntry.COLUMN_NAME_DETAIL, detail);
        values.put(ExpenseEntry.COLUMN_NAME_AMOUNT, amount);
        values.put(ExpenseEntry.COLUMN_NAME_CATEGORY, category);

        // Insert the new row, returning the primary key value of the new row
        // if not inserted then return -1
        long newRowId = dbWrite.insert(ExpenseEntry.TABLE_NAME, null, values);

    }

    public void deleteEntry(int id) {
        SQLiteDatabase dbWrite = getWritableDatabase();
        String[] whereArgs = new String[]{"" + id};
        Cursor c = dbWrite.rawQuery("DELETE FROM " + ExpenseEntry.TABLE_NAME + " WHERE " + ExpenseEntry._ID + " = ?", whereArgs);
        while (c.moveToNext()) {
            System.out.println("Deleting..");
        }
        c.close();
    }


    public static class ExpenseEntry implements BaseColumns {
        public static final String TABLE_NAME = "expense";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DETAIL = "detail";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_CATEGORY = "category";
    }
}
