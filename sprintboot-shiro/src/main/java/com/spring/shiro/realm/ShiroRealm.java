package com.spring.shiro.realm;


import com.spring.shiro.bean.User;
import com.spring.shiro.dao.UserMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.security.auth.login.AccountNotFoundException;
import java.util.HashSet;
import java.util.Set;

/**
 * 自定义 realm 类,实现自定义的权限认证操作
 * 继承 AuthorizingReam 类 重写 doGetAuthorizationInfo (授权) 和 doGetAuthenticationInfo (认证)
 */

@Component
@Log4j2
public class ShiroRealm extends AuthorizingRealm {
	@Autowired
	private UserMapper userMapper;

	//认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authen) throws AuthenticationException {
		//将authen的类型转换为其子类下的UsernamePasswordToken
		log.info( " ----- 进入 shiro 验证 ----- ");
		UsernamePasswordToken token = (UsernamePasswordToken) authen;
		// 从 authenticationToken 中获取 用户传递的密码
		// 获取密码时,AuthenticationToken 获取的密码会乱码,用其子类下的UsernamePasswordToken子类获取密码
		String truePassword = userMapper.selectPasswordByUsername(token.getUsername());
		if (StringUtils.isEmpty(truePassword)) {
			throw new AccountException("用户名不正确");
		}else if(!truePassword.equals(new String(token.getPassword()))){
			throw new AccountException("密码不正确");
		}
		// 如果身份认证验证成功，返回一个AuthenticationInfo实现,失败则抛出异常
		// 参数 依次 为 用户名 密码 盐值 getName() 不做了解
		SimpleAuthenticationInfo sa = new SimpleAuthenticationInfo(token.getPrincipal(),truePassword,ByteSource.Util.bytes(token.getPrincipal()),getName());
		return sa;
	}


	//授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
		log.info("----- 进入权限认证 -----");

		Set<String> roles = new HashSet<>();
		Set<String> stringPermissions = new HashSet<>();
		//通过用户名查询角色,存入set集合
		List<Roles> roleList = navServie.queryRolesByName(principal.getPrimaryPrincipal().toString());
		for (Roles role : roleList) {
			roles.add(role.getRoleCode());
		}
		//通过用户名查询权限,存入set 集合
		List<Pers> perList = navServie.queryAllPers(principal.getPrimaryPrincipal().toString());
		for (Pers pers : perList) {
			stringPermissions.add(pers.getPerCode());
		}
		SimpleAuthorizationInfo sz = new SimpleAuthorizationInfo();
		sz.setRoles(roles);
		sz.setStringPermissions(stringPermissions);
		return sz;
	}
}

