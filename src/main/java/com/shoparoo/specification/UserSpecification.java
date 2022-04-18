package com.shoparoo.specification;

import com.shoparoo.entity.Role;
import com.shoparoo.entity.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Optional;

public class UserSpecification implements Specification<User> {
    @Override
    public Predicate toPredicate(Root<User> root,
                                 CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        return null;
    }

    public static Specification<User> noFilter() {
        return (userRoot, query, builder) ->
                builder.isTrue(builder.literal(true));
    }

    public static Specification<User> idIs(int id) {
        return (userRoot, query, builder) ->
                builder.equal(userRoot.get("id"), id);
    }

    public static Specification<User> emailLike(String email) {
        return (userRoot, query, builder) ->
                builder.like(userRoot.get("email"), "%" + email + "%");
    }

    public static Specification<User> firstNameLike(String firstName) {
        return (userRoot, query, builder) ->
                builder.like(userRoot.get("firstName"), "%" + firstName + "%");
    }

    public static Specification<User> lastNameLike(String lastName) {
        return (userRoot, query, builder) ->
                builder.like(userRoot.get("lastName"), "%" + lastName + "%");
    }

    public static Specification<User> genderIs(String gender) {
        return (userRoot, query, builder) -> {
            if (gender.equals("")) {
                // no gender filter
                return builder.isTrue(builder.literal(true));
            }
            return builder.equal(userRoot.get("gender"), gender);
        };
    }

    public static Specification<User> isEnabled(Optional<Boolean> enabled) {
        return (userRoot, query, builder) -> {
            if (enabled.isEmpty()) {
                // no enabled filter
                return builder.isTrue(builder.literal(true));
            }
            return builder.equal(userRoot.get("enabled"), enabled.get());
        };
    }

    public static Specification<User> hasRole(String role) {
        return (userRoot, query, builder) -> {
            if (role.equals("")) {
                // no role filter
                return builder.isTrue(builder.literal(true));
            }

            query.distinct(true);
            Subquery<Role> roleSubQuery = query.subquery(Role.class);
            Root<Role> roleRoot = roleSubQuery.from(Role.class);
            Expression<List<User>> roleUsers = roleRoot.get("users");
            roleSubQuery.select(roleRoot);
            roleSubQuery.where(builder.equal(roleRoot.get("name"), role), builder.isMember(userRoot, roleUsers));

            return builder.exists(roleSubQuery);
        };
    }
}
