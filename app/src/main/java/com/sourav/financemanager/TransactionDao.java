package com.sourav.financemanager;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TransactionDao {

    @Insert
    void insert(Transaction transaction);

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    List<Transaction> getAllTransactions();

    @Query("SELECT SUM(amount) FROM transactions WHERE type='income'")
    Double getTotalIncome();

    @Query("SELECT SUM(amount) FROM transactions WHERE type='expense'")
    Double getTotalExpense();
    @Query("SELECT category, SUM(amount) as total FROM transactions WHERE type='expense' GROUP BY category")
    List<CategoryTotal> getCategoryTotals();
    @Query("""
SELECT 
   ((strftime('%w', date/1000, 'unixepoch', 'localtime') + 6) % 7) as day,
    SUM(amount) as total
FROM transactions
WHERE type='expense'
AND date >= (strftime('%s','now','-6 days') * 1000)
GROUP BY day
""")
    List<DailyExpense> getWeeklyExpenses();
    @Query("SELECT * FROM transactions WHERE LOWER(type) = 'expense' ORDER BY date DESC")
    List<Transaction> getExpenseTransactions();

    @Query("SELECT * FROM transactions WHERE LOWER(type) = 'income' ORDER BY date DESC")
    List<Transaction> getIncomeTransactions();
}