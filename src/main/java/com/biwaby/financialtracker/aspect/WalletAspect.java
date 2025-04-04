package com.biwaby.financialtracker.aspect;

import com.biwaby.financialtracker.context.WalletOperationContext;
import com.biwaby.financialtracker.entity.Category;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.entity.WalletTransaction;
import com.biwaby.financialtracker.enums.WalletTransactionType;
import com.biwaby.financialtracker.service.CategoryService;
import com.biwaby.financialtracker.service.WalletService;
import com.biwaby.financialtracker.service.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
@Aspect
@RequiredArgsConstructor
public class WalletAspect {

    private final WalletTransactionService walletTransactionService;
    private final WalletService walletService;
    private final CategoryService categoryService;
    private final ThreadLocal<WalletOperationContext> walletContext = ThreadLocal.withInitial(WalletOperationContext::new);
    private static final String SERVICE_CATEGORY_NAME = "Service";

    private void createAndSaveTransaction(
            Wallet wallet,
            Category category,
            String name,
            WalletTransactionType type,
            BigDecimal amount,
            String description
    ) {
        WalletTransaction transaction = new WalletTransaction(
                null,
                wallet.getUser(),
                wallet,
                category,
                name,
                type,
                amount,
                LocalDateTime.now(),
                description
        );
        walletTransactionService.save(transaction);
    }

    @Pointcut("execution(public * com.biwaby.financialtracker.service.impl.WalletServiceImpl.update(..))")
    public void walletUpdatingPointcut() {}

    @Before("walletUpdatingPointcut()")
    public void beforeWalletUpdating(JoinPoint joinPoint) {
        List<Object> args = List.of(joinPoint.getArgs());
        if (!args.isEmpty() && args.getFirst() instanceof Long walletId) {
            Wallet wallet = walletService.getById(walletId);
            walletContext.get().setOldBalance(wallet.getBalance());
        }
    }

    @AfterReturning(pointcut = "walletUpdatingPointcut()", returning = "updatedWallet")
    public void afterBalanceUpdating(Wallet updatedWallet) {
        if (
                Objects.nonNull(updatedWallet.getBalance()) &&
                Objects.nonNull(walletContext.get().getOldBalance()) &&
                updatedWallet.getBalance().compareTo(walletContext.get().getOldBalance()) != 0
        ) {
            BigDecimal diff = updatedWallet.getBalance().subtract(walletContext.get().getOldBalance());
            WalletTransactionType type = diff.compareTo(BigDecimal.ZERO) > 0 ? WalletTransactionType.INCOME : WalletTransactionType.EXPENSE;
            createAndSaveTransaction(
                    updatedWallet,
                    categoryService.getByName(SERVICE_CATEGORY_NAME),
                    "Updating balance",
                    type,
                    diff.abs(),
                    "Manually updating the wallet balance from <%s %s> to <%s %s>"
                            .formatted(walletContext.get().getOldBalance(), updatedWallet.getCurrency().getCode(), updatedWallet.getBalance(), updatedWallet.getCurrency().getCode())
            );
        }
        walletContext.remove();
    }

    @Pointcut("execution(public * com.biwaby.financialtracker.service.impl.WalletServiceImpl.deposit(..))")
    public void walletDepositPointcut() {}

    @Before("walletDepositPointcut()")
    public void beforeWalletDeposit(JoinPoint joinPoint) {
        List<Object> args = List.of(joinPoint.getArgs());
        if (!args.isEmpty() && args.get(1) instanceof Long depositCategoryId && args.getLast() instanceof BigDecimal amount) {
            Category category = categoryService.getById(depositCategoryId);
            walletContext.get().setDepositCategory(category);
            walletContext.get().setDepositAmount(amount);
        }
    }

    @AfterReturning(pointcut = "walletDepositPointcut()", returning = "wallet")
    public void afterWalletDepositing(Wallet wallet) {
        if (
                Objects.nonNull(wallet) &&
                Objects.nonNull(walletContext.get().getDepositCategory()) &&
                Objects.nonNull(walletContext.get().getDepositAmount())
        ) {
            createAndSaveTransaction(
                    wallet,
                    walletContext.get().getDepositCategory(),
                    "Deposit",
                    WalletTransactionType.INCOME,
                    walletContext.get().getDepositAmount(),
                    "Depositing funds to the wallet."
            );
        }
        walletContext.remove();
    }

    @Pointcut("execution(public * com.biwaby.financialtracker.service.impl.WalletServiceImpl.withdraw(..))")
    public void walletWithdrawPointcut() {}

    @Before("walletWithdrawPointcut()")
    public void beforeWalletWithdraw(JoinPoint joinPoint) {
        List<Object> args = List.of(joinPoint.getArgs());
        if (!args.isEmpty() && args.get(1) instanceof Long withdrawCategoryId && args.getLast() instanceof BigDecimal amount) {
            Category category = categoryService.getById(withdrawCategoryId);
            walletContext.get().setWithdrawCategory(category);
            walletContext.get().setWithdrawAmount(amount);
        }
    }

    @AfterReturning(pointcut = "walletWithdrawPointcut()", returning = "wallet")
    public void afterWalletWithdraw(Wallet wallet) {
        if (
                Objects.nonNull(wallet) &&
                Objects.nonNull(walletContext.get().getWithdrawCategory()) &&
                Objects.nonNull(walletContext.get().getWithdrawAmount())
        ) {
            createAndSaveTransaction(
                    wallet,
                    walletContext.get().getWithdrawCategory(),
                    "Withdraw",
                    WalletTransactionType.EXPENSE,
                    walletContext.get().getWithdrawAmount(),
                    "Withdrawing funds to the wallet."
            );
        }
        walletContext.remove();
    }

    @Pointcut("execution(public * com.biwaby.financialtracker.service.impl.WalletServiceImpl.transfer(..))")
    public void walletTransferPointcut() {}

    @Before("walletTransferPointcut()")
    public void beforeWalletTransfer(JoinPoint joinPoint) {
        List<Object> args = List.of(joinPoint.getArgs());
        if (!args.isEmpty() && args.get(0) instanceof Long && args.get(1) instanceof Long && args.getLast() instanceof BigDecimal amount) {
            walletContext.get().setTransferAmount(amount);
        }
    }

    @AfterReturning(pointcut = "walletTransferPointcut()", returning = "walletPair")
    public void afterWalletTransfer(Pair<Wallet, Wallet> walletPair) {
        if (
                Objects.nonNull(walletPair) &&
                Objects.nonNull(walletContext.get().getTransferAmount())
        ) {
            Wallet senderWallet = walletPair.getFirst();
            Wallet targetWallet = walletPair.getSecond();

            createAndSaveTransaction(
                    senderWallet,
                    categoryService.getByName(SERVICE_CATEGORY_NAME),
                    "Transfer to wallet <%s>".formatted(targetWallet.getName()),
                    WalletTransactionType.TRANSFER_EXPENSE,
                    walletContext.get().getTransferAmount(),
                    "Transfer funds to the wallet with name <%s>".formatted(targetWallet.getName())
            );

            createAndSaveTransaction(
                    targetWallet,
                    categoryService.getByName(SERVICE_CATEGORY_NAME),
                    "Transfer from wallet <%s>".formatted(senderWallet.getName()),
                    WalletTransactionType.TRANSFER_INCOME,
                    walletContext.get().getTransferAmount(),
                    "Receiving a transfer from wallet with name <%s>".formatted(senderWallet.getName())
            );
        }
        walletContext.remove();
    }
}
