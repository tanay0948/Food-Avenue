package com.tanaysaxena.recyclerdatafetch;

/**
 * Created by Tanay Saxena on 7/31/2018.
 */

public class ItemOrder {
    public Item item;
    public int count;
    ItemOrder(Item item,int count){
        this.item=item;
        this.count=count;
    }
    public Item getItem(){
        return item;
    }
    public int getCount(){
        return count;
    }
    public void setItem(Item item){
        this.item=item;
    }
    public void setCount(int count){
        this.count=count;
    }
}
