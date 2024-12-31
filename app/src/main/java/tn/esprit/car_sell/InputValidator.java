package tn.esprit.car_sell;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class InputValidator {
    private static final List<String> VALID_STATUSES = Arrays.asList("Available", "Sold", "Pending");
    private static final Pattern BRAND_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s-]{2,30}$");
    private static final Pattern MODEL_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s-]{2,30}$");
    private static final double MAX_PRICE = 10000000.0; // 10 million
    private static final double MIN_PRICE = 100.0; // Minimum reasonable price

    public static ValidationResult validateBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            return new ValidationResult(false, "Brand is required");
        }
        if (!BRAND_PATTERN.matcher(brand).matches()) {
            return new ValidationResult(false, "Brand must be 2-30 characters long and contain only letters, numbers, spaces, and hyphens");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validateModel(String model) {
        if (model == null || model.trim().isEmpty()) {
            return new ValidationResult(false, "Model is required");
        }
        if (!MODEL_PATTERN.matcher(model).matches()) {
            return new ValidationResult(false, "Model must be 2-30 characters long and contain only letters, numbers, spaces, and hyphens");
        }
        return new ValidationResult(true, null);
    }

    public static ValidationResult validateYear(String yearStr) {
        if (yearStr == null || yearStr.trim().isEmpty()) {
            return new ValidationResult(false, "Year is required");
        }
        try {
            int year = Integer.parseInt(yearStr);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (year < 1900 || year > currentYear + 1) { // Allow next year's models
                return new ValidationResult(false, "Year must be between 1900 and " + (currentYear + 1));
            }
            return new ValidationResult(true, null);
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Year must be a valid number");
        }
    }

    public static ValidationResult validatePrice(String priceStr) {
        if (priceStr == null || priceStr.trim().isEmpty()) {
            return new ValidationResult(false, "Price is required");
        }
        try {
            double price = Double.parseDouble(priceStr);
            if (price < MIN_PRICE) {
                return new ValidationResult(false, "Price must be at least " + MIN_PRICE);
            }
            if (price > MAX_PRICE) {
                return new ValidationResult(false, "Price cannot exceed " + MAX_PRICE);
            }
            return new ValidationResult(true, null);
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Price must be a valid number");
        }
    }

    public static ValidationResult validateStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return new ValidationResult(false, "Status is required");
        }
        if (!VALID_STATUSES.contains(status)) {
            return new ValidationResult(false, "Status must be one of: " + String.join(", ", VALID_STATUSES));
        }
        return new ValidationResult(true, null);
    }

    public static class ValidationResult {
        private final boolean isValid;
        private final String errorMessage;

        public ValidationResult(boolean isValid, String errorMessage) {
            this.isValid = isValid;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() {
            return isValid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }
}