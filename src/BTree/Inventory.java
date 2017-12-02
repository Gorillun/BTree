/*Keith Fosmire
 CSC365
 Due 4 March 2014
 */
package BTree;
/*
 * This is the "storage room" for the program. The Inventory class stores all Categories and 
 * associated strings. Nodes are instantiated here using the keywords found in the added Category
 * Every node is then added to the Tree. This Inventory Class has a function called findCategory().
 * The findCategory() may end up as its own class due to the shear madness of it. Regardless
 * of the mess I have made...the program will find what category that a given URL belongs to..
 *you can either trust me for now and wait until I make it more efficient, or soon discover that
 *I have the thought process of a raging lunatic...your choice.
 */

import java.util.*;

public class Inventory {

    // What a nice view...simple...I am finally allowed to use some Java tools other than an array
    //Once again private variables for an ArrayList and Nodes
    private final ArrayList<Category> categories = new ArrayList();
    private BTree tree = new BTree();
    private Map<String, List<String>> urls = new HashMap<>();
    private BNode root;

    //all categories must enter here, did not want to add through a constructor...obviously
    Inventory() {

    }

    public void insertURL(Category inv) {

        categories.add(inv);
        String url = inv.getName();
        Set keys = inv.getInventory().keySet();
        Iterator it = keys.iterator();
        int i = 0;
        while (it.hasNext())//checking if it exists
        {
            String key = (String) it.next();
            if (urls.containsKey(key)) {
                List<String> temp = urls.get(key);
                temp.add(url);
            } else {
                List<String> temp = new ArrayList();
                temp.add(url);
                urls.put(key, temp);
                tree.addKey(key);

            }
        }

    }

    //Recursive search..its just the right thing to do
    public InventoryNode searchTree(String name, InventoryNode current) {
        if (current == null)//nothing there
        {
            return null;
        } else if (current.getName().equalsIgnoreCase(name))//found it
        {
            return current;
        } else if (current.getName().compareToIgnoreCase(name) < 0)//too small
        {
            return searchTree(name, current.getRight());//trying a bigger size

        } else if (current.getName().compareToIgnoreCase(name) > 0)//too big
        {
            return searchTree(name, current.getLeft());//trying a smaller size

        }
        return current;
    }

    //Adding the node alphabetically
    private InventoryNode addNode(InventoryNode in, InventoryNode current) {
        if (current == null)//tree is empty
        {
            return in;
        } else if (current.getName().compareToIgnoreCase(in.getName()) < 0)//too small
        {
            current.setRight(addNode(in, current.getRight())); //try a bigger one
            current.getRight().setParent(current);//got it...set it and get it later

        } else if (current.getName().compareToIgnoreCase(in.getName()) > 0)//too big
        {
            current.setLeft(addNode(in, current.getLeft()));//get a smaller one
            current.getLeft().setParent(current);//thats a good spot

        }
        return current;
    }

    public ArrayList<String> search(String key) {
        return tree.search(key);
    }

    public void printTree() {
        tree.printTree();
    }

    // Might want a category
    public String getCategory(String in) {
        String rightOne = null;
        String mate = in;
        Iterator<Category> getIt = categories.iterator();//Once again
        while (getIt.hasNext()) {
            Category cat = getIt.next();
            if (mate.equalsIgnoreCase(cat.getName()))//are you my dad?
            {
                rightOne = cat.getName();//guess so
            }
        }
        return rightOne;
    }

    public void delete(String key) {
        tree.deleteKey(key);
    }

    public int totalKeys() {
        int total = 0;
        Iterator<Category> it = categories.iterator();
        while (it.hasNext()) {
            total = total + it.next().getCount();
        }
        return total;
    }

}
