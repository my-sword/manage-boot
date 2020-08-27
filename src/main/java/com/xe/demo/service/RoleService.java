package com.xe.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xe.demo.common.annotation.ServiceLog;
import com.xe.demo.common.pojo.AjaxResult;
import com.xe.demo.common.utils.AppUtil;
import com.xe.demo.mapper.AuthRoleMapper;
import com.xe.demo.mapper.AuthRoleOperationMapper;
import com.xe.demo.model.AuthRole;
import com.xe.demo.model.AuthRoleOperation;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 角色管理：测试、超级、匿名
 */
@Service
public class RoleService extends AbstratService<AuthRole> {

	@Autowired
	private AuthRoleMapper roleMapper;
	@Autowired
	private AuthRoleOperationMapper roleOperMapper;
	
	public AuthRole queryRoleById(int roleid){
		return roleMapper.queryRoleById(roleid);
	}

	@ServiceLog("新增角色")
	public AjaxResult saveRole(AuthRole role) {
		String result = null;
		AuthRole $role = roleMapper.queryByRolename(role.getRolename());
		if (null == $role) {
			save(role);
		} else {
			result = "角色名已存在";
		}
		return AppUtil.returnObj(result);
	}


	@ServiceLog("更新角色")
	public AjaxResult updateRole(AuthRole role) {
		String result = null;
		AuthRole $role = roleMapper.queryByRolename(role.getRolename());
		if (null != $role && $role.getRoleid() != role.getRoleid()) {
			result = "角色名已存在";
		} else {
			updateByID(role);
		}
		return AppUtil.returnObj(result);
	}
	//查询非"admin"的角色权限列表
	public List<AuthRole> queryNotAdmin() {
		//example是Mybatis数据层框架中的一个工具，可以帮我们完成sql语句中where条件句的书写，相当于where后面的部分，我们可以根据不同的条件来查询和操作数据库，简化书写sql的过程。
		Example example = new Example(AuthRole.class);//MyBatis的逆向工程可以自动生成Example类:andXXNotEqualTo
		Criteria criteria = example.createCriteria();
		criteria.andNotEqualTo("rolename", "admin");//rolename!=admin
		return roleMapper.selectByExample(example);//逆向工程生成的自动化语句
	}

	@ServiceLog("绑定角色权限")
	public AjaxResult bindOpers(int roleid, int[] opids) {
		List<AuthRoleOperation> list = new ArrayList<AuthRoleOperation>();
		AuthRoleOperation roleOperation = null;
		for(int opid: opids){
			roleOperation = new AuthRoleOperation();
			roleOperation.setRoleid(roleid);
			roleOperation.setOpid(opid);
			list.add(roleOperation);
		}
		//通用mapper的批量插入竟然不行
//		roleOperMapper.insertList(list);
		roleOperMapper.batchInsert(list);
		return AppUtil.returnObj(null);
	}

	@ServiceLog("解除角色权限")
	public AjaxResult unbindOpers(int roleid, int[] opids){
		List<AuthRoleOperation> list = new ArrayList<AuthRoleOperation>();
		AuthRoleOperation roleOperation = null;
		for(int opid: opids){
			roleOperation = new AuthRoleOperation();
			roleOperation.setRoleid(roleid);
			roleOperation.setOpid(opid);
			list.add(roleOperation);
		}
		roleOperMapper.delRoleOpers(list);
		return AppUtil.returnObj(null);
	}

}
