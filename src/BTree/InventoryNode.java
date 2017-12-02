/*Keith Fosmire
  CSC365
   Due 1 April 2014
*/

package BTree;
/*
* The InventoryNode class is used to store each string found on all URLs and the appropriate
* category class is stored in a collection ArrayList(). The number of hits and all connected Nodes
* are accessable from the Node. Node is designed for a Balance Tree.
*/

import java.util.*;
public class InventoryNode
{
    //all variables are declared private
    //all nodes are set to null
    private final String name;
    private final Collection<Category> categories=new ArrayList<>();
    private InventoryNode left=null;
    private InventoryNode right=null;
    private InventoryNode parent=null;
    private int hits;
    //Constructor sets node name and adds the first associated Category
    public InventoryNode(String name,Category category)
    {
        this.name =name;
        categories.add(category);
        hits=1;
    }
    //Can't recall why I have this....I am sure I will either delete or figure it out
    public void addHits()
    {
        hits +=1;
    }
    //self explanatory
    public int getHits()
    {
        return hits;
    }
     //self explanatory
    public String getName()
    {
        return name;
    }
     //self explanatory
    public void setLeft(InventoryNode lft)
    {
        left=lft;
    }    
     //self explanatory
    public InventoryNode getLeft()
    {
        return left;
    }
     //self explanatory
    public void setRight(InventoryNode rt)
    {
        right =rt;
    }
     //self explanatory
    public InventoryNode getRight()
    {
        return right;
    }
     //self explanatory
    public void setParent(InventoryNode pt)
    {
        parent=pt;
    }
     //self explanatory...sorry...if you are actually reading this...you must be annoyed
    public InventoryNode getParent()
    {
        return parent;
    }
     //self explanatory
    public Collection getCategories()
    {
        return categories;
    }
     //self explanatory
    public void addCategory(Category in)
    {
        categories.add(in);
        ++hits;
        
    }


}
