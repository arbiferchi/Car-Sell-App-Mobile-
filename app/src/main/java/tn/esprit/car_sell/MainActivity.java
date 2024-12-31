package tn.esprit.car_sell;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.graphics.pdf.PdfDocument;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CarAdapter.OnCarClickListener {
    private CarDAO carDAO;
    private CarAdapter adapter;
    private RecyclerView recyclerView;
    private Button generatePdfButton;
    private SearchView searchView;
    private List<Car> carList;
    private List<Car> filteredCarList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize database
        carDAO = new CarDAO(this);
        carDAO.open();

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carList = new ArrayList<>();
        filteredCarList = new ArrayList<>();
        adapter = new CarAdapter(filteredCarList, this);
        recyclerView.setAdapter(adapter);

        // Setup FAB for adding cars
        FloatingActionButton addButton = findViewById(R.id.addCarButton);
        addButton.setOnClickListener(v -> showAddCarDialog());

        // Button for generating PDF
        generatePdfButton = findViewById(R.id.generatePdfButton);
        generatePdfButton.setOnClickListener(v -> generatePdf());

        // Setup SearchView
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterCars(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCars(newText);
                return false;
            }
        });

        // Load initial data
        loadCars();
    }


    private void loadCars() {
        carList = carDAO.getAllCars();
        filteredCarList.clear();
        filteredCarList.addAll(carList);
        adapter.updateData(filteredCarList);
    }
    // Method to filter cars based on the search query
    private void filterCars(String query) {
        filteredCarList.clear();
        if (query.isEmpty()) {
            filteredCarList.addAll(carList);
        } else {
            for (Car car : carList) {
                if (car.getBrand().toLowerCase().contains(query.toLowerCase()) ||
                        car.getModel().toLowerCase().contains(query.toLowerCase())) {
                    filteredCarList.add(car);
                }
            }
        }
        adapter.updateData(filteredCarList);
    }
    private void showAddCarDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_car, null);
        EditText brandInput = dialogView.findViewById(R.id.brandInput);
        EditText modelInput = dialogView.findViewById(R.id.modelInput);
        EditText yearInput = dialogView.findViewById(R.id.yearInput);
        EditText priceInput = dialogView.findViewById(R.id.priceInput);
        EditText statusInput = dialogView.findViewById(R.id.statusInput);

        new MaterialAlertDialogBuilder(this)
                .setTitle("Add New Car")
                .setView(dialogView)
                .setPositiveButton("Add", (dialog, which) -> {
                    try {
                        Car car = new Car(
                                brandInput.getText().toString(),
                                modelInput.getText().toString(),
                                Integer.parseInt(yearInput.getText().toString()),
                                Double.parseDouble(priceInput.getText().toString()),
                                statusInput.getText().toString()
                        );
                        carDAO.insertCar(car);
                        loadCars();
                        Toast.makeText(this, "Car added successfully", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showEditCarDialog(Car car) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_car, null);
        EditText brandInput = dialogView.findViewById(R.id.brandInput);
        EditText modelInput = dialogView.findViewById(R.id.modelInput);
        EditText yearInput = dialogView.findViewById(R.id.yearInput);
        EditText priceInput = dialogView.findViewById(R.id.priceInput);
        EditText statusInput = dialogView.findViewById(R.id.statusInput);

        // Pre-fill existing data
        brandInput.setText(car.getBrand());
        modelInput.setText(car.getModel());
        yearInput.setText(String.valueOf(car.getYear()));
        priceInput.setText(String.valueOf(car.getPrice()));
        statusInput.setText(car.getStatus());

        new MaterialAlertDialogBuilder(this)
                .setTitle("Edit Car")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    try {
                        car.setBrand(brandInput.getText().toString());
                        car.setModel(modelInput.getText().toString());
                        car.setYear(Integer.parseInt(yearInput.getText().toString()));
                        car.setPrice(Double.parseDouble(priceInput.getText().toString()));
                        car.setStatus(statusInput.getText().toString());

                        carDAO.updateCar(car);
                        loadCars();
                        Toast.makeText(this, "Car updated successfully", Toast.LENGTH_SHORT).show();
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDeleteConfirmationDialog(Car car) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete Car")
                .setMessage("Are you sure you want to delete this car?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    carDAO.deleteCar(car.getId());
                    loadCars();
                    Toast.makeText(this, "Car deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onEditClick(Car car) {
        showEditCarDialog(car);
    }

    @Override
    public void onDeleteClick(Car car) {
        showDeleteConfirmationDialog(car);
    }

    @Override
    protected void onDestroy() {
        carDAO.close();
        super.onDestroy();
    }

    // Method to generate PDF
    private void generatePdf() {
        PdfDocument document = new PdfDocument();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(12);

        // Define the page size (A4)
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Add a title to the document
        canvas.drawText("Car List", 50, 50, paint);

        // Set the starting Y position for the list of cars
        int yPosition = 100;

        // Loop through the list of cars and add their details to the PDF
        for (Car car : carDAO.getAllCars()) {
            canvas.drawText("Brand: " + car.getBrand(), 50, yPosition, paint);
            yPosition += 20;
            canvas.drawText("Model: " + car.getModel(), 50, yPosition, paint);
            yPosition += 20;
            canvas.drawText("Year: " + car.getYear(), 50, yPosition, paint);
            yPosition += 20;
            canvas.drawText("Price: " + car.getPrice(), 50, yPosition, paint);
            yPosition += 20;
            canvas.drawText("Status: " + car.getStatus(), 50, yPosition, paint);
            yPosition += 40;  // Add space before the next car entry

            // Check if there's enough space left on the page for the next car
            if (yPosition > 800) {
                document.finishPage(page); // Finish the current page
                page = document.startPage(pageInfo); // Start a new page
                canvas = page.getCanvas(); // Get the new page's canvas
                yPosition = 100; // Reset Y position for the new page
            }
        }

        // Finish the last page
        document.finishPage(page);

        // Define the output file path
        String filePath = getExternalFilesDir(null) + "/car_list.pdf";
        File file = new File(filePath);

        try {
            // Write the document to a file
            FileOutputStream outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);
            outputStream.close();

            // Notify the user that the PDF was generated successfully
            Toast.makeText(this, "PDF generated at: " + filePath, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF", Toast.LENGTH_SHORT).show();
        }

        // Close the document
        document.close();
    }
}