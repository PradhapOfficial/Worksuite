package com.worksuite.core.bean;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.worksuite.db.util.DBUtil;

public class RolePOJO {

	private Long roleId;

	private String roleName;

	private Integer roleValue;

	public static RolePOJO convertResultSetToPojo(ResultSet resultSet) throws SQLException {
		DBUtil dbUtil = new DBUtil();
		RolePOJO rolePojo = new RolePOJO();

		ResultSetMetaData resultSetMeta = resultSet.getMetaData();

		if (dbUtil.hasColumn(resultSetMeta, "ROLE_ID")) {
			rolePojo.setRoleId(resultSet.getLong("ROLE_ID"));
		}

		if (dbUtil.hasColumn(resultSetMeta, "ROLE_NAME")) {
			rolePojo.setRoleName(resultSet.getString("ROLE_NAME"));
		}

		if (dbUtil.hasColumn(resultSetMeta, "ROLE_VALUE")) {
			rolePojo.setRoleValue(resultSet.getInt("ROLE_VALUE"));
		}
		return rolePojo;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getRoleValue() {
		return roleValue;
	}

	public void setRoleValue(Integer roleValue) {
		this.roleValue = roleValue;
	}

}
