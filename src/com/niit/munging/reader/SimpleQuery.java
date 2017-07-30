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
import com.niit.munging.reader.DataRow;
import com.niit.munging.parser.Criteria;
import com.niit.munging.parser.QueryParameter;
import com.niit.munging.parser.QueryParser;
import com.niit.munging.query.Query;

public class SimpleQuery implements Query {
	DataRow dataRow;
	List<DataRow> resultSet = new ArrayList<>();
	CsvFileHeader csvFileHeader = new CsvFileHeader();

	private boolean sortData(List<DataRow> dataRowList, QueryParameter queryParameter)
			throws FileNotFoundException, IOException {

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

	public List<DataRow> executeQuery(QueryParameter queryParameter) {
		// TODO Auto-generated method stub

		List<String> fieldList = queryParameter.getFields();

		for (String field : fieldList) {

			if (field.trim().equals("*")) {
				try {

					resultSet = getAllData(queryParameter);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					resultSet = getSelectedData(queryParameter);
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}

		}

		return resultSet;
	}

	private List<DataRow> getSelectedData(QueryParameter queryParameter) throws FileNotFoundException, IOException {

		// return index of query column in select clause
		List<Integer> headerColumnsIndex = csvFileHeader.getSelectedColumnIndexes(queryParameter);
		BufferedReader bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
		bufferReader.readLine();// goto first record
		String fileRecord;
		String[] record;
		int index;

		while ((fileRecord = bufferReader.readLine()) != null) {
			dataRow = new DataRow();
			index = 0;
			record = fileRecord.split(",");

			for (Integer columnIndex : headerColumnsIndex) {

				if (record[columnIndex].equals("")) {

					dataRow.put(index, "null");
				} else {

					dataRow.put(index, record[columnIndex]);

				}

				index++;

				resultSet.add(dataRow);
			}

			if (queryParameter.getOrderBy() != null) {
				sortData(resultSet, queryParameter);
			}

		}
		return resultSet;
	}

	private List<DataRow> getAllData(QueryParameter queryParameter) throws Exception {
		List<Integer> headerColumnsIndex = csvFileHeader.getSelectedColumnIndexes(queryParameter);
		BufferedReader bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
		bufferReader.readLine();// goto first record
		String fileRecord;
		int index ;

		if (queryParameter.isWhereCondition()) {

			String operator = queryParameter.getCriteriaList().get(0).getRelationalOperator();
			
			switch (operator) {
			case "=":
				resultSet = equalEqualsOperator(queryParameter);
				break;
			case "!=":
				resultSet = notEqualsOperator(queryParameter);
				break;
			case ">":
				resultSet = greaterThanOperator(queryParameter);
				break;
			case ">=":
				resultSet = greaterThanEqualsOperator(queryParameter);
				break;
			case "<":
				resultSet = lessthanOperator(queryParameter);
				break;
			case "<=":
				resultSet = lessthanEqualsOperator(queryParameter);
				break;

			}
		} 
		else {
			
			while ((fileRecord = bufferReader.readLine()) != null) {
				index = 0;
				dataRow = new DataRow();
				String record[] = fileRecord.split(",");
				for (Integer columnIndex : headerColumnsIndex) {

					if (record[columnIndex].equals("")) {

						dataRow.put(index, "null");
					} else {

						dataRow.put(index, record[columnIndex]);

					}

					index++;
				}
				resultSet.add(dataRow);
			}
		}

		// if sorting apply
		if (queryParameter.getOrderBy() != null) {
			sortData(resultSet, queryParameter);
		}

		return resultSet;
	}

	private List<DataRow> lessthanEqualsOperator(QueryParameter queryParameter) throws Exception {

		Criteria criteria = queryParameter.getCriteriaList().get(0);

		int whereColumnIndex = csvFileHeader.getColumnIndex(criteria.getSearchColumn(), queryParameter.getFilePath());
		List<Integer> headerColumnsIndex = csvFileHeader.getSelectedColumnIndexes(queryParameter);
		int searchValue = Integer.parseInt(criteria.getSearchValue());

		String fileRecord;
		BufferedReader bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
		bufferReader.readLine();// go to next line in file
		int index;
		while ((fileRecord = bufferReader.readLine()) != null) {
			index = 0;
			dataRow = new DataRow();
			String record[] = fileRecord.split(",");

			if (!(record[whereColumnIndex].equals(""))) {
				if ((Integer.parseInt(record[whereColumnIndex].trim())) <= searchValue) {
					for (Integer columnIndex : headerColumnsIndex) {
						if (record[columnIndex].equals("")) {

							dataRow.put(index, "null");
						} else {

							dataRow.put(index, record[columnIndex]);

						}
						index++;
					}

				}

			}
			resultSet.add(dataRow);
		}
		return resultSet;


	}

	private List<DataRow> lessthanOperator(QueryParameter queryParameter) throws Exception {

		Criteria criteria = queryParameter.getCriteriaList().get(0);

		int whereColumnIndex = csvFileHeader.getColumnIndex(criteria.getSearchColumn(), queryParameter.getFilePath());
		List<Integer> headerColumnsIndex = csvFileHeader.getSelectedColumnIndexes(queryParameter);
		int searchValue = Integer.parseInt(criteria.getSearchValue());

		String fileRecord;
		BufferedReader bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
		bufferReader.readLine();// go to next line in file
		int index;
		while ((fileRecord = bufferReader.readLine()) != null) {
			index = 0;
			dataRow = new DataRow();
			String record[] = fileRecord.split(",");

			if (!(record[whereColumnIndex].equals(""))) {
				if ((Integer.parseInt(record[whereColumnIndex].trim())) < searchValue) {
					for (Integer columnIndex : headerColumnsIndex) {
						if (record[columnIndex].equals("")) {

							dataRow.put(index, "null");
						} else {

							dataRow.put(index, record[columnIndex]);

						}
						index++;
					}

				}

			}
			resultSet.add(dataRow);
		}
		return resultSet;

	}

	private List<DataRow> greaterThanEqualsOperator(QueryParameter queryParameter) throws Exception {
		Criteria criteria = queryParameter.getCriteriaList().get(0);

		int whereColumnIndex = csvFileHeader.getColumnIndex(criteria.getSearchColumn(), queryParameter.getFilePath());
		List<Integer> headerColumnsIndex = csvFileHeader.getSelectedColumnIndexes(queryParameter);
		int searchValue = Integer.parseInt(criteria.getSearchValue());

		String fileRecord;
		BufferedReader bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
		bufferReader.readLine();// go to next line in file
		int index;
		while ((fileRecord = bufferReader.readLine()) != null) {
			index = 0;
			dataRow = new DataRow();
			String record[] = fileRecord.split(",");

			if (!(record[whereColumnIndex].equals(""))) {
				if ((Integer.parseInt(record[whereColumnIndex].trim())) >= searchValue) {
					for (Integer columnIndex : headerColumnsIndex) {
						if (record[columnIndex].equals("")) {

							dataRow.put(index, "null");
						} else {

							dataRow.put(index, record[columnIndex]);

						}
						index++;
					}

				}

			}
			resultSet.add(dataRow);
		}
		return resultSet;

	}

	private List<DataRow> greaterThanOperator(QueryParameter queryParameter) throws Exception {
		Criteria criteria = queryParameter.getCriteriaList().get(0);

		int whereColumnIndex = csvFileHeader.getColumnIndex(criteria.getSearchColumn(), queryParameter.getFilePath());
		List<Integer> headerColumnsIndex = csvFileHeader.getSelectedColumnIndexes(queryParameter);
		int searchValue = Integer.parseInt(criteria.getSearchValue());

		String fileRecord;
		BufferedReader bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
		bufferReader.readLine();// go to next line in file
		int index;
		while ((fileRecord = bufferReader.readLine()) != null) {
			index = 0;
			dataRow = new DataRow();
			String record[] = fileRecord.split(",");

			if (!(record[whereColumnIndex].equals(""))) {
				if ((Integer.parseInt(record[whereColumnIndex].trim())) > searchValue) {
					for (Integer columnIndex : headerColumnsIndex) {
						if (record[columnIndex].equals("")) {

							dataRow.put(index, "null");
						} else {

							dataRow.put(index, record[columnIndex]);

						}
						index++;
					}

				}

			}
			resultSet.add(dataRow);
		}
		return resultSet;

	}

	private List<DataRow> notEqualsOperator(QueryParameter queryParameter) throws Exception {
		Criteria criteria = queryParameter.getCriteriaList().get(0);

		int whereColumnIndex = csvFileHeader.getColumnIndex(criteria.getSearchColumn(), queryParameter.getFilePath());
		List<Integer> headerColumnsIndex = csvFileHeader.getSelectedColumnIndexes(queryParameter);
		String searchValue =criteria.getSearchValue();

		String fileRecord;
		BufferedReader bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
		bufferReader.readLine();// go to next line in file
		int index;
		while ((fileRecord = bufferReader.readLine()) != null) {
			index = 0;
			dataRow = new DataRow();
			String record[] = fileRecord.split(",");

			if (!(record[whereColumnIndex].equals(""))) {
				if ((record[whereColumnIndex].trim()) != searchValue) {
					for (Integer columnIndex : headerColumnsIndex) {
						if (record[columnIndex].equals("")) {

							dataRow.put(index, "null");
						} else {

							dataRow.put(index, record[columnIndex]);

						}
						index++;
					}

				}

			}
			resultSet.add(dataRow);
		}
		return resultSet;
	}

	private List<DataRow> equalEqualsOperator(QueryParameter queryParameter) throws Exception {
		Criteria criteria = queryParameter.getCriteriaList().get(0);

		int whereColumnIndex = csvFileHeader.getColumnIndex(criteria.getSearchColumn(), queryParameter.getFilePath());
		List<Integer> headerColumnsIndex = csvFileHeader.getSelectedColumnIndexes(queryParameter);
		String searchValue =criteria.getSearchValue();

		String fileRecord;
		BufferedReader bufferReader = new BufferedReader(new FileReader(queryParameter.getFilePath().trim()));
		bufferReader.readLine();// go to next line in file
		int index;
		while ((fileRecord = bufferReader.readLine()) != null) {
			index = 0;
			dataRow = new DataRow();
			String record[] = fileRecord.split(",");

			if (!(record[whereColumnIndex].equals(""))) {
				if ((record[whereColumnIndex].trim()).equals(searchValue)) {
					for (Integer columnIndex : headerColumnsIndex) {
						if (record[columnIndex].equals("")) {

							dataRow.put(index, "null");
						} else {

							dataRow.put(index, record[columnIndex]);

						}
						index++;
					}

				}

			}
			resultSet.add(dataRow);
		}
		return resultSet;

	}

}
