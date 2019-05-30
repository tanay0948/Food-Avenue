package com.tanaysaxena.recyclerdatafetch;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;

/**
 * Created by Tanay Saxena on 7/31/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CardViewHolder> {
    ArrayList<ItemOrder> arr;
    DatabaseHandler databaseHandler;
    public CartAdapter(ArrayList<ItemOrder> arr, Context context){
        this.arr=arr;
        databaseHandler=new DatabaseHandler(context);
    }
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.cart_item,parent,false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder holder, final int position) {
        final ItemOrder itemOrder=arr.get(position);
        holder.itemtitle.setText(itemOrder.item.Itemtitle);
        holder.itemcost.setText(itemOrder.item.ItemCost);
        holder.itemvalue.setText(String.valueOf(itemOrder.count));
        //Commenting bcz this will gie value only when loaded for first time
        String value=String.valueOf(holder.itemvalue.getText());
        int valueamount=Integer.parseInt(value);
        //To set the min Amount of product that can be ordered
        if(valueamount==1){
            holder.minusview.setVisibility(View.INVISIBLE);
        }
//        //To set the max Amount of product that can be ordered
//        if(valueamount==10){
//            holder.plusview.setVisibility(View.INVISIBLE);
//        }
        holder.plusview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=String.valueOf(holder.itemtitle.getText());
                String cost=String.valueOf(holder.itemcost.getText());
                String value=String.valueOf(holder.itemvalue.getText());
                int intcost=Integer.parseInt(cost);
                int intvalue=Integer.parseInt(value);
                //Incrementing whatever the value that is present
                intvalue+=1;
                holder.minusview.setVisibility(View.VISIBLE);
                holder.itemvalue.setText(String.valueOf(intvalue));
                databaseHandler.addValue(title,intcost,intvalue);

            }
        });
        holder.minusview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=String.valueOf(holder.itemtitle.getText());
                String cost=String.valueOf(holder.itemcost.getText());
                String value=String.valueOf(holder.itemvalue.getText());
                int intcost=Integer.parseInt(cost);
                int intvalue=Integer.parseInt(value);
                //Decrementing whatever the value that is present
                intvalue-=1;
                if(intvalue==1){

                    holder.minusview.setVisibility(View.INVISIBLE);
                }
                holder.itemvalue.setText(String.valueOf(intvalue));
                databaseHandler.addValue(title,intcost,intvalue);

            }
        });
        holder.trashview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(v.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Do you want to remove this item from Cart?")
                        .setCancelText("No")
                        .setConfirmText("Yes")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.setTitleText("Cancelled!")
                                        .setContentText("The item is present in the Cart")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.ERROR_TYPE);

                            }})
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.setTitleText("Removed!")
                                            .setContentText("The item is removed from the Cart ")
                                            .setConfirmText("OK")
                                            .showCancelButton(false)
                                            .setCancelClickListener(null)
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                            String title=String.valueOf(holder.itemtitle.getText());
                                            if(arr.size()==1){
                                               databaseHandler.clearTable();
                                             clear();
                                            }
                                            else {
                                                databaseHandler.deleteRecord(title);
                                                arr.remove(arr.get(position));
                                                notifyItemRemoved(position);
                                            }
                            }
                        }).show();



            }
        });

    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{
        TextView itemtitle;
        TextView itemcost;
        TextView itemvalue;
        ImageView plusview;
        ImageView minusview;
        ImageView trashview;
        public CardViewHolder(View v){
                super(v);
                itemtitle=(TextView)v.findViewById(R.id.title);
                itemcost=(TextView)v.findViewById(R.id.cost);
                itemvalue=(TextView)v.findViewById(R.id.value);
                plusview=(ImageView)v.findViewById(R.id.plus);
                minusview=(ImageView)v.findViewById(R.id.minus);
                trashview=(ImageView)v.findViewById(R.id.delete);


        }
    }
    public void clear() {
        CartActivity.emptycart.setVisibility(View.VISIBLE);
        CartActivity.menus.setVisibility(View.VISIBLE);
        CartActivity.rl.setVisibility(View.INVISIBLE);
        final int size = arr.size();
        arr.clear();
        notifyItemRangeRemoved(0, size);
    }

}
