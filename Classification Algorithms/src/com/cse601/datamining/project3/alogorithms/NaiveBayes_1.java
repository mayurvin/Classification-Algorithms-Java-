package com.cse601.datamining.project3.alogorithms;

import java.util.Vector;
import java.util.Arrays;

import com.cse601.datamining.project3.alogorithms.Main;

public class NaiveBayes_1 {

	private int dimensionDS = 0; // Store dimension count of dataset1

	private static String LABEL_0 = "LABEL0";
	private static String LABEL_1 = "LABEL1";

	int testPartitionIndex = 0;
	int booleanDimensionIndex = -1;

	Vector<Vector<Vector>> partitionLists = new Vector<>(); // list of partitions containing set of samples.

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

	public NaiveBayes_1(Vector<Vector> training, int kFactor, Vector<Vector> testing) {
		this.trainDataset = training;
		this.testDataset = testing;
		dimensionDS = this.trainDataset.get(0).size();
		operation();
	}

	public void operation(){

		samplingClassification(); // Separate the dataset based on probabilities.
		dimensionClassification(sample0DataSet, dimensionArray0); // Separate the above data based on dimensions.
		dimensionClassification(sample1DataSet, dimensionArray1);

		double total = sample0DataSet.size() + sample1DataSet.size();
		probLabel0 = (sample0DataSet.size()/total);
		probLabel1 = (sample1DataSet.size()/total);
		actualLabel = new String[testDataset.size()];
		predictionLabel = new String[testDataset.size()];

		for(int i=0;i<testDataset.size();i++){
			predictionLabel[i] = calcPosterior(testDataset.get(i));
			if((double)testDataset.get(i).get(dimensionDS-1) == 0.0)
				actualLabel[i] = LABEL_0;
			if((double)testDataset.get(i).get(dimensionDS-1) == 1.0)
				actualLabel[i] = LABEL_1;
		}
		accuracy();
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
		for(int i=0;i<trainDataset.size();i++){
			if((double)trainDataset.get(i).get(dimensionDS-1) == 0.0){
				sample0DataSet.add(trainDataset.get(i));
			}else if((double)trainDataset.get(i).get(dimensionDS-1) == 1.0){
				sample1DataSet.add(trainDataset.get(i));
			}
		}
	}

	public void dimensionClassification(Vector<Vector> sampleInputArr, Vector<Dimension> dimensionArr){
		Dimension dimension;
		for(int i=0; i< dimensionDS;i++){
			dimension = new Dimension();
			double[] column = new double[sampleInputArr.size()];
			boolean[] boolColumn = new boolean[sampleInputArr.size()];
			if(i==Main.datasetBooleanDimension){
				double count0s = 0;
				double count1s = 0;
				for(int j=0;j<boolColumn.length-1;j++ ){
					boolColumn[i] = (boolean)(sampleInputArr.get(j).get(i));
					if(boolColumn[i])
						count0s++;
					else
						count1s++;
				}
				dimension.setBoolColumn(boolColumn);
				dimension.setProbabilityOf0s(count0s/boolColumn.length);
				dimension.setProbabilityOf1s(count1s/boolColumn.length);

			}else{
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
			}
			dimensionArr.add(i,dimension);
		}
	}

	//Given vector which is the set of observations and the label that you want to predict the posterior for.
	public String calcPosterior(Vector observation){
		//p(Sample1|D) =  p(Sample1) * [p(D1|sample1) * p(D2|Sample1) * ..... * (D31|Sample1)] / [p(D1|sample1) * p(D2|Sample1) * ..... * (D31|Sample1)] * [p(D1|sample2) * p(D2|Sample2) * ..... * (D31|Sample2)]
		double[] posteriorNumArray0 =  new double[observation.size()];
		double[] posteriorNumArray1 =  new double[observation.size()];

		//For 0 labels
		for(int i = 0; i<dimensionDS-1;i++){
			double mean = dimensionArray0.get(i).getMean(); 
			double variance = dimensionArray0.get(i).getVariance();
			double obs = (double)observation.get(i);
			posteriorNumArray0[i] = normalDist(mean, variance, obs);
		}
		//For 1 labels
		for(int i = 0; i<dimensionDS-1;i++){
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
			if(actualLabel[i] == LABEL_0 && predictionLabel[i] == LABEL_0)
				a++;
			if(actualLabel[i] == LABEL_0 && predictionLabel[i] == LABEL_1)
				b++;
			if(actualLabel[i] == LABEL_1 && predictionLabel[i] == LABEL_0)
				c++;
			if(actualLabel[i] == LABEL_1 && predictionLabel[i] == LABEL_1)
				d++;
			System.out.println(actualLabel[i] + " ** " + predictionLabel[i]);
		}
		//going by the matrix in the slide 8 of Clustering Basics.
		double accuracy = (a + d)/(a + b + c + d);
		double precision = (a)/(a + c);
		double recall = (a/ (a + b));
		double fMeasure = ((2*accuracy*precision)/(accuracy + precision));

		System.out.println("************ Average ***************");
		System.out.println("Accuracy " + accuracy);
		System.out.println("Precision " + precision);
		System.out.println("Recall " + recall);
		System.out.println("fMeasure " + fMeasure);
		
	}

	//Given K partitions K-1 to training and 1 to testing. 
	public void assignPartitions(){
		testDataset = new Vector<>();
		trainDataset = new Vector<>();
		for(int i=0;i<partitionLists.size();i++){
			if(i==testPartitionIndex){
				Vector<Vector> current = partitionLists.get(i);
				for(int j=0;j<current.size();j++)
					testDataset.add(current.get(j));
			}else{
				Vector<Vector> current = partitionLists.get(i);
				for(int j=0;j<current.size();j++)
					trainDataset.add(current.get(j));
			}
		}
	}

	public int getDimensionDS() {
		return dimensionDS;
	}
	public void setDimensionDS(int dimensionDS) {
		this.dimensionDS = dimensionDS;
	}
}
