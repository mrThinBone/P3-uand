package com.udacity.stockhawk.task;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.udacity.stockhawk.task.ReadHistoryTask.StockHistory;

/**
 * Created by DELL-INSPIRON on 3/28/2017.
 */

public class ReadHistoryTask extends AsyncTaskLoader<List<StockHistory>> {

    private String strHistoryData;

    public ReadHistoryTask(Context context, String data) {
        super(context);
        this.strHistoryData = data;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<StockHistory> loadInBackground() {
        List<StockHistory> result = new ArrayList<>();

        if(strHistoryData == null || strHistoryData.equals("unknown"))
            return result;

        String[] histories = strHistoryData.split("\n");
        for (String history : histories) {
            String[] data = history.split(",");
            result.add(new StockHistory(Long.valueOf(data[0]), Double.valueOf(data[1])));
        }
        Collections.reverse(result);
        return result;
    }

    public static class StockHistory {
        private static final boolean localeUS = Locale.getDefault() == Locale.US;
        private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy", Locale.US);
        private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd MMM  yyyy", Locale.US);
        private static final SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        private static final NumberFormat formatter = new DecimalFormat("#0.00");


        public final String strDate;
        public final double value;
        public final String highlightValue;

        public StockHistory(long timeInMillis, double value) {
            Date date = new Date(timeInMillis);
            this.strDate = dateFormat.format(date);
            this.value = value;
            if(localeUS) {
                highlightValue = formatter.format(value) + "$ at " + dateFormat2.format(date);
            } else {
                highlightValue = formatter.format(value) + "$ at " + dateFormat3.format(date);
            }
        }
    }
}
