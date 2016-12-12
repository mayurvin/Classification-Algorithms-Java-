package com.cse601.datamining.project3.alogorithms;

import java.util.Vector;
import java.util.Arrays;

import com.cse601.datamining.project3.alogorithms.Main;

public class NaiveBayes {

	private int dimensionDS = 0; // Store dimension count of dataset1

	private static String LABEL_0 = "LABEL0";
	private static String LABEL_1 = "LABEL1";

	int kFactor = 0;
	int testPartitionIndex = 0;
	int booleanDimensionIndex = -1;
	
	Vector<Vector> fullDataset = new Vector<>();
	Vector<Vector<Vector>> partitionLists = new Vector<>(); // list of partitions containing set of samples.

	Vector<Vector> trainDataset = new Vector<>();
	Vector<Vector> testDataset = new Vector<>();

	Vector<Vector> sample0DataSet = new Vector<>(); //Dataset having label 0
	Vector<Vector> sample1DataSet = new Vector<>(); //Dataset having label 1

	Vector<Dimension> dimensionArray0 = new Vector<>();
	Vector<Dimension> dimensionArray1 = new Vector<>();

	private String actualLabel[] = null;
	private String predictionLabel[] = null;
	
	double[] accuracy = new double[Main.kValue];
	double[] precision = new double[Main.kValue];
	double[] recall = new double[Main.kValue];
	double[] fMeasure = new double[Main.kValue];

	double probLabel1; //Probability that the label is 1
	double probLabel0; //Probability that the label is 0

	public NaiveBayes(Vector<Vector> fullDataset, int kFactor) {
		this.fullDataset = fullDataset; 
		this.kFactor = kFactor;
		//this.booleanDimensionIndex = booleanDimensionIndex;
		dimensionDS = fullDataset.get(0).size();
		partitionDataset(this.fullDataset, this.kFactor);
		while(testPartitionIndex<partitionLists.size()){
			assignPartitions();
			operation();
			testPartitionIndex++;
		}
		Arrays.sort(this.accuracy);
		Arrays.sort(this.precision);
		Arrays.sort(this.recall);
		Arrays.sort(this.fMeasure);
		double a=0,p=0,r=0,fm=0;
		for(int i=0;i<Main.kValue;i++){
			a = a + accuracy[i];
			p = p + precision[i];
			r = r + recall[i];
			fm = fm + fMeasure[i];
		}
		
		a = a/Main.kValue;
		p = p/Main.kValue;
		r = r/Main.kValue;
		fm = fm/Main.kValue;
		
		System.out.println("************ Average ***************");
		System.out.println("Accuracy " + a);
		System.out.println("Precision " + p);
		System.out.println("Recall " + r);
		System.out.println("fMeasure " + fm);
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
		for(int i = 0; i<dimensionDS;i++){
			
			if(i == Main.datasetBooleanDimension){
				
				//Its a boolean value
				if((boolean)observation.get(i))
					posteriorNumArray0[i] = dimensionArray0.get(i).getProbabilityOf1s();//True represents Present / 1
				else
					posteriorNumArray0[i] = dimensionArray0.get(i).getProbabilityOf0s();//False represents Absent / 0 
			}else{
				double mean = dimensionArray0.get(i).getMean(); 
				double variance = dimensionArray0.get(i).getVariance();
				double obs = (double)observation.get(i);
				posteriorNumArray0[i] = normalDist(mean, variance, obs);
			}
		}
		//For 1 labels
		for(int i = 0; i<dimensionDS;i++){
			if(i == Main.datasetBooleanDimension){
				//Its a boolean value
				if((boolean)observation.get(i))
					posteriorNumArray1[i] = dimensionArray1.get(i).getProbabilityOf1s();//True represents Present / 1
				else
					posteriorNumArray1[i] = dimensionArray1.get(i).getProbabilityOf0s();//False represents Absent / 0				
			}else{
				double mean = dimensionArray1.get(i).getMean(); 
				double variance = dimensionArray1.get(i).getVariance();
				double obs = (double)observation.get(i);
				posteriorNumArray1[i] = normalDist(mean, variance, obs);
			}
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
		}
		//going by the matrix in the slide 8 of Clustering Basics.
		double accuracy = (a + d)/(a + b + c + d);
		double precision = (a)/(a + c);
		double recall = (a/ (a + b));
		double fMeasure = ((2*accuracy*precision)/(accuracy + precision));
		
		this.accuracy[testPartitionIndex] = accuracy;
		this.precision[testPartitionIndex] = precision;
		this.recall[testPartitionIndex] = recall;
		this.fMeasure[testPartitionIndex] = fMeasure;
	}

	//Partition the whole dataset into K partitions.
	public void partitionDataset(Vector<Vector> fullDataset, int kFactor){
		int paritionSize = 0;
		int size = fullDataset.size();
		if(size%kFactor == 0){
			paritionSize = size/kFactor;
			int start = 0;
			int end = 0;

			while(end<size){
				start = end;
				end = start + paritionSize;
				Vector<Vector> vectArr = new Vector<Vector>();
				while(start<end){
					vectArr.add(fullDataset.get(start));
					start++;
				}
				partitionLists.add(vectArr);
			}
		}
		else{
			paritionSize = size/kFactor;
			int start = 0;
			int end = 0;
			while(end<size){

				start = end;
				if((size - end) < paritionSize)
					end = size;
				else
					end = start + paritionSize;

				Vector<Vector> vectArr = new Vector<Vector>();
				while(start<end){
					vectArr.add(fullDataset.get(start));
					start++;
				}
				partitionLists.add(vectArr);
			}
			if(partitionLists.get(partitionLists.size()-1).size() < paritionSize){
				partitionLists.get(partitionLists.size()-2).addAll(
						partitionLists.get(partitionLists.size()-1));
				partitionLists.remove(partitionLists.size()-1);
			}
		}		
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
