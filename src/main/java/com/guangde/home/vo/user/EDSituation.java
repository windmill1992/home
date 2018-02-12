package com.guangde.home.vo.user;
/**
 * 
 * 员工动态
 *
 */
public class EDSituation {

	private String eName;//账户名称
	private String pName;//项目名称
	private int pId;//项目id
	private String field;
	private String pProcess;//项目进度
	private long pCtime;//捐款时间
	
	public String geteName() {
		return eName;
	}
	public void seteName(String eName) {
		this.eName = eName;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public String getpProcess() {
		return pProcess;
	}
	public void setpProcess(String pProcess) {
		this.pProcess = pProcess;
	}
	public long getpCtime() {
		return pCtime;
	}
	public void setpCtime(long pCtime) {
		this.pCtime = pCtime;
	}
	public int getpId() {
		return pId;
	}
	public void setpId(int pId) {
		this.pId = pId;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
}
