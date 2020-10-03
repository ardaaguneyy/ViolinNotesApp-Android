package com.example.kemannotalari.ui.notifications;


import android.content.Context;

import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kemannotalari.AddNoteActivity;

import com.example.kemannotalari.R;

import com.example.kemannotalari.adapters.ReCyclerViewAdapter2;

import java.util.ArrayList;



public class NotificationsFragment extends Fragment {
ArrayList<String> names;
ArrayList<Bitmap> bitmaps;
EditText editText;
ArrayAdapter arrayAdapter;
ListView listView;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //recyclerview
        bitmaps = new ArrayList<>();
        names = new ArrayList<>();

        getdatafromsqlite(view,names,bitmaps);
        System.out.println(names);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerviewitem);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        final ReCyclerViewAdapter2 adapter = new ReCyclerViewAdapter2(names,bitmaps);
        recyclerView.setAdapter(adapter);

        //searchnotes
        listviewadapter(view);

    }
    public void getdatafromsqlite(View view , ArrayList<String> namelist , ArrayList<Bitmap> bitmaparraylist){
        try {
            SQLiteDatabase database = view.getContext().openOrCreateDatabase("Violinnotes", Context.MODE_PRIVATE,null);
            Cursor cursor = database.rawQuery("SELECT * FROM violinnotes",null);
            int nameIx = cursor.getColumnIndex("name");
            int imageIx = cursor.getColumnIndex("image");
            while (cursor.moveToNext()){
                namelist.add(cursor.getString(nameIx));
                byte [] bytes = cursor.getBlob(imageIx);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmaparraylist.add(bitmap);
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }
    public void listviewadapter(final View view){
        listView = view.findViewById(R.id.listview);
        listView.setVisibility(View.INVISIBLE);
        ArrayList<String> names2 = new ArrayList<>();
        names2.addAll(names);
        arrayAdapter = new ArrayAdapter(view.getContext(),android.R.layout.simple_list_item_1, names2);
        listView.setAdapter(arrayAdapter);
        editText = view.findViewById(R.id.searchtext2);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
             arrayAdapter.getFilter().filter(s);
             listView.setVisibility(View.VISIBLE);


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches("")){
                    listView.setVisibility(View.INVISIBLE);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                System.out.println(arrayAdapter.getItem(position));
                intent.putExtra("item_name", String.valueOf(arrayAdapter.getItem(position)));
                startActivity(intent);


            }
        });

    }

}