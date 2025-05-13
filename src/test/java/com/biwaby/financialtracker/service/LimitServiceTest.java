package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.create.LimitCreateDto;
import com.biwaby.financialtracker.dto.update.LimitUpdateDto;
import com.biwaby.financialtracker.entity.Limit;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.enums.LimitType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.LimitRepository;
import com.biwaby.financialtracker.service.impl.LimitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class LimitServiceTest {

    @Mock
    private LimitRepository limitRepository;

    @InjectMocks
    private LimitServiceImpl limitService;

    private static final User TEST_USER = new User();
    private static final Wallet TEST_WALLET = new Wallet();
    private static final Limit EXISTING_LIMIT = new Limit(
            1L,
            TEST_USER,
            TEST_WALLET,
            LimitType.PERMANENT,
            BigDecimal.valueOf(200),
            BigDecimal.ZERO,
            Boolean.FALSE,
            LocalDateTime.now(),
            Boolean.TRUE
    );

    @BeforeEach
    public void setUp() {
        TEST_USER.setLimits(new ArrayList<>());
        TEST_WALLET.setId(1L);
        TEST_WALLET.setWalletLimit(EXISTING_LIMIT);
    }

    @Test
    public void create_newLimit_createsLimit() {
        LimitCreateDto createDto = new LimitCreateDto(
              EXISTING_LIMIT.getType().getDisplayName(),
              EXISTING_LIMIT.getTargetAmount()
        );
        doReturn(Boolean.FALSE).when(limitRepository).existsByWallet(EXISTING_LIMIT.getWallet());
        doReturn(EXISTING_LIMIT).when(limitRepository).save(any(Limit.class));

        Limit createdLimit = limitService.create(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getWallet(), createDto);

        assertNotNull(createdLimit);
        assertThat(createdLimit).isEqualTo(EXISTING_LIMIT);
        verify(limitRepository, times(1)).existsByWallet(any(Wallet.class));
        verify(limitRepository, times(1)).save(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void create_alreadyExistsForWallet_throwsException() {
        doReturn(Boolean.TRUE).when(limitRepository).existsByWallet(EXISTING_LIMIT.getWallet());

        assertThatThrownBy(() -> limitService.create(null, EXISTING_LIMIT.getWallet(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_LIMIT.getWallet().getId().toString());
        verify(limitRepository, times(1)).existsByWallet(any(Wallet.class));
        verify(limitRepository, never()).save(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void create_targetAmountIsNegativeOrEqualToZero_throwsException() {
        LimitCreateDto createDto = new LimitCreateDto(
                EXISTING_LIMIT.getType().getDisplayName(),
                BigDecimal.ZERO.subtract(EXISTING_LIMIT.getTargetAmount())
        );
        doReturn(Boolean.FALSE).when(limitRepository).existsByWallet(EXISTING_LIMIT.getWallet());

        assertThatThrownBy(() -> limitService.create(null, EXISTING_LIMIT.getWallet(), createDto))
                .isInstanceOf(ResponseException.class)
                .hasMessage("The <targetAmount> must be greater than zero");
        verify(limitRepository, times(1)).existsByWallet(any(Wallet.class));
        verify(limitRepository, never()).save(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void getById_limitExists_returnsFoundedLimit() {
        doReturn(Optional.of(EXISTING_LIMIT)).when(limitRepository).findByIdAndUser(EXISTING_LIMIT.getId(), EXISTING_LIMIT.getUser());

        Limit foundLimit = limitService.getById(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getId());

        assertNotNull(foundLimit);
        assertThat(foundLimit).isEqualTo(EXISTING_LIMIT);
        verify(limitRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void getById_limitNotExists_throwsException() {
        doReturn(Optional.empty()).when(limitRepository).findByIdAndUser(EXISTING_LIMIT.getId(), EXISTING_LIMIT.getUser());

        assertThatThrownBy(() -> limitService.getById(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_LIMIT.getId().toString());
        verify(limitRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void getByWallet_limitExists_returnsFoundedLimit() {
        doReturn(Optional.of(EXISTING_LIMIT)).when(limitRepository).findByWalletAndUser(EXISTING_LIMIT.getWallet(), EXISTING_LIMIT.getUser());

        Limit foundLimit = limitService.getByWallet(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getWallet());

        assertNotNull(foundLimit);
        assertThat(foundLimit).isEqualTo(EXISTING_LIMIT);
        verify(limitRepository, times(1)).findByWalletAndUser(any(Wallet.class), any(User.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void getByWallet_limitNotExists_throwsException() {
        doReturn(Optional.empty()).when(limitRepository).findByWalletAndUser(EXISTING_LIMIT.getWallet(), EXISTING_LIMIT.getUser());

        assertThatThrownBy(() -> limitService.getByWallet(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getWallet()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_LIMIT.getWallet().getId().toString());
        verify(limitRepository, times(1)).findByWalletAndUser(any(Wallet.class), any(User.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void getAll_returnsAllFoundedLimits() {
        doReturn(List.of(EXISTING_LIMIT)).when(limitRepository).findAllByUser(EXISTING_LIMIT.getUser());

        List<Limit> limits = limitService.getAll(EXISTING_LIMIT.getUser());

        assertNotNull(limits);
        assertThat(limits).hasSize(1);
        verify(limitRepository, times(1)).findAllByUser(any(User.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void updateForWallet_limitExists_updatesLimit() {
        BigDecimal newCurrentAmount = BigDecimal.valueOf(150);
        Limit expectedLimit = new Limit(
                EXISTING_LIMIT.getId(),
                EXISTING_LIMIT.getUser(),
                EXISTING_LIMIT.getWallet(),
                EXISTING_LIMIT.getType(),
                EXISTING_LIMIT.getTargetAmount(),
                newCurrentAmount,
                EXISTING_LIMIT.getIsExceeded(),
                EXISTING_LIMIT.getCreationDate(),
                EXISTING_LIMIT.getIsActive()
        );
        doReturn(Optional.of(EXISTING_LIMIT)).when(limitRepository).findByWalletAndUser(EXISTING_LIMIT.getWallet(), EXISTING_LIMIT.getUser());
        doReturn(expectedLimit).when(limitRepository).save(any(Limit.class));

        limitService.updateForWallet(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getWallet(), newCurrentAmount);

        verify(limitRepository, times(1)).findByWalletAndUser(any(Wallet.class), any(User.class));
        verify(limitRepository, times(1)).save(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void updateForWallet_limitNotExists_throwsException() {
        doReturn(Optional.empty()).when(limitRepository).findByWalletAndUser(EXISTING_LIMIT.getWallet(), EXISTING_LIMIT.getUser());

        assertThatThrownBy(() -> limitService.updateForWallet(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getWallet(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_LIMIT.getWallet().getId().toString());
        verify(limitRepository, times(1)).findByWalletAndUser(any(Wallet.class), any(User.class));
        verify(limitRepository, never()).save(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void update_limitExists_returnsUpdatedLimit() {
        LimitUpdateDto updateDto = new LimitUpdateDto(
                null,
                null,
                BigDecimal.valueOf(250)
        );
        Limit expectedLimit = new Limit(
                EXISTING_LIMIT.getId(),
                EXISTING_LIMIT.getUser(),
                EXISTING_LIMIT.getWallet(),
                EXISTING_LIMIT.getType(),
                EXISTING_LIMIT.getTargetAmount(),
                updateDto.getCurrentAmount(),
                !EXISTING_LIMIT.getIsExceeded(),
                EXISTING_LIMIT.getCreationDate(),
                EXISTING_LIMIT.getIsActive()
        );
        doReturn(Optional.of(EXISTING_LIMIT)).when(limitRepository).findByIdAndUser(EXISTING_LIMIT.getId(), EXISTING_LIMIT.getUser());
        doReturn(expectedLimit).when(limitRepository).save(expectedLimit);

        Limit updatedLimit = limitService.update(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getId(), updateDto);

        assertNotNull(updatedLimit);
        assertThat(updatedLimit).isEqualTo(expectedLimit);
        verify(limitRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verify(limitRepository, times(1)).save(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void update_limitNotExists_throwsException() {
        doReturn(Optional.empty()).when(limitRepository).findByIdAndUser(EXISTING_LIMIT.getId(), EXISTING_LIMIT.getUser());

        assertThatThrownBy(() -> limitService.update(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getId(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_LIMIT.getId().toString());
        verify(limitRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verify(limitRepository, never()).save(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void update_newTargetAmountIsNegativeOrEqualToZero_throwsException() {
        LimitUpdateDto updateDto = new LimitUpdateDto(
                null,
                BigDecimal.ZERO.subtract(EXISTING_LIMIT.getTargetAmount()),
                null
        );
        doReturn(Optional.of(EXISTING_LIMIT)).when(limitRepository).findByIdAndUser(EXISTING_LIMIT.getId(), EXISTING_LIMIT.getUser());

        assertThatThrownBy(() -> limitService.update(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getId(), updateDto))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining("The <targetAmount> must be greater than zero");
        verify(limitRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verify(limitRepository, never()).save(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void update_newCurrentAmountIsNegative_throwsException() {
        LimitUpdateDto updateDto = new LimitUpdateDto(
                null,
                null,
                BigDecimal.valueOf(-150)
        );
        doReturn(Optional.of(EXISTING_LIMIT)).when(limitRepository).findByIdAndUser(EXISTING_LIMIT.getId(), EXISTING_LIMIT.getUser());

        assertThatThrownBy(() -> limitService.update(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getId(), updateDto))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining("The <currentAmount> must be equal or greater than zero");
        verify(limitRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        verify(limitRepository, never()).save(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void deleteById_limitExists_deletesLimit() {
        doReturn(Optional.of(EXISTING_LIMIT)).when(limitRepository).findByIdAndUser(EXISTING_LIMIT.getId(), EXISTING_LIMIT.getUser());
        doNothing().when(limitRepository).delete(EXISTING_LIMIT);

        limitService.deleteById(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getId());

        verify(limitRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        assertThat(EXISTING_LIMIT.getWallet().getWalletLimit()).isNull();
        verify(limitRepository, times(1)).delete(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void deleteById_limitNotExists_throwsException() {
        doReturn(Optional.empty()).when(limitRepository).findByIdAndUser(EXISTING_LIMIT.getId(), EXISTING_LIMIT.getUser());

        assertThatThrownBy(() -> limitService.deleteById(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_LIMIT.getId().toString());
        verify(limitRepository, times(1)).findByIdAndUser(any(Long.class), any(User.class));
        assertThat(EXISTING_LIMIT.getWallet().getWalletLimit()).isEqualTo(EXISTING_LIMIT);
        verify(limitRepository, never()).delete(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void deleteByWallet_limitExists_deletesLimit() {
        doReturn(Optional.of(EXISTING_LIMIT)).when(limitRepository).findByWalletAndUser(EXISTING_LIMIT.getWallet(), EXISTING_LIMIT.getUser());
        doNothing().when(limitRepository).delete(EXISTING_LIMIT);

        limitService.deleteByWallet(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getWallet());

        verify(limitRepository, times(1)).findByWalletAndUser(any(Wallet.class), any(User.class));
        assertThat(EXISTING_LIMIT.getWallet().getWalletLimit()).isNull();
        verify(limitRepository, times(1)).delete(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }

    @Test
    public void deleteByWallet_limitNotExists_throwsException() {
        doReturn(Optional.empty()).when(limitRepository).findByWalletAndUser(EXISTING_LIMIT.getWallet(), EXISTING_LIMIT.getUser());

        assertThatThrownBy(() -> limitService.deleteByWallet(EXISTING_LIMIT.getUser(), EXISTING_LIMIT.getWallet()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_LIMIT.getId().toString());
        verify(limitRepository, times(1)).findByWalletAndUser(any(Wallet.class), any(User.class));
        assertThat(EXISTING_LIMIT.getWallet().getWalletLimit()).isEqualTo(EXISTING_LIMIT);
        verify(limitRepository, never()).delete(any(Limit.class));
        verifyNoMoreInteractions(limitRepository);
    }
}
