package tn.esprit.car_sell;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CarDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_CARS = "cars";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BRAND = "brand";
    public static final String COLUMN_MODEL = "model";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_STATUS = "status";

    // Create table query
    private static final String CREATE_CARS_TABLE = "CREATE TABLE " + TABLE_CARS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_BRAND + " TEXT NOT NULL, "
            + COLUMN_MODEL + " TEXT NOT NULL, "
            + COLUMN_YEAR + " INTEGER NOT NULL, "
            + COLUMN_PRICE + " REAL NOT NULL, "
            + COLUMN_STATUS + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CARS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARS);
        onCreate(db);
    }
}