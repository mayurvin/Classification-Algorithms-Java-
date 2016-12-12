package com.cse601.datamining.project3.alogorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Main{
	public static String PRESENT = "Present";
	public static String ABSENT = "Absent";

	Vector<Vector> dataset1 = new Vector<>();
	Vector<Vector> dataset2 = new Vector<>();
	Vector<Vector> dataset3 = new Vector<>();
	Vector<Vector> dataset4 = new Vector<>();
	Vector<Vector> data1 = new Vector<>();
	Vector<Vector> data2 = new Vector<>();

	public static int datasetBooleanDimension = -1;
	public static int kValue = 10;	

	int dimensionDS1; // Store dimension count of dataset1
	int dimensionDS2; // Store dimension count of dataset2

	static NearestNeighbour nearestNeighbour;
	static DecisionTree decisionTree;
	static RandomForest randomForest;

	public static void main(String args[]){

		Main main = new Main();
		main.readFile(System.getProperty("user.dir") + "/src/Data/project3_dataset1.txt", main.data1);
		main.readFile(System.getProperty("user.dir") + "/src/Data/project3_dataset2.txt", main.data2);

		/*
		//######################### NEAREST NEIGHBOUR BEGIN ############################
		nearestNeighbour = new NearestNeighbour();
		System.out.println("\n\n######################### NEAREST NEIGHBOUR BEGIN ############################");
		System.out.println("\n*** For Data 1 ***");
		nearestNeighbour.process(main.data1);
		System.out.println("\n\n***For Data 2 ***");
		nearestNeighbour.process(main.data2);
		System.out.println("\n\n######################### NEAREST NEIGHBOUR END ############################\n\n");
		//######################### NEAREST NEIGHBOUR END ############################


		
		//######################### DECISION TREE BEGIN ############################
		decisionTree = new DecisionTree();
		System.out.println("\n\n######################### DECISION TREE BEGIN ############################");
		System.out.println("\n*** For Data 1 ***");
		decisionTree.process(main.data1);
		System.out.println("\n\n*** For Data 2 ***");
		decisionTree.process(main.data2);
		System.out.println("\n\n######################### DECISION TREE End ############################\n\n");
		//######################### DECISION TREE END ############################
		 

		//######################### Cross Validation Begin ############################
		nearestNeighbour = new NearestNeighbour();
		System.out.println("\n\n######################### Nearest Neighbour "+kValue+"-Fold Cross Validation Begin ############################");
		System.out.println("\n*** For Data 1 ***");
		nearestNeighbour.CrossValidate(main.data1, kValue);
		System.out.println("\n\n***For Data 2 ***");
		nearestNeighbour.CrossValidate(main.data2, kValue);
		System.out.println("\n\n######################### Nearest Neighbour "+kValue+"-Fold Cross Validation End ############################\n\n");
		//######################### Cross Validation End ############################

		//######################### DECISION TREE BEGIN ############################
		decisionTree = new DecisionTree();
		System.out.println("\n\n######################### Decision Tree "+kValue+"-Fold Cross Validation Begin ############################");
		System.out.println("\n*** For Data 1 ***");
		decisionTree.CrossValidate(main.data1, kValue);
		System.out.println("\n\n*** For Data 2 ***");
		decisionTree.CrossValidate(main.data2, kValue);
		System.out.println("\n\n######################### Decision Tree "+kValue+"-Fold Cross Validation End ############################\n\n");
		//######################### DECISION TREE END ############################

		//######################### RANDOM FOREST BEGIN ############################
		randomForest = new RandomForest();
		System.out.println("\n\n######################### Random Forest "+kValue+"-Fold Cross Validation Begin ############################");
		System.out.println("\n*** For Data 1 ***");
		randomForest.process(main.data1);
		System.out.println("\n\n*** For Data 2 ***");
		randomForest.process(main.data2);
		System.out.println("\n\n######################### Random Forest "+kValue+"-Fold Cross Validation End ############################\n\n");
		//######################### RANDOM FOREST END ############################
		
		*/
		//######################### NAIVE BAYES BEGIN ############################
		System.out.println("\n\n######################### Naive Bayes  ############################");
		System.out.println("\n*** For Data 1 ***");
		main.readFiles(System.getProperty("user.dir") + "/src/Data/project3_dataset3_train.txt", main.dataset1);
		main.readFiles(System.getProperty("user.dir") + "/src/Data/project3_dataset3_test.txt", main.dataset2);
		NaiveBayes_1 nb1 = new NaiveBayes_1(main.dataset1,kValue, main.dataset2);
		System.out.println("\n\n*** For Data 2 ***");
		//main.readFiles(System.getProperty("user.dir") + "/src/Data/project3_dataset4.txt", main.dataset2);
		NaiveBayesString nbs = new NaiveBayesString(2);
		System.out.println("\n\n######################### Naive Bayes ############################\n\n");
		//######################### NAIVE BAYES END ############################
		
		/*for(int i=0; i<kValue; i++){
			float a, p, r, f;
			a = (float)nb.getAccuracy()[i];
			p = (float)nb.getPrecision()[i];
			r = (float)nb.getRecall()[i];
			f = (float)nb.getfMeasure()[i];
			System.out.println(a+" "+p+" "+r+" "+f);
		}*/

		//System.out.println(main.nb.);
		//main.nb = new NaiveBayes(main.dataset2,3);

	}

	public void readFiles(String filePath, Vector<Vector> dataset){
		FileReader fr;
		String[] elements = null;
		try {
			String line;
			fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine())!=null){
				Vector vect = new Vector();
				elements = line.split("\\s+"); // Extracting numbers in each line. Nodes per edge in our case.
				for(int i=0;i<elements.length;i++){
					try{
						vect.add(Double.parseDouble(elements[i]));
					}catch(NumberFormatException nfe){
						if(elements[i].equalsIgnoreCase(PRESENT)){
							datasetBooleanDimension = i;
							vect.add(Boolean.TRUE);
							//vectBool.add(Boolean.TRUE);
						}
						else if(elements[i].equalsIgnoreCase(ABSENT)){
							datasetBooleanDimension = i;
							vect.add(Boolean.FALSE);
						}
					}
				}
				dataset.add(vect);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readFile(String filePath, Vector<Vector> dataset){

		FileReader fr;
		String[] elements = null;
		try {
			System.out.println("readfiles : " + filePath);
			String line;
			fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			while((line = br.readLine())!=null){
				Vector vect = new Vector();
				//Vector<Boolean> vectBool = new Vector<Boolean>();
				elements = line.split("\\s+"); // Extracting numbers in each line. Nodes per edge in our case.
				for(int i=0;i<elements.length;i++){
					try{
						vect.add(Double.parseDouble(elements[i]));
					}catch(NumberFormatException nfe){
						if(elements[i].equalsIgnoreCase(PRESENT)){
							vect.add((double)1);
							//vectBool.add(Boolean.TRUE);
						}
						else if(elements[i].equalsIgnoreCase(ABSENT)){
							vect.add((double)0);
						}
					}
				}
				//if(datasetBooleanDimension!=-1)
				//System.out.println(datasetBooleanDimension);
				dataset.add(vect);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
