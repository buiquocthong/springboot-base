package com.base.auth.model.criteria;

import com.base.auth.model.Registration;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class RegistrationCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long courseId;
    private Long studentId;
    private LocalDate birthday;
    private String address;
    private String identification;
    private String courseName;
    private String studentFullName;
    private Boolean isFinished;

    public Specification<Registration> getSpecification() {
        return new Specification<Registration>() {
            @Override
            public Predicate toPredicate(Root<Registration> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getCourseId() != null) {
                    predicates.add(cb.equal(root.get("course").get("id"), getCourseId()));
                }
                if (getStudentId() != null) {
                    predicates.add(cb.equal(root.get("student").get("id"), getStudentId()));
                }
                if (getBirthday() != null) {
                    predicates.add(cb.equal(root.get("student").get("birthday"), getBirthday()));
                }
                if (!StringUtils.isEmpty(getAddress())) {
                    predicates.add(cb.like(cb.lower(root.get("student").get("address")), "%" + getAddress().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getIdentification())) {
                    predicates.add(cb.like(cb.lower(root.get("student").get("identification")), "%" + getIdentification().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getCourseName())) {
                    predicates.add(cb.like(cb.lower(root.get("course").get("name")), "%" + getCourseName().toLowerCase() + "%"));
                }
                if (!StringUtils.isEmpty(getStudentFullName())) {
                    predicates.add(cb.like(cb.lower(root.get("student").get("account").get("fullName")), "%" + getStudentFullName().toLowerCase() + "%"));
                }
                if (getIsFinished() != null) {
                    predicates.add(cb.equal(root.get("isFinished"), getIsFinished()));
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };
    }
}
