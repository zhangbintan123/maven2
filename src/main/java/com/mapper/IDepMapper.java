package com.mapper;

import com.po.Dep;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DepDAO")
public interface IDepMapper {
 public List<Dep> findAll(); 
}
