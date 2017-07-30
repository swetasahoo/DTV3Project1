package com.niit.munging.reader;

import java.util.Comparator;

public class DataSorting implements Comparator<DataRow>
 {
	private int sortingIndex;
	
	
	public int getSortingIndex() {
		return sortingIndex;
	}


	public void setSortingIndex(int sortingIndex) {
		this.sortingIndex = sortingIndex;
		
	}


	@Override
	public int compare(DataRow dataRow1, DataRow dataRow2) {
	
		// TODO Auto-generated method stub
		return dataRow1.get(sortingIndex).compareTo(dataRow2.get(sortingIndex));
	}

}
