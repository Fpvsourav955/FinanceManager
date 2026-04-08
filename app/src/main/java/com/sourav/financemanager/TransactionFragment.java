package com.sourav.financemanager;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class TransactionFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    FloatingActionButton fab;
    TextView tvBalance;

    private double currentDisplayedBalance = 0.0;

    public TransactionFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab      = view.findViewById(R.id.fabAdd);
        tabLayout  = view.findViewById(R.id.tabLayout);
        viewPager  = view.findViewById(R.id.viewPager);
        tvBalance  = view.findViewById(R.id.totalbanace);

        fab.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AddTransactionActivity.class)));

        TranVPAdapter adapter = new TranVPAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        loadBalance();
    }

    private void loadBalance() {
        new Thread(() -> {

            AppDatabase db = AppDatabase.getInstance(getContext());

            Double income  = db.transactionDao().getTotalIncome();
            Double expense = db.transactionDao().getTotalExpense();

            if (income  == null) income  = 0.0;
            if (expense == null) expense = 0.0;

            final double newBalance = income - expense;

            requireActivity().runOnUiThread(() ->
                    animateBalanceCountUp(currentDisplayedBalance, newBalance));

        }).start();
    }


    private void animateBalanceCountUp(double from, double to) {
        if (tvBalance == null) return;

        ValueAnimator animator = ValueAnimator.ofFloat((float) from, (float) to);
        animator.setDuration(1200);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(anim -> {
            float value = (float) anim.getAnimatedValue();
            tvBalance.setText(formatAmount(value));
        });


        animator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                currentDisplayedBalance = to;

                tvBalance.setText(formatAmount(to));
            }
        });

        animator.start();
    }

    private String formatAmount(double value) {
        return String.format("₹%,.2f", value);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBalance();
    }
}