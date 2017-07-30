package com.niit.munging.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.niit.munging.parser.AggregateFunction;
import com.niit.munging.parser.QueryParameter;
import com.niit.munging.parser.QueryParser;
import com.niit.munging.query.Query;

public class AggregateQuery implements Query {
	DataRow dataRow;
	List<DataRow> resultSet = new ArrayList<>();
	CsvFileHeader csvFileHeader = new CsvFileHeader();

	private boolean sortData(List<DataRow> dataRowList, QueryParameter queryParameter) {

		int orderColumnIndex = 0;

		if (queryParameter.getOrderBy() != null) {
			orderColumnIndex = csvFileHeader.getColumnIndex(queryParameter.getOrderBy(), queryParameter.getFilePath());
			DataSorting dataSort = new DataSorting();
			dataSort.setSortingIndex(orderColumnIndex);

			Collections.sort(dataRowList, dataSort);

			return true;
		} else {

			return false;
		}

	}

	@Override
	public List<DataRow> executeQuery(QueryParameter queryParameter) {
		// TODO Auto-generated method stub
		
		BufferedReader bufferReader;
		String fileRecord;
		List<AggregateFunction> aggregateFunctionList = queryParameter.getAggregateFunctionList();
		for (AggregateFunction aggregateFunction : aggregateFunctionList) {
			try {

				
				List<Integer> aggregateColumnList = new ArrayList<>();
				int aggregateColumnIndex = csvFileHeader.getColumnIndex(aggregateFunction.getFunctionColumn(),
						queryParameter.getFilePath());
				

				bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
				bufferReader.readLine();
				String record[];
				while ((fileRecord = bufferReader.readLine()) != null) {
					record = fileRecord.split(",");

					if (record[aggregateColumnIndex].isEmpty()) {
						aggregateColumnList.add(new Integer(0));
					} else {
						aggregateColumnList.add(Integer.parseInt(record[aggregateColumnIndex]));
					}
				}

				String functionName = aggregateFunction.getFunctionName();
				int counter = 0;
				dataRow = new DataRow();
				
			
				switch (functionName.trim()) {

				case "sum":
					int sum = 0;
					
					for (int i = 0; i < aggregateColumnList.size(); i++) {
						sum = sum + aggregateColumnList.get(i);
					}
					dataRow.put(counter++, String.valueOf(sum));
					break;

				case "avg":
					float average = 0.0f;
					int add=0;
					
					for (int i = 0; i < aggregateColumnList.size(); i++) {
						add = add + aggregateColumnList.get(i);
					}
					average=(float)add/aggregateColumnList.size();
					dataRow.put(counter++, String.valueOf(average));
					break;
				case "count":
					
					dataRow.put(counter, "Count");
					dataRow.put(counter++, String.valueOf(aggregateColumnList.size()));
					break;
					
				}

				resultSet.add(dataRow);
			} catch (Exception exception) {

			}

		}
		return resultSet;
	}

}
