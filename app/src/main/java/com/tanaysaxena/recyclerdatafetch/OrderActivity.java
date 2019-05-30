package com.tanaysaxena.recyclerdatafetch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

public class OrderActivity extends AppCompatActivity {
    TextView order;
    TextView total;
    Button edit;
    Button place;
    DatabaseHandler databaseHandler;
    EditText name;
    EditText address;
    EditText number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order2);
        order=(TextView)findViewById(R.id.orders);
        total=(TextView)findViewById(R.id.cost);
        edit=(Button)findViewById(R.id.button1);
        place=(Button)findViewById(R.id.button);
        name=(EditText)findViewById(R.id.yourName);
        address=(EditText)findViewById(R.id.yourAddress);
        number=(EditText)findViewById(R.id.yourNumber);
        //To Convert the data in table into String
        databaseHandler=new DatabaseHandler(this);
        ArrayList<ItemOrder> arr=databaseHandler.getItems();
        final StringBuffer sbr=new StringBuffer();

        for(int i=0;i<arr.size();i++){
            ItemOrder itemOrder=arr.get(i);
            sbr.append(itemOrder.getItem().Itemtitle+"  ");

            sbr.append(itemOrder.getItem().ItemCost+"  ");
            sbr.append(itemOrder.count);
            sbr.append("\n");
        }
        final StringBuffer mail=new StringBuffer(sbr);
        //Placing the data in text view
        order.setText(sbr.toString());
        mail.append("Total: "+String.valueOf(databaseHandler.getTotalCost()));
        total.setText(String.valueOf(databaseHandler.getTotalCost()));
        //Sending the data to email
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Sweet Dialog Box to confirm order
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to really place this order?")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("Cancelled!")
                                        .setContentText("Your Order is not placed")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                            }})
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("Placed")
                                        .setContentText("Your Order is Placed ")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                //To place an order real code to mail the host
                                databaseHandler.clearTable();
                                //So that user can move to home
                                edit.setText("Move to Home");
                                String name1=name.getText().toString();
                                String address1=address.getText().toString();
                                String number1=number.getText().toString();
                                //Adding name and address to sbr
                                mail.append("\n"+"Name:"+name1+"\n"+"Address:"+address1+"\n"+"Number"+number1+"\n");
                                Thread sender = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            GMailSender sender = new GMailSender("shreedhabavit@gmail.com", "Shreedhaba123");
                                            sender.sendMail("Order Recieved",
                                                    mail.toString(),
                                                    "shreedhabavit@gmail.com",
                                                    "tanay0948@gmail.com");


                                        } catch (Exception e) {
                                            Toast.makeText(OrderActivity.this,"Sorry Couldnot place order",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                sender.start();
                                //Code ends
                            }
                        }).show();


            }
        });
        //Edit order takes you back to cart activity
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {   //if user has placed an order
                ArrayList<ItemOrder> arr=databaseHandler.getItems();
                if(arr.size()<=0){
                    startActivity(new Intent(OrderActivity.this,MainActivity.class));
                }
                //if user has not placed an order
                else{
                startActivity(new Intent(OrderActivity.this,CartActivity.class));
                }
            }
        });



    }
        //Disable Back Button
    public void onBackPressed(){

    }
}
