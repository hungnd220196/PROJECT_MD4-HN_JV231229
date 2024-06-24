package com.ra.project_module4.validator;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HandleNameExist implements ConstraintValidator<NameExist, Object> {
    private String existName;
    @PersistenceContext
    private EntityManager entityManager;
    private Class<?> entityClass;

    @Override
    public void initialize(NameExist constraintAnnotation) {
        this.entityClass = constraintAnnotation.entityClass();
        this.existName = constraintAnnotation.existName();
    }

    @Override
    public boolean isValid(Object s, ConstraintValidatorContext context) {
        return entityManager.createQuery("select count (e) from " +entityClass.getSimpleName() + " e where  e." + existName + " = :value",Long.class)
                .setParameter("value",s).getSingleResult() == 0;
    }
}
