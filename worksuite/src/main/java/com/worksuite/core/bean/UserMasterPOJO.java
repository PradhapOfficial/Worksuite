package com.worksuite.core.bean;

public class UserMasterPOJO {

	private UserPOJO userDetails;

	private RolePOJO roleDetails;

	private OrgPOJO orgDetails;

	public UserPOJO getUserDetails() {
		return userDetails;
	}

	public UserMasterPOJO setUserDetails(UserPOJO userDetails) {
		this.userDetails = userDetails;
		return this;
	}

	public RolePOJO getRoleDetails() {
		return roleDetails;
	}

	public UserMasterPOJO setRoleDetails(RolePOJO roleDetails) {
		this.roleDetails = roleDetails;
		return this;
	}

	public OrgPOJO getOrgDetails() {
		return orgDetails;
	}

	public UserMasterPOJO setOrgDetails(OrgPOJO orgDetails) {
		this.orgDetails = orgDetails;
		return this;
	}
}
