package com.medical.center.user.model;

import com.medical.center.base.model.BaseEntity;
import javax.persistence.Entity;
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
}
