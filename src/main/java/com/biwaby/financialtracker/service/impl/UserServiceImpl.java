package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.read.UserReadDto;
import com.biwaby.financialtracker.entity.*;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.mapper.UserMapper;
import com.biwaby.financialtracker.repository.*;
import com.biwaby.financialtracker.service.UserService;
import com.biwaby.financialtracker.util.PasswordEncoderUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final CategoryRepository categoryRepository;
    private final LimitRepository limitRepository;
    private final WalletRepository walletRepository;
    private final SavingsTransactionRepository savingsTransactionRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "User with username <%s> already exists".formatted(user.getUsername())
            );
        }
        Role userRole = user.getRole();
        userRole.getUsersWithRole().add(user);
        return save(user);
    }

    @Override
    public User getEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "User with id <%s> is not found".formatted(id)
                )
        );
    }

    @Override
    public User getEntityByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "User with username <%s> is not found".formatted(username)
                )
        );
    }

    @Override
    public User getSelfEntity() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getEntityByUsername(username);
    }

    @Override
    public UserReadDto getDtoById(Long id) {
        return userMapper.toDto(getEntityById(id));
    }

    @Override
    public UserReadDto getDtoByUsername(String username) {
        return userMapper.toDto(getEntityByUsername(username));
    }

    @Override
    public UserReadDto getSelfDto() {
        return userMapper.toDto(getSelfEntity());
    }

    @Override
    public List<UserReadDto> getAll(Integer pageSize, Integer pageNumber) {
        return userRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .get()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserReadDto updateUsername(String username) {
        User userToUpdate = getSelfEntity();
        if (username != null && !username.trim().isEmpty()) {
            if (userRepository.existsByUsername(username)) {
                throw new ResponseException(
                        HttpStatus.BAD_REQUEST.value(),
                        "User with username <%s> already exists".formatted(username)
                );
            }
            userToUpdate.setUsername(username);
        }
        return userMapper.toDto(save(userToUpdate));
    }

    @Override
    @Transactional
    public UserReadDto updatePassword(String password) {
        User userToUpdate = getSelfEntity();
        if (password != null && !password.trim().isEmpty()) {
            userToUpdate.setPassword(PasswordEncoderUtil.getPasswordEncoder().encode(password));
        }
        return userMapper.toDto(save(userToUpdate));
    }

    @Override
    @Transactional
    public UserReadDto updateUserRole(Long userId, String authority) {
        User userToUpdate = getEntityById(userId);
        Role newRole = roleRepository.findByName(authority).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Role with authority <%s> is not found".formatted(authority)
                )
        );
        userToUpdate.setRole(newRole);
        newRole.getUsersWithRole().add(userToUpdate);
        return userMapper.toDto(save(userToUpdate));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User userToDelete = getEntityById(id);
        deleteUserData(userToDelete);
        userToDelete.getRole().getUsersWithRole().remove(userToDelete);
        userRepository.delete(userToDelete);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        User userToDelete = getEntityByUsername(username);
        deleteUserData(userToDelete);
        userToDelete.getRole().getUsersWithRole().remove(userToDelete);
        userRepository.delete(userToDelete);
    }

    @Override
    @Transactional
    public void deleteSelf() {
        User userToDelete = getSelfEntity();
        deleteUserData(userToDelete);
        userToDelete.getRole().getUsersWithRole().remove(userToDelete);
        userRepository.delete(userToDelete);
    }

    private void deleteUserData(User user) {
        List<Category> categories = user.getCategories();
        List<Limit> limits = user.getLimits();
        List<WalletTransaction> walletTransactions = user.getWalletTransactions();
        List<Wallet> wallets = user.getWallets();
        List<SavingsTransaction> savingsTransactions = user.getSavingsTransactions();
        List<SavingsAccount> savingsAccounts = user.getSavingsAccounts();

        walletTransactionRepository.deleteAll(walletTransactions);
        categoryRepository.deleteAll(categories);
        limitRepository.deleteAll(limits);
        wallets.forEach(wallet -> wallet.getCurrency().getWalletsWithCurrency().remove(wallet));
        walletRepository.deleteAll(wallets);
        savingsTransactionRepository.deleteAll(savingsTransactions);
        savingsAccounts.forEach(account -> account.getCurrency().getSavingsAccountsWithCurrency().remove(account));
        savingsAccountRepository.deleteAll(savingsAccounts);
    }

    @Override
    public UserDetailsService getUserDetailsService() {
        return this::getEntityByUsername;
    }
}
