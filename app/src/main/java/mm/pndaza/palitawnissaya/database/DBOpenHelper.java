package mm.pndaza.palitawnissaya.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import mm.pndaza.palitawnissaya.model.Category;
import mm.pndaza.palitawnissaya.model.Nsy;
import mm.pndaza.palitawnissaya.model.PaliBook;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static DBOpenHelper sInstance;
    private static final String DATABASE_NAME = "pali_nsy.db";
    private static final int DATABASE_VERSION = 2;


    public static synchronized DBOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.

        if (sInstance == null) {
            sInstance = new DBOpenHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DBOpenHelper(Context context) {
        super(context, context.getFilesDir() + "/databases/" + DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> categories = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query("categories", new String[]{"id", "description"},
                null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                final Category category = new Category(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("description"))
                );
                categories.add(category);
            } while (cursor.moveToNext());
        }
        return categories;
    }

    public ArrayList<PaliBook> getBooks(int category) {
        ArrayList<PaliBook> bookList = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT id, name, first_page, last_page FROM pali_books where category_id = " + category,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                bookList.add(new PaliBook(
                        cursor.getString(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getInt(cursor.getColumnIndex("first_page")),
                        cursor.getInt(cursor.getColumnIndex("last_page"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookList;
    }

    public ArrayList<Nsy> getNsyBooks(String pali_book_id, int pali_book_page_number) {

        ArrayList<Nsy> list = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT nsy_book_id, nsy_book_page_number, name "
                        + "FROM pali_nsy_page_map INNER JOIN nsy_books "
                        + "ON pali_nsy_page_map.nsy_book_id = nsy_books.id "
                        + "WHERE pali_book_id = ? AND pali_book_page_number = ? AND nsy_book_page_number != 0",
                new String[]{pali_book_id, String.valueOf(pali_book_page_number)});

        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            do {
                String nsyid = cursor.getString(cursor.getColumnIndex("nsy_book_id"));
                int nsyPageNumber = cursor.getInt(cursor.getColumnIndex("nsy_book_page_number"));
                String nsyName = cursor.getString(cursor.getColumnIndex("name"));
                list.add(new Nsy(nsyid, nsyName, nsyPageNumber));
            } while (cursor.moveToNext());
        }
        return list;
    }


    public boolean isNsyExist(String pali_book_id, int pali_page_number) {

        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT nsy_book_id FROM pali_nsy_page_map WHERE pali_book_id = ? and pali_book_page_number = ?",
                new String[]{pali_book_id, String.valueOf(pali_page_number)});
        if (cursor != null && cursor.getCount() > 0) {
            Log.d("isExist", "true");
            cursor.close();
            return true;
        }
        return false;
    }
}
