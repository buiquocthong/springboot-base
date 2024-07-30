package com.base.auth.repository;

import com.base.auth.model.Account;
import com.base.auth.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Optional<Student> findByAddress(String address);
    Optional<Student> findByIdentification(String identification);
}
