package com.evan.wj.service;


import com.evan.wj.dao.AdminRoleMenuDAO;
import com.evan.wj.pojo.AdminRoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminRoleMenuService {


    @Autowired
    private AdminRoleMenuDAO adminRoleMenuDAO;


    List<AdminRoleMenu> findAllByRid(Integer id){

      return   adminRoleMenuDAO.findAllByRid(id);
    }

}
