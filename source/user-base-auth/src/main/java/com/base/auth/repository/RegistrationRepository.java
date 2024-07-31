package com.base.auth.repository;

import com.base.auth.model.Permission;
import com.base.auth.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long>, JpaSpecificationExecutor<Registration> {
    Optional<Registration> findByStudentIdAndCourseId(Long studentId, Long courseId);

}
