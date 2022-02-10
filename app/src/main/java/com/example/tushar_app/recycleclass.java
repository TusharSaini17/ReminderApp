package com.example.tushar_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class recycleclass extends RecyclerView.Adapter<recycleclass.EntryViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public recycleclass(Context context, Cursor cursor){
        mContext = context;
        mCursor = cursor;

    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText;
        public TextView dateText;
        public TextView timeText;
        public Button edit;

        public EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText=itemView.findViewById(R.id.title_item);
            timeText=itemView.findViewById(R.id.time_item);
            dateText=itemView.findViewById(R.id.date_item);
            edit=itemView.findViewById(R.id.itemClick);

        }
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.single_item,parent,false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        if(!mCursor.moveToPosition(position)){
            return;
        }

        @SuppressLint("Range") String title = mCursor.getString(mCursor.getColumnIndex(names.Entries.COLUMN_TITLE));
        @SuppressLint("Range") String date = mCursor.getString(mCursor.getColumnIndex(names.Entries.COLUMN_DATE));
        @SuppressLint("Range") String time = mCursor.getString(mCursor.getColumnIndex(names.Entries.COLUMN_TIME));
        @SuppressLint("Range") long id =mCursor.getLong(mCursor.getColumnIndex(names.Entries._ID));

        holder.titleText.setText(title);
        holder.dateText.setText(date);
        holder.timeText.setText(time);
        holder.itemView.setTag(id);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title",names.Entries.COLUMN_TITLE);
                bundle.putString("date",names.Entries.COLUMN_DATE);
                bundle.putString("time",names.Entries.COLUMN_TIME);
                Intent intent = new Intent(mContext,second_page.class);
                intent.putExtra("editdata",bundle);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor){
        if(mCursor != null){
            mCursor.close();
        }
        mCursor = newCursor;

        if (newCursor != null){
            notifyDataSetChanged();
        }
    }

}
