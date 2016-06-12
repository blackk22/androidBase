package com.tyl.framework.update;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VersionInfo {
	
	private int versioncode;
	private String versionname;
	private String downurl;
	private List<String> message;
	private int position;
	private VersionStatus status;
	
	public VersionInfo() {
	}
	
	
	public VersionInfo(int position, VersionStatus status) {
		this.position = position;
		this.status = status;
	}


	public VersionInfo(int versioncode, String versionname, String downurl,
			List<String> message, int position) {
		this.versioncode = versioncode;
		this.versionname = versionname;
		this.downurl = downurl;
		this.message = message;
		this.position = position;
	}


	public int getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(int versioncode) {
		this.versioncode = versioncode;
	}
	public String getVersionname() {
		return versionname;
	}
	public void setVersionname(String versionname) {
		this.versionname = versionname;
	}
	public String getDownurl() {
		return downurl;
	}
	public void setDownurl(String downurl) {
		this.downurl = downurl;
	}
	public List<String> getMessage() {
		return message;
	}
	public void setMessage(List<String> message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return versioncode+","+versionname+","+downurl+","+Arrays.toString(message.toArray(new String[0]));
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}


	public VersionStatus getStatus() {
		return status;
	}


	public void setStatus(VersionStatus status) {
		this.status = status;
	}
	
	
}
