package com.niit.munging.reader;

import java.io.BufferedReader;

import java.io.FileReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.niit.munging.parser.AggregateFunction;
import com.niit.munging.parser.QueryParameter;
import com.niit.munging.query.Query;

public class GroupByQuery implements Query {
	DataRow dataRow;
	List<DataRow> resultSet = new ArrayList<>();
	CsvFileHeader csvFileHeader = new CsvFileHeader();

	private boolean sortData(List<DataRow> dataRowList, QueryParameter queryParameter) {
		int orderByColumnIndex = 0;

		if (queryParameter.getOrderBy() != null) {
			orderByColumnIndex = csvFileHeader.getColumnIndex(queryParameter.getOrderBy(),
					queryParameter.getFilePath());

			DataSorting dataSort = new DataSorting();
			dataSort.setSortingIndex(orderByColumnIndex);

			Collections.sort(dataRowList, dataSort);

			return true;
		} else {

			return false;
		}

	}

	@Override
	public List<DataRow> executeQuery(QueryParameter queryParameter) {

		BufferedReader bufferReader;
		String fileRecord;
		int groupByColumnIndex;

		List<String> groupByListColumn = new ArrayList<>();

		groupByColumnIndex = csvFileHeader.getColumnIndex(queryParameter.getGroupBy(), queryParameter.getFilePath());

		
		try {
			bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
			bufferReader.readLine();// goto first record
			while ((fileRecord = bufferReader.readLine()) != null) {
				String record[] = fileRecord.split(",");
				groupByListColumn.add(record[groupByColumnIndex]);
			}
			bufferReader.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		//eleminate the duplicate value from groupByColumnList and add it in a LinkHashSet Datastructure
		Set<String> groupByColumnSet = new LinkedHashSet<>();
		for (String uniqueColumn : groupByListColumn) {
			groupByColumnSet.add(uniqueColumn);

		}

		List<AggregateFunction> aggregateFunctionList = queryParameter.getAggregateFunctionList();
		for (AggregateFunction aggregateFunction : aggregateFunctionList) {
			try {

				int sum;
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

				for (String column : groupByColumnSet) {
					int counter = 0;
					dataRow = new DataRow();
					dataRow.put(counter, column);

					switch (functionName) {
					case "sum":
						sum = 0;
						for (int i = 0; i < groupByListColumn.size(); i++) {
							if (column.trim().equals(groupByListColumn.get(i).trim())) {
								sum = sum + aggregateColumnList.get(i);
							}
						}

						dataRow.put(++counter, String.valueOf(sum));
						break;
					case "avg":
						int noOfColumn = 0;
						sum = 0;
						for (int i = 0; i < groupByListColumn.size(); i++) {
							if (column.trim().equals(groupByListColumn.get(i).trim())) {
								noOfColumn++;
								sum = sum + aggregateColumnList.get(i);
							}
						}
						float avg = (float) sum / noOfColumn;
						dataRow.put(++counter, String.valueOf(avg));
						break;
					case "min":
						
						break;
					case "max":
						break;
					case "count":
						int count=0;
						for (int i = 0; i < groupByListColumn.size(); i++) {
							if (column.trim().equals(groupByListColumn.get(i).trim())) {
								count ++;
							}
						}

						dataRow.put(++counter, String.valueOf(count));
						break;

					}

					resultSet.add(dataRow);
				}

			}

			catch (Exception exception) {
				

			}

		}

		// TODO Auto-generated method stub
		//sortData(resultSet, queryParameter);
		return resultSet;
	}

}
