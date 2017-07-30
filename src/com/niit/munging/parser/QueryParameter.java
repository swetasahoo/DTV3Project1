package com.niit.munging.parser;

import java.util.ArrayList;
import java.util.List;

public class QueryParameter {
	
	private String groupBy;
	private String orderBy;
	private String filePath;
	private boolean allColumns;
	private List<Criteria> criteriaList = new ArrayList<>();
	private List<AggregateFunction> aggregateFunctionList = new ArrayList<>();
	private boolean whereCondition;
	List<String> logicalOperators = new ArrayList<>();
	private boolean aggregate = false;
	private List<String> fields = new ArrayList<>();
	
	private String QUERY_TYPE;

	public String getQUERY_TYPE() {
		return QUERY_TYPE;
	}

	public void setQUERY_TYPE(String qUERY_TYPE) {
		QUERY_TYPE = qUERY_TYPE;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public boolean isAllColumns() {
		return allColumns;
	}

	public void setAllColumns(boolean allColumns) {
		this.allColumns = allColumns;
	}

	public List<Criteria> getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(List<Criteria> criteriaList) {
		this.criteriaList = criteriaList;
	}

	

	public boolean isWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(boolean whereCondition) {
		this.whereCondition = whereCondition;
	}

	public List<String> getLogicalOperators() {
		return logicalOperators;
	}

	public void setLogicalOperators(List<String> logicalOperators) {
		this.logicalOperators = logicalOperators;
	}

	public List<String> getFields() {
		return fields;
	}

	public List<AggregateFunction> getAggregateFunctionList() {
		return aggregateFunctionList;
	}

	public void setAggregateFunctionList(List<AggregateFunction> aggregateFunctionList) {
		this.aggregateFunctionList = aggregateFunctionList;
	}

	public boolean isAggregate() {
		return aggregate;
	}

	public void setAggregate(boolean aggregate) {
		this.aggregate = aggregate;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

}
