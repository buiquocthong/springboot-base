package com.base.auth.model.criteria;

import com.base.auth.model.Account;
import com.base.auth.model.Course;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class CourseCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String name;

    private String shortDescription;

    private String description;

    private String banner;

    public Specification<Course> getSpecification() {
        return new Specification<Course>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(!StringUtils.isEmpty(getShortDescription())){
                    predicates.add(cb.like(cb.lower(root.get("shortDescription")), "%"+getName().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getName())){
                    predicates.add(cb.like(cb.lower(root.get("name")), "%"+getName().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getBanner())){
                    predicates.add(cb.like(cb.lower(root.get("banner")), "%"+getBanner().toLowerCase()+"%"));
                }
                if(!StringUtils.isEmpty(getDescription())){
                    predicates.add(cb.like(cb.lower(root.get("description")), "%"+getDescription().toLowerCase()+"%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
