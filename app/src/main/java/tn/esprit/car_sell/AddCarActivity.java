package tn.esprit.car_sell;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddCarActivity extends AppCompatActivity {
    private CarDAO carDAO;
    private TextInputEditText brandInput;
    private TextInputEditText modelInput;
    private TextInputEditText yearInput;
    private TextInputEditText priceInput;
    private TextInputEditText statusInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Add New Car");
        }

        // Initialize database
        carDAO = new CarDAO(this);
        carDAO.open();

        // Initialize views
        initializeViews();

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveCar());
    }

    private void initializeViews() {
        brandInput = findViewById(R.id.brandInput);
        modelInput = findViewById(R.id.modelInput);
        yearInput = findViewById(R.id.yearInput);
        priceInput = findViewById(R.id.priceInput);
        statusInput = findViewById(R.id.statusInput);
    }

    private void saveCar() {
        String brand = brandInput.getText().toString().trim();
        String model = modelInput.getText().toString().trim();
        String yearStr = yearInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String status = statusInput.getText().toString().trim();

        if (validateInput(brand, model, yearStr, priceStr, status)) {
            try {

                int year = Integer.parseInt(yearStr);
                double price = Double.parseDouble(priceStr);

                Car car = new Car(brand, model, year, price, status);
                long result = carDAO.insertCar(car);

                if (result != -1) {
                    Toast.makeText(this, "Car added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to add car", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        carDAO.close();
        super.onDestroy();
    }

    private boolean validateInput(String brand, String model, String year, String price, String status) {
        boolean isValid = true;

        // Validate brand
        InputValidator.ValidationResult brandResult = InputValidator.validateBrand(brand);
        if (!brandResult.isValid()) {
            brandInput.setError(brandResult.getErrorMessage());
            Log.e("Validation", "Brand error: " + brandResult.getErrorMessage());
            isValid = false;
        }

        // Validate model
        InputValidator.ValidationResult modelResult = InputValidator.validateModel(model);
        if (!modelResult.isValid()) {
            modelInput.setError(modelResult.getErrorMessage());
            Log.e("Validation", "Model error: " + modelResult.getErrorMessage());
            isValid = false;
        }

        // Continue for year, price, and status
        return isValid;
    }


}