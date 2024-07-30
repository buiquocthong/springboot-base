package com.base.auth.model.criteria;

import com.base.auth.model.Account;
import com.base.auth.model.Student;
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
public class StudentCriteria implements Serializable{
    private static final long serialVersionUID = 1L;
    private Long id;
    private LocalDate birthday;
    private Account account;
    private String address;
    private String identification;

    public Specification<Student> getSpecification() {
        return new Specification<Student>() {
            @Override
            public Predicate toPredicate(Root<Student> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getBirthday() !=null){
                    predicates.add(cb.equal(root.get("birthday"), getBirthday()));
                }
                if(getAccount() != null){
                    predicates.add(cb.equal(root.get("account"), getAccount()));
                }
                if(!StringUtils.isEmpty(getAddress())){
                    predicates.add(cb.like(cb.lower(root.get("address")), "%"+getAddress().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getIdentification())){
                    predicates.add(cb.like(cb.lower(root.get("identification")), "%"+getIdentification().toLowerCase()+"%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }

        };
    }

}

