package com.niit.munging.testcases;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.niit.munging.parser.QueryParameter;
import com.niit.munging.parser.QueryParser;
import com.niit.munging.reader.CsvFileHeader;



public class QueryParserTestCases {
	QueryParser queryParser;
	
	@Before
	public void init() {
		queryParser = new QueryParser();
		
	}
	@Test
	public void selectAllWithoutWhereTestCase()  {
		QueryParameter queryParameter = queryParser.parseQuery("select name,city,EmpId from d:\\DTV3\\emp.csv");

		
				
		//assertNotNull(queryParameter);
		//display("Available Parameter",queryParameter);
	}
	private void display(String string, QueryParameter queryParameter) {
		
		while(queryParameter!=null)
		{
			System.out.println(queryParameter.getFilePath());
			
		}
		// TODO Auto-generated method stub
		
	}

}
