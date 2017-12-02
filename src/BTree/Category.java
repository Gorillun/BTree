/*Keith Fosmire
  CSC365
   Due 4 March 2014
*/

package BTree;
/*
   * all categories inherit this class...:"keep it simple stupid"...Infantry!!!

*/

import java.util.*;
public class Category
{
    Map siteInfo =new HashMap<>();
    private final String name;
    protected int numOfInventory;
   private final String category;
   private int count=0;
    Category(String url)
    {
        name=url;
        numOfInventory =0;
        category = "misc";
        
        
    }
    public void setInventory(String in)
    {
        ++numOfInventory;
        String test= in;
        int i=testInputString(test);
        if(i!=0)
        {
            addInventory(test); 
        }     
    }
    //**********************************************************
    private int testInputString(String in)
    {
        String test=in;
        if((test.length()<4))
        {
            return 0;
        }
        else
        return 1;
    } 
    //*****************************************************
    private void addInventory(String add)
    {
        String input =add;
        if(!siteInfo.containsKey(input))
        {
            siteInfo.put(input,1);
        }
        else
        {
            Integer value =(Integer) siteInfo.get(input);
            siteInfo.put(input,value+1); 
        }
    }
    //***********************************************************
    public String getName()
    {
        return name;
    }
    //************************************************************
    public Map<String,Integer> getInventory()
    {
        return siteInfo;
    }
    //************************************************************
    public int getNumOfInventory()
    {
        return numOfInventory;
    }
    //************************************************************
    public String getCategoryType()
    {
        return category;
    }
            public int getCount()
       {
           return count;
       }
}
