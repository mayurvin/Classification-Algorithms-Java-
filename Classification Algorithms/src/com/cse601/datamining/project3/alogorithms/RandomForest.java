package com.cse601.datamining.project3.alogorithms;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import com.cse601.datamining.project3.util.BinaryTree;
import com.cse601.datamining.project3.util.BinaryTree.Node;
import com.cse601.datamining.project3.util.String.EvaluateGini;

public class RandomForest {

	int numTree = 7;
	Vector<BinaryTree> decisionTrees = new Vector<BinaryTree>();
	int minCol = 0;
	int maxCol = 0;
	int numRanCol = 7;

	public void process(Vector<Vector> dataSet) {
		Vector<Vector> test = new Vector<Vector>();
		Vector<Vector> train = new Vector<Vector>();
		DecisionTree dt = new DecisionTree();
		decisionTrees = new Vector<BinaryTree>();

		int size = dataSet.size();
		int len = (int) (size * (0.25));

		for (int i = 0; i < len; i++) {
			test.add(dataSet.get(i));
		}
		for (int j = len; j < dataSet.size(); j++) {
			train.add(dataSet.get(j));
		}
		maxCol = train.get(0).size() - 1;
		
		
		
		Random rn = new Random();
		for (int i = 0; i < numTree; i++) {
			//Vector<Integer> ranCols =  new Vector<Integer>(); 
			Vector<Vector> randomTrainData = new Vector<Vector>();
			
			/*while(ranCols.size()<numRanCol){
				//int n = maxCol - minCol + 1;
				int num = (int) (Math.random() * ( maxCol - minCol ));//rn.nextInt() % n;
				if(ranCols.contains(num)){
					continue;
				}else{
					ranCols.add(num);
				}				
			}*/
			
			Vector<Integer> ranRowIndex =  new Vector<Integer>();
			while(ranRowIndex.size() < train.size()/numTree){
				//int n = maxCol - minCol + 1;
				int num = (int) (Math.random() * ( (train.size()-1) - 0 ));//rn.nextInt() % n;
				if(ranRowIndex.contains(num)){
					continue;
				}else{
					ranRowIndex.add(num);
					randomTrainData.add(train.get(num));
				}				
			}
			//System.out.println("Send Train Size: "+ranRowIndex.size() + "  Train Size: "+train.size()+"  Trees: "+numTree+ " Desired Size: "+ (train.size()/numTree));
			
			decisionTrees.add(dt.process(test, randomTrainData, true, numRanCol));
			//System.out.println("############  Tree Number: "+ (i+1)+" ##############");
			//decisionTrees.get(i).inOrderTraverseTree(decisionTrees.get(i).root);
		}
		


		double a=0;
		double b=0;
		double c=0;
		double d=0;
		for(int i=0; i<test.size(); i++){
			Vector<Double> row = new Vector<Double>();
			row = test.get(i);
			int label = ValidateRow(row);
			//System.out.println("Actual: "+row.get(row.size()-1)+"  Guessed: "+label);
			
			if (row.get(row.size()-1) == label) {
				if(label==0){
					d++;
				}
				if(label==1){
					a++;
				}

			}
			if (row.get(row.size()-1) != label) {
				if(label==0){
					b++;
				}
				if(label==1){
					c++;
				}
			}
		}
		
		double accuracy = (a + d)/(a + b + c + d);
		double precision = (a)/(a + c); if((a+c) == 0) {precision = a;}
		double recall = (a/ (a + b)); if((a+b) == 0) {recall = a;}
		double fMeasure = ((2*recall*precision)/(recall + precision));
		
		DecimalFormat df = new DecimalFormat("#.###");
		System.out.println("************ Average ***************");
		System.out.println("Accuracy : "+df.format(accuracy));
		System.out.println("Precision: "+df.format(precision));
		System.out.println("Recall   : "+df.format(recall));
		System.out.println("F-Measure: "+df.format(fMeasure));  
		

	}
	
	private int ValidateRow(Vector<Double> row) {
		
		double label = -1;
		int count0 = 0;
		int count1 = 0;
		
		for(int i=0; i<decisionTrees.size(); i++){
			BinaryTree decisionTree = new BinaryTree();
			decisionTree = decisionTrees.get(i);
			
			
			
			if (decisionTree.root == null) {
				System.out.println("No Decision tree exits!!!");
				return -1;
			} else {

				Node parentNode = decisionTree.root;
				Node focusNode = decisionTree.root;

				while (focusNode != null) {
					parentNode = focusNode; 
					
					/*if(parentNode.node == "leaf"){
						break;
					}*/
					
					int col = focusNode.getColumn();
					//System.out.println(col+" "+tc[col]+" "+focusNode.getKey());
					if ( row.get(col) < focusNode.getKey()) {
						focusNode = focusNode.leftChild;
						//System.out.println("Left");

					} 

					//System.out.println(col+" "+tc[col]+" "+focusNode.getKey());
					if ( focusNode != null && row.get(col) >= focusNode.getKey()){
						//System.out.println("Right  ");
						focusNode = focusNode.rightChild;
					}
					//System.out.print(col+" "+tc[col]+" "+focusNode.getKey());
				}
				
				//System.out.println("Done");
				if (row.get(parentNode.getColumn()) < parentNode.getKey()) {
					label = parentNode.leftLabel;
				} 
				if ( row.get(parentNode.getColumn()) >= parentNode.getKey()) {
					label = parentNode.rightLabel;
				}
				
				if(label == 0){
					count0++;
				}
				if(label == 1){
					count1++;
				}			
			}
			
		}
		
		if(count0 > count1){
			return 0;
		}
		
		if(count1 > count0){
			return 1;
		}

		return -1;
	}

	
}
