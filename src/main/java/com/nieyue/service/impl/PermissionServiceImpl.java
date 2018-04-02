package com.nieyue.service.impl;

import com.nieyue.bean.Permission;
import com.nieyue.dao.PermissionDao;
import com.nieyue.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService{
	@Resource
	PermissionDao permissionDao;
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addPermission(Permission permission) {
		permission.setUpdateDate(new Date());
		boolean b = permissionDao.addPermission(permission);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delPermission(Integer permissionId) {
		boolean b = permissionDao.delPermission(permissionId);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updatePermission(Permission permission) {
		permission.setUpdateDate(new Date());
		boolean b = permissionDao.updatePermission(permission);
		return b;
	}

	@Override
	public Permission loadPermission(Integer permissionId) {
		Permission r = permissionDao.loadPermission(permissionId);
		return r;
	}

	@Override
	public int countAll(
						Integer type,
						String managerName,
						String name,
						String route) {
		int c = permissionDao.countAll(
				 type,
				 managerName,
				 name,
				 route
		);
		return c;
	}

	@Override
	public List<Permission> browsePagingPermission(
			Integer type,
			String managerName,
			String name,
			String route,
			int pageNum, int pageSize,
			String orderName, String orderWay) {
		if(pageNum<1){
			pageNum=1;
		}
		if(pageSize<1){
			pageSize=0;//没有数据
		}
		List<Permission> l = permissionDao.browsePagingPermission(
				 type,
				 managerName,
				 name,
				 route,
				pageNum-1, pageSize, orderName, orderWay);
		return l;
	}

	
}
