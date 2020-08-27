package com.xe.demo.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xe.demo.common.Constant;
import com.xe.demo.common.annotation.ServiceLog;
import com.xe.demo.common.pojo.AjaxResult;
import com.xe.demo.common.pojo.Identity;
import com.xe.demo.common.pojo.ParamData;
import com.xe.demo.common.support.DataCache;
import com.xe.demo.common.utils.AppUtil;
import com.xe.demo.common.utils.CookieUtil;
import com.xe.demo.common.utils.DateUtil;
import com.xe.demo.common.utils.IPUtil;
import com.xe.demo.common.utils.MD5Util;
import com.xe.demo.mapper.AuthRoleMapper;
import com.xe.demo.mapper.AuthUserMapper;
import com.xe.demo.model.AuthRole;
import com.xe.demo.model.AuthUser;

/**
 * 登录管理业务层
 */
@Service
public class LoginService extends AbstratService<AuthUser> {
	@Autowired
	private AuthUserMapper userMapper;
	@Autowired
	private AuthRoleMapper roleMapper;
	@Autowired
	private DataCache dataCache;

	@ServiceLog("登录")//ParamData=={password=21232f297a57a5a743894a0e4a801fc3, loginIp=192.168.1.155, username=admin}
	public AjaxResult login(HttpServletRequest request, HttpServletResponse response) {
		String verifyCode = (String) request.getSession().getAttribute(Constant.VERIFY_CODE);//获取验证码(输错3次出现)
		String result = null;
		ParamData params = new ParamData();//接收的登录信息 Map封装
		String vcode = params.getString("vcode");
		if (params.containsKey("vcode") && (StringUtils.isEmpty(verifyCode) || !verifyCode.equalsIgnoreCase(vcode))) {
			result = "验证码错误";//登录的信息有vcode且验证码没有生成 或者 验证码不匹配
		}else{
			String username = params.getString("username");
			String loginIp = params.getString("loginIp");
			String key = username + loginIp + Constant.LOGIN_ERROR_TIMES;//LOGIN_ERROR_TIMES登录失败次数
			AuthUser user = userMapper.queryByUsername(username);		//AuthUser存在则存入，不存在舍弃字段
			
			if (user == null || !user.getPassword().equals(params.getString("password"))) {
				int errTimes = dataCache.getInt(key);
				//记录密码错误次数,达到3次则需要输出验证码
				dataCache.setValue(key, errTimes += 1);
				result = "用户名或密码错误|" + errTimes;
			}else if(Constant.ROLE_ANONYMOUS.equals(user.getRole().getRolename())){
				result = "用户未分组,无法登录";
			}else{
				// 更新登录IP和登录时间
				user.setLoginip(loginIp);
				user.setLogintime(DateUtil.getCurDateTime());//AuthUser实体类的@Table(name = "auth_user")对表字段进行更新
				userMapper.updateByPrimaryKeySelective(user);//包方法：对字段进行判断再更新(继承mybatis的common方法 )
				//session初始化
				Identity identity = new Identity();//封装的Session  包括sessionId、IP、User、操作权限列表
				AuthRole role = roleMapper.queryRoleById(user.getRoleid());//Roleid超级管理员、管理员、匿名用户组
				identity.setOperationList(role.getOperations());
				identity.setLoginUser(user);
				identity.setLoginIp(loginIp);
				String sessionId = getSessionId(username, identity.getLoginIp());//自方法生成token
				identity.setSessionId(sessionId);
				//新增用户信息缓存 dataCache：权限列表缓存
				dataCache.setValue(username + loginIp, identity);
				dataCache.setValue(sessionId, username);
				dataCache.remove(key);//登录成功移除 验证码错误的设置
				CookieUtil.set(Constant.SESSION_IDENTITY_KEY, sessionId, response);//设置Cookie
			}
		}
		return AppUtil.returnObj(result);//封装返回数据AjaxResult
	}

	@ServiceLog("退出")
	public AjaxResult logout(HttpServletResponse response, HttpServletRequest request) {
		String sessionId = CookieUtil.get(Constant.SESSION_IDENTITY_KEY, request);
		//移除dataCache和Cookie的id
		if (StringUtils.isNotEmpty(sessionId)) {
			dataCache.remove(sessionId);
			String userName = (String) dataCache.getValue(sessionId);
			if (StringUtils.isNotEmpty(userName)) {
				dataCache.remove(userName + IPUtil.getIpAdd(request));
			}
			CookieUtil.delete(Constant.SESSION_IDENTITY_KEY, request, response);
		}
		return AppUtil.returnObj(null);
	}

	private String getSessionId(String userName, String ip) {//token:防止密码错误反复查询数据库，将id放入session中
		String str = userName + "_" + System.currentTimeMillis() + "_" + ip;
		try {
			return MD5Util.encrypt(str);
		} catch (Exception e) {
			return "生成token错误";
		}
	}
}
