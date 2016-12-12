package com.cse601.datamining.project3.alogorithms;

public class Dimension {
	
	private double[] column;
	private boolean[] boolColumn;
	private double mean;
	private double variance;
	double probabilityOf0s = 0;
	double probabilityOf1s = 0;
	
	public double getProbabilityOf0s() {
		return probabilityOf0s;
	}
	public void setProbabilityOf0s(double probabilityOf0s) {
		this.probabilityOf0s = probabilityOf0s;
	}
	public double getProbabilityOf1s() {
		return probabilityOf1s;
	}
	public void setProbabilityOf1s(double probabilityOf1s) {
		this.probabilityOf1s = probabilityOf1s;
	}
	public double[] getColumn() {
		return column;
	}
	public void setColumn(double[] column) {
		this.column = column;
	}
	public double getMean() {
		return mean;
	}
	public void setMean(double mean) {
		this.mean = mean;
	}
	public double getVariance() {
		return variance;
	}
	public void setVariance(double variance) {
		this.variance = variance;
	}
	public boolean[] getBoolColumn() {
		return boolColumn;
	}
	public void setBoolColumn(boolean[] boolColumn) {
		this.boolColumn = boolColumn;
	}
}
