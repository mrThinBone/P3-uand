package com.udacity.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.Utility;
import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.data.PrefUtils;

/**
 * Created by DELL-INSPIRON on 3/29/2017.
 */

public class StockHawkWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private int defaultTextColor = Color.WHITE;
            private int invalidTextColor;
            private Cursor data = null;

            @Override
            public void onCreate() {
                invalidTextColor = getResources().getColor(R.color.material_red_700);
            }

            @Override
            public void onDataSetChanged() {
                if(data != null) data.close();

                // in order to get data from Content Provider, then we need identity token
                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(Contract.Quote.URI,
                        Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                        null, null, Contract.Quote.COLUMN_SYMBOL);
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if(data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int pos) {
                if(pos == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(pos)) {
                    return null;
                }
                String symbol = data.getString(Contract.Quote.POSITION_SYMBOL);
                float price = data.getFloat(Contract.Quote.POSITION_PRICE);
                float rawAbsoluteChange = data.getFloat(Contract.Quote.POSITION_ABSOLUTE_CHANGE);
                float percentageChange = data.getFloat(Contract.Quote.POSITION_PERCENTAGE_CHANGE);
                String stockHistory = data.getString(Contract.Quote.POSITION_HISTORY);

                RemoteViews views = getLoadingView();
                // symbol
                views.setTextViewText(R.id.symbol, symbol);
                views.setContentDescription(R.id.symbol, getString(R.string.a11y_stock_symbol, symbol));
                // price
                if(price < 0) {
                    views.setViewVisibility(R.id.change, View.GONE);
                    views.setTextColor(R.id.price, invalidTextColor);
                    views.setTextViewText(R.id.price, getString(R.string.alert_invalid_stock));
                    views.setContentDescription(R.id.price, getString(R.string.a11y_invalid_stock));
                } else {
                    views.setViewVisibility(R.id.change, View.VISIBLE);
                    views.setTextColor(R.id.price, defaultTextColor);
                    views.setTextViewText(R.id.price, Utility.priceFormat(price));
                    views.setContentDescription(R.id.price, getString(R.string.a11y_stock_price, price));
                }

                // change
                if (rawAbsoluteChange > 0) {
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                } else {
                    views.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                }
                String change = Utility.dollarFormatWithPlus(rawAbsoluteChange);
                String percentage = Utility.percentageFormat(percentageChange / 100);
                if (PrefUtils.getDisplayMode(getApplicationContext())
                        .equals(getString(R.string.pref_display_mode_absolute_key))) {
                    views.setTextViewText(R.id.change, change);
                    views.setContentDescription(R.id.change, getString(R.string.a11y_stock_change, rawAbsoluteChange));
                } else {
                    views.setTextViewText(R.id.change, percentage);
                    views.setContentDescription(R.id.change, getString(R.string.a11y_stock_change_pct, percentageChange));
                }

                // on click
                Bundle extras = new Bundle();
                extras.putString("symbol", symbol);
                extras.putString("history", stockHistory);
                final Intent fillInIntent = new Intent();
                fillInIntent.putExtras(extras);
                views.setOnClickFillInIntent(R.id.stock_quote_item, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.list_item_quote);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int i) {
                return data != null && data.moveToPosition(i) ? data.getLong(Contract.Quote.POSITION_ID) : i;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
