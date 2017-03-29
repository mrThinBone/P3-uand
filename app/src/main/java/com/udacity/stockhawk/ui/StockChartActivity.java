package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.task.ReadHistoryTask;
import com.udacity.stockhawk.task.ReadHistoryTask.StockHistory;

import java.util.ArrayList;
import java.util.List;

public class StockChartActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<StockHistory>> {

    private static final int LOADER_ID = 2003;

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_chart);
        Resources resources = getResources();

        int chartBackgroundColor = resources.getColor(R.color.chartBackground);
        int chartLabelColor = resources.getColor(R.color.chartLabel);
        int chartBackLinesColor = resources.getColor(R.color.chartBackLine);


        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setViewPortOffsets(50, 0, 50, 0);
        mChart.setBackgroundColor(chartBackgroundColor);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        mChart.setMaxHighlightDistance(300);

        XAxis x = mChart.getXAxis();
        x.setLabelCount(4, true);
        x.setTextColor(chartLabelColor);
        x.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        x.setDrawGridLines(true);
        x.setGridColor(chartBackLinesColor);
        x.setAxisLineColor(chartBackLinesColor);

        YAxis y = mChart.getAxisLeft();
//        y.setTypeface(mTfLight);
        y.setLabelCount(6, false);
        y.setTextColor(chartLabelColor);
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setDrawGridLines(true);
        y.setGridColor(chartBackLinesColor);
        y.setAxisLineColor(chartBackLinesColor);

        mChart.getAxisRight().setEnabled(false);

        mChart.getLegend().setEnabled(false);

        Bundle extras = getIntent().getExtras();
        String symbol = extras.getString("symbol");
        String historyData = extras.getString("history");

        getSupportActionBar().setTitle(symbol);

        Bundle args = new Bundle();
        args.putString("historyData", historyData);
        getSupportLoaderManager().initLoader(LOADER_ID, args, this);

    }

    private void setChartData(List<StockHistory> stockHistories) {

        ArrayList<Entry> yVals = new ArrayList<>();

        for (int i = 0; i < stockHistories.size(); i++) {

            yVals.add(new Entry(i, (float) stockHistories.get(i).value));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals, "DataSet 1");

            Resources resources = getResources();
            int highLightColor = resources.getColor(R.color.chartHighlight);
            int lineColor = resources.getColor(R.color.chartLine);

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            //set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.BLUE);
            set1.setHighLightColor(highLightColor);
            set1.setColor(lineColor);
            set1.setFillColor(highLightColor);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            // create a data object with the datasets
            LineData data = new LineData(set1);
//            data.setValueTypeface(mTfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            data.setValueTextColor(Color.WHITE);

            // set data
            mChart.setData(data);
        }
    }

    @Override
    public Loader<List<StockHistory>> onCreateLoader(int id, Bundle args) {
        return new ReadHistoryTask(this, args.getString("historyData"));
    }

    @Override
    public void onLoadFinished(Loader<List<StockHistory>> loader, List<StockHistory> data) {
        if(data.size() > 0) {
            setChartData(data);
            mChart.animateXY(2000, 2000);
            mChart.invalidate();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<StockHistory>> loader) {}
}
