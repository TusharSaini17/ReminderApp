package com.example.tushar_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    recycleclass mAdapter;
    SQLiteDatabase database;

    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper dbHelper=new dbHelper(getApplicationContext());
        database = dbHelper.getWritableDatabase();

        RecyclerView recyclerView =findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Cursor cursor = new dbHelper(getApplicationContext()).getAllItems();
        mAdapter = new recycleclass(this,cursor);
        recyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long)viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(recyclerView);


        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton2);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditpage();
            }
        });

    }

    private void updateItems(long tag) {
        Intent intent = new Intent(this,second_page.class);
        mAdapter.swapCursor(new dbHelper(getApplicationContext()).getAllItems());
        startActivity(intent);
    }

    private void openEditpage() {
        Intent intent = new Intent(this,second_page.class);
        startActivity(intent);
    }
    public void removeItem(long id){
        Cursor cursor = new dbHelper(getApplicationContext()).getAllItems();
        database.delete(names.Entries.TABLE_NAME, names.Entries._ID+"="+id,null);
        mAdapter.swapCursor(cursor);
    }

}