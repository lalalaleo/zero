package model;

import java.sql.Timestamp;

public class Information {
	private String infId = "";
	private String inf = "";
	private Timestamp infTime = Timestamp.valueOf("1970-01-02 00:00:00");
	private String fromId = "";
	private String toClaId = "";
	private String toUseId = "";
	
	public String getInfId() {
		return infId;
	}
	public void setInfId(String infId) {
		this.infId = infId;
	}
	public String getInf() {
		return inf;
	}
	public void setInf(String inf) {
		this.inf = inf;
	}
	public Timestamp getInfTime() {
		return infTime;
	}
	public void setInfTime(Timestamp infTime) {
		this.infTime = infTime;
	}
	public String getFromId() {
		return fromId;
	}
	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
	public String getToClaId() {
		return toClaId;
	}
	public void setToClaId(String toClaId) {
		this.toClaId = toClaId;
	}
	public String getToUseId() {
		return toUseId;
	}
	public void setToUseId(String toUseId) {
		this.toUseId = toUseId;
	}
	
}
