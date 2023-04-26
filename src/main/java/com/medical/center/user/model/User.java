package com.medical.center.user.model;

import com.medical.center.base.model.BaseEntity;
import com.medical.center.employee.model.Employee;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity<Long> {

    private String email;

    private String password;

    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "employee")
    private Employee employee;

    @Builder
    public User(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String email,
        String password, Employee employee) {
        super(id, createdAt, updatedAt, null);
        this.email = email;
        this.password = password;
        this.employee = employee;
    }
}
