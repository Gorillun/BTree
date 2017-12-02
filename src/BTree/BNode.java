/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTree;

/**
 *
 * @author BlackHawk31
 */
import java.util.Map;
import java.util.*;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BNode {

    private final String KEYWORD = "||||||||||||||||||||";
    private ArrayList<BNode> children = new ArrayList();
    private RandomAccessFile raf;
    private BNode rightSibling;
    private BNode leftSibling;
    private BNode parent;
    private boolean leaf;
    private final String FILE_NAME = "nodes.bin";
    private int numOfChildren, numOfKeys;
    private final int pointer;
    private ArrayList<String> finds = new ArrayList();

    BNode(long position) {
        numOfChildren = 0;
        numOfKeys = 0;
        pointer = (int) position;
        leaf = true;
        parent = null;
    }

    public ArrayList<String> getKeys() {
        ArrayList<String> keys = new ArrayList();
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            raf.seek(0);
            int i = 0;
            raf.readInt();
            while (i < numOfKeys) {
                keys.add(i, raf.readUTF());
                raf.readInt();
                ++i;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error getting keys");
        }
        return keys;
    }

    public ArrayList<String> getFound() {

        return finds;
    }

    public void clearFound() {
        finds.clear();
    }

    public ArrayList<Integer> search(String key) {

        ArrayList<Integer> out = new ArrayList();
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            raf.seek(0);
            int i = 0;
            int size = key.length();
            int child = raf.readInt();
            while (i < numOfKeys) {
                String temp = raf.readUTF();
                if (temp.substring(0, size).compareToIgnoreCase(key) == 0) {
                    finds.add(temp);
                    out.add(child);
                    child = raf.readInt();
                } else if (temp.substring(0, size).compareToIgnoreCase(key) > 0) {
                    out.add(child);
                    child = raf.readInt();
                } else if (temp.substring(0, size).compareToIgnoreCase(key) < 0) {
                    child = raf.readInt();
                    out.add(child);
                } else {
                    child = raf.readInt();
                }
                ++i;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding key");
        }
        return out;
    }

    public int findHigh() {
        int out = 0;
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }

        try {
            raf.seek(0);
            raf.readInt();
            int i = 0;
            while (i < numOfKeys) {
                raf.readUTF();
                out = raf.readInt();
                ++i;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding key");
        }
        return out;
    }

    public int findKey(String key) {
        int out = 0;
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }

        try {
            raf.seek(0);
            int l = raf.readInt();
            int i = 0;
            while (i < numOfKeys) {
                String s = raf.readUTF();
                if (s.equalsIgnoreCase(key)) {
                    out = -2;
                    i = numOfKeys;
                } else if (s.compareToIgnoreCase(key) > 0) {
                    out = l;
                    i = numOfKeys;
                } else {
                    l = raf.readInt();
                }
                ++i;
            }
            if (out == 0) {
                out = l;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding key");
        }
        return out;
    }
    public String replaceHighKey(String key)
    {
        String high = " ";
          try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        
        try {
            raf.seek(0);
            int i=0;
            raf.readInt();
            while(i<numOfKeys-1)
            {
                raf.readUTF();
                raf.readInt();
                ++i;
            }
            high=raf.readUTF();
            raf.seek(0);
            i=0;
            raf.readInt();
            while(i<numOfKeys-1)
            {
                raf.readUTF();
                raf.readInt();
                ++i;
            }
            raf.writeUTF(key);
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding key");
        }
        return high;
    }
    public void replaceKey(String key)
    {
          try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        
        try {
           
            raf.seek(0);
            int i=0;
            int me =raf.readInt();
            while(i<numOfKeys)
            {
                String chk=raf.readUTF();
                if(chk.equalsIgnoreCase(key))
                {
                    i=numOfKeys;
                }
                else
                {
                    raf.readInt();
                }
                ++i;
            }
            raf.seek(0);
            i=0;
            int you = raf.readInt();
            while(i<numOfKeys)
            {
                if(you==me)
                {
                    raf.writeUTF(key);
                    i=numOfKeys;
                }
                else
                {
                    raf.readUTF();
                    you=raf.readInt();
                }
                ++i;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding key");
        }
    }
    public String deleteHighKey(String key)
    {
        String high = " ";
          try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        
        try {
           
            raf.seek(0);
            int i=0;
            raf.readInt();
            while(i<numOfKeys)
            {
                high=raf.readUTF();
                raf.readInt();
                ++i;
            }
            raf.seek(0);
            --numOfKeys;
            i=0;
            raf.readInt();
            while(i<numOfKeys)
            {
                raf.readUTF();
                raf.readInt();
                ++i;
            }
            raf.writeUTF(KEYWORD);
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding key");
        }
        return high;
    }
    public int deleteKey(String key) {
        int out = 0;
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }

        try {

            raf.seek(0);
            ArrayList<String> k = new ArrayList();
            int i = 0;
            raf.readInt();
            int c = 0;
            while (i < numOfKeys) {
                String s = raf.readUTF();
                if (s.equalsIgnoreCase(key)) {
                } else {
                    k.add(c, s);
                    ++c;
                }
                raf.readInt();
                ++i;
            }
            k.add(KEYWORD);
            raf.seek(0);
            i = 0;
            raf.readInt();
            while (i < numOfKeys) {
                raf.writeUTF(k.get(i));
                raf.readInt();
                ++i;
            }
            --numOfKeys;
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding key");
        }
        return out;
    }

    public void addChildKey(int lftPtr, String key, int rghtPtr) {
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            int i = 0;
            int me;
            String k;
            ArrayList<Integer> ptrs = new ArrayList();
            ArrayList<String> keys = new ArrayList();
            raf.seek(0);
            me = raf.readInt();
            while (i < numOfKeys + 1) {
                k = raf.readUTF();
                if (lftPtr == me) {
                    ptrs.add(me);
                    keys.add(key);
                    ptrs.add(rghtPtr);
                    keys.add(k);
                } else {
                    ptrs.add(me);
                    keys.add(k);
                }
                me = raf.readInt();
                ++i;
            }
            ptrs.add(raf.readInt());
            i = 0;
            ++numOfKeys;
            raf.seek(0);
            while (i < numOfKeys) {
                raf.writeInt(ptrs.get(i));
                raf.writeUTF(keys.get(i));
                ++i;
            }
            raf.writeInt(ptrs.get(i));
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error raplaceAt");
        }

    }

    public String replaceAt(int at, String key) {
        String out = KEYWORD;
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            raf.seek(0);
            int i = 0;
            int check = raf.readInt();
            while (i < numOfKeys) {
                if (check == at) {
                    out = raf.readUTF();
                    i = numOfKeys;
                } else {
                    raf.readUTF();
                    check = raf.readInt();
                }
                ++i;
            }
            raf.seek(0);
            i = 0;
            check = raf.readInt();
            while (i < numOfKeys) {
                if (check == at) {
                    raf.writeUTF(key);
                    i = numOfKeys;
                } else {
                    raf.readUTF();
                    check = raf.readInt();
                }
                ++i;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error raplaceAt");
        }
        return out;
    }

    public ArrayList<Integer> getPointers() {
        ArrayList<Integer> pointers = new ArrayList();
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            raf.seek(0);
            int i = 0;

            pointers.add(0, raf.readInt());
            while (i < numOfKeys) {
                raf.readUTF();
                pointers.add(i + 1, raf.readInt());
                ++i;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error getting pointer");
        }
        return pointers;
    }

    public boolean hasParent() {
        boolean test = true;
        if (parent == null) {
            test = false;
        }
        return test;
    }

    public ArrayList<BNode> getChildren() {
        return children;
    }

    public void setParent(BNode in) {
        parent = in;
    }

    public boolean fullChildren() {
        boolean full = true;
        if (numOfChildren == 0) {
            full = false;
        }
        Iterator<BNode> it = children.iterator();
        while (it.hasNext()) {
            BNode temp = it.next();
            if (!temp.isFull()) {
                full = false;
            }
        }
        return full;
    }

    public int findChild(String key) {
        int out = -1;
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            boolean high = true;
            raf.seek(0);
            int i = 0;
            int ptr = raf.readInt();
            while (i < 32) {

                String check = raf.readUTF();
                if (check.compareToIgnoreCase(key) > 0) {
                    out = ptr;
                    high = false;
                    i = 32;
                }
                ptr = raf.readInt();

                ++i;
            }
            if (high == true) {
                out = ptr;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            // System.out.println("error finding child");
        }
        return out;
    }

    public void formatNode() {
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            raf.seek(0);
            int i = 0;
            raf.writeInt(-1);
            while (i < 32) {
                raf.writeUTF(KEYWORD);
                raf.writeInt(-1);
                ++i;
            }
            numOfKeys = 0;
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println(" error formatting");
        }
    }

    public long getPointer() {
        return pointer;
    }

    public BNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }

        try {
            raf.seek(0);
            int chck = raf.readInt();
            if (chck != -1) {
                leaf = false;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding key");
        }
        return leaf;
    }

    public void makeParent() {
        leaf = false;
    }

    public void setLeftSibling(BNode in) {
        leftSibling = in;
    }

    public BNode getLeftSibling() {
        return leftSibling;
    }

    public int checkSiblings() {
        int out;
        if (leftSibling == null && rightSibling != null) {
            if (!rightSibling.isFull()) {
                out = 1;
            } else {
                out = 0;
            }
        } else if (leftSibling != null && rightSibling == null) {
            if (!leftSibling.isFull()) {
                out = -1;
            } else {
                out = 0;
            }
        } else if (leftSibling == null && rightSibling == null) {
            out = 0;
        } else if (leftSibling.isFull() && rightSibling.isFull()) {
            out = 0;
        } else if (!leftSibling.isFull() && rightSibling.isFull()) {
            out = -1;
        } else {
            out = 1;
        }
        return out;
    }

    public void setRightSibling(BNode in) {
        rightSibling = in;
    }

    public BNode getRightSibling() {
        return rightSibling;
    }

    public boolean isFull() {
        boolean test = false;
        if (numOfKeys >= 32) {
            test = true;
        }
        return test;
    }

    public boolean isUnder() {
        boolean test = false;
        if (numOfKeys < 16) {
            test = true;
        }
        return test;
    }

    public boolean hasLeftSibling() {
        boolean test = false;
        if (leftSibling != null) {
            test = true;
        }
        return test;
    }

    public boolean hasRightSibling() {
        boolean test = false;
        if (rightSibling != null) {
            test = true;
        }
        return test;
    }

    public void setNumOfKeys(int in) {
        numOfKeys = in;
    }

    public void addKey(String key) {
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }

        try {
            List<String> tempS = new ArrayList();
            List<Integer> tempP = new ArrayList();
            raf.seek(0);
            int i = 0;
            String temp;
            String tempx;
            while (i < 32) {
                tempP.add(raf.readInt());
                temp = raf.readUTF();
                if (temp.compareToIgnoreCase(key) > 0) {
                    tempx = key;
                    key = temp;
                    temp = tempx;
                }
                tempS.add(temp);
                ++i;
            }
            tempP.add(raf.readInt());
            ++numOfKeys;
            i = 0;
            raf.seek(0);
            while (i < 32) {
                raf.writeInt(tempP.get(i));
                raf.writeUTF(tempS.get(i));
                ++i;
            }
            raf.writeInt(tempP.get(i));
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding key");
        }
    }

    public void addChild(BNode in) {
        children.add(numOfChildren, in);
        ++numOfChildren;
    }

    public void addKeylist(ArrayList keys) {
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            raf.seek(0);
            int i = 0;
            raf.readInt();
            while (i < keys.size()) {
                raf.writeUTF(keys.get(i).toString());
                raf.readInt();
                ++i;
                ++numOfKeys;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            //System.out.println("error adding keys");
        }
    }

    public void addPtrList(ArrayList<Integer> ptrs) {
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }

        try {
            raf.seek(0);
            int i = 0;
            raf.writeInt(ptrs.get(i));
            ++i;
            while (i < ptrs.size()) {
                raf.readUTF();
                raf.writeInt(ptrs.get(i));
                ++i;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error adding pointer");

        }

    }

    public int highOrLow(String key) {
        int out = 0;
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            raf.seek(0);
            int i = 0;
            raf.readInt();
            while (i < numOfKeys) {
                String temp = raf.readUTF();
                if (temp.compareToIgnoreCase(key) > 0) {
                    out = 1;
                    raf.readInt();
                } else {
                    raf.readInt();
                }

                ++i;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("Error highOrLow");
        }

        return out;
    }

    public int getNumOfChildren() {
        return numOfChildren;
    }

    public String getHigh(String key) {
        String out = " ";
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
            System.out.println("error");
        }
        try {
            raf.seek(0);
            int i = 0;
            raf.readInt();
            while (i < numOfKeys) {
                out = raf.readUTF();
                raf.readInt();
                ++i;
            }
            i = 1;
            raf.seek(0);
            while (i < numOfKeys) {
                raf.readInt();
                raf.readUTF();
                ++i;
            }
            raf.readInt();
            raf.writeUTF(key);
            raf.readInt();
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error gethigh");
        }
        return out;
    }

    public BNode getLowest() {
        int out = 0;
        int i = 0;
        BNode current = null;
        while (i < numOfChildren) {
            int c = 0;
            BNode temp = children.get(i);
            int d = temp.getNumOfKeys();
            if (c > d) {
                c = d;
                current = temp;
            }
            ++i;
        }
        return current;
    }

    public String getLow() {
        String out = " ";
        try {
            raf = new RandomAccessFile(pointer + FILE_NAME, "rw");
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
        try {
            ArrayList<String> tempKeys = new ArrayList();
            raf.seek(0);
            int i = 0;
            raf.readInt();
            out = raf.readUTF();
            --numOfKeys;
            while (i < numOfKeys) {
                raf.readInt();
                tempKeys.add(i, raf.readUTF());
                ++i;
            }
            raf.seek(0);
            raf.readInt();
            i = 0;
            while (i < numOfKeys) {
                raf.writeUTF(tempKeys.get(i));
                raf.readInt();
                ++i;
            }
            raf.close();
        } catch (IOException e) {
            e.getLocalizedMessage();
            System.out.println("error getLow");
        }
        return out;

    }

    public int getNumOfKeys() {
        return numOfKeys;
    }

}
