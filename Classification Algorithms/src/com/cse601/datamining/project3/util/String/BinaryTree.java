package com.cse601.datamining.project3.util.String;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;



public class BinaryTree {

	public Node root;
	Node root1;
	private int size = -1;

	public void addNode(double key, int column, String node, double leftLabel, double rightLabel, int begin, int row, int end) {

		Node newNode = new Node(key, column, node, leftLabel, rightLabel, begin, row, end);

		if (root == null) {
			root = newNode;
		} else {

			Node focusNode = root;
			Node parent;

			while (true) {
				parent = focusNode;
				if (end == focusNode.row) {
					focusNode.leftChild = newNode;
					return;
				}else if (begin == focusNode.row) {
					focusNode.rightChild = newNode;
					return;
				}else{
					if (end < focusNode.row) {	
						focusNode = focusNode.leftChild;
						if (focusNode == null) {
							parent.leftChild = newNode;
							return;
						}
					}else{
						
						focusNode = focusNode.rightChild;
						if (focusNode == null) {
							parent.rightChild = newNode;
							return;
						}
					}
				}				
			}
		}
	}


	public void printTree(Node tmpRoot) {

        Queue<Node> currentLevel = new LinkedList<Node>();
        Queue<Node> nextLevel = new LinkedList<Node>();

        currentLevel.add(tmpRoot);

        while (!currentLevel.isEmpty()) {
            Iterator<Node> iter = currentLevel.iterator();
            while (iter.hasNext()) {
                Node currentNode = iter.next();
                if (currentNode.leftChild != null) {
                    nextLevel.add(currentNode.leftChild);
                }
                if (currentNode.rightChild != null) {
                    nextLevel.add(currentNode.rightChild);
                }
                System.out.print(StringValue(currentNode.key) + " ");
            }
            System.out.println();
            currentLevel = nextLevel;
            nextLevel = new LinkedList<Node>();

        }

    }
	

	public void inOrderTraverseTree(Node focusNode) {

		if (focusNode != null) {
			inOrderTraverseTree(focusNode.leftChild);
			System.out.println("Key: "+StringValue(focusNode.key));
			//System.out.println("Col: "+focusNode.column);
			//System.out.println(focusNode.node);
			//System.out.println("Left: "+focusNode.leftLabel);
			//System.out.println("Right: "+focusNode.rightLabel);
			System.out.print("\n");
			inOrderTraverseTree(focusNode.rightChild);
		}

	}

	public void preorderTraverseTree(Node focusNode) {

		if (focusNode != null) {
			System.out.print(StringValue(focusNode.key) + " ");
			preorderTraverseTree(focusNode.leftChild);
			preorderTraverseTree(focusNode.rightChild);
		}
	}

	public void postOrderTraverseTree(Node focusNode) {

		if (focusNode != null) {
			postOrderTraverseTree(focusNode.leftChild);
			postOrderTraverseTree(focusNode.rightChild);
			System.out.print(focusNode.key + " ");

		}

	}


	public Node findNode(int key) {

		Node focusNode = root;

		while (focusNode.key != key) {

			if (key < focusNode.key) {
				focusNode = focusNode.leftChild;
			} else {
				focusNode = focusNode.rightChild;
			}

			if (focusNode == null)
				return null;
		}
		return focusNode;
	}
	
	
	public class Node {

		public double key;
		public int column;
		public String node;
		public double leftLabel;
		public double rightLabel;
		public int begin;
		public int row;
		public int end;
		

		public Node leftChild;
		public Node rightChild;
		public Node(double key, int column, String node, double leftLabel,
				double rightLabel, int begin, int row, int end) {
			super();
			this.key = key;
			this.column = column;
			this.node = node;
			this.leftLabel = leftLabel;
			this.rightLabel = rightLabel;
			this.begin = begin;
			this.row = row;
			this.end = end;
		}
		public double getKey() {
			return key;
		}
		public void setKey(double key) {
			this.key = key;
		}
		public int getColumn() {
			return column;
		}
		public void setColumn(int column) {
			this.column = column;
		}
		public String getNode() {
			return node;
		}
		public void setNode(String node) {
			this.node = node;
		}
		public double getLeftLabel() {
			return leftLabel;
		}
		public void setLeftLabel(double leftLabel) {
			this.leftLabel = leftLabel;
		}
		public double getRightLabel() {
			return rightLabel;
		}
		public void setRightLabel(double rightLabel) {
			this.rightLabel = rightLabel;
		}
		public int getBegin() {
			return begin;
		}
		public void setBegin(int begin) {
			this.begin = begin;
		}
		public int getRow() {
			return row;
		}
		public void setRow(int row) {
			this.row = row;
		}
		public int getEnd() {
			return end;
		}
		public void setEnd(int end) {
			this.end = end;
		}
		public Node getLeftChild() {
			return leftChild;
		}
		public void setLeftChild(Node leftChild) {
			this.leftChild = leftChild;
		}
		public Node getRightChild() {
			return rightChild;
		}
		public void setRightChild(Node rightChild) {
			this.rightChild = rightChild;
		}	

	}
	public String StringValue(Double value) {
		int val = value.intValue();
		String string = null;
		switch(val){
			case 2: string = "sunny"; break;
			case 3: string = "overcast"; break;
			case 4: string = "rain"; break;
			case 5: string = "hot"; break;
			case 6: string = "mild"; break;
			case 7: string = "cool"; break;
			case 8: string = "high"; break;
			case 9: string = "normal"; break;
			case 10: string = "weak"; break;
			case 11: string = "strong"; break;
		}
		return string;

	}




}