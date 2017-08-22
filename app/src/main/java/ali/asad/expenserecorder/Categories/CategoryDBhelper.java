package ali.asad.expenserecorder.Categories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import ali.asad.expenserecorder.Expenses.ExpenseDBhelper;

/**
 * Created by AsadAli on 13-Jul-17.
 */

public class CategoryDBhelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "main.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " +CategoryEntry.TABLE_NAME + " (" +
                    CategoryEntry._ID + " INTEGER PRIMARY KEY," +
                    CategoryEntry.COLUMN_NAME_CATEGORY + " TEXT," +
                    CategoryEntry.COLUMN_NAME_ICON + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME;


    public CategoryDBhelper(Context context) {
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
        boolean exists=false;
        SQLiteDatabase dbRead=getReadableDatabase();
        if(dbRead == null || !dbRead.isOpen()) {
            dbRead = getReadableDatabase();
        }

        if(!dbRead.isReadOnly()) {
            dbRead.close();
            dbRead = getReadableDatabase();
        }

        Cursor cursor = dbRead.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+CategoryEntry.TABLE_NAME+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                exists=true;
            }
            cursor.close();
        }
        if (!exists) {
            SQLiteDatabase dbWrite = getWritableDatabase();
            onUpgrade(dbWrite, 1, 1);
            System.out.println("Created CategoryDB again");
        }
        return exists;
    }

    public List<String[]> getAllEntries() {
        List<String[]> list = new ArrayList<>();
        SQLiteDatabase dbRead = getReadableDatabase();

        String[] projection = {
                CategoryEntry._ID,
                CategoryEntry.COLUMN_NAME_CATEGORY,
                CategoryEntry.COLUMN_NAME_ICON
        };
        String orderBy = CategoryEntry.COLUMN_NAME_CATEGORY;

        Cursor cursor = dbRead.query(
                CategoryEntry.TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                null,                            // The columns for the WHERE clause >>whereClause
                null,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                orderBy                                 // The sort order >>orderBy
        );

        while (cursor.moveToNext()) {

            String GOTvals[] = new String[3];

            int id = cursor.getInt(0);
            String category = cursor.getString(1);
            String icon = cursor.getString(2);

            GOTvals[0] = "" + id;
            GOTvals[1] = category;
            GOTvals[2] = icon;
            list.add(GOTvals);
        }
        cursor.close();
        return list;
    }

    public String getIconOf(String category){
        String icon="";
        SQLiteDatabase dbRead = getReadableDatabase();
        String[] projection = {CategoryEntry.COLUMN_NAME_ICON};
        String whereClause = CategoryEntry.COLUMN_NAME_CATEGORY + " = ?";
        String[] whereArgs = new String[]{category};
        Cursor cursor = dbRead.query(
                CategoryEntry.TABLE_NAME,                // The table to query >>TABLE_NAME
                projection,                             // The columns to return >>projection
                whereClause,                            // The columns for the WHERE clause >>whereClause
                whereArgs,                              // The values for the WHERE clause >>whereArgs
                null,                                   // group the rows
                null,                                   // filter by row groups
                null                                    // The sort order >>orderBy
        );

        while (cursor.moveToNext()) {
            icon= cursor.getString(0);
        }

        return icon;
    }

    public void insertEntry(String category, String icon) {
        SQLiteDatabase dbWrite = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategoryEntry.COLUMN_NAME_CATEGORY, category);
        values.put(CategoryEntry.COLUMN_NAME_ICON, icon);

        // Insert the new row, returning the primary key value of the new row
        // if not inserted then return -1
        long newRowId = dbWrite.insert(CategoryEntry.TABLE_NAME, null, values);

    }

    public void deleteEntry(int id) {
        SQLiteDatabase dbWrite = getWritableDatabase();
        String[] whereArgs = new String[]{""+id};
        Cursor c =dbWrite.rawQuery("DELETE FROM "+ CategoryEntry.TABLE_NAME+" WHERE "
                + CategoryEntry._ID+" = ?", whereArgs);
        while (c.moveToNext()){
            System.out.println("Deleting..");//This doesn't run..
        }
        c.close();
    }



    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_CATEGORY = "CategoryName";
        public static final String COLUMN_NAME_ICON = "CategoryIcon";
    }
}
