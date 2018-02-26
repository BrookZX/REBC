package odata.rebc.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntitySetResp  {
	
	@JsonProperty("status")
	public String status;
	@JsonProperty("txId")
	public String txId;
	@JsonProperty("data")
	public ArrayList<HouseLease> data;
}
