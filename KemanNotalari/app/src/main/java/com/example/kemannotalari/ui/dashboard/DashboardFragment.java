package com.example.kemannotalari.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kemannotalari.R;
import com.example.kemannotalari.adapters.Recyclerviewadapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class DashboardFragment extends Fragment {
    FirebaseFirestore firestore;
    ArrayList<String> emaillist;
    ArrayList<String> urlist;
    ArrayList<String> commentlist;
    RecyclerView recyclerView;
    Recyclerviewadapter recyclerviewadapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        emaillist = new ArrayList<>();
        urlist = new ArrayList<>();
        commentlist = new ArrayList<>();
        recyclerView = view.findViewById(R.id.RecyclerView);

        getdata();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerviewadapter = new Recyclerviewadapter(emaillist,urlist,commentlist);
        recyclerView.setAdapter(recyclerviewadapter);



    }



    public void  getdata(){
        emaillist.clear();
        commentlist.clear();
        urlist.clear();
        firestore = FirebaseFirestore.getInstance();
        try {
            CollectionReference collectionReference = firestore.collection("Posts");
            collectionReference.orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(getActivity().getApplicationContext(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                    if (value!=null){

                        for (DocumentSnapshot snapshot : value.getDocuments()){
                            Map<String,Object> data = snapshot.getData();

                            //Casting
                            String comment = (String) data.get("comment");
                            String userEmail = (String) data.get("email");
                            String downloadUrl = (String) data.get("url");
                            commentlist.add(comment);
                            emaillist.add(userEmail);
                            urlist.add(downloadUrl);

                        }
                        recyclerviewadapter.notifyDataSetChanged();
                    }


                }
            });
        }catch (Exception ad){
            ad.printStackTrace();
            System.out.println("hata");
        }




    }

}