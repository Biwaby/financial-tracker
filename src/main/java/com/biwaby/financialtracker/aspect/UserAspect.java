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
    public void userCreationServicePointcut() {}

    @AfterReturning(pointcut = "userCreationServicePointcut()", returning = "user")
    public void afterUserCreation(User user) {
        Category category = new Category(
                null,
                user,
                "Common",
                CategoryType.BOTH,
                "This category accumulates all types of transactions, including both income and expense, providing a single place for accounting. It is protected from deletion to keep your financial records up to date."
        );
        categoryService.save(category);
    }
}
