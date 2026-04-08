package com.sourav.financemanager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.*;

import com.google.android.material.radiobutton.MaterialRadioButton;

import java.text.SimpleDateFormat;
import java.util.*;

public class AddTransactionActivity extends AppCompatActivity {

    LinearLayout llDatePicker;
    TextView tvDate;
    EditText etAmount, etTitle;
    Button btnAdd;

    String selectedCategory = "";
    String selectedType = "expense";

    Calendar selectedDate = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_add_transaction);

        llDatePicker = findViewById(R.id.ll_date_picker);
        tvDate = findViewById(R.id.tv_date);
        etAmount = findViewById(R.id.et_amount);
        etTitle = findViewById(R.id.et_title);
        btnAdd = findViewById(R.id.btn_add_transaction);

        LinearLayout walletItem = findViewById(R.id.wallet_item);
        LinearLayout expenseItem = findViewById(R.id.expanse_item);
        MaterialRadioButton rbIncome = findViewById(R.id.rb_income);
        MaterialRadioButton rbExpense = findViewById(R.id.rb_expense);
        LinearLayout backbutton = findViewById(R.id.btnBack);

        rbExpense.setChecked(true);
        rbIncome.setChecked(false);

        expenseItem.setSelected(true);
        walletItem.setSelected(false);

        selectedType = "expense";
        rbIncome.setClickable(false);
        rbExpense.setClickable(false);


        backbutton.setOnClickListener(v -> finish());


        updateDateText();

        llDatePicker.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(
                    this,
                    (view, y, m, d) -> {
                        selectedDate.set(y, m, d);
                        updateDateText();
                    },
                    selectedDate.get(Calendar.YEAR),
                    selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });
        etAmount.addTextChangedListener(new TextWatcher() {

            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().equals(current)) return;

                etAmount.removeTextChangedListener(this);

                try {
                    String input = s.toString();

                    if (input.isEmpty()) {
                        current = "";
                        etAmount.setText("");
                        etAmount.addTextChangedListener(this);
                        return;
                    }

                    String clean = input.replace(",", "");
                    String[] parts = clean.split("\\.");

                    String integerPart = parts[0];
                    String decimalPart = parts.length > 1 ? parts[1] : "";

                    String formattedInt = String.format("%,d", Long.parseLong(integerPart));

                    String formatted;

                    if (input.contains(".")) {
                        formatted = formattedInt + "." + decimalPart;
                    } else {
                        formatted = formattedInt;
                    }

                    current = formatted;
                    etAmount.setText(formatted);
                    etAmount.setSelection(formatted.length());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                etAmount.addTextChangedListener(this);
            }
        });

        walletItem.setOnClickListener(v -> {

            selectedType = "income";

            rbIncome.setChecked(true);
            rbExpense.setChecked(false);

            walletItem.setSelected(true);
            expenseItem.setSelected(false);
        });
        expenseItem.setOnClickListener(v -> {
            selectedType = "expense";

            rbIncome.setChecked(false);
            rbExpense.setChecked(true);

            walletItem.setSelected(false);
            expenseItem.setSelected(true);
        });


        LinearLayout catShopping  = findViewById(R.id.cat_shopping);
        LinearLayout catDining    = findViewById(R.id.cat_dining);
        LinearLayout catTransport = findViewById(R.id.cat_transport);
        LinearLayout catMovies    = findViewById(R.id.cat_movies);
        LinearLayout catRent      = findViewById(R.id.cat_rent);
        LinearLayout catHealth    = findViewById(R.id.cat_health);
        LinearLayout catGym       = findViewById(R.id.cat_gym);
        LinearLayout catOther     = findViewById(R.id.cat_other);

        List<LinearLayout> categories = Arrays.asList(
                catShopping, catDining, catTransport, catMovies,
                catRent, catHealth, catGym, catOther
        );

        for (LinearLayout cat : categories) {
            cat.setOnClickListener(v -> {

                for (LinearLayout c : categories) {
                    c.setSelected(false);

                    ImageView icon = (ImageView) c.getChildAt(0);
                    TextView label = (TextView) c.getChildAt(1);

                    icon.setColorFilter(ContextCompat.getColor(this, R.color.on_surface));
                    label.setTextColor(ContextCompat.getColor(this, R.color.on_surface));
                    label.setTypeface(null, Typeface.NORMAL);
                }

                v.setSelected(true);

                LinearLayout layout = (LinearLayout) v;

                ImageView icon = (ImageView) layout.getChildAt(0);
                TextView label = (TextView) layout.getChildAt(1);

                selectedCategory = label.getText().toString();

                icon.setColorFilter(ContextCompat.getColor(this, R.color.primary));
                label.setTextColor(ContextCompat.getColor(this, R.color.primary));
                label.setTypeface(null, Typeface.BOLD);
            });
        }

        btnAdd.setOnClickListener(v -> {

            String amountStr = etAmount.getText().toString().trim();
            String title = etTitle.getText().toString().trim();

            if (amountStr.isEmpty()) {
                etAmount.setError("Enter amount");
                return;
            }

            if (selectedCategory.isEmpty()) {
                Toast.makeText(this, "Select category", Toast.LENGTH_SHORT).show();
                return;
            }

            String cleanAmount = amountStr.replace(",", "");

            double amount;
            try {
                amount = Double.parseDouble(cleanAmount);
            } catch (Exception e) {
                etAmount.setError("Invalid amount");
                return;
            }

            long date = selectedDate.getTimeInMillis();

            Transaction t = new Transaction(
                    amount,
                    selectedCategory,
                    date,
                    title,
                    selectedType.toLowerCase()
            );

            AppDatabase db = AppDatabase.getInstance(this);

            new Thread(() -> {
                db.transactionDao().insert(t);

                runOnUiThread(() -> {

                    showSuccessDialog();

                });
            }).start();
        });

        getWindow().getDecorView().post(() -> {
            WindowInsetsControllerCompat controller =
                    new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
            controller.setAppearanceLightStatusBars(true);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
            return insets;
        });
        WindowInsetsControllerCompat controller =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        int nightModeFlags =
                getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            controller.setAppearanceLightStatusBars(false);
            controller.setAppearanceLightNavigationBars(false);
        } else {
            controller.setAppearanceLightStatusBars(true);
            controller.setAppearanceLightNavigationBars(true);
        }
    }

    private void showSuccessDialog() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success);
        dialog.setCancelable(false);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dialog.dismiss();
            finish();
        }, 2000);
    }
    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault());
        tvDate.setText(sdf.format(selectedDate.getTime()));
    }
}