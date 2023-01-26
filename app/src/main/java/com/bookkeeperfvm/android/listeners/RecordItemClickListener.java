package com.bookkeeperfvm.android.listeners;

import android.widget.ImageView;

import com.bookkeeperfvm.android.models.Record;

public interface RecordItemClickListener {
    void onRecordItemClick(Record record, ImageView imageView);
}
