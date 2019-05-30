package com.tanaysaxena.recyclerdatafetch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.w3c.dom.Text;

import java.util.ArrayList;



/**
 * Created by Tanay Saxena on 7/31/2018.
 */

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuViewHolder> {
    public ArrayList<Item> arr;
    public DatabaseHandler databaseHandler;
    //Database Handler to access database
    public MenuItemAdapter(ArrayList<Item> arr,Context context){
        this.arr=arr;
        databaseHandler=new DatabaseHandler(context);
    }
    @NonNull

    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.menu_item_layout,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    //Used to put data inTO ViewHolder
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
           final Item i= arr.get(position);
           holder.itemTitle.setText(i.Itemtitle);
           holder.itemDescription.setText(i.ItemDescription);
           holder.itemCost.setText(i.ItemCost);
           //Catching the Item Click
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(final View v) {
                   ///Not used Dialog Box
//                   DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                       @Override
//                       public void onClick(DialogInterface dialog, int which) {
//                           switch (which){
//                               case DialogInterface.BUTTON_POSITIVE:
//                                   //Yes button clicked
//                                   break;
//
//                               case DialogInterface.BUTTON_NEGATIVE:
//                                   //No button clicked
//                                   break;
//                           }
//                       }
//                   };
//                   AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                   builder.setMessage("Do you want to add this to Card?").setPositiveButton("Yes", dialogClickListener)
//                           .setNegativeButton("No", dialogClickListener).show();
                         // Sweet Alert Dialog
                   new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                           .setContentText("Do you want to add this item to Cart?")
                           .setCancelText("No")
                           .setConfirmText("Yes,I would Love it")
                           .showCancelButton(true)
                           .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                               @Override
                               public void onClick(SweetAlertDialog sDialog) {
                                   sDialog.setTitleText("Cancelled!")
                                           .setContentText("The item is not added to Cart")
                                           .setConfirmText("OK")
                                           .showCancelButton(false)
                                           .setCancelClickListener(null)
                                           .setConfirmClickListener(null)
                                           .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                               }})
                           .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                               @Override
                               public void onClick(SweetAlertDialog sDialog) {
                                   //To add data in SQLlite Database
                                   String title=i.Itemtitle;
                                   String cost=i.ItemCost;
                                   String description=i.ItemDescription;
                                   Item item=new Item(title,cost,description);
                                   ItemOrder itemOrder=new ItemOrder(item,1);
                                     boolean isPresent=databaseHandler.isPresent(itemOrder);
                                     if(isPresent){
                                         sDialog.setTitleText("Already Present")
                                                 .setContentText("The item is Already Present to Cart ")
                                                 .setConfirmText("OK")
                                                 .showCancelButton(false)
                                                 .setCancelClickListener(null)
                                                 .setConfirmClickListener(null)
                                                 .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                     }
                                     else {
                                         databaseHandler.addItem(itemOrder);
                                         sDialog.setTitleText("Added!")
                                                 .setContentText("The item is added to Cart ")
                                                 .setConfirmText("OK")
                                                 .showCancelButton(false)
                                                 .setCancelClickListener(null)
                                                 .setConfirmClickListener(null)
                                                 .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                     }
                               }
                           }).show();
               }
           });
    }

    @Override
    // Used FOR GETTING SIZE OF dATA
    public int getItemCount() {
        return arr.size();
    }
    /// Used For Accessing Components of XML
    public static class MenuViewHolder extends RecyclerView.ViewHolder{
        TextView itemDescription;
        TextView itemTitle;
        TextView itemCost;
        public MenuViewHolder(View itemView) {
            super(itemView);
            itemDescription=(TextView)itemView.findViewById(R.id.itemDescription);
            itemTitle=(TextView)itemView.findViewById(R.id.itemtitle);
            itemCost=(TextView)itemView.findViewById(R.id.itemCost);
        }
    }
}
