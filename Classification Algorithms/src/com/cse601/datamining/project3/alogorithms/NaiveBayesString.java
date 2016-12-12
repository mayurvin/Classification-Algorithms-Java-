package com.cse601.datamining.project3.alogorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class NaiveBayesString {

	private int dimensionDS = 0; // Store dimension count of dataset1

	private static String LABEL_0 = "LABEL0";
	private static String LABEL_1 = "LABEL1";

	HashMap<String,Double> datasetValues = new HashMap<String,Double>();
	HashMap<String,Double> testDatasetValues = new HashMap<>();
	int kFactor = 0;
	int testPartitionIndex = 0;
	int booleanDimensionIndex = -1;

	Vector<Vector> finalDataset = new Vector<>();
	Vector<Vector> finalTestDataset = new Vector<>();

	Vector<Vector> trainDataset = new Vector<>();
	Vector<Vector> testDataset = new Vector<>();
	

	Vector<Vector> sample0DataSet = new Vector<>(); //Dataset having label 0
	Vector<Vector> sample1DataSet = new Vector<>(); //Dataset having label 1

	Vector<Dimension> dimensionArray0 = new Vector<>();
	Vector<Dimension> dimensionArray1 = new Vector<>();

	private String actualLabel[] = null;
	private String predictionLabel[] = null;

	double probLabel1; //Probability that the label is 1
	double probLabel0; //Probability that the label is 0

	public NaiveBayesString(int kFactor) {
		
		readFiles(System.getProperty("user.dir") + "/src/Data/project3_dataset4.txt", trainDataset);
		prepareDataset();
		//System.out.println("asdfas" + trainDataset.get(0));
		this.kFactor = kFactor;
		dimensionDS = finalDataset.get(0).size();
		//partitionDataset(this.finalDataset, this.kFactor);
			//assignPartitions();
			trainDataset = trainDataset;
			Vector vect = new Vector<>();
			vect.add("sunny");
			vect.add("cool");
			vect.add("high");
			vect.add("weak");
			vect.add(0.0);
			testDataset.add(vect);
			prepareTestDataset();
			operation();
			testPartitionIndex++;
	}

	public void operation(){
		samplingClassification(); // Separate the dataset based on probabilities.
		dimensionClassification(sample0DataSet, dimensionArray0); // Separate the above data based on dimensions.
		dimensionClassification(sample1DataSet, dimensionArray1);

		double total = sample0DataSet.size() + sample1DataSet.size();
		probLabel0 = (sample0DataSet.size()/total);
		probLabel1 = (sample1DataSet.size()/total);

		actualLabel = new String[finalTestDataset.size()];
		predictionLabel = new String[finalTestDataset.size()];
		//System.out.println("*** " + finalTestDataset.get(0));
		for(int i=0;i<finalTestDataset.size();i++){
			predictionLabel[i] = calcPosterior(finalTestDataset.get(i));
			if((double)finalTestDataset.get(i).get(dimensionDS-1) == 0.0)
				actualLabel[i] = LABEL_0;
			if((double)finalTestDataset.get(i).get(dimensionDS-1) == 1.0)
				actualLabel[i] = LABEL_1;
		}
		//accuracy();
	}

	//Find the conditional probability using the Normal Distribution.
	public double normalDist(double mean, double variance, double observation){
		double denom1 = 2*Math.PI * variance;
		double denom = Math.pow(denom1, 0.5); // variance is signma squared
		double EPow = (-Math.pow((observation-mean),2)) / (2*variance);
		return ((Math.pow(Math.E, EPow))/denom);
	}

	//Sampling data based on the labels. Put label0 data into one Vector and label1 data set into another Vector.
	public void samplingClassification(){
		for(int i=0;i<finalDataset.size();i++){
			//System.out.println(finalDataset.get(i));
			if((double)finalDataset.get(i).get(dimensionDS-1) == 0.0){
				sample0DataSet.add(finalDataset.get(i));
			}else if((double)finalDataset.get(i).get(dimensionDS-1) == 1.0){
				sample1DataSet.add(finalDataset.get(i));
			}
		}
	}
	
	void prepareDataset(){
		for(int i=0;i<trainDataset.size();i++){
			for(int j=0;j<trainDataset.get(i).size()-1;j++){
				datasetValues.put((String.valueOf(trainDataset.get(i).get(j))),0.0);
			}
		}
		
		Set<String> temp = datasetValues.keySet();
		Iterator<String> iter = temp.iterator();
		double count = 0;
		while(iter.hasNext()){
			String str = iter.next();
			datasetValues.put(str, count);
			count++;
		}
		
		//System.out.println(datasetValues.keySet());
		//System.out.println(datasetValues.values());
		for(int i=0;i<trainDataset.size();i++){
			Vector vect = new Vector<>();
			for(int j=0;j<trainDataset.get(i).size()-2;j++){
				vect.add(datasetValues.get(trainDataset.get(i).get(j)));
			}
			vect.add(trainDataset.get(i).get(trainDataset.get(i).size()-1));
			finalDataset.add(vect);
		}
	}

	void prepareTestDataset(){
		/*for(int i=0;i<testDataset.size();i++){
			for(int j=0;j<testDataset.get(i).size()-1;j++){
				datasetValues.put((String.valueOf(trainDataset.get(i).get(j))),0.0);
			}
		}*/
		
		Set<String> temp = datasetValues.keySet();
		Iterator<String> iter = temp.iterator();
		double count = 0;
		while(iter.hasNext()){
			String str = iter.next();
			datasetValues.put(str, count);
			count++;
		}
		
		//System.out.println(datasetValues.keySet());
		//System.out.println(datasetValues.values());
		for(int i=0;i<testDataset.size();i++){
			//System.out.println("**** " + testDataset.get(i));
			Vector vect = new Vector<>();
			for(int j=0;j<testDataset.get(i).size()-2;j++){
				vect.add(datasetValues.get(testDataset.get(i).get(j)));
			}
			vect.add(testDataset.get(i).get(4));
			finalTestDataset.add(vect);
			System.out.println("Test Data " + finalTestDataset.get(i));
		}
	}

	
	public void dimensionClassification(Vector<Vector> sampleInputArr, Vector<Dimension> dimensionArr){
		Dimension dimension;
		for(int i=0; i< dimensionDS;i++){
			dimension = new Dimension();
			double[] column = new double[sampleInputArr.size()];
				for(int j=0;j<column.length-1;j++ ){
					column[j] = (double)(sampleInputArr.get(j).get(i));
				}
				dimension.setColumn(column);

				double sum =0;
				for(int j=0;j<column.length-1;j++ ){
					sum = sum + column[j];
				}
				double mean = (sum/column.length);
				dimension.setMean(mean);

				sum = 0;
				for(int j=0;j<column.length-1;j++ ){
					sum = sum + Math.pow((mean - column[j]),2); 
				}
				double variance = sum/column.length;
				dimension.setVariance(variance);
			dimensionArr.add(i,dimension);
		}
	}

	//Given vector which is the set of observations and the label that you want to predict the posterior for.
	public String calcPosterior(Vector observation){
		//p(Sample1|D) =  p(Sample1) * [p(D1|sample1) * p(D2|Sample1) * ..... * (D31|Sample1)] / [p(D1|sample1) * p(D2|Sample1) * ..... * (D31|Sample1)] * [p(D1|sample2) * p(D2|Sample2) * ..... * (D31|Sample2)]
		double[] posteriorNumArray0 =  new double[observation.size()];
		double[] posteriorNumArray1 =  new double[observation.size()];

		//For 0 labels
		for(int i = 0; i<dimensionDS;i++){
			double mean = dimensionArray0.get(i).getMean(); 
			double variance = dimensionArray0.get(i).getVariance();
			double obs = (double)observation.get(i);
			posteriorNumArray0[i] = normalDist(mean, variance, obs);
		}
		//For 1 labels
		for(int i = 0; i<dimensionDS;i++){

			double mean = dimensionArray1.get(i).getMean(); 
			double variance = dimensionArray1.get(i).getVariance();
			double obs = (double)observation.get(i);
			posteriorNumArray1[i] = normalDist(mean, variance, obs);
		}

		//multiply all the probabilities
		double posteriorNumerator0 = 0;
		if(posteriorNumArray0.length>0){
			posteriorNumerator0 = posteriorNumArray0[0];
			for(int i=1;i<posteriorNumArray0.length-1;i++){
				posteriorNumerator0 = posteriorNumerator0 * posteriorNumArray0[i];
			}
			posteriorNumerator0 = posteriorNumerator0 * probLabel0;
		}

		double posteriorNumerator1 = 0;
		if(posteriorNumArray1.length>0){
			posteriorNumerator1 = posteriorNumArray1[0];
			for(int i=0;i<posteriorNumArray1.length-1;i++){
				posteriorNumerator1 = posteriorNumerator1 * posteriorNumArray1[i];
			}
			posteriorNumerator1 = posteriorNumerator1 * probLabel1;
		}

		double posterior0 = (posteriorNumerator0/(posteriorNumerator0+posteriorNumerator1));
		double posterior1 = (posteriorNumerator1/(posteriorNumerator0+posteriorNumerator1));
		
		System.out.println("Probability for label 0 : " + posterior0);
		System.out.println("Probability for label 1 : " + posterior1);
		
		if(posterior0 > posterior1)
			return LABEL_0;
		else
			return LABEL_1;
	}

	public void accuracy(){
		double a = 0;
		double b = 0;
		double c = 0;
		double d = 0;

		for(int i=0; i<actualLabel.length; i++){
			if(actualLabel[i] == LABEL_0 && predictionLabel[i] == LABEL_0){
				a++;
			}
			if(actualLabel[i] == LABEL_0 && predictionLabel[i] == LABEL_1){
				b++;
			}
			if(actualLabel[i] == LABEL_1 && predictionLabel[i] == LABEL_0){
				c++;
			}
			if(actualLabel[i] == LABEL_1 && predictionLabel[i] == LABEL_1){
				d++;
			}
		}
		double accuracy = (a + d)/(a + b + c + d);
		
		if(a==0)
			a=1;
		if(b==0)
			b=1;
		if(c==0)
			c=1;
		if(d==0)
			d=1;
		//going by the matrix in the slide 8 of Clustering Basics.
		
		double precision = (a)/(a + c);
		double recall = (a/ (a + b));
		double fMeasure = ((2*accuracy*precision)/(accuracy + precision));
		
		System.out.println("************ Average ***************");
		System.out.println("Accuracy " + accuracy);
		System.out.println("Precision " + precision);
		System.out.println("Recall " + recall);
		System.out.println("fMeasure " + fMeasure);
	}

	public int getDimensionDS() {
		return dimensionDS;
	}
	public void setDimensionDS(int dimensionDS) {
		this.dimensionDS = dimensionDS;
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
						vect.add(elements[i]);
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
}
