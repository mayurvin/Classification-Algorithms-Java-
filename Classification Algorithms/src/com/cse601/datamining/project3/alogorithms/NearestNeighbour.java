package com.cse601.datamining.project3.alogorithms;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;

public class NearestNeighbour {

	private int k = 5;

	private Vector<Vector<Double>> train;
	private Vector<Vector<Double>> test;
	final double max = 1;
	final double min = 0;
	final double range = 1;
	ArrayList<ArrayList<Double>> statsByCount;
	ArrayList<ArrayList<Double>> statsByWeight;
	double ac;
	double bc;
	double cc;
	double dc;
	double aw;
	double bw;
	double cw;
	double dw;


	

	public void process(Vector<Vector> dataSet){
		
		statsByCount = new ArrayList<ArrayList<Double>> ();
		statsByWeight = new ArrayList<ArrayList<Double>> ();
		
		Vector<Vector> test = new Vector<Vector>();
		Vector<Vector> train = new Vector<Vector>();
		//Vector<Vector>  = new Vector<Vector>();
		//int size = dataSet.size();
		//size = size - (size%10);
		int len= (int) ((dataSet.size()) * (0.5));
		//int len= (int) (size * (0.75));
		
		for(int i=0; i<len; i++){
			test.add(dataSet.get(i));
		}

		for(int j=len; j<dataSet.size() ;j++){
			train.add(dataSet.get(j));
		}
		
		DecimalFormat df = new DecimalFormat("#.###");
		
		process(test, train);
		
		for(int k=0; k<statsByCount.size() ;k++){
			ArrayList<Double> statByCount = new ArrayList<Double>();
			ArrayList<Double> statByWeight = new ArrayList<Double>();
			statByCount = statsByCount.get(k);
			statByWeight = statsByWeight.get(k);
			System.out.println("Accuracy By Count : "+df.format(statByCount.get(0))+"      Accuracy By Weight : "+df.format(statByWeight.get(0)));
			System.out.println("Precision By Count: "+df.format(statByCount.get(1))+"      Precision By Weight: "+df.format(statByWeight.get(1)));
			System.out.println("Recall By Count   : "+df.format(statByCount.get(2))+"      Recall By Weight   : "+df.format(statByWeight.get(2)));
			System.out.println("F-Measure By Count: "+df.format(statByCount.get(3))+"      F-Measure By Weight: "+df.format(statByWeight.get(3)));

		}
		statsByCount = new ArrayList<ArrayList<Double>> ();
		statsByWeight = new ArrayList<ArrayList<Double>> ();
	}
	

	
	@SuppressWarnings("unchecked")
	public void process(Vector<Vector> test_data, Vector<Vector> train_data) {

		// declare variables
		Vector<Vector<Double>> kNearest = null; // store k nearest samples
		Vector<Double> kNearestDistance = null; // store k nearest distances
		train = new Vector<Vector<Double>>();
		test = new Vector<Vector<Double>>();
		int count; // count to k
		double distance; //distance
		ac=0;		aw=0;
		bc=0;		bw=0;
		cc=0;		cw=0;
		dc=0;		dw=0;
		
		// use local copy of given data
		// normalize test & train data
		test = MinMaxNormalize(test_data);
		train = MinMaxNormalize(train_data);


		
		// Algorithm
		for(int tc=0; tc<test.size(); tc++){ //initilize variables for each test case
			
			kNearest = new Vector<Vector<Double>>();
			kNearestDistance = new Vector<Double>();
			count = 0; //count to k
			int pos = 0; //farthest smaple or longest distance index
			int i= 0;

			for(int tr=0; tr<train.size(); tr++){
				
				distance = Distance(test.get(tc), train.get(tr), range);
				
				if(count < k) {
					kNearest.add(train.get(tr));
					kNearestDistance.add(distance);
					if(distance > kNearestDistance.get(pos)) pos = count;	// store the furtherest distance to test
					count++;
				} else{
					
					if (distance < kNearestDistance.get(pos)){
						
						kNearest.set(pos, train.get(tr)); //replace the largest farthest sample
						kNearestDistance.set(pos, distance); // replace the longest distance
						
						// then find which training value distance is longest
						for (i=0; i<kNearestDistance.size(); i++){
							if (kNearestDistance.get(i) > kNearestDistance.get(pos)){
								pos = i;
							}
						}
					}
						
				}
				
			}
			Classify(kNearest, kNearestDistance, test.get(tc));
		}
	
		//System.out.print("Accuracy By Count: "+accuracyByCount+"   Accuracy By Weight: "+accuracyByWeight);
		double accuracyC = (ac + dc)/(ac + bc + cc + dc);
		double precisionC = (ac)/(ac + cc);
		double recallC = (ac/ (ac + bc));
		double fMeasureC = ((2*recallC*precisionC)/(recallC + precisionC));
		
		ArrayList<Double> statC = new ArrayList<Double>();
		statC.add(accuracyC);
		statC.add(precisionC);
		statC.add(recallC);
		statC.add(fMeasureC);
		statsByCount.add(statC);
		
		double accuracyW = (aw + dw)/(aw + bw + cw + dw);
		double precisionW = (aw)/(aw + cw);
		double recallW = (aw/ (aw + bw));
		double fMeasureW = ((2*recallW*precisionW)/(recallW + precisionW));
		
		ArrayList<Double> statW = new ArrayList<Double>();
		statW.add(accuracyW);
		statW.add(precisionW);
		statW.add(recallW);
		statW.add(fMeasureW);
		statsByWeight.add(statW);
	}
	
	
	
	
	
	
	
	
	
	
	private void Classify(Vector<Vector<Double>> kNearest, Vector<Double> kNearestDistance, Vector<Double> tc) {
		
		String byCount = null;
		String byWeight = null;
		double classByCount;
		double classByWeight = 0;
		
		// By count
		int count0 = 0;
		int count1 = 0;
		
		for(int i=0; i<kNearest.size(); i++){
			Vector<Double> row = kNearest.get(i);
			if(row.get(row.size()-1) == 1){
				count1++;
			}
			if(row.get(row.size()-1) == 0){
				count0++;
			}
		}
		
		if(count0 > count1){
			classByCount = 0;
		}else{
			classByCount = 1;
		}
		
		
		// By weight
		double w0 = -1;
		double w1 = -1;
		Vector<Double> kWeights = new Vector<Double>();
		
		for(int i=0; i<kNearest.size(); i++){
			Vector<Double> row = kNearest.get(i);

			if(row.get(row.size()-1) == 0){
				if(w0 ==-1)
					w0 = 0;
				w0 = w0 + (1/kNearestDistance.get(i));
			}else{
				if(w1 ==-1)
					w1 = 0;
				w1 = w1 + (1/kNearestDistance.get(i));
			}
		}
		
		
		if(w0>w1){
			classByWeight = 0;
		}else{
			classByWeight = 1;
		}

		
		
		
		// compare 
		if(tc.get(tc.size()-1) == classByCount){
			if(classByCount==0){
				dc++;
			}else{
				ac++;
			}
		}else{
			if(classByCount==0){
				bc++;
			}else{
				cc++;
			}
		}
		
		if(tc.get(tc.size()-1) == classByWeight){
			if(classByWeight==0){
				dw++;
			}else{
				aw++;
			}
		}else{
			if(classByWeight==0){
				bw++;
			}else{
				cw++;
			}
		}	
	}
	
	

	private double Distance(Vector<Double> tc, Vector<Double> tr, double range) {
		
		double distance = 0;
		double element = 0;
		
		//System.out.println(test.get(0).size()-1);
		for(int i = 0; i < test.get(0).size()-1; i++){
			element = tc.get(i) - tr.get(i);
			element = Math.pow(element, 2);
			element /= range; // no effect as range is constant at 1
			distance += element;
		}
		//distance = Math.sqrt(distance);
		return  distance; // return squared distance  for efficiency avaoid sqrt!
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
				double element = ((newRow.get(j)-minValues.get(j)) / (maxValues.get(j)-minValues.get(j))) * (max-min) + min;
				newRow.set(j, element);

			}
			// add rows to new data
			data.add(newRow);
		}
		
		return data;
	}

	
	public void CrossValidate(Vector<Vector> dataSet, int k){
		statsByCount = new ArrayList<ArrayList<Double>> ();
		statsByWeight = new ArrayList<ArrayList<Double>> ();
		
		Vector<Vector> test;
		Vector<Vector> train;
        int partSize = 0;        
        int size = dataSet.size();
        //size=size-(size%k);
        //System.out.println(size);

        if(size%k == 0){
        	
            partSize = size/k;
            
            int start = 0;
            int end = 0;
            int count = 0;

            while(count<k){
                start = end;
                end = start + partSize;
                test = new Vector<Vector>();
                train = new Vector<Vector>();
                for(int i=start; i<end; i++){
                	test.add(dataSet.get(i));
                }
                
                for(int j=0; j<size; j++){
                	if(j<start || j>=end)
                		train.add(dataSet.get(j));
                   // start++;
                }
                //System.out.println(start+"  "+end+"  "+test.size() +"  "+train.size());
                
                process(test, train);
                count++;
            }
        }
        else{
            partSize = size/k;
            int buffer = size%k;

            
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
                
                //System.out.println(end+buffer +"  " +start+"  "+(end+buffer-start));
                //System.out.println(start+"  "+end+"  "+test.size() +"  "+train.size());
                process(test, train);
                count++;
            }
            
        }
        DecimalFormat df = new DecimalFormat("#.###");
        double accuracyC = 0;
        double precisionC = 0;
        double recallC = 0;
        double fMeasureC = 0;
        double accuracyW = 0;
        double precisionW = 0;
        double recallW = 0;
        double fMeasureW = 0;
        
		/*for(int i=0; i<statsByCount.size() ;i++){
			int fold = i+1;
			System.out.println("******************* "+fold+"th Fold ****************************");
			ArrayList<Double> statByCount = new ArrayList<Double>();
			ArrayList<Double> statByWeight = new ArrayList<Double>();
			statByCount = statsByCount.get(i);
			statByWeight = statsByWeight.get(i);
			System.out.println("Accuracy By Count : "+df.format(statByCount.get(0))+"      Accuracy By Weight : "+df.format(statByWeight.get(0)));
			System.out.println("Precision By Count: "+df.format(statByCount.get(1))+"      Precision By Weight: "+df.format(statByWeight.get(1)));
			System.out.println("Recall By Count   : "+df.format(statByCount.get(2))+"      Recall By Weight   : "+df.format(statByWeight.get(2)));
			System.out.println("F-Measure By Count: "+df.format(statByCount.get(3))+"      F-Measure By Weight: "+df.format(statByWeight.get(3)));

		}*/
		for(int i=0; i<statsByCount.size() ;i++){

			ArrayList<Double> statByCount = new ArrayList<Double>();
			ArrayList<Double> statByWeight = new ArrayList<Double>();
			statByCount = statsByCount.get(i);
			statByWeight = statsByWeight.get(i);
			accuracyC += statByCount.get(0); 	accuracyW += statByWeight.get(0);			
			precisionC += statByCount.get(1);	precisionW += statByWeight.get(1);
			recallC += statByCount.get(2);		recallW += statByWeight.get(2);
			fMeasureC += statByCount.get(3);	fMeasureW += statByWeight.get(3);			
		}
		System.out.println("********************** Average ***************************");
		System.out.println("Accuracy By Count : "+df.format(accuracyC/k)+"      Accuracy By Weight : "+df.format(accuracyW/k));
		System.out.println("Precision By Count: "+df.format(precisionC/k)+"      Precision By Weight: "+df.format(precisionW/k));
		System.out.println("Recall By Count   : "+df.format(recallC/k)+"      Recall By Weight   : "+df.format(recallW/k));
		System.out.println("F-Measure By Count: "+df.format(fMeasureC/k)+"      F-Measure By Weight: "+df.format(fMeasureW/k));
		
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void Normalize(Vector<Vector> dataSet) {

		Vector<Double> sum = new Vector<Double>();

		// compute sum for each column
		for (int i = 0; i < dataSet.size(); i++) {// for each row

			Vector<Double> row = dataSet.get(i);

			for (int j = 0; j < row.size() - 1; j++) {// for each column
				if (i == 0) {
					sum.add(row.get(j));
				} else {
					double element = sum.get(j) + row.get(j);
					sum.set(j, element);
				}
			}
		}

		for (int i = 0; i < dataSet.size(); i++) {// for each row
			Vector<Double> row = dataSet.get(i);
			for (int j = 0; j < row.size() - 1; j++) {// for each column
				double element = row.get(j) / sum.get(j);
				row.set(j, element);
			}
		}

	}
	
	

}
