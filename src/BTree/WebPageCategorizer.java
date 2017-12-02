/*Keith Fosmire
  CSC365
   BTree
*/

package BTree;

/**
 *The program uses Java and JSoup components to scan HTML code from 
 * given URLS. The information is then handed to the appropriate Category class for storing
 * the string and number of times it appears in each URL. The Category is then stored in the
 * Inventory class in an Inventory node that stores strings and the category classes that are 
 * associated with that String. The user is prompted via a GUI which accepts a URL and 
 * returns the appropriate category.
 */


public class WebPageCategorizer {

  
    public static void main(String[] args)
    {
                
       //calling GUI
       UrlDisplay display = new UrlDisplay();
       display.setVisible(true);
     
       
    }
       
}
