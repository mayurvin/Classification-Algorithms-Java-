package com.cse601.datamining.project3.util;


public class EvaluateGini {
	
	public double gini;
	public double key;
	public int column;
	public int row;
	public int begin;
	public int end;
	public double leftLabel;
	public double rightLabel;
	public String node;
	





	public EvaluateGini(double gini, double key, int column, int row,
			int begin, int end, double leftLabel, double rightLabel, String node) {
		super();
		this.gini = gini;
		this.key = key;
		this.column = column;
		this.row = row;
		this.begin = begin;
		this.end = end;
		this.leftLabel = leftLabel;
		this.rightLabel = rightLabel;
		this.node = node;
	}

	public double getGini() {
		return gini;
	}

	public void setGini(double gini) {
		this.gini = gini;
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

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
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

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

}
