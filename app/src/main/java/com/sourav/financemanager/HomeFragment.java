package com.sourav.financemanager;

import android.annotation.SuppressLint;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.*;

public class HomeFragment extends Fragment {

    TextView tvIncome, tvExpense, tvBalance, tvMonth, tvTotalSpent;
    RecyclerView recyclerView;
    LinearLayout categoryContainer;
    FloatingActionButton fab;
    TransactionAdapter adapter;
    CircularProgressIndicator donutChart;

    boolean isExpanded = false;
    List<Transaction> fullList = new ArrayList<>();

    View barMon, barTue, barWed, barThu, barFri, barSat, barSun;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryContainer = view.findViewById(R.id.categoryContainer);
        donutChart = view.findViewById(R.id.donut_chart);
        tvTotalSpent = view.findViewById(R.id.tvTotalSpent);

        tvIncome = view.findViewById(R.id.tvIncome);
        tvExpense = view.findViewById(R.id.tvExpense);
        tvBalance = view.findViewById(R.id.tvBalance);
        tvMonth = view.findViewById(R.id.tvMonth);

        barMon = view.findViewById(R.id.barMon);
        barTue = view.findViewById(R.id.barTue);
        barWed = view.findViewById(R.id.barWed);
        barThu = view.findViewById(R.id.barThu);
        barFri = view.findViewById(R.id.barFri);
        barSat = view.findViewById(R.id.barSat);
        barSun = view.findViewById(R.id.barSun);

        recyclerView = view.findViewById(R.id.recyclerView);
        fab = view.findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);

        TextView btnSeeAll = view.findViewById(R.id.btnSeeAll);

        btnSeeAll.setOnClickListener(v -> {
            if (fullList == null || fullList.isEmpty()) return;
            isExpanded = !isExpanded;
            btnSeeAll.setText(isExpanded ? "Show Less" : "See All");
            updateTransactionList();
        });

        String currentMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(new Date());
        tvMonth.setText(currentMonth);

        loadData();

        fab.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddTransactionActivity.class));
        });
    }

    @SuppressLint("SetTextI18n")
    private void loadData() {

        AppDatabase db = AppDatabase.getInstance(getContext());

        List<DailyExpense> dailyList = db.transactionDao().getWeeklyExpenses();
        Double income = db.transactionDao().getTotalIncome();
        Double expense = db.transactionDao().getTotalExpense();

        if (income == null) income = 0.0;
        if (expense == null) expense = 0.0;

        final double finalIncome = income;
        final double finalExpense = expense;
        final double finalBalance = income - expense;


        int progress = income == 0 ? 0 : (int) ((expense / income) * 100);
        if (progress > 100) progress = 100;
        animateDonut(progress);


        animateCountUp(tvTotalSpent,  0, finalExpense,  1200, "₹", "");
        animateCountUp(tvIncome,      0, finalIncome,   1000, "+₹", "");
        animateCountUp(tvExpense,     0, finalExpense,  1000, "-₹", "");
        animateCountUp(tvBalance,     0, finalBalance,  1000, "₹", "");


        List<CategoryTotal> categoryList = db.transactionDao().getCategoryTotals();
        Collections.sort(categoryList, (a, b) -> Double.compare(b.total, a.total));

        categoryContainer.removeAllViews();

        double total = 0;
        for (CategoryTotal c : categoryList) total += c.total;

        double othersTotal = 0;
        for (int i = 0; i < categoryList.size(); i++) {
            if (i < 3) {
                addCategoryItem(categoryList.get(i), total);
            } else {
                othersTotal += categoryList.get(i).total;
            }
        }

        if (othersTotal > 0) {
            CategoryTotal others = new CategoryTotal();
            others.category = "Others";
            others.total = othersTotal;
            addCategoryItem(others, total);
        }

        fullList = db.transactionDao().getAllTransactions();
        if (fullList == null) fullList = new ArrayList<>();
        updateTransactionList();


        double[] week = new double[7];
        for (DailyExpense d : dailyList) {
            int index = d.day;
            week[index] = d.total;
        }

        double max = 0;
        for (double v : week) if (v > max) max = v;
        if (max == 0) max = 1;

        final double finalMax = max;


        View[] bars = {barMon, barTue, barWed, barThu, barFri, barSat, barSun};
        for (int i = 0; i < bars.length; i++) {
            final int idx = i;
            new Handler(Looper.getMainLooper()).postDelayed(() ->
                            animateBar(bars[idx], week[idx], finalMax),
                    idx * 80L
            );
        }
    }


    private void animateBar(View bar, double value, double max) {
        if (bar == null) return;

        int maxHeightPx = dpToPx(120);
        int startHeight  = dpToPx(2);
        int targetHeight;

        if (value == 0) {
            targetHeight = startHeight;
        } else {
            targetHeight = (int) ((value / max) * maxHeightPx);
            if (targetHeight < dpToPx(8)) targetHeight = dpToPx(8);
        }

        bar.setBackgroundResource(value == max ?
                R.drawable.bg_bar_active :
                R.drawable.bg_bar_inactive);

        ViewGroup.LayoutParams params = bar.getLayoutParams();
        params.height = startHeight;
        bar.setLayoutParams(params);

        ValueAnimator animator = ValueAnimator.ofInt(startHeight, targetHeight);
        animator.setDuration(600);
        animator.setInterpolator(new DecelerateInterpolator(1.5f));
        animator.addUpdateListener(anim -> {
            params.height = (int) anim.getAnimatedValue();
            bar.setLayoutParams(params);
        });
        animator.start();
    }

    private void animateDonut(int targetProgress) {
        donutChart.setProgressCompat(0, false);

        ValueAnimator animator = ValueAnimator.ofInt(0, targetProgress);
        animator.setDuration(1200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(anim -> {
            int val = (int) anim.getAnimatedValue();
            donutChart.setProgressCompat(val, false);
        });
        animator.start();
    }


    private void animateCountUp(TextView tv,
                                double from, double to,
                                long durationMs,
                                String prefix, String suffix) {
        if (tv == null) return;

        ValueAnimator animator = ValueAnimator.ofFloat((float) from, (float) to);
        animator.setDuration(durationMs);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(anim -> {
            float value = (float) anim.getAnimatedValue();

            tv.setText(prefix + String.format("%,.2f", value) + suffix);
        });
        animator.start();
    }

    private void updateTransactionList() {
        if (adapter == null || fullList == null) return;

        List<Transaction> displayList = new ArrayList<>();
        if (isExpanded) {
            displayList.addAll(fullList);
        } else {
            for (int i = 0; i < fullList.size() && i < 5; i++) {
                displayList.add(fullList.get(i));
            }
        }
        adapter.setData(displayList);
    }

    private void addCategoryItem(CategoryTotal item, double totalAmount) {
        View v = LayoutInflater.from(getContext())
                .inflate(R.layout.item_category, categoryContainer, false);

        TextView tvCategory = v.findViewById(R.id.tvCategory);
        TextView tvPercent  = v.findViewById(R.id.tvPercent);
        View colorDot       = v.findViewById(R.id.colorDot);

        tvCategory.setText(item.category);

        int percent = totalAmount == 0 ? 0 : (int) ((item.total / totalAmount) * 100);
        tvPercent.setText(percent + "%");

        colorDot.setBackgroundResource(R.drawable.bg_dot_primary);

        categoryContainer.addView(v);
    }

    private int dpToPx(int dp) {
        if (getContext() == null) return dp;
        return (int) (dp * getContext().getResources().getDisplayMetrics().density);
    }

    private String formatAmount(double value) {
        return String.format("₹%,.2f", value);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}