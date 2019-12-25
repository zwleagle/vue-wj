package com.evan.wj.pojo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "admin_permission")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
@Data
public class AdminPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    String name;
    String desc_;
    String url;
}
