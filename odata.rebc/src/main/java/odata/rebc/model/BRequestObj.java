package odata.rebc.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BRequestObj  {
	public String chaincodeId;
	public String fcn;
	public List<String> args ;
	public BRequestObj() {
		super();
		
		args = new ArrayList<String>();
	}  
}
