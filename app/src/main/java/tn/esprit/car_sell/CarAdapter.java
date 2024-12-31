package tn.esprit.car_sell;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private List<Car> cars;
    private OnCarClickListener listener;

    public interface OnCarClickListener {
        void onEditClick(Car car);
        void onDeleteClick(Car car);
    }

    public CarAdapter(List<Car> cars, OnCarClickListener listener) {
        this.cars = cars;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = cars.get(position);
        holder.bind(car);
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    public void updateData(List<Car> newCars) {
        this.cars = newCars;
        notifyDataSetChanged();
    }

    class CarViewHolder extends RecyclerView.ViewHolder {
        private TextView brandModelText;
        private TextView yearText;
        private TextView priceText;
        private TextView statusText;
        private Button editButton;
        private Button deleteButton;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            brandModelText = itemView.findViewById(R.id.brandModelText);
            yearText = itemView.findViewById(R.id.yearText);
            priceText = itemView.findViewById(R.id.priceText);
            statusText = itemView.findViewById(R.id.statusText);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bind(final Car car) {
            brandModelText.setText(String.format("%s %s", car.getBrand(), car.getModel()));
            yearText.setText(String.valueOf(car.getYear()));
            priceText.setText(String.format(Locale.getDefault(), "$%.2f", car.getPrice()));
            statusText.setText(car.getStatus());

            editButton.setOnClickListener(v -> listener.onEditClick(car));
            deleteButton.setOnClickListener(v -> listener.onDeleteClick(car));
        }
    }
}