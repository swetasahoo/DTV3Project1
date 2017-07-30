package com.niit.munging.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.niit.munging.parser.QueryParameter;
import com.niit.munging.query.Query;

public class CsvFileHeader {

	public Map<String, Integer> getHeader(String filePath) {
		Map<String, Integer> columnHeaderMap = null;

		try {
			BufferedReader bufferReader = new BufferedReader(new FileReader(filePath.trim()));

			columnHeaderMap = new LinkedHashMap<String, Integer>();

			String firstRow = bufferReader.readLine();

			String columnTitles[] = firstRow.split(",");

			int indexValue = 0;
			for (String columnTitle : columnTitles) {

				columnHeaderMap.put(columnTitle, indexValue);
				indexValue++;
			}
		} catch (Exception exception) {

		}
		return columnHeaderMap;
	}

	//return index of particular header 
	public int getColumnIndex(String columname, String fileName) {
		
		int searchColumnIndex = 0;
		Map<String, Integer> csvFileColumnsMap = getHeader(fileName);

		for (Map.Entry<String, Integer> colname : csvFileColumnsMap.entrySet()) {

			if (colname.getKey().trim().equals(columname.trim())) {
				searchColumnIndex = colname.getValue();
			}
		}
		return searchColumnIndex;
	}

	//return the values of the key column set in HeaderMap
	public List<Integer> getSelectedColumnIndexes(QueryParameter queryParameter) {
		
		List<Integer> keyColumnValues=new ArrayList<>();
	
		Map<String, Integer> csvFileColumnsMap = getHeader(queryParameter.getFilePath());
		
		for (String queryColumn : queryParameter.getFields()) {
			if (queryColumn.trim().equals("*"))
			{
				for (Map.Entry<String, Integer> column : csvFileColumnsMap.entrySet())
				{
					keyColumnValues.add(column.getValue());
				}
				
			}
			
		else{
			for (Map.Entry<String, Integer> column : csvFileColumnsMap.entrySet()) {
			
				if (queryColumn.trim().equalsIgnoreCase(column.getKey().trim())) {
					{
				keyColumnValues.add(column.getValue());
					}
			}
			}

		}
		
	}
		return keyColumnValues;
}
	
	
	
}