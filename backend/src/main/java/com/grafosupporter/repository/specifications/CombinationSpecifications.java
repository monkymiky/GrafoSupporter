package com.grafosupporter.repository.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Expression;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.domain.Specification;

import com.grafosupporter.model.Combination;
import com.grafosupporter.model.User;
import com.grafosupporter.model.ValuatedSign;

public class CombinationSpecifications {
    private CombinationSpecifications() {
    }

    private static Predicate buildMatchValuesPredicate(CriteriaBuilder cb, Expression<Integer> backendMinExpression,
            Expression<Integer> backendMaxExpression,
            Integer frontendValue) {
        switch (frontendValue) {
            case 0:
                return cb.and(cb.equal(backendMinExpression, 0), cb.equal(backendMaxExpression, 0));
            case 1:
                return cb.and(cb.lessThanOrEqualTo(backendMinExpression, 1),
                        cb.greaterThanOrEqualTo(backendMaxExpression, 2));
            case 2:
                return cb.and(cb.lessThanOrEqualTo(backendMinExpression, 3),
                        cb.greaterThanOrEqualTo(backendMaxExpression, 4));
            case 3:
                return cb.and(cb.lessThanOrEqualTo(backendMinExpression, 5),
                        cb.greaterThanOrEqualTo(backendMaxExpression, 5));
            case 4:
                return cb.and(cb.lessThanOrEqualTo(backendMinExpression, 6),
                        cb.greaterThanOrEqualTo(backendMaxExpression, 7));
            case 5:
                return cb.and(cb.lessThanOrEqualTo(backendMinExpression, 8),
                        cb.greaterThanOrEqualTo(backendMaxExpression, 10));
            default:
                throw new InvalidParameterException("Invalid frontendValue for JPA specification: " + frontendValue);
        }
    }

    public static Specification<Combination> allSignsInCombinationMustMatchCriteria(
            Map<Long, Integer> signMapInput) {
        return (Root<Combination> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            if (signMapInput == null || signMapInput.isEmpty()) {
                return cb.disjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Combination> subRoot = subquery.correlate(root);
            Join<Combination, ValuatedSign> valuatedSignsOfTheCombination = subRoot.join("signs");

            subquery.select(cb.literal(1L));

            List<Predicate> criteriaFromMap = new ArrayList<>();
            for (Map.Entry<Long, Integer> signMapInputTuple : signMapInput.entrySet()) {
                Long inputSignId = signMapInputTuple.getKey();
                Integer inputSignRange = signMapInputTuple.getValue();

                Predicate signIdAreEqual = cb.equal(valuatedSignsOfTheCombination.get("sign").get("id"),
                        inputSignId);
                Predicate signIsOptional = cb.equal(valuatedSignsOfTheCombination.get("isOptional"), true);
                Predicate valueIsInRange = buildMatchValuesPredicate(cb,
                        valuatedSignsOfTheCombination.get("min"), valuatedSignsOfTheCombination.get("max"),
                        inputSignRange);

                criteriaFromMap
                        .add(cb.or(signIsOptional, cb.and(signIdAreEqual, valueIsInRange)));
            }

            Predicate isSatisfactory = cb.or(criteriaFromMap.toArray(new Predicate[0]));

            Predicate isProblematic = cb.not(isSatisfactory);

            subquery.where(isProblematic);

            return cb.not(cb.exists(subquery));
        };
    }

    public static Specification<Combination> byAuthorCustomUsernames(List<String> customUsernames) {
        return (Root<Combination> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (customUsernames == null || customUsernames.isEmpty()) {
                return cb.conjunction();
            }

            Join<Combination, User> authorJoin = root.join("author");
            List<Predicate> authorPredicates = new ArrayList<>();

            for (String searchTerm : customUsernames) {
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    String trimmedTerm = searchTerm.trim().toLowerCase();
                    
                    Predicate customUsernameNotNull = cb.isNotNull(authorJoin.get("customUsername"));
                    Predicate customUsernameNotEmpty = cb.notEqual(authorJoin.get("customUsername"), "");
                    Predicate customUsernameEqual = cb.equal(
                            cb.lower(authorJoin.get("customUsername")),
                            trimmedTerm
                    );
                    Predicate customUsernameMatch = cb.and(
                            customUsernameNotNull,
                            customUsernameNotEmpty,
                            customUsernameEqual
                    );
                    
                    Predicate nameMatch = cb.equal(
                            cb.lower(authorJoin.get("name")),
                            trimmedTerm
                    );
                    
                    Predicate customUsernameOrNameMatch = cb.or(
                            customUsernameMatch,
                            cb.and(
                                    cb.or(
                                            cb.isNull(authorJoin.get("customUsername")),
                                            cb.equal(authorJoin.get("customUsername"), "")
                                    ),
                                    nameMatch
                            )
                    );
                    
                    authorPredicates.add(customUsernameOrNameMatch);
                }
            }

            if (authorPredicates.isEmpty()) {
                return cb.conjunction();
            }

            return cb.or(authorPredicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Combination> withFilters(
            Map<Long, Integer> signMapInput,
            List<String> authorCustomUsernames) {
        Specification<Combination> spec = Specification.where(null);

        if (signMapInput != null && !signMapInput.isEmpty()) {
            boolean allZero = signMapInput.values().stream().allMatch(v -> v == 0);
            if (!allZero) {
                spec = spec.and(allSignsInCombinationMustMatchCriteria(signMapInput));
            }
        }

        if (authorCustomUsernames != null && !authorCustomUsernames.isEmpty()) {
            spec = spec.and(byAuthorCustomUsernames(authorCustomUsernames));
        }

        return spec;
    }

}
