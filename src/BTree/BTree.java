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
import java.util.*;
import java.io.RandomAccessFile;
import java.io.*;

public class BTree {

    RandomAccessFile raf;
    private final String FILE_NAME = "nodes.bin";
    BNode root;
    ArrayList<BNode> nodes;
    private final String KEYWORD = "||||||||||||||||||||";
    private ArrayList<String> found = new ArrayList();
    private final int NUM_BLOCKS = 320;
    public int numOfNodes = 0;
    public int nd = -1;
    int m = 0;

    BTree() {
        initializeRaf();
        numOfNodes = 0;
        nodes = new ArrayList();
        root = new BNode(0);
        nodes.add(0, root);
        ++numOfNodes;

    }

    public void addKey(String key) {
        String formatted = formatKey(key);
        if (formatted == null) {
        } else if (numOfNodes == 1) {
            ++m;
            if (root.isFull()) {
                split(root);
                addKey(formatted);
            } else {
                root.addKey(formatted);
            }

        } else if (root.isFull()) {
            split(root);
            addKey(formatted);
        }//else if(numOfNodes>36){}
        else {
            findNode(formatted, root);
        }
        ++m;
    }

    private void findNode(String key, BNode in) {
        BNode current = in;
        if (current.isLeaf() && !current.isFull()) {
            current.addKey(key);
        } else if (current.isLeaf() && current.isFull()) {
            splitChild(current);
            addKey(key);
        } else {
            int chk = current.findChild(key);
            findNode(key, nodes.get(chk));
        }
    }

    private String formatKey(String in) {
        int dif = KEYWORD.length() - in.length();
        String format = in;
        char[] chk = format.toCharArray();
        int i = 0;
        while (i < chk.length) {
            int t = chk[i];
            if (t < 65) {
                format = null;
                i = chk.length;
            } else if (t > 90 && t < 97) {
                format = null;
                i = chk.length;
            } else if (t > 122) {
                format = null;
                i = chk.length;
            }
            ++i;
        }
        if (format != null) {
            byte[] test = format.getBytes();
            if (test.length > 20) {
                format = null;
            } else {

                for (i = 0; i < dif; ++i) {
                    format = format + " ";
                }

            }
        } else {
        }
        return format;
    }

    private void setChildren(BNode in) {
        BNode current = in;
        ArrayList<Integer> ptrs = current.getPointers();
        ArrayList<Integer> actualPtrs = new ArrayList();
        int i = 0;
        int c = 0;
        while (i < ptrs.size()) {
            int p = ptrs.get(i);
            if (p != -1) {
                actualPtrs.add(p);
                ++c;
            } else {
            }
            ++i;
        }
        i = 0;
        if (c != 0) {
            while (i < c) {
                BNode temp = nodes.get(actualPtrs.get(i));
                temp.setParent(current);
                current.addChild(temp);
                ++i;
            }
        } else {
        }

    }

    private void splitChild(BNode in) {
        BNode current = in;
        String parentKey = " ";
        ArrayList<String> keys = current.getKeys();
        ArrayList<String> left = new ArrayList();
        ArrayList<String> right = new ArrayList();
        ArrayList<Integer> ptrs = current.getPointers();
        ArrayList<Integer> leftPtrs = new ArrayList();
        ArrayList<Integer> rightPtrs = new ArrayList();
        BNode child = new BNode(numOfNodes);
        int i = 0;
        while (i < keys.size()) {
            if (i < 16) {
                left.add(keys.get(i));
            } else if (i == 16) {
                parentKey = keys.get(i);
            } else {
                right.add(keys.get(i));
            }
            ++i;
        }
        i = 0;
        while (i < ptrs.size()) {
            if (i < 17) {
                rightPtrs.add(ptrs.get(i));
            } else {
                leftPtrs.add(ptrs.get(i));
            }
            ++i;
        }
        current.formatNode();
        current.addKeylist(left);
        current.addPtrList(leftPtrs);
        child.addKeylist(right);
        child.addPtrList(rightPtrs);
        BNode temp = null;
        if (current.getRightSibling() != null) {
            temp = current.getRightSibling();
            child.setRightSibling(temp);
            temp.setLeftSibling(child);
        }

        current.setRightSibling(child);
        child.setLeftSibling(current);
        child.setParent(current.getParent());
        int l = (int) current.getPointer();
        int r = (int) child.getPointer();
        current.getParent().addChildKey(l, parentKey, r);
        nodes.add(numOfNodes, child);
        ++numOfNodes;
    }

    private void moveLeft(BNode in, String key) {
        BNode current = in;
        String low = current.getLow();
        current.addKey(key);
        current = current.getLeftSibling();
        int p = (int) current.getPointer();
        low = current.getParent().replaceAt(p, low);
        current.addKey(low);

    }

    public ArrayList<String> search(String key) {
        found.clear();
        search(root, key);
        return found;
    }
    public void deleteKey(String key)
    {
        search(root,key);
        int i=0;
        while(i<found.size()){
            deleteKey(root,found.get(i));
           ++i;
        }
    }
    public String deleteKey(BNode in, String key)
    {
       String deleted="deleted";
       String k=formatKey(key);
       k=key;
       BNode current = in;
       int chk =current.findKey(k);
       if(chk==-2&&current.isLeaf())
       {
           current.deleteKey(k);
       }
       else if(chk==-2&&!current.isLeaf())
       {
           deleteHighest(current,key);
       }
       else if(chk==-1)
       {
           deleted=key+" was not found";
       }
       else
       {
           deleteKey(nodes.get(chk),key);
       }
       return deleted;
    }
    private void deleteHighest(BNode in, String key)
    {
        BNode current =in;
        int highest = findHighest(current);
        int p=(int)current.getPointer();
        BNode temp=nodes.get(highest);
        int cP=(int)temp.getParent().getPointer();
        String replace = temp.deleteHighKey(key);
        while(p!=cP)
        {
            temp =nodes.get(cP);
            replace=temp.replaceHighKey(replace);
            cP=(int)temp.getParent().getPointer();
        }
        current.replaceKey(replace);
    }
    private int findHighest(BNode in)
    {
        BNode current=in;
        int out = current.findHigh();
        if(out!=-1)
        {
            findHighest(nodes.get(out));
        }
        out =(int)current.getPointer();
        return out;
    }
    private void search(BNode in, String key) {
        BNode current = in;
        ArrayList<Integer> kids = current.search(key);
        ArrayList<String> finds = current.getFound();
        int i = 0;
        while (i < finds.size()) {
            found.add(finds.get(i));
            ++i;
        }
        finds.clear();
        i = 0;
        while (i < kids.size()) {
            int chck = kids.get(i);
            if (chck != -1) {
                BNode temp = nodes.get(chck);
                search(temp, key);
            } else {
                i = kids.size();
            }
            ++i;
        }
        kids.clear();
        
    }

    private void moveRight(BNode in, String key) {
        BNode current = in;
        current = current.getRightSibling();
        int p = (int) current.getPointer();
        String high = current.getParent().replaceAt(p, key);
        current.addKey(high);
    }

    public void addSearchWord(String word) {
        found.add(word);
    }

    private void splitToTheRight(BNode in) {
        BNode current = in;
        BNode created = new BNode(numOfNodes);
        created.setParent(current);
        ArrayList<BNode> children = current.getChildren();
        ArrayList<String> keys = new ArrayList();
        ArrayList<Integer> ptrs = new ArrayList();
        ArrayList<String> keysOut = new ArrayList();
        ArrayList<Integer> ptrsOut = new ArrayList();
        ArrayList<String> keysIn = current.getKeys();
        ArrayList<Integer> ptrsIn = current.getPointers();
        int count = current.getNumOfChildren();
        ptrsIn.add(numOfNodes);
        BNode child = null;
        int divider;
        if (count == 2) {
            divider = 24;
        } else if (count < 7) {
            divider = 29;
        } else if (count < 16) {
            divider = 30;
        } else {
            divider = 31;
        }
        int i = 0;
        int p = 0;
        int k = 0;
        while (i < count) {
            child = children.get(i);
            ArrayList<String> tempKeys = child.getKeys();
            ArrayList<Integer> tempPts = child.getPointers();
            int j = 0;
            ptrs.add(p, tempPts.get(i));
            ++p;
            while (j < 32) {
                keys.add(k, tempKeys.get(j));
                ptrs.add(p, tempPts.get(j));
                ++j;
                ++k;
                ++p;
            }
            child.formatNode();
            ++i;
        }
        i = 0;
        p = 0;
        k = 0;
        while (i < count) {
            child = children.get(i);
            ArrayList<String> tempKeys = new ArrayList();
            ArrayList<Integer> tempPts = new ArrayList();
            int j = 0;
            tempPts.add(ptrs.get(p));
            ++p;
            while (j < divider) {
                tempKeys.add(keys.get(k));
                tempPts.add(ptrs.get(p));
                ++j;
                ++k;
                ++p;
            }
            keysOut.add(keys.get(k));
            child.addKeylist(tempKeys);
            child.addPtrList(tempPts);
            ++i;
            ++k;
        }
        current.formatNode();
        current.addKeylist(keysOut);
        current.addPtrList(ptrsIn);
        keysOut.clear();
        i = 0;
        while (k < keys.size()) {
            keysOut.add(i, keys.get(k));
            ++i;
            ++k;
        }
        i = 0;
        while (p < ptrs.size()) {
            ptrsOut.add(i, ptrs.get(p));
            ++p;
            ++i;
        }
        child.setRightSibling(created);
        created.addKeylist(keysOut);
        created.addPtrList(ptrsOut);
        created.setLeftSibling(child);
        nodes.add(numOfNodes, created);
        current.addChild(created);
        ++numOfNodes;
    }

    public void printTree() {
        try {
            int i = 0;
            while (i < nodes.size()) {
                RandomAccessFile sraf = new RandomAccessFile(i + FILE_NAME, "rw");
                try {
                    raf.seek(0);
                    int j = 0;
                    System.out.println("-------------------NODE #" + i + "-------------------------------");
                    while (j < 32) {
                        System.out.print(sraf.readInt());
                        System.out.print(sraf.readUTF());
                        ++j;
                    }
                    System.out.println(sraf.readInt());
                } catch (IOException e) {
                    e.getLocalizedMessage();
                    System.out.println("printing");
                }
                ++i;
            }
        } catch (FileNotFoundException e) {

            System.out.println("error printing");
            e.getLocalizedMessage();
        }
    }

    private void split(BNode in) {
        BNode current = in;
        ArrayList<String> keys = in.getKeys();
        ArrayList<Integer> ptrs = in.getPointers();
        ArrayList<String> lKeys = new ArrayList();
        ArrayList<String> rKeys = new ArrayList();
        ArrayList<String> pKeys = new ArrayList();
        ArrayList<Integer> lPtrs = new ArrayList();
        ArrayList<Integer> rPtrs = new ArrayList();
        ArrayList<Integer> pPtrs = new ArrayList();
        int i = 0;
        int r = 0;
        int l = 0;
        while (i < keys.size()) {
            if (i < 16) {
                lKeys.add(l, keys.get(i));
                ++l;
            } else if (i == 16) {
                pKeys.add(keys.get(i));
            } else {
                rKeys.add(keys.get(i));
                ++r;
            }
            ++i;
        }
        i = 0;
        l = 0;
        r = 0;
        while (i < 33) {
            if (i < 17) {
                lPtrs.add(l, ptrs.get(i));
                ++l;
            } else {
                rPtrs.add(r, ptrs.get(i));
                ++r;
            }
            ++i;
        }
        pPtrs.add(0, numOfNodes);
        pPtrs.add(1, numOfNodes + 1);
        BNode left = new BNode(numOfNodes);
        nodes.add(numOfNodes, left);
        ++numOfNodes;
        BNode right = new BNode(numOfNodes);
        nodes.add(numOfNodes, right);
        System.out.println(numOfNodes);
        ++numOfNodes;
        current.formatNode();
        right.addKeylist(rKeys);
        left.addKeylist(lKeys);
        right.addPtrList(rPtrs);
        left.addPtrList(lPtrs);
        current.addKeylist(pKeys);
        current.addPtrList(pPtrs);
        current.addChild(left);
        current.addChild(right);
        current.makeParent();
        left.setParent(current);
        right.setParent(current);
        left.setRightSibling(right);
        right.setLeftSibling(left);
        setChildren(left);
        setChildren(right);
    }

    private void initializeRaf() {

        try {
            int i = 0;
            while (i < NUM_BLOCKS) {
                File file = new File(i + FILE_NAME);
                raf = new RandomAccessFile(file, "rw");

                try {
                    raf.setLength(1058);
                    raf.seek(0);
                    int j = 0;
                    while (j < 32) {
                        raf.writeInt(nd);
                        raf.writeUTF(KEYWORD);
                        ++j;
                    }
                    raf.writeInt(nd);
                    ++i;
                } catch (IOException e) {
                    e.getLocalizedMessage();
                }
            }
        } catch (FileNotFoundException e) {
            e.getLocalizedMessage();
        }
    }

}
