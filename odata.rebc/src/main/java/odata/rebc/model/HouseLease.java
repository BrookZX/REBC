package odata.rebc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HouseLease   {
	@JsonProperty("UUID")
	private String UUID;
	@JsonProperty("PRO_NAME")
	private String PRO_NAME;
	@JsonProperty("ADDRESS")
	private String ADDRESS;
	@JsonProperty("PRICE")
	private float PRICE;
	@JsonProperty("LEASE_FROM")
	private String LEASE_FROM;
	@JsonProperty("LEASE_TO")
	private String LEASE_TO;
	@JsonProperty("OWNER")
	private String OWNER;
	@JsonProperty("TENANT")
	private String TENANT;
	@JsonProperty("APPLIER")
	private String APPLIER;
	@JsonProperty("TERMS")
	private String TERMS;
	@JsonProperty("STATUS")
	private String STATUS;
	@JsonProperty("UPDATER")
	private String UPDATER;
	
	public HouseLease(String uUID) {
		super();
		UUID = uUID;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getPRO_NAME() {
		return PRO_NAME;
	}
	public void setPRO_NAME(String pRO_NAME) {
		PRO_NAME = pRO_NAME;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public float getPRICE() {
		return PRICE;
	}
	public void setPRICE(float pRICE) {
		PRICE = pRICE;
	}
	public String getLEASE_FROM() {
		return LEASE_FROM;
	}
	public void setLEASE_FROM(String lEASE_FROM) {
		LEASE_FROM = lEASE_FROM;
	}
	public String getLEASE_TO() {
		return LEASE_TO;
	}
	public void setLEASE_TO(String lEASE_TO) {
		LEASE_TO = lEASE_TO;
	}
	public String getOWNER() {
		return OWNER;
	}
	public void setOWNER(String oWNER) {
		OWNER = oWNER;
	}
	public String getTENANT() {
		return TENANT;
	}
	public void setTENANT(String tENANT) {
		TENANT = tENANT;
	}
	public String getAPPLIER() {
		return APPLIER;
	}
	public void setAPPLIER(String aPPLIER) {
		APPLIER = aPPLIER;
	}
	public String getTERMS() {
		return TERMS;
	}
	public void setTERMS(String tERMS) {
		TERMS = tERMS;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getUPDATER() {
		return UPDATER;
	}
	public void setUPDATER(String uPDATER) {
		UPDATER = uPDATER;
	}
	 //Introducing the dummy constructor
    public HouseLease() {
    }
}
