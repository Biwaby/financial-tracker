package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.read.UserReadDto;
import com.biwaby.financialtracker.entity.Role;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.mapper.UserMapper;
import com.biwaby.financialtracker.repository.*;
import com.biwaby.financialtracker.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private WalletTransactionRepository walletTransactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private LimitRepository limitRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private SavingsTransactionRepository savingsTransactionRepository;

    @Mock
    private SavingsAccountRepository savingsAccountRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private static final String ANONYMOUS_USERNAME = "anonymousUser";

    private static final User EXISTING_USER_ENTITY = new User(
            1L,
            "testUser",
            "paSSword228",
            new Role(null, null, new ArrayList<>()),
            LocalDateTime.now(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
    );
    
    private static final UserReadDto EXISTING_USER_READ_DTO = new UserReadDto(
            EXISTING_USER_ENTITY.getId(),
            EXISTING_USER_ENTITY.getUsername(),
            EXISTING_USER_ENTITY.getRole().getName(),
            EXISTING_USER_ENTITY.getRegisteredAt()
    );

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void create_newUser_createsUser_returnsUserEntity() {
        doReturn(Boolean.FALSE).when(userRepository).existsByUsername(EXISTING_USER_ENTITY.getUsername());
        doReturn(EXISTING_USER_ENTITY).when(userRepository).save(EXISTING_USER_ENTITY);

        User createdUser = userService.create(EXISTING_USER_ENTITY);

        assertNotNull(createdUser);
        assertThat(createdUser).isEqualTo(EXISTING_USER_ENTITY);
        verify(userRepository, times(1)).existsByUsername(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void create_alreadyExistsByUsername_throwsException() {
        doReturn(Boolean.TRUE).when(userRepository).existsByUsername(EXISTING_USER_ENTITY.getUsername());

        assertThatThrownBy(() -> userService.create(EXISTING_USER_ENTITY))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_USER_ENTITY.getUsername());
        verify(userRepository, times(1)).existsByUsername(any(String.class));
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getEntityById_userExists_returnsUserEntity() {
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findById(EXISTING_USER_ENTITY.getId());

        User foundUser = userService.getEntityById(EXISTING_USER_ENTITY.getId());

        assertNotNull(foundUser);
        assertThat(foundUser).isEqualTo(EXISTING_USER_ENTITY);
        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getEntityById_userNotExists_throwsException() {
        doReturn(Optional.empty()).when(userRepository).findById(EXISTING_USER_ENTITY.getId());

        assertThatThrownBy(() -> userService.getDtoById(EXISTING_USER_ENTITY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_USER_ENTITY.getId().toString());
        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getEntityByUsername_userExists_returnsUserEntity() {
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());

        User foundUser = userService.getEntityByUsername(EXISTING_USER_ENTITY.getUsername());

        assertNotNull(foundUser);
        assertThat(foundUser).isEqualTo(EXISTING_USER_ENTITY);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getEntityByUsername_userNotExists_throwsException() {
        doReturn(Optional.empty()).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());

        assertThatThrownBy(() -> userService.getEntityByUsername(EXISTING_USER_ENTITY.getUsername()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_USER_ENTITY.getUsername());
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void getSelfEntity_userAuthorized_returnsUserEntity() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(EXISTING_USER_ENTITY.getUsername()).when(authentication).getName();
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());

        User foundUser = userService.getSelfEntity();

        assertNotNull(foundUser);
        assertThat(foundUser).isEqualTo(EXISTING_USER_ENTITY);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication);
    }

    @Test
    public void getSelfEntity_userNotAuthorized_throwsException() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(ANONYMOUS_USERNAME).when(authentication).getName();
        doReturn(Optional.empty()).when(userRepository).findByUsername(ANONYMOUS_USERNAME);

        assertThatThrownBy(() -> userService.getSelfEntity())
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(ANONYMOUS_USERNAME);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication);
    }

    @Test
    public void getDtoById_userExists_returnsUserReadDto() {
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findById(EXISTING_USER_ENTITY.getId());
        doReturn(EXISTING_USER_READ_DTO).when(userMapper).toDto(EXISTING_USER_ENTITY);

        UserReadDto foundUser = userService.getDtoById(EXISTING_USER_ENTITY.getId());

        assertNotNull(foundUser);
        assertThat(foundUser).isEqualTo(EXISTING_USER_READ_DTO);
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userMapper, times(1)).toDto(any(User.class));
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    public void getDtoById_userNotExists_throwsException() {
        doReturn(Optional.empty()).when(userRepository).findById(EXISTING_USER_ENTITY.getId());

        assertThatThrownBy(() -> userService.getDtoById(EXISTING_USER_ENTITY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_USER_ENTITY.getId().toString());
        verify(userRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void getDtoByUsername_userExists_returnsUserReadDto() {
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());
        doReturn(EXISTING_USER_READ_DTO).when(userMapper).toDto(EXISTING_USER_ENTITY);

        UserReadDto foundUser = userService.getDtoByUsername(EXISTING_USER_ENTITY.getUsername());

        assertNotNull(foundUser);
        assertThat(foundUser).isEqualTo(EXISTING_USER_READ_DTO);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userMapper, times(1)).toDto(any(User.class));
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    public void getDtoByUsername_userNotExists_throwsException() {
        doReturn(Optional.empty()).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());

        assertThatThrownBy(() -> userService.getDtoByUsername(EXISTING_USER_ENTITY.getUsername()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_USER_ENTITY.getUsername());
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void getSelfDto_userAuthorized_returnsUserReadDto() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(EXISTING_USER_ENTITY.getUsername()).when(authentication).getName();
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());
        doReturn(EXISTING_USER_READ_DTO).when(userMapper).toDto(EXISTING_USER_ENTITY);

        UserReadDto foundUser = userService.getSelfDto();

        assertNotNull(foundUser);
        assertThat(foundUser).isEqualTo(EXISTING_USER_READ_DTO);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userMapper, times(1)).toDto(any(User.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication, userMapper);
    }

    @Test
    public void getSelfDto_userNotAuthorized_throwsException() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(ANONYMOUS_USERNAME).when(authentication).getName();
        doReturn(Optional.empty()).when(userRepository).findByUsername(ANONYMOUS_USERNAME);

        assertThatThrownBy(() -> userService.getSelfDto())
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(ANONYMOUS_USERNAME);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void getAll_returnsAllFoundedUsersReadDto() {
        doReturn(new PageImpl<>(List.of(EXISTING_USER_ENTITY))).when(userRepository).findAll(any(Pageable.class));
        doReturn(EXISTING_USER_READ_DTO).when(userMapper).toDto(EXISTING_USER_ENTITY);

        List<UserReadDto> users = userService.getAll(5, 0);

        assertNotNull(users);
        assertThat(users).hasSize(1);
        assertThat(users.getFirst().getId()).isEqualTo(EXISTING_USER_ENTITY.getId());
        verify(userRepository, times(1)).findAll(any(Pageable.class));
        verify(userMapper, times(1)).toDto(any(User.class));
        verifyNoMoreInteractions(userRepository, userMapper);
    }

    @Test
    public void updateUsername_userAuthorized_returnsUpdatedUserReadDto() {
        String newUsername = "editedTestUser";
        User expectedUserEntity = new User(
                EXISTING_USER_ENTITY.getId(),
                newUsername,
                EXISTING_USER_ENTITY.getPassword(),
                EXISTING_USER_ENTITY.getRole(),
                EXISTING_USER_ENTITY.getRegisteredAt(),
                EXISTING_USER_ENTITY.getCategories(),
                EXISTING_USER_ENTITY.getLimits(),
                EXISTING_USER_ENTITY.getWalletTransactions(),
                EXISTING_USER_ENTITY.getWallets(),
                EXISTING_USER_ENTITY.getSavingsTransactions(),
                EXISTING_USER_ENTITY.getSavingsAccounts()
        );
        UserReadDto expectedUserDto = new UserReadDto(
                EXISTING_USER_READ_DTO.getId(),
                newUsername,
                EXISTING_USER_READ_DTO.getRoleName(),
                EXISTING_USER_READ_DTO.getRegisteredAt()
        );
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(EXISTING_USER_ENTITY.getUsername()).when(authentication).getName();
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());
        doReturn(Boolean.FALSE).when(userRepository).existsByUsername(newUsername);
        doReturn(expectedUserEntity).when(userRepository).save(expectedUserEntity);
        doReturn(expectedUserDto).when(userMapper).toDto(expectedUserEntity);

        UserReadDto updatedUser = userService.updateUsername(newUsername);

        assertNotNull(updatedUser);
        assertThat(updatedUser).isEqualTo(expectedUserDto);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).existsByUsername(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDto(any(User.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication, userMapper);
    }

    @Test
    public void updateUsername_userNotAuthorized_throwsException() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(ANONYMOUS_USERNAME).when(authentication).getName();
        doReturn(Optional.empty()).when(userRepository).findByUsername(ANONYMOUS_USERNAME);

        assertThatThrownBy(() -> userService.updateUsername(null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(ANONYMOUS_USERNAME);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void updateUsername_userWithUsernameAlreadyExists_throwsException() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(EXISTING_USER_ENTITY.getUsername()).when(authentication).getName();
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());
        doReturn(Boolean.TRUE).when(userRepository).existsByUsername(EXISTING_USER_ENTITY.getUsername());

        assertThatThrownBy(() -> userService.updateUsername(EXISTING_USER_ENTITY.getUsername()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_USER_ENTITY.getUsername());
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void updatePassword_userAuthorized_returnsUpdatedUserReadDto() {
        String newPassword = "paZZword330";
        User expectedUserEntity = new User(
                EXISTING_USER_ENTITY.getId(),
                EXISTING_USER_ENTITY.getUsername(),
                newPassword,
                EXISTING_USER_ENTITY.getRole(),
                EXISTING_USER_ENTITY.getRegisteredAt(),
                EXISTING_USER_ENTITY.getCategories(),
                EXISTING_USER_ENTITY.getLimits(),
                EXISTING_USER_ENTITY.getWalletTransactions(),
                EXISTING_USER_ENTITY.getWallets(),
                EXISTING_USER_ENTITY.getSavingsTransactions(),
                EXISTING_USER_ENTITY.getSavingsAccounts()
        );
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(EXISTING_USER_ENTITY.getUsername()).when(authentication).getName();
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());
        doReturn(expectedUserEntity).when(userRepository).save(expectedUserEntity);
        doReturn(EXISTING_USER_READ_DTO).when(userMapper).toDto(expectedUserEntity);

        UserReadDto updatedUser = userService.updatePassword(newPassword);

        assertNotNull(updatedUser);
        assertThat(updatedUser).isEqualTo(EXISTING_USER_READ_DTO);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDto(any(User.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication, userMapper);
    }

    @Test
    public void updatePassword_userNotAuthorized_throwsException() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(ANONYMOUS_USERNAME).when(authentication).getName();
        doReturn(Optional.empty()).when(userRepository).findByUsername(ANONYMOUS_USERNAME);

        assertThatThrownBy(() -> userService.updatePassword(null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(ANONYMOUS_USERNAME);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication);
        verifyNoInteractions(userMapper);
    }

    @Test
    public void updateUserRole_userExists_returnsUpdatedUserReadDto() {
        Role newRole = new Role(
                null,
                "EDITED_TEST",
                new ArrayList<>()
        );
        User expectedUserEntity = new User(
                EXISTING_USER_ENTITY.getId(),
                EXISTING_USER_ENTITY.getUsername(),
                EXISTING_USER_ENTITY.getPassword(),
                newRole,
                EXISTING_USER_ENTITY.getRegisteredAt(),
                EXISTING_USER_ENTITY.getCategories(),
                EXISTING_USER_ENTITY.getLimits(),
                EXISTING_USER_ENTITY.getWalletTransactions(),
                EXISTING_USER_ENTITY.getWallets(),
                EXISTING_USER_ENTITY.getSavingsTransactions(),
                EXISTING_USER_ENTITY.getSavingsAccounts()
        );
        UserReadDto expectedUserDto = new UserReadDto(
                EXISTING_USER_READ_DTO.getId(),
                EXISTING_USER_READ_DTO.getUsername(),
                newRole.getName(),
                EXISTING_USER_READ_DTO.getRegisteredAt()
        );
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findById(EXISTING_USER_ENTITY.getId());
        doReturn(Optional.of(newRole)).when(roleRepository).findByName(newRole.getName());
        doReturn(expectedUserEntity).when(userRepository).save(expectedUserEntity);
        doReturn(expectedUserDto).when(userMapper).toDto(expectedUserEntity);

        UserReadDto updatedUser = userService.updateUserRole(EXISTING_USER_ENTITY.getId(), newRole.getName());

        assertNotNull(updatedUser);
        assertThat(updatedUser).isEqualTo(expectedUserDto);
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(roleRepository, times(1)).findByName(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).toDto(any(User.class));
        verifyNoMoreInteractions(userRepository, roleRepository, userMapper);
    }

    @Test
    public void updateUserRole_userNotExists_throwsException() {
        doReturn(Optional.empty()).when(userRepository).findById(EXISTING_USER_ENTITY.getId());

        assertThatThrownBy(() -> userService.updateUserRole(EXISTING_USER_ENTITY.getId(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_USER_ENTITY.getId().toString());
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(roleRepository, userMapper);
    }

    @Test
    public void deleteById_userExists_deletesUser() {
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findById(EXISTING_USER_ENTITY.getId());
        doNothing().when(walletTransactionRepository).deleteAll(EXISTING_USER_ENTITY.getWalletTransactions());
        doNothing().when(categoryRepository).deleteAll(EXISTING_USER_ENTITY.getCategories());
        doNothing().when(limitRepository).deleteAll(EXISTING_USER_ENTITY.getLimits());
        doNothing().when(walletRepository).deleteAll(EXISTING_USER_ENTITY.getWallets());
        doNothing().when(savingsTransactionRepository).deleteAll(EXISTING_USER_ENTITY.getSavingsTransactions());
        doNothing().when(savingsAccountRepository).deleteAll(EXISTING_USER_ENTITY.getSavingsAccounts());
        doNothing().when(userRepository).delete(EXISTING_USER_ENTITY);

        userService.deleteById(EXISTING_USER_ENTITY.getId());

        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userRepository, times(1)).delete(any(User.class));
        assertThat(EXISTING_USER_ENTITY.getWalletTransactions()).isEmpty();
        verify(walletTransactionRepository, times(1)).deleteAll(any(List.class));
        assertThat(EXISTING_USER_ENTITY.getCategories()).isEmpty();
        verify(categoryRepository, times(1)).deleteAll(any(List.class));
        assertThat(EXISTING_USER_ENTITY.getLimits()).isEmpty();
        verify(limitRepository, times(1)).deleteAll(any(List.class));
        EXISTING_USER_ENTITY.getWallets().forEach(wallet -> assertThat(wallet).isNotIn(wallet.getCurrency().getWalletsWithCurrency()));
        assertThat(EXISTING_USER_ENTITY.getWallets()).isEmpty();
        verify(walletRepository, times(1)).deleteAll(any(List.class));
        assertThat(EXISTING_USER_ENTITY.getSavingsTransactions()).isEmpty();
        verify(savingsTransactionRepository, times(1)).deleteAll(any(List.class));
        EXISTING_USER_ENTITY.getSavingsAccounts().forEach(account -> assertThat(account).isNotIn(account.getCurrency().getSavingsAccountsWithCurrency()));
        assertThat(EXISTING_USER_ENTITY.getSavingsAccounts()).isEmpty();
        verify(savingsAccountRepository, times(1)).deleteAll(any(List.class));
        verifyNoMoreInteractions(
                userRepository,
                walletTransactionRepository,
                categoryRepository,
                limitRepository,
                walletRepository,
                savingsTransactionRepository,
                savingsAccountRepository
        );
    }

    @Test
    public void deleteById_userNotExists_throwsException() {
        doReturn(Optional.empty()).when(userRepository).findById(EXISTING_USER_ENTITY.getId());

        assertThatThrownBy(() -> userService.deleteById(EXISTING_USER_ENTITY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_USER_ENTITY.getId().toString());
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(userRepository, never()).delete(any(User.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(
                walletTransactionRepository,
                categoryRepository,
                limitRepository,
                walletRepository,
                savingsTransactionRepository,
                savingsAccountRepository
        );
    }

    @Test
    public void deleteByUsername_userExists_deletesUser() {
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());
        doNothing().when(walletTransactionRepository).deleteAll(EXISTING_USER_ENTITY.getWalletTransactions());
        doNothing().when(categoryRepository).deleteAll(EXISTING_USER_ENTITY.getCategories());
        doNothing().when(limitRepository).deleteAll(EXISTING_USER_ENTITY.getLimits());
        doNothing().when(walletRepository).deleteAll(EXISTING_USER_ENTITY.getWallets());
        doNothing().when(savingsTransactionRepository).deleteAll(EXISTING_USER_ENTITY.getSavingsTransactions());
        doNothing().when(savingsAccountRepository).deleteAll(EXISTING_USER_ENTITY.getSavingsAccounts());
        doNothing().when(userRepository).delete(EXISTING_USER_ENTITY);

        userService.deleteByUsername(EXISTING_USER_ENTITY.getUsername());

        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).delete(any(User.class));
        assertThat(EXISTING_USER_ENTITY.getWalletTransactions()).isEmpty();
        verify(walletTransactionRepository, times(1)).deleteAll(any(List.class));
        assertThat(EXISTING_USER_ENTITY.getCategories()).isEmpty();
        verify(categoryRepository, times(1)).deleteAll(any(List.class));
        assertThat(EXISTING_USER_ENTITY.getLimits()).isEmpty();
        verify(limitRepository, times(1)).deleteAll(any(List.class));
        EXISTING_USER_ENTITY.getWallets().forEach(wallet -> assertThat(wallet).isNotIn(wallet.getCurrency().getWalletsWithCurrency()));
        assertThat(EXISTING_USER_ENTITY.getWallets()).isEmpty();
        verify(walletRepository, times(1)).deleteAll(any(List.class));
        assertThat(EXISTING_USER_ENTITY.getSavingsTransactions()).isEmpty();
        verify(savingsTransactionRepository, times(1)).deleteAll(any(List.class));
        EXISTING_USER_ENTITY.getSavingsAccounts().forEach(account -> assertThat(account).isNotIn(account.getCurrency().getSavingsAccountsWithCurrency()));
        assertThat(EXISTING_USER_ENTITY.getSavingsAccounts()).isEmpty();
        verify(savingsAccountRepository, times(1)).deleteAll(any(List.class));
        verifyNoMoreInteractions(
                userRepository,
                walletTransactionRepository,
                categoryRepository,
                limitRepository,
                walletRepository,
                savingsTransactionRepository,
                savingsAccountRepository
        );
    }

    @Test
    public void deleteByUsername_userNotExists_throwsException() {
        doReturn(Optional.empty()).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());

        assertThatThrownBy(() -> userService.deleteByUsername(EXISTING_USER_ENTITY.getUsername()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_USER_ENTITY.getUsername());
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, never()).delete(any(User.class));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(
                walletTransactionRepository,
                categoryRepository,
                limitRepository,
                walletRepository,
                savingsTransactionRepository,
                savingsAccountRepository
        );
    }

    @Test
    public void deleteSelf_userAuthorized_deletesUser() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(EXISTING_USER_ENTITY.getUsername()).when(authentication).getName();
        doReturn(Optional.of(EXISTING_USER_ENTITY)).when(userRepository).findByUsername(EXISTING_USER_ENTITY.getUsername());
        doNothing().when(walletTransactionRepository).deleteAll(EXISTING_USER_ENTITY.getWalletTransactions());
        doNothing().when(categoryRepository).deleteAll(EXISTING_USER_ENTITY.getCategories());
        doNothing().when(limitRepository).deleteAll(EXISTING_USER_ENTITY.getLimits());
        doNothing().when(walletRepository).deleteAll(EXISTING_USER_ENTITY.getWallets());
        doNothing().when(savingsTransactionRepository).deleteAll(EXISTING_USER_ENTITY.getSavingsTransactions());
        doNothing().when(savingsAccountRepository).deleteAll(EXISTING_USER_ENTITY.getSavingsAccounts());
        doNothing().when(userRepository).delete(EXISTING_USER_ENTITY);

        userService.deleteSelf();

        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).delete(any(User.class));
        assertThat(EXISTING_USER_ENTITY.getWalletTransactions()).isEmpty();
        verify(walletTransactionRepository, times(1)).deleteAll(any(List.class));
        assertThat(EXISTING_USER_ENTITY.getCategories()).isEmpty();
        verify(categoryRepository, times(1)).deleteAll(any(List.class));
        assertThat(EXISTING_USER_ENTITY.getLimits()).isEmpty();
        verify(limitRepository, times(1)).deleteAll(any(List.class));
        EXISTING_USER_ENTITY.getWallets().forEach(wallet -> assertThat(wallet).isNotIn(wallet.getCurrency().getWalletsWithCurrency()));
        assertThat(EXISTING_USER_ENTITY.getWallets()).isEmpty();
        verify(walletRepository, times(1)).deleteAll(any(List.class));
        assertThat(EXISTING_USER_ENTITY.getSavingsTransactions()).isEmpty();
        verify(savingsTransactionRepository, times(1)).deleteAll(any(List.class));
        EXISTING_USER_ENTITY.getSavingsAccounts().forEach(account -> assertThat(account).isNotIn(account.getCurrency().getSavingsAccountsWithCurrency()));
        assertThat(EXISTING_USER_ENTITY.getSavingsAccounts()).isEmpty();
        verify(savingsAccountRepository, times(1)).deleteAll(any(List.class));
        verifyNoMoreInteractions(
                securityContext,
                authentication,
                userRepository,
                walletTransactionRepository,
                categoryRepository,
                limitRepository,
                walletRepository,
                savingsTransactionRepository,
                savingsAccountRepository
        );
    }

    @Test
    public void deleteSelf_userNotAuthorized_throwsException() {
        doReturn(authentication).when(securityContext).getAuthentication();
        doReturn(ANONYMOUS_USERNAME).when(authentication).getName();
        doReturn(Optional.empty()).when(userRepository).findByUsername(ANONYMOUS_USERNAME);

        assertThatThrownBy(() -> userService.deleteSelf())
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(ANONYMOUS_USERNAME);
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, never()).delete(any(User.class));
        verifyNoMoreInteractions(userRepository, securityContext, authentication);
        verifyNoInteractions(
                walletTransactionRepository,
                categoryRepository,
                limitRepository,
                walletRepository,
                savingsTransactionRepository,
                savingsAccountRepository
        );
    }
}
