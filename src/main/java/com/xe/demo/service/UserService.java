package com.xe.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.page.PageMethod;
import com.xe.demo.common.Constant;
import com.xe.demo.common.annotation.ServiceLog;
import com.xe.demo.common.pojo.AjaxResult;
import com.xe.demo.common.pojo.PageAjax;
import com.xe.demo.common.support.DataCache;
import com.xe.demo.common.utils.AppUtil;
import com.xe.demo.common.utils.CookieUtil;
import com.xe.demo.common.utils.DateUtil;
import com.xe.demo.common.utils.IPUtil;
import com.xe.demo.mapper.AuthRoleMapper;
import com.xe.demo.mapper.AuthUserMapper;
import com.xe.demo.model.AuthRole;
import com.xe.demo.model.AuthUser;

/**
 * 用户管理
 */
@Service
public class UserService extends AbstratService<AuthUser> {

	@Autowired
	private AuthUserMapper userMapper;
	@Autowired
	private AuthRoleMapper roleMapper;
	@Autowired
	private DataCache dataCache;

	@Override
	@ServiceLog("查询用户列表")
	public PageAjax<AuthUser> queryPage(PageAjax<AuthUser> page, AuthUser user) {
		PageMethod.startPage(page.getPageNo(), page.getPageSize());
		List<AuthUser> list = userMapper.queryList(user);//返回匹配且可用的权限列表
		return AppUtil.returnPage(list);//封装分页请求结果
	}

	@ServiceLog("添加用户")//"添加"确定按钮
	public AjaxResult saveUser(AuthUser user) {
		String result = null;
		AuthUser $user = userMapper.queryByUsername(user.getUsername());
		if (null == $user) {
			user.setAddtime(DateUtil.getCurDateTime());
			save(user);//AbstratService通用接口调用
		} else {
			result = "用户名已存在";
		}
		return AppUtil.returnObj(result);//返回"操作成功"字样
	}

	@ServiceLog("修改用户")//"修改"确定按钮
	public AjaxResult updateUser(AuthUser user) {
		String result = null;
		AuthUser $user = userMapper.queryByUsername(user.getUsername());
		if (null != $user && $user.getId() != user.getId()) {
			result = "用户名已存在";
		} else {
			updateByID(user);
		}
		return AppUtil.returnObj(result);
	}

	@ServiceLog("修改个人密码")//
	//提交是对应的class="id"...类似$("#div").attr("src", "${ctx}/getVcode?random=" + Math.random());
	public AjaxResult updatePwd(HttpServletResponse response, HttpServletRequest request, int id, String oldPwd, String newPwd) {
		AuthUser user = null;
		if(StringUtils.isNotEmpty(oldPwd)){
			user = queryByID(id);
			if(!oldPwd.equals(user.getPassword())){
				return new AjaxResult(0, "旧密码不正确");
			}
		}
		//对auth_user表单更新
		user = new AuthUser();
		user.setId(id);
		user.setPassword(newPwd);
		int ret = userMapper.updateByPrimaryKeySelective(user);//包方法：AuthUser实体类的@Table(name = "auth_user")对表字段进行更新
		//移除Cookie和缓存
		if(ret > 0){
			String sessionId = CookieUtil.get(Constant.SESSION_IDENTITY_KEY, request);
			if (StringUtils.isNotEmpty(sessionId)) {
				dataCache.remove(sessionId);
				String userName = (String) dataCache.getValue(sessionId);
				if (StringUtils.isNotEmpty(userName)) {
					dataCache.remove(userName + IPUtil.getIpAdd(request));
				}
				CookieUtil.delete(Constant.SESSION_IDENTITY_KEY, request, response);
			}
		}
		return new AjaxResult(1, "修改成功");//返回指定的操作结果
	}

	@ServiceLog("查询用户列表")
	public List<AuthUser> queryRoleUsers(int roleid) {
		AuthUser user = new AuthUser();
		user.setRoleid(roleid);
		return userMapper.select(user);
	}

	@ServiceLog("角色绑定用户")//"分配用户"确认按钮
	public AjaxResult bindUser(int roleid, Integer[] ids) {
		AuthRole role = roleMapper.queryByRolename(Constant.ROLE_ANONYMOUS);
		//剔除的用户
		List<Map<String, Integer>> userList = new ArrayList<Map<String, Integer>>();
		Map<String, Integer> user = null;
		//该角色原拥有的用户
		List<Integer> userids = userMapper.queryRoleUids(roleid);//根据操作角色查询所有匹配的用户id
		for(int id: userids){
			if(!Arrays.asList(ids).contains(id)){
				user = new HashMap<String, Integer>();
				user.put("id", id);
				user.put("roleid", role.getRoleid());
				userList.add(user);
			}
		}
		dao.batchUpdate("AuthUserMapper.bindUser", userList);
		//新增的用户
		userList = new ArrayList<Map<String, Integer>>();
		for(int id: ids){
			user = new HashMap<String, Integer>();
			user.put("id", id);
			user.put("roleid", roleid);
			userList.add(user);
		}//主要更新 auth_user内的字段roleid(角色权限id)
		dao.batchUpdate("AuthUserMapper.bindUser", userList);//调用DaoSupport操作DAO层指向AuthUserMapper.xml批量更新
		return AppUtil.returnObj(null);//返回默认的“操作成功”
	}

}
