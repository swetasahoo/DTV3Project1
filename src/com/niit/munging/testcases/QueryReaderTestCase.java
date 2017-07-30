package com.niit.munging.testcases;

import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.niit.munging.parser.QueryParameter;
import com.niit.munging.parser.QueryParser;
import com.niit.munging.query.Query;
import com.niit.munging.query.QueryProcessingEngine;
import com.niit.munging.reader.DataRow;

public class QueryReaderTestCase {
	
QueryParser queryParser;
QueryProcessingEngine queryProcessingEngine;
	
	@Before
	public void init() {
		queryParser=new QueryParser();
		queryProcessingEngine=new QueryProcessingEngine();
		//query=QueryFactory.getQuery();
	}
	@Ignore
	@Test
	public void selectAllWithoutWhereTestCase()  {
		
		QueryParameter queryParameter=queryParser.parseQuery("select * from  D:\\DTV3\\csvfile\\emp.csv");
		List<DataRow> resultSet=queryProcessingEngine.executeQuery(queryParameter);	
		
		assertNotNull(resultSet);
		display("selectAllWithoutWhereTestCase", resultSet);

	}
	@Ignore
	@Test
	public void selectColumnsWithoutWhereTestCase()  {

		QueryParameter queryParameter=queryParser.parseQuery("select EmpId,Name,City,Salary from D:\\DTV3\\csvfile\\emp.csv");
		List<DataRow> resultSet=queryProcessingEngine.executeQuery(queryParameter);	
		assertNotNull(resultSet);
		display("selectColumnsWithoutWhere", resultSet);

	}
	@Ignore
	@Test
	public void selectAllWithWhereTestCase()  {
		
		QueryParameter queryParameter=queryParser.parseQuery("select * from  D:\\DTV3\\csvfile\\emp.csv where Salary<35000");
		List<DataRow> resultSet=queryProcessingEngine.executeQuery(queryParameter);	
		
		assertNotNull(resultSet);
		display("selectAllWithoutWhereTestCase", resultSet);

	}

	@Test
	public void selectwithGroupByTestCase()  {
		
		QueryParameter queryParameter=queryParser.parseQuery("select Dept,avg(Salary),sum(Salary),count(Salary) from  D:\\DTV3\\csvfile\\emp.csv group by Dept");
		List<DataRow> resultSet=queryProcessingEngine.executeQuery(queryParameter);	
		
		assertNotNull(resultSet);
		display("selectwithGroupByTestCase", resultSet);

	}
	@Ignore
	@Test
	public void selectwithAggregateTestCase()  {
		
		QueryParameter queryParameter=queryParser.parseQuery("select sum(Salary),count(Salary),avg(Salary) from  D:\\DTV3\\csvfile\\emp.csv");
		List<DataRow> resultSet=queryProcessingEngine.executeQuery(queryParameter);	
		
		assertNotNull(resultSet);
		display("selectwithAggregateTestCase", resultSet);

	}
	private void display(String testCaseName, List<DataRow> resultSet) {
		// TODO Auto-generated method stub
		System.out.println(testCaseName);
		System.out.println("******************************************");

		for (Map<Integer, String> dataRow : resultSet) {
			if (!dataRow.isEmpty()) {
				for (Map.Entry<Integer, String> result : dataRow.entrySet()) {
					System.out.print("\n " + result.getValue());
				}
				System.out.println();
			}
		}
		System.out.println("******************************************");
		
		
	}

}
