package com.udacity.stockhawk.task;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        public final String strDate;
        public final long date;
        public final double value;

        public StockHistory(long date, double value) {
            this.strDate = dateFormat(date);
            this.date = date;
            this.value = value;
        }

        private static String dateFormat(long date) {
            return "";
        }
    }
}
