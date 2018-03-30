package com.nieyue.service.impl;

import com.nieyue.bean.Account;
import com.nieyue.dao.AccountDao;
import com.nieyue.exception.CommonRollbackException;
import com.nieyue.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
public class AccountServiceImpl implements AccountService{
	@Autowired
	AccountDao accountDao;

	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean addAccount(Account account) {
		boolean b=false;
		account.setCreateDate(new Date());
		account.setLoginDate(new Date());
		account.setStatus(0);
		account.setCardSecretReceive(0);//默认卡密接收全部
		account.setSafetyGrade(1);//默认安全低
		account.setAuth(0);//没认证
		//增加账户
		b = accountDao.addAccount(account);
		if(!b){
		throw new CommonRollbackException("账户增加异常");
			}

		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean delAccount(Integer accountId) {
		boolean b = accountDao.delAccount(accountId);
		return b;
	}
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean updateAccount(Account account) {
		boolean b = accountDao.updateAccount(account);
		return b;
	}

	@Override
	public Account loadAccount(Integer accountId) {
		Account r = accountDao.loadAccount(accountId);
		return r;
	}

	@Override
	public int countAll(
			Integer accountId,
			Integer auth,
			String phone,
			String email,
			String realname,
			Integer roleId,
			Integer status,
			Date createDate,
			Date loginDate) {
		int c = accountDao.countAll(
				accountId,
				auth,
				phone,
				email,
				realname,
				roleId,
				status,
				createDate,
				loginDate);
		return c;
	}

	@Override
	public List<Account> browsePagingAccount(
			Integer accountId,
			Integer auth,
			String phone,
			String email,
			String realname,
			Integer roleId,
			Integer status,
			Date createDate,
			Date loginDate,
			int pageNum, 
			int pageSize,
			String orderName,
			String orderWay) {
		if(pageNum<1){
			pageNum=1;
		}
		if(pageSize<1){
			pageSize=0;//没有数据
		}
		List<Account> l = accountDao.browsePagingAccount(
				accountId,
				auth,
				phone,
				email,
				realname,
				roleId,
				status,
				createDate,
				loginDate,
				pageNum-1,
				pageSize, 
				orderName,
				orderWay);
		return l;
	}
	@Override
	public Account loginAccount(String adminName, String password,Integer accountId) {
		Account Account = accountDao.loginAccount(adminName, password, accountId);
		return Account;
	}

}
