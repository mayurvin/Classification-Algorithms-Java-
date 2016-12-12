
package com.cse601.datamining.project3.alogorithms;

import java.util.ArrayList;
import java.util.List;

import com.cse601.datamining.project3.util.BinaryTree.Node;

public class PrintTree {

    public static void drawTree(Node head){
        List<Node> n = new ArrayList<Node>();
        n.add(head);
        System.out.println("\nDecision Tree:");
        levelPrint(n);
        }

        private static void levelPrint(List<Node> n) {
        List<Node> next = new ArrayList<Node>();
        for (Node t: n){
        System.out.print(Integer.toString(t.getColumn())+"\t");
        if ((t.leftChild!=null)){
            if (t.leftChild.node.equals("leaf")){
                t.leftChild.setColumn(-1);
            }
        next.add(t.leftChild);
        }
        if ((t.rightChild!=null)){
            if (t.rightChild.node.equals("leaf")){
                t.rightChild.setColumn(-1);
            }
        next.add(t.rightChild);
        }
        }
        System.out.println();
        if (next.size()>0){
        levelPrint(next);
        }
    }
}