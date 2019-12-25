package com.evan.wj.service;


import com.evan.wj.dao.AdminUserRoleDAO;
import com.evan.wj.pojo.AdminUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserRoleService {


    @Autowired
    private AdminUserRoleDAO adminUserRoleDAO;


    public List<AdminUserRole> listAllByUid(Integer id){

       return   adminUserRoleDAO.findAllByUid(id);
    }
}
