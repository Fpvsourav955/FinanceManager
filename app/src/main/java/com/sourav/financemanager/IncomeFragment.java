package com.sourav.financemanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class IncomeFragment extends Fragment {

    RecyclerView recyclerView;
    TransactionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new TransactionAdapter();
        recyclerView.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        new Thread(() -> {
            List<Transaction> list =
                    AppDatabase.getInstance(getContext())
                            .transactionDao()
                            .getIncomeTransactions();

            requireActivity().runOnUiThread(() -> {
                adapter.setData(list);
            });
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}