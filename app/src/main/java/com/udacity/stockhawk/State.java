package com.udacity.stockhawk;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.udacity.stockhawk.State.STATUS_EMPTY_INPUT;
import static com.udacity.stockhawk.State.STATUS_OK;
import static com.udacity.stockhawk.State.STATUS_NO_NETWORK;
import static com.udacity.stockhawk.State.STATUS_SERVER_DOWN;
import static com.udacity.stockhawk.State.STATUS_UNKNOWN;

/**
 * Created by DELL-INSPIRON on 3/28/2017.
 */

@Retention(RetentionPolicy.SOURCE)
@IntDef({STATUS_OK, STATUS_NO_NETWORK, STATUS_SERVER_DOWN, STATUS_EMPTY_INPUT, STATUS_UNKNOWN})
public @interface State {
    int STATUS_OK = 0;
    int STATUS_NO_NETWORK = 1;
    int STATUS_SERVER_DOWN = 2;
    int STATUS_EMPTY_INPUT = 3;
    int STATUS_UNKNOWN = 7;
}
