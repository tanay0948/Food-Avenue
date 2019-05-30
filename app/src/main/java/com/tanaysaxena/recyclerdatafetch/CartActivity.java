package com.tanaysaxena.recyclerdatafetch;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    // To access Database Values
    DatabaseHandler databaseHandler;
    //Deleting All Records Button
    Button deleteall;
    static  TextView total;
    CartAdapter cartAdapter;
    //Button pLaceOrder
    Button placeOrder;
    //Static to access it from Cart Adapter
    //Empty cart textview
    static TextView emptycart;static Button menus;
    //If cart has items
    static RelativeLayout rl;
    //Button Menu
    Button menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_cart);
        rl=(RelativeLayout)findViewById(R.id.cartlayout);
        emptycart=(TextView)findViewById(R.id.emptycart);
        menu=(Button)findViewById(R.id.btnmenu);
        menus=(Button)findViewById(R.id.menus);
        //To move to the menu Button
        menus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,MainActivity.class));
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this,MainActivity.class));
            }
        });
        //To get items from database
        databaseHandler=new DatabaseHandler(this);
        //Set the view of activity
        if(databaseHandler.getItems().size()<=0){
            emptycart.setVisibility(View.VISIBLE);
            menus.setVisibility(View.VISIBLE);
            rl.setVisibility(View.INVISIBLE);
        }
        else{
            emptycart.setVisibility(View.INVISIBLE);
            menus.setVisibility(View.INVISIBLE);
            rl.setVisibility(View.VISIBLE);
        }
        ArrayList<ItemOrder> arr=databaseHandler.getItems();
        // To set Adapter
       cartAdapter=new CartAdapter(arr,CartActivity.this);
       placeOrder=(Button)findViewById(R.id.btn_placeorder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
        //Deleting All Records
        deleteall=(Button) findViewById(R.id.btnclear);
        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to clear the Cart?")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("Cancelled!")
                                        .setContentText("The Cart is not cleared")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                            }})
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("Cleared!")
                                        .setContentText("The Cart is cleared ")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                        databaseHandler.clearTable();
                                        cartAdapter.clear();
                            }
                        }).show();
            }
        });
        //Total Amount
        total=(TextView)findViewById(R.id.tv_total);
        calculateTotal();
//        To hide the Place Order Button
        placeOrderButton();
        //To move to order to place the order
   placeOrder.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           startActivity(new Intent(CartActivity.this,OrderActivity.class));
       }
   });

    }
    public void calculateTotal(){
        //USING thread to calculate total amount
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                total.setText(String.valueOf(databaseHandler.getTotalCost()));
                handler.postDelayed(this,100);
            }
        });

    }
    public  void placeOrderButton(){
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
               ArrayList<ItemOrder> arr=databaseHandler.getItems();
               if(arr.size()>0){
                   placeOrder.setVisibility(View.VISIBLE);
               }
               else {
                   placeOrder.setVisibility(View.INVISIBLE);
               }
                handler.postDelayed(this,100);
            }
        });
    }
    //Disable Back Button
    public void onBackPressed(){

    }
}
