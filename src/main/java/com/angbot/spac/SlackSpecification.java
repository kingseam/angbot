package com.angbot.spac;

import org.springframework.data.jpa.domain.Specification;

import com.angbot.domain.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SlackSpecification {

	 public static Specification<User> activeUser(final String active) {
	        return new Specification<User>() {
	            @Override
	            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
	                return cb.equal(root.get("active"), active);
	            }
	        };
	    }

}