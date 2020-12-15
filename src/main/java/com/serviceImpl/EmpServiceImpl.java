package com.serviceImpl;

import com.po.*;
import com.service.IEmpService;
import com.util.DaoMapperUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service("EmpService")
@Transactional
public class EmpServiceImpl implements IEmpService {
	 @Resource(name="DaoMapper")
		private DaoMapperUtil daoMapper;
		public DaoMapperUtil getDaoMapper() {
		return daoMapper;
	}
	public void setDaoMapper(DaoMapperUtil daoMapper) {
		this.daoMapper = daoMapper;
	}
	@Override
	public boolean save(Emp emp) {
		//添加emp对象
		int code=daoMapper.getEmpMapper().save(emp);
		if(code>0){
			//获取刚才添加的员工Id
			Integer eid=daoMapper.getEmpMapper().findMaxEid();
		   //添加薪资
			Salary sa=new Salary(eid,emp.getEmmoney());
			daoMapper.getSalaryMapper().save(sa);
		   //添加员工福利表
			String[] wids=emp.getWids();
			if(wids!=null&&wids.length>0){
				for(int i=0;i<wids.length;i++){
					EmpWelfare ewf=new EmpWelfare(eid,new Integer(wids[i]));
					daoMapper.getEmpWelfareMapper().save(ewf);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean update(Emp emp) {
		//修改emp对象
		int code=daoMapper.getEmpMapper().update(emp);
		if(code>0){
		   //修改薪资（1.原来薪资2.薪资修改）
		  Salary oldsa=daoMapper.getSalaryMapper().findByEid(emp.getEid());
		  if(oldsa!=null&&oldsa.getEmoney()!=null){
			  if(oldsa.getEmoney()<emp.getEmmoney()){//原有薪资小于新的薪资
				  oldsa.setEmoney(emp.getEmmoney());
				  daoMapper.getSalaryMapper().updateByEid(oldsa);
			  }else{//原有薪资大于新的薪资
				  daoMapper.getSalaryMapper().updateByEid(oldsa);
			  }
		  }else{//原来没有薪资
			  Salary sa=new Salary(emp.getEid(),emp.getEmmoney());
				daoMapper.getSalaryMapper().save(sa);
		  }
		   //修改员工福利表
			//获取原来的员工福利
		  List<Welfare> lswf=daoMapper.getEmpWelfareMapper().findByEid(emp.getEid());
		  if(lswf!=null&&lswf.size()>0){
			  //删除原来的福利
			  daoMapper.getEmpWelfareMapper().delByEid(emp.getEid());
		  }
		  //添加新的福利
		  String[] wids=emp.getWids();
			if(wids!=null&&wids.length>0){
				for(int i=0;i<wids.length;i++){
					EmpWelfare ewf=new EmpWelfare(emp.getEid(),new Integer(wids[i]));
					daoMapper.getEmpWelfareMapper().save(ewf);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean delById(Integer eid) {
		//删除子表数据
		daoMapper.getSalaryMapper().delByEid(eid);
		daoMapper.getEmpWelfareMapper().delByEid(eid);
		//删除员工表
		int code=daoMapper.getEmpMapper().delById(eid);
		if(code>0){
			return true;
		}
		return false;
	}

	@Override
	public Emp findById(Integer eid) {
		//获取员工对象
		Emp oldemp=daoMapper.getEmpMapper().findById(eid);
		//获取薪资
		Salary oldsa=daoMapper.getSalaryMapper().findByEid(eid);
		if(oldsa!=null&&oldsa.getEmoney()!=null){
			oldemp.setEmmoney(oldsa.getEmoney());
		}
		//获取福利
		List<Welfare> lswf=daoMapper.getEmpWelfareMapper().findByEid(eid);
		if(lswf!=null&&lswf.size()>0){
			//创建福利编号数组
			String[] wids=new String[lswf.size()];
			for(int i=0;i<lswf.size();i++){
				Welfare wf=lswf.get(i);
				wids[i]=wf.getWid().toString();
			}
			oldemp.setWids(wids);
		}
		oldemp.setLswf(lswf);
		return oldemp;
	}

	@Override
	public List<Emp> findPageAll(PageBean pb) {
		if(pb==null){
		 pb=new PageBean();
		}
		return daoMapper.getEmpMapper().findPageAll(pb);
	}

	@Override
	public int findMaxRow() {
		// TODO Auto-generated method stub
		return daoMapper.getEmpMapper().findMaxRow();
	}

}
