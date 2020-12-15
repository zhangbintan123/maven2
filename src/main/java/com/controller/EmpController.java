package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.po.Dep;
import com.po.Emp;
import com.po.PageBean;
import com.po.Welfare;
import com.util.AJAXUtils;
import com.util.BizServiceUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmpController {
	@Resource(name="BizService")
  private BizServiceUtil bizService;
public BizServiceUtil getBizService() {
	return bizService;
}
public void setBizService(BizServiceUtil bizService) {
	this.bizService = bizService;
}
@RequestMapping(value="save_Emp.do")
public String save(HttpServletRequest request,HttpServletResponse response,Emp emp){
	 String realpath=request.getRealPath("/");
	 /************文件上传****************/
	   //获取文件上传的对象
	   MultipartFile multipartFile=emp.getPic();
	   if(multipartFile!=null&&!multipartFile.isEmpty()){
		   //获取文件上传名称
		   String fname=multipartFile.getOriginalFilename();
		   //更名
		   if(fname.lastIndexOf(".")!=-1){
			   //获取存在的后缀
			   String ext=fname.substring(fname.lastIndexOf("."));
			   //判断后缀格式
			   if(ext.equalsIgnoreCase(".jpg")){
				   String newfname=new Date().getTime()+ext;
				   //创建文件对象指定上传路径
				   File dostfile=new File(realpath+"/uppic/"+newfname);
				   //复制文件内容,上传
				   try {
					FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostfile);
					emp.setPhoto(newfname);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
		   }
	   }
	 /************文件上传end****************/
	boolean flag=bizService.getEmpService().save(emp);
	if(flag){
		AJAXUtils.printString(response, 1+"");
	}else{
		AJAXUtils.printString(response, 0+"");
	}
	return null;
  }
@RequestMapping(value="update_Emp.do")
public String update(HttpServletRequest request,HttpServletResponse response,Emp emp){
	//获取原来照片
	String oldphoto=bizService.getEmpService().findById(emp.getEid()).getPhoto();
	String realpath=request.getRealPath("/");
	 /************文件上传****************/
	   //获取文件上传的对象
	   MultipartFile multipartFile=emp.getPic();
	   if(multipartFile!=null&&!multipartFile.isEmpty()){
		   //获取文件上传名称
		   String fname=multipartFile.getOriginalFilename();
		   //更名
		   if(fname.lastIndexOf(".")!=-1){
			   //获取存在的后缀
			   String ext=fname.substring(fname.lastIndexOf("."));
			   //判断后缀格式
			   if(ext.equalsIgnoreCase(".jpg")){
				   String newfname=new Date().getTime()+ext;
				   //创建文件对象指定上传路径
				   File dostfile=new File(realpath+"/uppic/"+newfname);
				   //复制文件内容,上传
				   try {
					FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), dostfile);
					emp.setPhoto(newfname);
					//删除原来照片
					File oldfile=new File(realpath+"/uppic/"+oldphoto);
					if(oldfile.exists()&&!oldphoto.equalsIgnoreCase("default.jpg")){
						oldfile.delete();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }
		   }
	   }else{
		   emp.setPhoto(oldphoto);
	   }
	 /************文件上传end****************/
	boolean flag=bizService.getEmpService().update(emp);
	if(flag){
		AJAXUtils.printString(response, 1+"");
	}else{
		AJAXUtils.printString(response, 0+"");
	}
	return null;
  }
@RequestMapping(value="delById_Emp.do")
public String delById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	boolean flag=bizService.getEmpService().delById(eid);
	if(flag){
		AJAXUtils.printString(response, 1+"");
	}else{
		AJAXUtils.printString(response, 0+"");
	}  
	return null;
  }
@RequestMapping(value="findById_Emp.do")
public String findById(HttpServletRequest request,HttpServletResponse response,Integer eid){
	Emp oldemp=bizService.getEmpService().findById(eid);
	PropertyFilter propertyFilter=AJAXUtils.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	AJAXUtils.printString(response,jsonstr);
	return null;
  }
@RequestMapping(value="findDetail_Emp.do")
public String findDetail(HttpServletRequest request,HttpServletResponse response,Integer eid){
	Emp oldemp=bizService.getEmpService().findById(eid);
	PropertyFilter propertyFilter=AJAXUtils.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(oldemp,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	AJAXUtils.printString(response,jsonstr);  
	return null;
  }
@RequestMapping(value="findPageAll_Emp.do")
public String findPageAll(HttpServletRequest request,HttpServletResponse response,Integer page,Integer rows){
	Map<String,Object> map= new HashMap<String,Object>();
	PageBean pb=new PageBean();
	page=page==null||page<1?pb.getPage():page;
	rows=rows==null||rows<1?pb.getRows():rows;
	pb.setPage(page);
	pb.setRows(rows);
	List<Emp> lsemp=bizService.getEmpService().findPageAll(pb);
	int maxrows=bizService.getEmpService().findMaxRow();
	//按照easyUI的格式封装数据
	map.put("page",page);
	map.put("rows",lsemp);
	map.put("total",maxrows);
	PropertyFilter propertyFilter=AJAXUtils.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(map,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	AJAXUtils.printString(response,jsonstr); 
	return null;
  }
@RequestMapping(value="doinit_Emp.do")
public String doinit(HttpServletRequest request,HttpServletResponse response){
	System.out.println("111111111111111111111");
	Map<String,Object> map= new HashMap<String,Object>();
	List<Dep> lsdep=bizService.getDepService().findAll();
	List<Welfare> lswf=bizService.getWelfareService().findAll();
	map.put("lsdep", lsdep);
	map.put("lswf", lswf);
	PropertyFilter propertyFilter=AJAXUtils.filterProperts("birthday","pic");
	String jsonstr=JSONObject.toJSONString(map,propertyFilter,SerializerFeature.DisableCircularReferenceDetect);
	AJAXUtils.printString(response,jsonstr); 
	return null;
  }
}
