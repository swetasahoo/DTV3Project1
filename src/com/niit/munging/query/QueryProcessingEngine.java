package com.niit.munging.query;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.niit.munging.parser.QueryParameter;
import com.niit.munging.parser.QueryParser;
import com.niit.munging.reader.AggregateQuery;
import com.niit.munging.reader.DataRow;
import com.niit.munging.reader.GroupByQuery;
import com.niit.munging.reader.SimpleQuery;

public class QueryProcessingEngine  {

	
	SimpleQuery simpleQuery;
	AggregateQuery aggregateQuery;
	GroupByQuery groupByQuery;
	

	List<DataRow> resultSet = new ArrayList<>();
	
	 public QueryProcessingEngine() {
		 
	
		 simpleQuery=new SimpleQuery();
		 aggregateQuery=new AggregateQuery();
		 groupByQuery=new GroupByQuery();
	}
	 public List<DataRow> executeQuery(QueryParameter queryParameter)
	 {
		 
		 
		 try{
		 switch(queryParameter.getQUERY_TYPE())
		 {
		 
		 case "SIMPLE_QUERY":resultSet=simpleQuery.executeQuery(queryParameter);break;
		 
		 case "AGGREGATE_QUERY":resultSet=aggregateQuery.executeQuery(queryParameter);break;
		 
		 case "GROUPBY_QUERY":resultSet=groupByQuery.executeQuery(queryParameter);break;
		 }
		 }
		 catch(Exception exception)
		 {
			
		 }
		 return resultSet;
	 }
	
	 
	 
	 
}
