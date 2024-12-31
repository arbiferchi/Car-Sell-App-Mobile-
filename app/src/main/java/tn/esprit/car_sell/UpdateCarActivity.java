package tn.esprit.car_sell;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class UpdateCarActivity extends AppCompatActivity {
    private CarDAO carDAO;
    private TextInputEditText brandInput;
    private TextInputEditText modelInput;
    private TextInputEditText yearInput;
    private TextInputEditText priceInput;
    private TextInputEditText statusInput;
    private Car car;
    public static final String EXTRA_CAR_ID = "car_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_car);

        // Enable back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Edit Car");
        }

        // Initialize database
        carDAO = new CarDAO(this);
        carDAO.open();

        // Initialize views
        initializeViews();

        // Get car ID from intent
        long carId = getIntent().getLongExtra(EXTRA_CAR_ID, -1);
        if (carId == -1) {
            Toast.makeText(this, "Error loading car data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load car data
        car = carDAO.getCarById(carId);
        if (car != null) {
            populateFields();
        }

        Button updateButton = findViewById(R.id.updateButton);
        updateButton.setOnClickListener(v -> updateCar());
    }

    private void initializeViews() {
        brandInput = findViewById(R.id.brandInput);
        modelInput = findViewById(R.id.modelInput);
        yearInput = findViewById(R.id.yearInput);
        priceInput = findViewById(R.id.priceInput);
        statusInput = findViewById(R.id.statusInput);
    }

    private void populateFields() {
        brandInput.setText(car.getBrand());
        modelInput.setText(car.getModel());
        yearInput.setText(String.valueOf(car.getYear()));
        priceInput.setText(String.valueOf(car.getPrice()));
        statusInput.setText(car.getStatus());
    }

    private void updateCar() {
        String brand = brandInput.getText().toString().trim();
        String model = modelInput.getText().toString().trim();
        String yearStr = yearInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String status = statusInput.getText().toString().trim();

        if (validateInput(brand, model, yearStr, priceStr, status)) {
            try {
                int year = Integer.parseInt(yearStr);
                double price = Double.parseDouble(priceStr);

                car.setBrand(brand);
                car.setModel(model);
                car.setYear(year);
                car.setPrice(price);
                car.setStatus(status);

                int result = carDAO.updateCar(car);

                if (result > 0) {
                    Toast.makeText(this, "Car updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to update car", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String brand, String model, String year, String price, String status) {
        if (brand.isEmpty()) {
            brandInput.setError("Brand is required");
            return false;
        }
        if (model.isEmpty()) {
            modelInput.setError("Model is required");
            return false;
        }
        if (year.isEmpty()) {
            yearInput.setError("Year is required");
            return false;
        }
        if (price.isEmpty()) {
            priceInput.setError("Price is required");
            return false;
        }
        if (status.isEmpty()) {
            statusInput.setError("Status is required");
            return false;
        }
        return true;
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
}