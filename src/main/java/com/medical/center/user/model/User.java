package com.medical.center.user.model;

import com.medical.center.base.model.BaseEntity;
import com.medical.center.employee.model.Employee;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity<Long> {

    private String email;

    private String password;

    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "employee")
    private Employee employee;
}
