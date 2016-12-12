package com.cse601.datamining.project3.alogorithms;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

import com.cse601.datamining.project3.util.BinaryTree;
import com.cse601.datamining.project3.util.EvaluateGini;
import com.cse601.datamining.project3.util.BinaryTree.Node;

public class DecisionTree {

	private int rows;
	private int cols;
	ArrayList<EvaluateGini> externalSplitGini;  // list of all minimum gini values corresponding to each column
	ArrayList<ArrayList<Double>> stats = new ArrayList<ArrayList<Double>> ();
	double a;
	double b;
	double c;
	double d;
	
	int lc = 0;
	int nlc = 0;
	
	private Vector<Vector> trainN;
	private Vector<Vector> testN;

	BinaryTree tree;
	
	
	public void process(Vector<Vector> dataSet){
		Vector<Vector> test = new Vector<Vector>();
		Vector<Vector> train = new Vector<Vector>();
		int size = dataSet.size();
		//size = size-(size%10);
		int len= (int) (size * (0.5));
		
		for(int i=0; i<len; i++){
			test.add(dataSet.get(i));
		}
		for(int j=len; j<dataSet.size() ;j++){
			train.add(dataSet.get(j));
		}
		
		stats = new ArrayList<ArrayList<Double>> ();		
		
		process(test, train, false, 0);
		
		DecimalFormat df = new DecimalFormat("#.###");
		for(int k=0; k<stats.size() ;k++){
			ArrayList<Double> stat = new ArrayList<Double>();
			stat = stats.get(k);
			
			System.out.println("Accuracy : "+df.format(stat.get(0)));
			System.out.println("Precision: "+df.format(stat.get(1)));
			System.out.println("Recall   : "+df.format(stat.get(2)));
			System.out.println("F-Measure: "+df.format(stat.get(3)));  
		}
		
		//System.out.println("Leaf: "+lc+" NLC: "+nlc);
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public BinaryTree process(Vector<Vector> test_data, Vector<Vector> train_data, boolean randomForest, int numRanCols) {
		// use local copy of given data
		
		//double[][] test = new double[test_data.size()][test_data.get(0).size()];
		//double[][] train = new double[train_data.size()][train_data.get(0).size()];
		
		trainN = new Vector<Vector>();
		testN = new Vector<Vector>();
		tree = new BinaryTree();
		a=0;
		b=0;
		c=0;
		d=0;
		
		//testN = MinMaxNormalize(test_data);
		//trainN = MinMaxNormalize(train_data);
		testN = test_data;
		trainN = train_data;
		
		double[][] test = new double[testN.size()][testN.get(0).size()];
		double[][] train = new double[trainN.size()][trainN.get(0).size()];


		// convert test to matrix
		rows = test_data.size();
		cols = test_data.get(0).size();
		for (int i = 0; i < rows; i++) {
			Vector<Double> row = test_data.get(i);
			for (int j = 0; j < cols; j++) {
				test[i][j] = row.get(j);
			}
		}

		// convert train to matrix
		rows = train_data.size();
		cols = train_data.get(0).size();
		for (int i = 0; i < rows; i++) {
			Vector<Double> row = train_data.get(i);
			for (int j = 0; j < cols; j++) {
				train[i][j] = row.get(j);
			}
		}
		//System.out.println("here");
		CreateTree(train, 0, rows-1, 0, 0, randomForest, numRanCols);
		//tree.inOrderTraverseTree(tree.root);

		
		//System.exit(0);
		for(int i=0; i<test_data.size(); i++){//
			//System.out.println("\n\n\t For row: "+ i);
			ValidateRow(test[i]);
		}
		
		
		if(randomForest)
			return tree;
		
		//System.out.println(a+"  "+b+"  "+c+"  "+d);
		double accuracy = (a + d)/(a + b + c + d);
		double precision = (a)/(a + c); if((a+c) == 0) {precision = a;}
		double recall = (a/ (a + b)); if((a+b) == 0) {recall = a;}
		double fMeasure = ((2*recall*precision)/(recall + precision));  if(fMeasure == 0) {fMeasure = 2*recall*precision;}
		
		DecimalFormat df = new DecimalFormat("#.#####");
		System.out.println();
		System.out.println("Accuracy : "+df.format(accuracy));
		System.out.println("Precision: "+df.format(precision));
		System.out.println("Recall   : "+df.format(recall));
		System.out.println("F-Measure: "+df.format(fMeasure)); 
		
		ArrayList<Double> stat = new ArrayList<Double>();
		stat.add(accuracy);
		stat.add(precision);
		stat.add(recall);
		stat.add(fMeasure);
		//System.out.println(stat);
		stats.add(stat);
		
		//System.out.println();
		return tree;
	}


	private void ValidateRow(double[] tc) {
		
		double label = -1;

		if (tree.root == null) {
			System.out.println("No Decision tree exits!!!");
			return;
		} else {

			Node parentNode = tree.root;
			Node focusNode = tree.root;
			
			while (focusNode != null) {
				parentNode = focusNode; 
				
				if(parentNode.node == "leaf"){
					break;
				}
				
				int col = focusNode.getColumn();
				//System.out.println(col+" "+tc[col]+" "+focusNode.getKey());
				if ( tc[col] < focusNode.getKey()) {
					focusNode = focusNode.leftChild;
					//System.out.println("Left");

				} else{
					//System.out.println("Right  ");
					focusNode = focusNode.rightChild;
				}
				//System.out.print(col+" "+tc[col]+" "+focusNode.getKey());
			}
			
			//System.out.println("Done");
			if ( tc[parentNode.getColumn()] < parentNode.getKey()) {
				label = parentNode.leftLabel;
			} else{// ( tc[parentNode.getColumn()] >= parentNode.getKey()) {
				label = parentNode.rightLabel;
			}
			

			
			if (tc[cols - 1] == label) {
				if(label==0){
					d++;
				}
				if(label==1){
					a++;
				}

			}
			if (tc[cols - 1] != label) {
				if(label==0){
					b++;
				}
				if(label==1){
					c++;
				}
			}			
		}
	}


	private void CreateTree(double[][] train, int begin1, int end1, int begin2, int end2, boolean randomForest, int numRanCols) {
		
		if(begin1<end1){
			//if(end1-begin1 == 1)
				//System.out.println("Left has 1 row");
			CreateNode(train, begin1, end1, randomForest, numRanCols);
		}
		
		if(begin2<end2){
			//if(end2-begin2 == 1)
				//System.out.println("Right has 1 row");
			CreateNode(train, begin2, end2, randomForest, numRanCols);
		}
		
	}


	private void CreateNode(double[][] train, int begin, int end, boolean randomForest, int numRanCols) {
		
		externalSplitGini = new ArrayList<EvaluateGini>();
		Vector<Integer> ranCols =  new Vector<Integer>(); 
		
		if (randomForest) {
			while(ranCols.size() < numRanCols){
				int num = (int) (Math.random() * ( cols-1 - 0 ));//rn.nextInt() % n;
				if(ranCols.contains(num)){
					continue;
				}else{
					ranCols.add(num);
				}			
			}
			//System.out.println(ranCols);
			for (int i = 0; i < ranCols.size(); i++) { // sort data by each column and create a node

				Sort(train, begin, end, ranCols.get(i));
				InternalGini(train, begin, end, ranCols.get(i));
			}
			

		} else {
			for (int i = 0; i < cols-1; i++) { // sort data by each column and create a node

				Sort(train, begin, end, i);
				InternalGini(train, begin, end, i);
			}
		}
		
		
		if (externalSplitGini.size() == 0) {
			return;
		} 
		
		// find minimum gini value in externalGini list
		int minPos = 0;
		for (int i = 0; i < externalSplitGini.size(); i++) {
			if (externalSplitGini.get(i).gini < externalSplitGini.get(minPos).gini) {
				minPos = i;
			}
		}

		// re-arrange data as per the most significant column
		Sort(train, begin, end, externalSplitGini.get(minPos).column);

		// mark the most significant column as used. set all elements as -1
		int col = externalSplitGini.get(minPos).column;
		for (int row = begin; row < end; row++) {
			train[row][col] = -1;
		}

		// add node to the tree
		tree.addNode(externalSplitGini.get(minPos).key,
				externalSplitGini.get(minPos).column,
				externalSplitGini.get(minPos).node,
				externalSplitGini.get(minPos).leftLabel,
				externalSplitGini.get(minPos).rightLabel, externalSplitGini.get(minPos).begin, externalSplitGini.get(minPos).row, externalSplitGini.get(minPos).end );

		
		
		if (externalSplitGini.get(minPos).node == "leaf") {
			lc++;
			//CreateTree(train, 0, 0, 0, 0, randomForest, numRanCols);
			return;
		} else {
			nlc++;
		}
			
			
			
			// recursion
		int mid = (externalSplitGini.get(minPos).begin + externalSplitGini.get(minPos).end)/2;
		CreateTree(train, externalSplitGini.get(minPos).begin, externalSplitGini.get(minPos).row, externalSplitGini.get(minPos).row, externalSplitGini.get(minPos).end, randomForest, numRanCols);		
		

	}

	
	private void InternalGini(double[][] attributeClass, int begin, int end, int col) {

		ArrayList<EvaluateGini> internalGini = new ArrayList<EvaluateGini>(); // list of all gini values corresponding to a column
		String node;
		double leftLabel =-1;
		double rightLabel =-1;

		int count0;
		int count1;
		
		if(attributeClass[begin][col] == -1){
			return; // check for visited			
		}else{
			// find spits
			for (int row = begin; row < end-1; row++) {
				if (attributeClass[row][col] < attributeClass[row + 1][col]) {
					int[][] count = new int[2][2];

					// find counts till splits
					count0 = 0;
					count1 = 0;
					for (int k = begin; k <= row; k++) {
						if (attributeClass[k][cols-1] == 0) {
							count0++;
						} else {
							count1++;
						}
					}
					count[0][0] = count0;
					count[0][1] = count1;

					// find counts after splits
					count0 = 0;
					count1 = 0;
					for (int k = row + 1; k < end; k++) {
						if (attributeClass[k][cols-1] == 0) {
							count0++;
						} else {
							count1++;
						}
					}
					count[1][0] = count0;
					count[1][1] = count1;

					// calculate Gini
					double gini = Gini(count);
							

					
					if(gini==0){
						node = "leaf";
						//System.out.println(count[0][0]+"  "+count[0][1]);
						//System.out.println(count[1][0]+"  "+count[1][1]);
						//System.out.println();
						
						if(count[0][0] == 0){
							leftLabel = 1;
						}else{//						if(count[0][0] == 1){
							leftLabel = 0;
						}
						
						if(count[1][0] == 0){
							rightLabel = 1;
						}else{//						if(count[1][1] == 0){
							rightLabel = 0;
						}
						//lc++;

					}else{
						node = "not-leaf";
						if(count[0][0]>count[0][1]){
							leftLabel = 0;
						}else{//	if(count[0][0]<count[0][1]){
							leftLabel = 1;
						}
						
						if(count[1][0]>count[1][1]){
							rightLabel = 0;
						}else{//	if(count[1][0]<count[1][1]){
							rightLabel = 1;
						}
						//nlc++;
					}
					
					double key = (attributeClass[row][col] + attributeClass[row+1][col])/2;
					EvaluateGini eachSplitGini = new EvaluateGini(gini, key, col, row, begin, end, leftLabel, rightLabel, node);
					internalGini.add(eachSplitGini);					
				}
			}
		}
		
		//System.out.println("internal Gini Size: "+internalGini.size()+ " for col: "+col);

		// find minimum gini value in internalGini list
		int minPos = -1;
		if (internalGini.size() > 0) {
			minPos = 0;

			for (int i = 0; i < internalGini.size(); i++) {
				if (internalGini.get(i).gini < internalGini.get(minPos).gini) {
					minPos = i;
				}
			}
			externalSplitGini.add(internalGini.get(minPos));
		}
	}

	// claculate Gini for each column
	private double Gini(int[][] count) {

		double sumBefore = count[0][0] + count[0][1];
		double sumAfter = count[1][0] + count[1][1];
		double sum = sumBefore + sumAfter;
		double p0Before = Math.pow(((count[0][0]) / sumBefore), 2);
		double p1Before = Math.pow(((count[0][1]) / sumBefore), 2);
		double p0After = Math.pow(((count[1][0]) / sumAfter), 2);
		double p1After = Math.pow(((count[1][1]) / sumAfter), 2);
		double giniBefore = 1 - p0Before - p1Before;
		double giniAfter = 1 - p0After - p1After;
		double gini = (sumBefore / sum) * giniBefore + (sumAfter / sum)
				* giniAfter;

		return gini;
	}
	

	// sort matrix based on 0th column
	private void Sort(double[][] attributeClass, int begin, int end, int col) {

		for (int i = begin; i < end; i++) {
			for (int j = begin; j < end - 1; j++) {
				if (attributeClass[j][col] > attributeClass[j + 1][col]) {
					double[] temp = attributeClass[j];
					attributeClass[j] = attributeClass[j + 1];
					attributeClass[j + 1] = temp;
				}
			}
		}
	}
	
private Vector<Vector<Double>> MinMaxNormalize(Vector<Vector> dataSet) {
		
		Vector<Double> minValues = new Vector<Double>();
		Vector<Double> maxValues = new Vector<Double>();
		Vector<Vector<Double>> data = new Vector<Vector<Double>>();
		
		for (int i = 0; i < dataSet.get(0).size()-1; i++) {// for each row
			minValues.add(Double.MAX_VALUE);
			maxValues.add(Double.MIN_VALUE);
		}
		
		// find min & max values of each coulun
		for (int i = 0; i < dataSet.size(); i++) {// for each row
			Vector<Double> row = dataSet.get(i);
			for (int j = 0; j < row.size() - 1; j++) {// for each element in a column
				
				if(row.get(j)>maxValues.get(j)){ // check for max value
					maxValues.set(j, row.get(j));
				}
				if(row.get(j)<minValues.get(j)){ // check for min value
					minValues.set(j, row.get(j));
				}
			}
		}
		
		for (int i = 0; i < dataSet.size(); i++) {// for each row
			
			Vector<Double> row = dataSet.get(i);
			
			// create local copy of data to avoid conflict
			Vector<Double> newRow = new Vector<Double>();
			for (int j = 0; j < row.size(); j++) {
				newRow.add(row.get(j));
			}
			
			//Normalize
			for (int j = 0; j < newRow.size() - 1; j++) {// for each column
				double element = ((newRow.get(j)-minValues.get(j)) / (maxValues.get(j)-minValues.get(j))) * (1-0) + 0;
				newRow.set(j, element);

			}
			// add rows to new data
			data.add(newRow);
		}
		
		return data;
	}

	public void CrossValidate(Vector<Vector> dataSet, int k) {
		stats = new ArrayList<ArrayList<Double>>();
		
		Vector<Vector> test;
		Vector<Vector> train;
		int partSize = 0;
		int size = dataSet.size();

		if (size % k == 0) {

			partSize = size / k;

			int start = 0;
			int end = 0;
			int count = 0;

			while (count < k) {
				start = end;
				end = start + partSize;
				test = new Vector<Vector>();
				train = new Vector<Vector>();
				for (int i = start; i < end; i++) {
					test.add(dataSet.get(i));
				}

				for (int j = 0; j < size; j++) {
					if (j < start || j >= end)
						train.add(dataSet.get(j));
					// start++;
				}
				// System.out.println(start+"  "+end+"  "+test.size()
				// +"  "+train.size());

				process(test, train, false, 0);
				count++;
			}
		} else {
			partSize = size / k;
			int buffer = size % k;
			if(buffer<(k/2)){
				size = size - (size%k);
				buffer = 0;
			}

			int start = 0;
			int end = 0;
			int count = 0;

            while(count<k){
                start = end;
                end = start + partSize;
               if(count == 0){
                	end += buffer;
                	buffer = 0;
                }
                test = new Vector<Vector>();
                train = new Vector<Vector>();
                
                for(int i=start; i<end+buffer; i++){
                	test.add(dataSet.get(i));
                }
                
                for(int j=0; j<size; j++){
                	if(j<start || j>=end+buffer)
                		train.add(dataSet.get(j));
                }
                
                //System.out.println(end-start);
                //System.out.println(start+"  "+end+"  "+test.size() +"  "+train.size());
                process(test, train, false, 0);
                count++;
            }

		}
		DecimalFormat df = new DecimalFormat("###.######");
		double accuracy = 0;
		double precision = 0;
		double recall = 0;
		double fMeasure = 0;

		for (int i = 0; i < stats.size(); i++) {
			ArrayList<Double> stat = new ArrayList<Double>();
			stat = stats.get(i);
			int fold = i + 1;
			/*System.out.println("************ "+fold+"th Fold ***************");
			System.out.println("Accuracy : " + df.format(stat.get(0)));
			System.out.println("Precision: " + df.format(stat.get(1)));
			System.out.println("Recall   : " + df.format(stat.get(2)));
			System.out.println("F-Measure: " + df.format(stat.get(3)));*/
			accuracy += stat.get(0);
			precision += stat.get(1);
			recall += stat.get(2);
			fMeasure += stat.get(3);
		}
		
		System.out.println("************ Average ***************");
		//k = k-1;
		System.out.println("Accuracy : " + df.format(accuracy/k));
		System.out.println("Precision: " + df.format(precision/k));
		System.out.println("Recall   : " + df.format(recall/k));
		System.out.println("F-Measure: " + df.format(fMeasure/k));

	}

}
