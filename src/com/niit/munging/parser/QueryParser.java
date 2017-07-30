package com.niit.munging.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParser {
	
	QueryParameter queryParameter;

	public QueryParser() {
		queryParameter = new QueryParameter();
	}

	public QueryParameter parseQuery(String queryString) {

		String splitQuery[];
		queryParameter.setQUERY_TYPE("SIMPLE_QUERY");
		if (queryString.contains("where")) {
			
			splitQuery = queryString.split("where");
			
			queryString=splitQuery[0];
			queryParameter = this.whereOrderClause(splitQuery[1].trim());

		}
		
		
		queryParameter = this.selectwithOrderByClause(queryString.trim());
	
		return queryParameter;

	}

	private QueryParameter selectwithOrderByClause(String selectOrderParameter) {

		String splitQuery[];
		String orderbyString = null;
		if (selectOrderParameter.contains("order by")) {
			splitQuery = selectOrderParameter.split("order by");
			orderbyString = this.orderbyClause(splitQuery[1]);
			queryParameter.setOrderBy(orderbyString);
			return this.selectGroupClause(splitQuery[0]);

		}
		else{
			String selectgroupParam = selectOrderParameter;

			queryParameter = this.selectGroupClause(selectgroupParam);
		}
		
		return queryParameter;
	}

	private QueryParameter selectGroupClause(String selectGroupParam) {
		// TODO Auto-generated method stub

		String splitQuery[];
		String groupbyString = null;
		if (selectGroupParam.contains("group")) {
			splitQuery = selectGroupParam.split("group by");
			groupbyString = this.groupbyClause(splitQuery[1]);
			queryParameter.setGroupBy(groupbyString);
			queryParameter.setQUERY_TYPE("GROUPBY_QUERY");
			return this.selectToFromClause(splitQuery[0]);

		} 
		else
		{
			String selectParam = selectGroupParam;

			queryParameter = this.selectToFromClause(selectParam);
		}
		
		return queryParameter;
	}

	private QueryParameter selectToFromClause(String selectFromParameter) {

		List<String> fieldlist = new ArrayList<>();
		//String patternString = "select(.*)from(.*)";
		Pattern pattern = Pattern.compile("select(.*)from(.*)");
		List<AggregateFunction> aggregateFunctionlist=new ArrayList<>();

		String filename = null;
		Matcher matcher = pattern.matcher(selectFromParameter);
		List<String> list=new ArrayList<>();
		if (matcher.find()) {
			if (matcher.group(1).contains("*")) {
				
				queryParameter.setAllColumns(true);
				fieldlist.add(matcher.group(1));
				
			} else {
				
				String[] fielditem = matcher.group(1).split(",");
				
				for (String field : fielditem) {
					
					//if (queryColumn.trim().equalsIgnoreCase(column.getKey().trim()))
					if (field.contains("sum") || field.contains("avg") || field.contains("min")
							|| field.contains("max") || field.contains("count")) {
						
						AggregateFunction aggregateFunction=new AggregateFunction();
						String[] splitField1 = field.split("\\(");
						aggregateFunction.setFunctionName(splitField1[0]);
						String[] splitField2 = splitField1[1].split("\\)");	
						aggregateFunction.setFunctionColumn(splitField2[0]);
						aggregateFunctionlist.add(aggregateFunction);
						
						if(!(queryParameter.getQUERY_TYPE().equals("GROUPBY_QUERY")))
						{
						queryParameter.setQUERY_TYPE("AGGREGATE_QUERY");
						}
						
					}
					else
					{
					fieldlist.add(field.trim());
					}
					}
				
				}
			}

			filename = matcher.group(2);
		
			queryParameter.setFields(fieldlist);
			queryParameter.setFilePath(filename.trim());
			queryParameter.setAggregateFunctionList(aggregateFunctionlist);
			return queryParameter;

	}

	private QueryParameter whereOrderClause(String whereOrderParam) {
		// TODO Auto-generated method stub
	
		String splitQuery[];
		String orderbyString = null;
		if (whereOrderParam.contains("order by")) {
			splitQuery = whereOrderParam.split("order by");
			orderbyString = this.orderbyClause(splitQuery[1]);
			queryParameter.setOrderBy(orderbyString);	
			return this.whereGroupClause(splitQuery[0]);

		} else {
			
			String wheregroupParam = whereOrderParam;		
			queryParameter = this.whereGroupClause(wheregroupParam);

		}
		return queryParameter;
	}

	private QueryParameter whereGroupClause(String whereGroupParam) {
		// TODO Auto-generated method stub

		String splitQuery[];
		String groupbyString = null;
		if (whereGroupParam.contains("group by")) {
			splitQuery = whereGroupParam.split("group by");
			groupbyString = this.groupbyClause(splitQuery[1]);
			queryParameter.setGroupBy(groupbyString);
			queryParameter.setQUERY_TYPE("GROUPBY_QUERY");
			return this.onlyWhereClause(splitQuery[0]);

		} else {
		
			String whereParameter = whereGroupParam;	
			queryParameter = this.onlyWhereClause(whereParameter);
			queryParameter.setWhereCondition(true);
		}
		
		return queryParameter;
	}

	private QueryParameter onlyWhereClause(String whereParam) {
		queryParameter.setWhereCondition(true);
		String pattern = "(.*)";		
		// add criteria object List<criteria>
		List<Criteria> listCriteria = new ArrayList<>();
		String[] whereArrayConditions = null;
		// to store list of logical operator
		List<String> logicalOperator = new ArrayList<>();
		// splitting condition through operator
		String[] patternrelation;
		Pattern wherePattern = Pattern.compile(pattern);
		Matcher whereMatcher = wherePattern.matcher(whereParam);

		if (whereMatcher.find()) {
			
			
			whereArrayConditions = whereParam.split(" ");

			for (String condition : whereArrayConditions) {
				
				 
				if ((condition.trim().equalsIgnoreCase("AND")) ||(condition.trim().equalsIgnoreCase("or"))||(condition.trim().equals("!")))
				{
					logicalOperator.add(condition.trim());

				} else {
					Criteria criteria = new Criteria();
					patternrelation = condition.split("([<|>|!|=])+");		
					criteria.setSearchColumn(patternrelation[0]);
					criteria.setSearchValue(patternrelation[1]);
					
					int startIndex = condition.indexOf(patternrelation[0]) + patternrelation[0].length();
					int endIndex = condition.indexOf(patternrelation[1]);
					String operator = condition.substring(startIndex, endIndex).trim();	
					criteria.setRelationalOperator(operator);

					// add criteria object to list
					listCriteria.add(criteria);
				}
			}
			queryParameter.setCriteriaList(listCriteria);
		}
		
		queryParameter.setLogicalOperators(logicalOperator);
		
		return queryParameter;

	}

	private String groupbyClause(String groupByParam) {
		Pattern pattern = Pattern.compile("(.*\\S)");
		Matcher matcher = pattern.matcher(groupByParam);

		String groupByString = null;

		if (matcher.find()) {
			groupByString = matcher.group(1);

		}
		return groupByString;

	}

	private String havingClause(String havingParam) {
		// TODO Auto-generated method stub
		Pattern pattern = Pattern.compile("(.*\\S)");
		Matcher matcher = pattern.matcher(havingParam);

		String havingString = null;

		if (matcher.find()) {
			havingString = matcher.group(1);

		}	
		return havingString;
	}

	private String orderbyClause(String orderbyParam) {

		Pattern pattern1 = Pattern.compile("(.*\\S)");
		Matcher matcher = pattern1.matcher(orderbyParam);

		String orderbyString = null;

		if (matcher.find()) {
			orderbyString = matcher.group(1);

		}
		return orderbyString;

	}
}