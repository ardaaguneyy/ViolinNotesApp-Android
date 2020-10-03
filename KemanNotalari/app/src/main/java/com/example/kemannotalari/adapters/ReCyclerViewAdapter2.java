package com.example.kemannotalari.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.kemannotalari.AddNoteActivity;
import com.example.kemannotalari.R;

import com.example.kemannotalari.Violinnoteclass;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class ReCyclerViewAdapter2 extends RecyclerView.Adapter<ReCyclerViewAdapter2.NoteHolder> {
    ArrayList<String> names;
    ArrayList<Bitmap> images;

    public ReCyclerViewAdapter2(ArrayList<String> names, ArrayList<Bitmap> images) {
        this.names = names;
        this.images = images;
    }

    @NonNull
    @Override
    public ReCyclerViewAdapter2.NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
       View view = layoutInflater.inflate(R.layout.recyclerlayout,parent,false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReCyclerViewAdapter2.NoteHolder holder, final int position) {
     holder.textView.setText(names.get(position));
     holder.imageView.setImageBitmap(images.get(position));
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                PopupMenu popup = new PopupMenu(v.getContext(),holder.imageView);
                popup.getMenuInflater().inflate(R.menu.edit_pop_up_menu,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(final MenuItem item) {
                        if (item.getItemId()== R.id.edit){
                            Intent intent = new Intent(v.getContext(), AddNoteActivity.class);
                            intent.putExtra("edititem",names.get(position));
                            v.getContext().startActivity(intent);
                        }if (item.getItemId()==R.id.delete){
                            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                            alert.setTitle("Emin Misin");
                            alert.setCancelable(false);
                            alert.setMessage("Seçilen Nota Silinecek : " + holder.textView.getText().toString());
                            alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SQLiteDatabase database = v.getContext().openOrCreateDatabase("Violinnotes", Context.MODE_PRIVATE,null);
                                    Violinnoteclass violinnoteclass = new Violinnoteclass(null,null,null);
                                    violinnoteclass.deletesqlite(database,holder.textView.getText().toString(),v.getContext());
                                }
                            });
                            alert.show();
                        }

                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public static class NoteHolder extends RecyclerView.ViewHolder {
        TextView textView;
        PhotoView imageView;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.imageView5);
            textView = itemView.findViewById(R.id.textView3);

        }
    }


}
