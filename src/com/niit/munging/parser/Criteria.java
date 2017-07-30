package com.niit.munging.parser;

public class Criteria {
	
	String searchColumn;
	String searchValue;
	String relationalOperator;

	
	public Criteria()
	{
		
		
	}
	public String getSearchColumn() {
		return searchColumn;
	}
	public void setSearchColumn(String searchColumn) {
		this.searchColumn = searchColumn;
	
	}
	public String getSearchValue() {
		return searchValue;
	}
	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
		
	}
	public String getRelationalOperator() {
		return relationalOperator;
	}
	public void setRelationalOperator(String relationalOperator) {
		this.relationalOperator = relationalOperator;
		
	}
	

}
