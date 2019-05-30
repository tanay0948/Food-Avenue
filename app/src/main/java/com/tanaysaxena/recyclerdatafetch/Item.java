package com.tanaysaxena.recyclerdatafetch;

/**
 * Created by Tanay Saxena on 7/31/2018.
 */

public class Item {
    public String Itemtitle;
    public String ItemCost;
   public String ItemDescription;
    public Item(String itemtitle,String itemCost,String itemDescription){
        this.Itemtitle=itemtitle;
        this.ItemCost=itemCost;
        this.ItemDescription=itemDescription;
    }
    public Item(){

    }
}
