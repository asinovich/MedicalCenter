package com.medical.center.employee.model;

import com.medical.center.base.model.BaseEntity;
import com.medical.center.user.model.User;
import com.medical.center.base.enums.EmployeeType;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
public class Employee extends BaseEntity<Long> {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type", nullable = false,
        columnDefinition = "enum('ADMIN', 'DOCTOR', 'ACCOUNTANT', 'STAFF')")
    private EmployeeType employeeType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @NotFound(action = NotFoundAction.IGNORE)
    private User user;

    public String getFullName() {
        return this.lastName + " " + this.firstName;
    }
}
