package com.unipd.synclab.grafosupporter.repository.specifications;

import com.unipd.synclab.grafosupporter.model.Combination;
import com.unipd.synclab.grafosupporter.model.ValuatedSign;

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

public class CombinationSpecifications {
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
            Map<Long, Integer> referenceCriteriaSigns) {
        // la condizione "tutti i segni soddisfano la condizione x" equivale a dire
        // "non esiste alcun segno che non soddisfa x"
        return (Root<Combination> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            if (referenceCriteriaSigns == null || referenceCriteriaSigns.isEmpty()) {
                return cb.disjunction();
            }

            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Combination> subRoot = subquery.correlate(root);
            Join<Combination, ValuatedSign> valuatedSignFromCombination = subRoot.join("signs");

            subquery.select(cb.literal(1L));
            // Identifico le combinazioni "problematiche" per poi escluderle e tenere solo
            // le altre
            // per ogni chiave/valore nella mappa referenceCriteriaSigns,
            // creiamo una condizione che soddisfa il valuatedSignFromCombination.
            // Poi neghiamo l'OR di tutte queste condizioni soddisfacenti.

            List<Predicate> orConditionsForSatisfactoryMatch = new ArrayList<>();
            for (Map.Entry<Long, Integer> criteriaEntry : referenceCriteriaSigns.entrySet()) {
                Long criteriaSignId = criteriaEntry.getKey();
                Integer frontendValue = criteriaEntry.getValue();

                Predicate signIdMatchesCriteria = cb.equal(valuatedSignFromCombination.get("sign").get("id"),
                        criteriaSignId);
                Predicate signIsOptional = cb.equal(valuatedSignFromCombination.get("isOptional"), true);
                Predicate valueMatchesCriteria = buildMatchValuesPredicate(cb,
                        valuatedSignFromCombination.get("min"), valuatedSignFromCombination.get("max"),
                        frontendValue);

                orConditionsForSatisfactoryMatch
                        .add(cb.or(signIsOptional, cb.and(signIdMatchesCriteria, valueMatchesCriteria)));
            }

            // Un ValuatedSign è "soddisfacente" se metcha con un entry della map
            // referenceCriteriaSigns

            Predicate isSatisfactory = cb.or(orConditionsForSatisfactoryMatch.toArray(new Predicate[0]));

            // Un ValuatedSign è "problematico" se NON matcha con nessun entry di
            // referenceCriteriaSigns ovvero non è "soddisfacente"

            Predicate isProblematic = cb.not(isSatisfactory);

            subquery.where(isProblematic);

            // NON ESISTE un ValuatedSign "problematico" --> tutti i ValuatedSign matchano
            return cb.not(cb.exists(subquery));
        };
    }

}
