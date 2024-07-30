package com.base.auth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "db_user_base_student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.base.auth.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;

    @Column(name="birthday")
    private LocalDate birthday;
    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "address")
    private String address;

    @Column(name = "identification")
    private String identification;
}
