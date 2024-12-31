package tn.esprit.car_sell;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CarDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public CarDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertCar(Car car) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BRAND, car.getBrand());
        values.put(DatabaseHelper.COLUMN_MODEL, car.getModel());
        values.put(DatabaseHelper.COLUMN_YEAR, car.getYear());
        values.put(DatabaseHelper.COLUMN_PRICE, car.getPrice());
        values.put(DatabaseHelper.COLUMN_STATUS, car.getStatus());

        return database.insert(DatabaseHelper.TABLE_CARS, null, values);
    }

    public int updateCar(Car car) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_BRAND, car.getBrand());
        values.put(DatabaseHelper.COLUMN_MODEL, car.getModel());
        values.put(DatabaseHelper.COLUMN_YEAR, car.getYear());
        values.put(DatabaseHelper.COLUMN_PRICE, car.getPrice());
        values.put(DatabaseHelper.COLUMN_STATUS, car.getStatus());

        return database.update(DatabaseHelper.TABLE_CARS, values,
                DatabaseHelper.COLUMN_ID + " = " + car.getId(), null);
    }

    public void deleteCar(long id) {
        database.delete(DatabaseHelper.TABLE_CARS,
                DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_CARS,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Car car = cursorToCar(cursor);
            cars.add(car);
            cursor.moveToNext();
        }
        cursor.close();
        return cars;
    }

    public Car getCarById(long id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_CARS,
                null, DatabaseHelper.COLUMN_ID + " = " + id,
                null, null, null, null);

        cursor.moveToFirst();
        Car car = cursorToCar(cursor);
        cursor.close();
        return car;
    }

    private Car cursorToCar(Cursor cursor) {
        Car car = new Car();
        car.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        car.setBrand(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_BRAND)));
        car.setModel(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_MODEL)));
        car.setYear(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR)));
        car.setPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE)));
        car.setStatus(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATUS)));
        return car;
    }
}
