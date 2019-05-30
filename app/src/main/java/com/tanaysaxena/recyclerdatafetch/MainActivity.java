package com.tanaysaxena.recyclerdatafetch;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public RecyclerView rv;
    DatabaseReference ref;
    ProgressBar progressBar;
    //
    Button cart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv=(RecyclerView)findViewById(R.id.recyclerview);
        //
        cart=(Button)findViewById(R.id.button1);
        cart.setVisibility(View.INVISIBLE);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CartActivity.class));
            }
        });
        //Progress bAR
        progressBar=(ProgressBar)findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        //Getting Database Reference
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        ref=firebaseDatabase.getReference();
        DatabaseReference menus=ref.child("Menus");
        //Reading Values
        menus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Item> arr=new ArrayList<Item>();
                //rEmoving Progress bar visibility
                progressBar.setVisibility(View.INVISIBLE);
                cart.setVisibility(View.VISIBLE);
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Item item=new Item();
                    //Reading Values from Item
                    item.Itemtitle=ds.child("Title").getValue(String.class);
                    item.ItemCost=ds.child("Cost").getValue(String.class);
                    item.ItemDescription=ds.child("Description").getValue(String.class);
                    arr.add(item);
                }


                //Used For Setting Adapter for Recycler View and passing data and context for sqlite database
                MenuItemAdapter adapter=new MenuItemAdapter(arr,MainActivity.this);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    //Disable Back Press
    public void onBackPressed(){

    }
}
