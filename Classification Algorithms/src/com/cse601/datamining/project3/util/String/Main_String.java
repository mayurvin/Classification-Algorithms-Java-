package com.cse601.datamining.project3.util.String;



import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import com.cse601.datamining.project3.alogorithms.DecisionTree;

public class Main_String{
	
	Vector<Vector> dataset4 = new Vector<Vector>();
	static DecisionTree decisionTree;
	
	public static void main(String args[]){

		Main_String main = new Main_String();
		//System.out.println(main.dataset4.size());
		main.readFile(System.getProperty("user.dir") + "/src/Data/project3_dataset4.txt", main.dataset4);
		//System.out.println(main.dataset4.size());

		//######################### DECISION TREE BEGIN ############################
		decisionTree = new DecisionTree();
		System.out.println("\n\n######################### DECISION TREE BEGIN ############################");
		//System.out.println("\n*** For Data 1 ***");
		decisionTree.process(main.dataset4);
		//System.out.println("\n*** For Data 2 ***");
		//decisionTree.process(main.data2);
		System.out.println("\n######################### DECISION TREE End ############################\n");
		//######################### DECISION TREE END ############################
				
	}
	
	public void readFile(String filePath, Vector<Vector> dataset) { // to read contents of the file

		try {
			FileInputStream fileInputStream = null;
			BufferedInputStream bufferedInputStream = null;
			DataInputStream dataInputStream = null;

			File file = new File(filePath);
			fileInputStream = new FileInputStream(file);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			dataInputStream = new DataInputStream(bufferedInputStream);

			try {
				while (dataInputStream.available() != 0) {
					String line = dataInputStream.readLine();
					String[] col = line.split("\\t");				
					Vector<Double> row = new Vector<Double>();

					for (int i = 0; i < col.length; i++) {
						try {
							row.add(Double.parseDouble(col[i]));
						} catch (NumberFormatException nfe) {
							row.add(DoubleValue(col[i]));
						}

					}
					dataset.add(row);
					
				}
				fileInputStream.close();
				bufferedInputStream.close();
				dataInputStream.close();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			

		} catch (FileNotFoundException e) {						
			System.out.println("Error Reading File: Please check file name!");
			System.exit(0);
		}
	}
	
	public static double DoubleValue(String value) {
		double val = -1;

		switch(value){
			case "sunny"   : val = 2; break;
			case "overcast": val = 3; break;
			case "rain"    : val = 4; break;
			case "hot"     : val = 5; break;
			case "mild"    : val = 6; break;
			case "cool"    : val = 7; break;
			case "high"    : val = 8; break;
			case "normal"  : val = 9; break;
			case "weak"    : val = 10; break; 
			case "strong"  : val = 11; break;
		}
		return val;
	}
}
