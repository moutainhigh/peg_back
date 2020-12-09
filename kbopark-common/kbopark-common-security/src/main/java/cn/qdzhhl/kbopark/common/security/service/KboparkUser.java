/*
 *    Copyright (c) 2018-2025, kbopark All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: kbopark
 */

package cn.qdzhhl.kbopark.common.security.service;

import lombok.Getter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author kbopark
 * @date 2020/4/16 扩展用户信息
 */
public class KboparkUser extends User {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	/**
	 * 用户ID
	 */
	@Getter
	private Integer id;

	/**
	 * 部门ID
	 */
	@Getter
	private Integer deptId;

	/**
	 * 商家ID
	 */
	@Getter
	private Integer merchantId;

	/**
	 * 用户类型
	 */
	@Getter
	private Integer userType;

	/**
	 * 姓名
	 */
	@Getter
	private String realName;

	/**
	 * 邮箱
	 */
	@Getter
	private String email;

	/**
	 * 推广码
	 */
	@Getter
	private String promoteCode;

	/**
	 * 手机号
	 */
	@Getter
	private String phone;

	/**
	 * 头像
	 */
	@Getter
	private String avatar;

	/**
	 * 租户ID
	 */
	@Getter
	private Integer tenantId;

	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link DaoAuthenticationProvider}.
	 * @param id 用户ID
	 * @param deptId 部门ID
	 * @param tenantId 租户ID
	 * @param username the username presented to the
	 * <code>DaoAuthenticationProvider</code>
	 * @param password the password that should be presented to the
	 * <code>DaoAuthenticationProvider</code>
	 * @param enabled set to <code>true</code> if the user is enabled
	 * @param accountNonExpired set to <code>true</code> if the account has not expired
	 * @param credentialsNonExpired set to <code>true</code> if the credentials have not
	 * expired
	 * @param accountNonLocked set to <code>true</code> if the account is not locked
	 * @param authorities the authorities that should be granted to the caller if they
	 * presented the correct username and password and the user is enabled. Not null.
	 * @throws IllegalArgumentException if a <code>null</code> value was passed either as
	 * a parameter or as an element in the <code>GrantedAuthority</code> collection
	 */
	public KboparkUser(Integer id, Integer deptId, Integer merchantId, Integer userType, String realName, String email, String promoteCode, String phone, String avatar, Integer tenantId, String username,
			String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.deptId = deptId;
		this.phone = phone;
		this.avatar = avatar;
		this.tenantId = tenantId;
		this.merchantId = merchantId;
		this.userType = userType;
		this.promoteCode = promoteCode;
		this.realName = realName;
		this.email = email;
	}

}
