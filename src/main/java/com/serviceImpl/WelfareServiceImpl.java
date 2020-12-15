package com.serviceImpl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mapper.IWelfareMapper;
import com.po.Welfare;
import com.service.IWelfareService;
import com.util.DaoMapperUtil;
@Service("WelfareService")
@Transactional
public class WelfareServiceImpl implements IWelfareService {
	 @Resource(name="DaoMapper")
		private DaoMapperUtil daoMapper;
		public DaoMapperUtil getDaoMapper() {
		return daoMapper;
	}
	public void setDaoMapper(DaoMapperUtil daoMapper) {
		this.daoMapper = daoMapper;
	}
	@Override
	public List<Welfare> findAll() {
		// TODO Auto-generated method stub
		return daoMapper.getWelfareMapper().findAll();
	}

}
