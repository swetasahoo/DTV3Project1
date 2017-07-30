package com.niit.munging.query;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.niit.munging.parser.QueryParameter;
import com.niit.munging.reader.DataRow;

public interface Query {
	public  List<DataRow> executeQuery(QueryParameter queryParameter);
	

}
