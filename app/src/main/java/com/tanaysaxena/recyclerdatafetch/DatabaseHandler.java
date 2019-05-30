package com.tanaysaxena.recyclerdatafetch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Tanay Saxena on 7/31/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CartItems";
    private static final String TABLE_NAME= "ItemsOrder";
    private static final String KEY_ID = "id";
    private static final String Item_Title = "Item_Title";
    private static final String Item_Cost= "Item_Cost";
    private static final String Item_value="Item_Value";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
    public void onCreate(SQLiteDatabase db) {
        String sql="create table "+TABLE_NAME+"("+KEY_ID+" INTEGER Primary key AutoIncrement, "+
                Item_Title+" String,"+Item_Cost+" Int,"+Item_value+" Int)";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // Add item in sqlite database
    public void addItem(ItemOrder itemOrder){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Item_Title,itemOrder.getItem().Itemtitle);
        values.put(Item_Cost,Integer.parseInt(itemOrder.getItem().ItemCost));
        values.put(Item_value,itemOrder.getCount());
       db.insert(TABLE_NAME,null,values);
       db.close();
    }
   ///Get Count of the number of Rows //

   //Get the List of all ItemOrder to display on Cart//
   public ArrayList<ItemOrder> getItems(){
       ArrayList<ItemOrder> arr=new ArrayList<ItemOrder>();
       String selectQuery = "SELECT  * FROM " + TABLE_NAME;

       SQLiteDatabase db = this.getWritableDatabase();
       Cursor cursor = db.rawQuery(selectQuery, null);
       if(cursor.moveToFirst()){
        do{
            String title=cursor.getString(1);
            int cost=cursor.getInt(2);
            int value=cursor.getInt(3);
            Item a=new Item(title,String.valueOf(cost),"");
            ItemOrder itemOrder=new ItemOrder(a,value);
            arr.add(itemOrder);
        }while(cursor.moveToNext());

       }
       cursor.close();db.close();
       return arr;

   }
   //This is used to clear the Contents of the table
   public void clearTable(){
       SQLiteDatabase db=this.getWritableDatabase();
       String sql="Delete from "+TABLE_NAME;
       db.execSQL(sql);db.close();
   }
   //This is used to check whether a record is present in Table or not
    public boolean isPresent(ItemOrder itemOrder){
       SQLiteDatabase db=this.getReadableDatabase();

       //String sql="Select * from "+TABLE_NAME+" where "+Item_Title+"="+itemOrder.getItem().Itemtitle;
       Cursor cursor=db.query(TABLE_NAME,null,Item_Title+"=?",new String[]{itemOrder.getItem().Itemtitle},null,null,null);
       if(cursor.getCount()<=0){
           cursor.close();
           return false;
       }
       cursor.close();db.close();
       return true;

    }
    //This is used get the total cost from the table by transvering the table
    public int getTotalCost(){
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int Totalcost=0;
        if(cursor==null) return Totalcost;
        if(cursor.moveToFirst()){
            do{
                int cost=cursor.getInt(2);
                int value=cursor.getInt(3);
                Totalcost+=(cost*value);
            }while(cursor.moveToNext());

    }
    cursor.close();db.close();
    return Totalcost;
    }
    //This is used to update a record in table//
    public void addValue(String title,int cost,int value){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Item_Title,title);
        values.put(Item_Cost,cost);
        values.put(Item_value,value);
        int a=db.update(TABLE_NAME,values,Item_Title+"=?",new String[]{title});
        db.close();
    }

    //This is used to delete a record in table
    public void deleteRecord(String title){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,Item_Title+"=?",new String[]{title});
        db.close();
    }

}
