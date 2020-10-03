package com.example.kemannotalari.adapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.kemannotalari.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.kemannotalari.R.color.denemecolor;

public class Recyclerviewadapterhorizontal extends RecyclerView.Adapter<Recyclerviewadapterhorizontal.Imageholder> {
    ArrayList<Bitmap> imagelist;
    ArrayList<String> namelist;
    View view;
    public Recyclerviewadapterhorizontal(ArrayList<Bitmap> imagelist, ArrayList<String> namelist) {
        this.imagelist = imagelist;
        this.namelist = namelist;
    }

    @NonNull
    @Override
    public Recyclerviewadapterhorizontal.Imageholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.recyclerviewlayouthorizontal,parent,false);
        return new Imageholder(view);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final Recyclerviewadapterhorizontal.Imageholder holder, int position) {
        holder.imageView.setImageBitmap(imagelist.get(position));
        holder.textView.setText(namelist.get(position));
        final ImagePopup imagePopup = new ImagePopup(view.getContext());
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setWindowWidth(800); // Optional
        imagePopup.setBackgroundColor(denemecolor);
        imagePopup.setFullScreen(true); // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional*/
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePopup.initiatePopup(holder.imageView.getDrawable()); // Load Image from Drawable
                imagePopup.viewPopup();

            }
        });
    }

    @Override
    public int getItemCount() {
        return namelist.size();
    }

    public class Imageholder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public Imageholder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.imagetext);
            imageView =itemView.findViewById(R.id.imageviewrecycler);

        }
    }
}
