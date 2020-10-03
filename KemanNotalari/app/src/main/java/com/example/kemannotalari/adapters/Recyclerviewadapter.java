package com.example.kemannotalari.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kemannotalari.R;
import com.example.kemannotalari.Violinnoteclass;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class Recyclerviewadapter extends RecyclerView.Adapter<Recyclerviewadapter.Postholder> {
    ArrayList<String> emaillist;
    ArrayList<String> imagelist;
    ArrayList<String> commentlist;
    ViewGroup parentdeneme;
    View view;
    Bitmap bitmapsqlite;
    public Recyclerviewadapter(ArrayList<String> emaillist, ArrayList<String> imagelist, ArrayList<String> commentlist) {
        this.emaillist = emaillist;
        this.imagelist = imagelist;
        this.commentlist = commentlist;
    }

    @NonNull
    @Override
    public Recyclerviewadapter.Postholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.recyclerviewlayout,parent,false);
        parentdeneme = parent;
        return new Postholder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final Recyclerviewadapter.Postholder holder, final int position) {

        holder.emailtext.setText(emaillist.get(position));
        holder.commenttext.setText(commentlist.get(position));
          if (!imagelist.get(position).matches("null")){
        Picasso.get().load(imagelist.get(position)).into(holder.imageView);
        }else {

              holder.imageView.setImageBitmap(null);
          }
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                PopupMenu popup = new PopupMenu(v.getContext(),holder.emailtext);
                popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        getbitmapfrompicasso(imagelist.get(position));
                        Violinnoteclass violinnoteclass = new Violinnoteclass(commentlist.get(position),"url",bitmapsqlite);
                        SQLiteDatabase database = v.getContext().openOrCreateDatabase("Violinnotes", Context.MODE_PRIVATE,null);
                        violinnoteclass.addSqlite(database);
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
        return commentlist.size();
    }

    public class Postholder extends RecyclerView.ViewHolder {
       TextView emailtext,commenttext;
        PhotoView imageView;

        public Postholder(@NonNull View itemView) {
            super(itemView);
            emailtext = itemView.findViewById(R.id.textView);
            commenttext = itemView.findViewById(R.id.commenttextrecycler);
            imageView = itemView.findViewById(R.id.imageView2);
        }
    }
    public void getbitmapfrompicasso(String downloadurl){
        Target mtarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (bitmap!=null){
                    bitmapsqlite = bitmap;

                }
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.get().load(downloadurl).into(mtarget);


    }

}
