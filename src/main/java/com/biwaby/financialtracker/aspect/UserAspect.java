package com.biwaby.financialtracker.aspect;

import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.enums.CategoryType;
import com.biwaby.financialtracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class UserAspect {

    private final CategoryService categoryService;

    @Pointcut("execution(public * com.biwaby.financialtracker.service.impl.UserServiceImpl.create(..))")
    public void userCreationPointcut() {}

    @AfterReturning(pointcut = "userCreationPointcut()", returning = "user")
    public void afterUserCreation(User user) {
        Category commonCategory = new Category(
                null,
                user,
                "Common",
                CategoryType.COMMON,
                "This category accumulates all types of transactions, including both income and expense. It is protected from modification or deletion."
        );
        categoryService.save(commonCategory);

        Category otherCategory = new Category(
                null,
                user,
                "Service",
                CategoryType.SERVICE,
                "This category refers to operations performed by the service. It is protected from modification or deletion."
        );
        categoryService.save(otherCategory);
    }
}
