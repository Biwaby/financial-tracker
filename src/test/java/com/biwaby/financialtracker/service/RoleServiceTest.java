package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.entity.Role;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.RoleRepository;
import com.biwaby.financialtracker.repository.UserRepository;
import com.biwaby.financialtracker.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private static final Role USER_DEFAULT_ROLE = new Role(
            1L,
            "USER",
            new ArrayList<>()
    );

    private static final Role EXISTING_ROLE = new Role(
            2L,
            "TEST",
            List.of(new User())
    );

    @Test
    public void create_newRole_createsRole() {
        doReturn(Boolean.FALSE).when(roleRepository).existsByName(EXISTING_ROLE.getName());
        doReturn(EXISTING_ROLE).when(roleRepository).save(EXISTING_ROLE);

        Role createdRole = roleService.create(EXISTING_ROLE);

        assertNotNull(createdRole);
        assertThat(createdRole).isEqualTo(EXISTING_ROLE);
        verify(roleRepository, times(1)).existsByName(any(String.class));
        verify(roleRepository, times(1)).save(any(Role.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void create_alreadyExistsByName_throwsException() {
        doReturn(Boolean.TRUE).when(roleRepository).existsByName(EXISTING_ROLE.getName());

        assertThatThrownBy(() -> roleService.create(EXISTING_ROLE))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_ROLE.getName());
        verify(roleRepository, times(1)).existsByName(any(String.class));
        verify(roleRepository, never()).save(any(Role.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void getById_roleExists_returnsFoundedRole() {
        doReturn(Optional.of(EXISTING_ROLE)).when(roleRepository).findById(EXISTING_ROLE.getId());

        Role foundRole = roleService.getById(EXISTING_ROLE.getId());

        assertNotNull(foundRole);
        assertThat(foundRole).isEqualTo(EXISTING_ROLE);
        verify(roleRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void getById_roleNotExists_throwsException() {
        doReturn(Optional.empty()).when(roleRepository).findById(EXISTING_ROLE.getId());

        assertThatThrownBy(() -> roleService.getById(EXISTING_ROLE.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_ROLE.getId().toString());
        verify(roleRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void getByAuthority_roleExists_returnsRole() {
        doReturn(Optional.of(EXISTING_ROLE)).when(roleRepository).findByName(EXISTING_ROLE.getName());

        Role foundRole = roleService.getByAuthority(EXISTING_ROLE.getName());

        assertNotNull(foundRole);
        assertThat(foundRole).isEqualTo(EXISTING_ROLE);
        verify(roleRepository, times(1)).findByName(any(String.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void getByAuthority_roleNotExists_throwsException() {
        doReturn(Optional.empty()).when(roleRepository).findByName(EXISTING_ROLE.getName());

        assertThatThrownBy(() -> roleService.getByAuthority(EXISTING_ROLE.getName()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_ROLE.getName());
        verify(roleRepository, times(1)).findByName(any(String.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void getAll_returnsAllRoles() {
        doReturn(List.of(EXISTING_ROLE)).when(roleRepository).findAll();

        List<Role> roles = roleService.getAll();

        assertNotNull(roles);
        assertThat(roles).hasSize(1);
        assertThat(roles.getFirst().getId()).isEqualTo(EXISTING_ROLE.getId());
        verify(roleRepository, times(1)).findAll();
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void update_roleExists_returnsUpdatedRole() {
        Role expectedRole = new Role(2L, "TEST_EDITED", new ArrayList<>());
        doReturn(Optional.of(EXISTING_ROLE)).when(roleRepository).findById(EXISTING_ROLE.getId());
        doReturn(Boolean.FALSE).when(roleRepository).existsByName(expectedRole.getName());
        doReturn(expectedRole).when(roleRepository).save(expectedRole);

        Role updatedRole = roleService.update(EXISTING_ROLE.getId(), expectedRole);

        assertNotNull(updatedRole);
        assertThat(updatedRole).isEqualTo(expectedRole);
        verify(roleRepository, times(1)).findById(any(Long.class));
        verify(roleRepository, times(1)).existsByName(any(String.class));
        verify(roleRepository, times(1)).save(any(Role.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void update_roleNotExists_throwsException() {
        doReturn(Optional.empty()).when(roleRepository).findById(EXISTING_ROLE.getId());

        assertThatThrownBy(() -> roleService.update(EXISTING_ROLE.getId(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_ROLE.getId().toString());
        verify(roleRepository, times(1)).findById(any(Long.class));
        verify(roleRepository, never()).save(any(Role.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void update_roleEqualsUserDefaultRole_throwsException() {
        doReturn(Optional.of(USER_DEFAULT_ROLE)).when(roleRepository).findById(USER_DEFAULT_ROLE.getId());

        assertThatThrownBy(() -> roleService.update(USER_DEFAULT_ROLE.getId(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(USER_DEFAULT_ROLE.getName());
        verify(roleRepository, times(1)).findById(any(Long.class));
        verify(roleRepository, never()).save(any(Role.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void update_alreadyExistsByName_throwsException() {
        Role expectedRole = new Role(null, EXISTING_ROLE.getName(), new ArrayList<>());
        doReturn(Optional.of(EXISTING_ROLE)).when(roleRepository).findById(EXISTING_ROLE.getId());
        doReturn(Boolean.TRUE).when(roleRepository).existsByName(EXISTING_ROLE.getName());

        assertThatThrownBy(() -> roleService.update(EXISTING_ROLE.getId(), expectedRole))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_ROLE.getName());
        verify(roleRepository, times(1)).findById(any(Long.class));
        verify(roleRepository, times(1)).existsByName(any(String.class));
        verify(roleRepository, never()).save(any(Role.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void deleteById_roleExists_deletesRole() {
        doReturn(Optional.of(EXISTING_ROLE)).when(roleRepository).findById(EXISTING_ROLE.getId());
        doReturn(Optional.of(USER_DEFAULT_ROLE)).when(roleRepository).findByName(USER_DEFAULT_ROLE.getName());
        doReturn(List.of()).when(userRepository).saveAll(EXISTING_ROLE.getUsersWithRole());
        doNothing().when(roleRepository).delete(EXISTING_ROLE);

        roleService.deleteById(EXISTING_ROLE.getId());

        verify(roleRepository, times(1)).findById(any(Long.class));
        verify(roleRepository, times(1)).delete(any(Role.class));
        assertThatList(EXISTING_ROLE.getUsersWithRole())
                .extracting(User::getRole)
                .containsOnly(USER_DEFAULT_ROLE);
        verify(userRepository, times(1)).saveAll(any(List.class));
        verifyNoMoreInteractions(roleRepository, userRepository);
    }

    @Test
    public void deleteById_roleNotExists_throwsException() {
        doReturn(Optional.empty()).when(roleRepository).findById(EXISTING_ROLE.getId());

        assertThatThrownBy(() -> roleService.deleteById(EXISTING_ROLE.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_ROLE.getId().toString());
        verify(roleRepository, times(1)).findById(any(Long.class));
        verify(roleRepository, never()).delete(any(Role.class));
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void deleteById_roleEqualsUserDefaultRole_throwsException() {
        doReturn(Optional.of(USER_DEFAULT_ROLE)).when(roleRepository).findById(USER_DEFAULT_ROLE.getId());
        doReturn(Optional.of(USER_DEFAULT_ROLE)).when(roleRepository).findByName(USER_DEFAULT_ROLE.getName());

        assertThatThrownBy(() -> roleService.deleteById(USER_DEFAULT_ROLE.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(USER_DEFAULT_ROLE.getName());
        verify(roleRepository, times(1)).findById(any(Long.class));
        verify(roleRepository, times(1)).findByName(any(String.class));
        verify(roleRepository, never()).delete(any(Role.class));
        verifyNoMoreInteractions(roleRepository);
    }
}
