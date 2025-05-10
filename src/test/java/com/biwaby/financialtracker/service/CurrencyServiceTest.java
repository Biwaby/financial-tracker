package com.biwaby.financialtracker.service;

import com.biwaby.financialtracker.dto.update.CurrencyUpdateDto;
import com.biwaby.financialtracker.entity.Currency;
import com.biwaby.financialtracker.entity.SavingsAccount;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.CurrencyRepository;
import com.biwaby.financialtracker.repository.SavingsAccountRepository;
import com.biwaby.financialtracker.repository.WalletRepository;
import com.biwaby.financialtracker.service.impl.CurrencyServiceImpl;
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
public class CurrencyServiceTest {

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private SavingsAccountRepository savingsAccountRepository;

    @InjectMocks
    private CurrencyServiceImpl currencyService;

    private static final Currency NON_CURRENCY = new Currency(
            1L,
            "000",
            "NON",
            "Missing currency",
            new ArrayList<>(),
            new ArrayList<>()
    );

    private static final Currency EXISTING_CURRENCY = new Currency(
            2L,
            "000",
            "AAA",
            "Test currency",
            List.of(new Wallet()),
            List.of(new SavingsAccount())
    );
    
    @Test
    public void create_newCurrency_createsCurrency() {
        doReturn(Boolean.FALSE).when(currencyRepository).existsByCode(EXISTING_CURRENCY.getCode());
        doReturn(Boolean.FALSE).when(currencyRepository).existsByLetterCode(EXISTING_CURRENCY.getLetterCode());
        doReturn(Boolean.FALSE).when(currencyRepository).existsByName(EXISTING_CURRENCY.getName());
        doReturn(EXISTING_CURRENCY).when(currencyRepository).save(EXISTING_CURRENCY);

        Currency createdCurrency = currencyService.create(EXISTING_CURRENCY);

        assertNotNull(createdCurrency);
        assertThat(createdCurrency).isEqualTo(EXISTING_CURRENCY);
        verify(currencyRepository, times(1)).existsByCode(any(String.class));
        verify(currencyRepository, times(1)).existsByLetterCode(any(String.class));
        verify(currencyRepository, times(1)).existsByName(any(String.class));
        verify(currencyRepository, times(1)).save(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void create_alreadyExistsByCode_throwsException() {
        doReturn(Boolean.TRUE).when(currencyRepository).existsByCode(EXISTING_CURRENCY.getCode());

        assertThatThrownBy(() -> currencyService.create(EXISTING_CURRENCY))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CURRENCY.getCode());
        verify(currencyRepository, times(1)).existsByCode(any(String.class));
        verify(currencyRepository, never()).save(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void create_alreadyExistsByLetterCode_throwsException() {
        doReturn(Boolean.TRUE).when(currencyRepository).existsByLetterCode(EXISTING_CURRENCY.getLetterCode());

        assertThatThrownBy(() -> currencyService.create(EXISTING_CURRENCY))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CURRENCY.getLetterCode());
        verify(currencyRepository, times(1)).existsByCode(any(String.class));
        verify(currencyRepository, times(1)).existsByLetterCode(any(String.class));
        verify(currencyRepository, never()).save(any());
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void create_alreadyExistsByName_throwsException() {
        doReturn(Boolean.TRUE).when(currencyRepository).existsByName(EXISTING_CURRENCY.getName());

        assertThatThrownBy(() -> currencyService.create(EXISTING_CURRENCY))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CURRENCY.getName());
        verify(currencyRepository, times(1)).existsByCode(any(String.class));
        verify(currencyRepository, times(1)).existsByLetterCode(any(String.class));
        verify(currencyRepository, times(1)).existsByName(any(String.class));
        verify(currencyRepository, never()).save(any());
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void getById_currencyExists_returnsFoundedCurrency() {
        doReturn(Optional.of(EXISTING_CURRENCY)).when(currencyRepository).findById(EXISTING_CURRENCY.getId());

        Currency foundCurrency = currencyService.getById(EXISTING_CURRENCY.getId());

        assertNotNull(foundCurrency);
        assertThat(foundCurrency).isEqualTo(EXISTING_CURRENCY);
        verify(currencyRepository).findById(any(Long.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void getById_currencyNotExists_throwsException() {
        doReturn(Optional.empty()).when(currencyRepository).findById(EXISTING_CURRENCY.getId());
        
        assertThatThrownBy(() -> currencyService.getById(EXISTING_CURRENCY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CURRENCY.getId().toString());
        verify(currencyRepository, times(1)).findById(any(Long.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void getByCode_currencyExists_returnsFoundedCurrency() {
        doReturn(Optional.of(EXISTING_CURRENCY)).when(currencyRepository).findByCode(EXISTING_CURRENCY.getCode());

        Currency foundCurrency = currencyService.getByCode(EXISTING_CURRENCY.getCode());

        assertNotNull(foundCurrency);
        assertThat(foundCurrency).isEqualTo(EXISTING_CURRENCY);
        verify(currencyRepository).findByCode(any(String.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void getByCode_currencyNotExists_throwsException() {
        doReturn(Optional.empty()).when(currencyRepository).findByCode(EXISTING_CURRENCY.getCode());

        assertThatThrownBy(() -> currencyService.getByCode(EXISTING_CURRENCY.getCode()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CURRENCY.getCode());
        verify(currencyRepository, times(1)).findByCode(any(String.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void getByLetterCode_currencyExists_returnsFoundedCurrency() {
        doReturn(Optional.of(EXISTING_CURRENCY)).when(currencyRepository).findByLetterCode(EXISTING_CURRENCY.getLetterCode());

        Currency foundCurrency = currencyService.getByLetterCode(EXISTING_CURRENCY.getLetterCode());

        assertNotNull(foundCurrency);
        assertThat(foundCurrency).isEqualTo(EXISTING_CURRENCY);
        verify(currencyRepository).findByLetterCode(any(String.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void getByLetterCode_currencyNotExists_throwsException() {
        doReturn(Optional.empty()).when(currencyRepository).findByLetterCode(EXISTING_CURRENCY.getLetterCode());

        assertThatThrownBy(() -> currencyService.getByLetterCode(EXISTING_CURRENCY.getLetterCode()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CURRENCY.getLetterCode());
        verify(currencyRepository, times(1)).findByLetterCode(any(String.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void getAll_returnsAllFoundedCurrencies() {
        doReturn(List.of(EXISTING_CURRENCY)).when(currencyRepository).findAll();

        List<Currency> currencies = currencyService.getAll();

        assertNotNull(currencies);
        assertThat(currencies).hasSize(1);
        assertThat(currencies.getFirst().getId()).isEqualTo(EXISTING_CURRENCY.getId());
        verify(currencyRepository, times(1)).findAll();
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void update_currencyExists_returnsUpdatedCurrency() {
        CurrencyUpdateDto updateDto = new CurrencyUpdateDto(null, "BBB", "Very Test Currency");
        Currency expectedCurrency = new Currency(
                2L,
                "000",
                "BBB",
                "Very Test Currency",
                new ArrayList<>(),
                new ArrayList<>()
        );
        doReturn(Optional.of(EXISTING_CURRENCY)).when(currencyRepository).findById(EXISTING_CURRENCY.getId());
        doReturn(Boolean.FALSE).when(currencyRepository).existsByLetterCode(updateDto.getLetterCode());
        doReturn(Boolean.FALSE).when(currencyRepository).existsByName(updateDto.getName());
        doReturn(expectedCurrency).when(currencyRepository).save(expectedCurrency);

        Currency updatedCurrency = currencyService.update(EXISTING_CURRENCY.getId(), updateDto);

        assertNotNull(updatedCurrency);
        assertThat(updatedCurrency).isEqualTo(expectedCurrency);
        verify(currencyRepository, times(1)).findById(any(Long.class));
        verify(currencyRepository, times(1)).existsByLetterCode(any(String.class));
        verify(currencyRepository, times(1)).existsByName(any(String.class));
        verify(currencyRepository, times(1)).save(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void update_currencyNotExists_throwsException() {
        doReturn(Optional.empty()).when(currencyRepository).findById(EXISTING_CURRENCY.getId());

        assertThatThrownBy(() -> currencyService.update(EXISTING_CURRENCY.getId(), null))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CURRENCY.getId().toString());
        verify(currencyRepository, times(1)).findById(any(Long.class));
        verify(currencyRepository, never()).save(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void update_currencyLetterCodeEqualsNoN_throwsException() {
        CurrencyUpdateDto updateDto = new CurrencyUpdateDto(null, NON_CURRENCY.getLetterCode(), null);
        doReturn(Optional.of(EXISTING_CURRENCY)).when(currencyRepository).findById(EXISTING_CURRENCY.getId());

        assertThatThrownBy(() -> currencyService.update(EXISTING_CURRENCY.getId(), updateDto))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(updateDto.getLetterCode());
        verify(currencyRepository, times(1)).findById(any(Long.class));
        verify(currencyRepository, never()).save(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void update_currencyLetterCodeAlreadyExists_throwsException() {
        CurrencyUpdateDto updateDto = new CurrencyUpdateDto(null, EXISTING_CURRENCY.getLetterCode(), null);
        doReturn(Optional.of(EXISTING_CURRENCY)).when(currencyRepository).findById(EXISTING_CURRENCY.getId());
        doReturn(Boolean.TRUE).when(currencyRepository).existsByLetterCode(updateDto.getLetterCode());

        assertThatThrownBy(() -> currencyService.update(EXISTING_CURRENCY.getId(), updateDto))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(updateDto.getLetterCode());
        verify(currencyRepository, times(1)).findById(any(Long.class));
        verify(currencyRepository, times(1)).existsByLetterCode(any(String.class));
        verify(currencyRepository, never()).save(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void update_currencyNameEqualsNoN_throwsException() {
        CurrencyUpdateDto updateDto = new CurrencyUpdateDto(null, null, NON_CURRENCY.getName());
        doReturn(Optional.of(EXISTING_CURRENCY)).when(currencyRepository).findById(EXISTING_CURRENCY.getId());

        assertThatThrownBy(() -> currencyService.update(EXISTING_CURRENCY.getId(), updateDto))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(updateDto.getName());
        verify(currencyRepository, times(1)).findById(any(Long.class));
        verify(currencyRepository, never()).save(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void update_currencyNameAlreadyExists_throwsException() {
        CurrencyUpdateDto updateDto = new CurrencyUpdateDto(null, null, EXISTING_CURRENCY.getName());
        doReturn(Optional.of(EXISTING_CURRENCY)).when(currencyRepository).findById(EXISTING_CURRENCY.getId());
        doReturn(Boolean.TRUE).when(currencyRepository).existsByName(updateDto.getName());

        assertThatThrownBy(() -> currencyService.update(EXISTING_CURRENCY.getId(), updateDto))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(updateDto.getName());
        verify(currencyRepository, times(1)).findById(any(Long.class));
        verify(currencyRepository, times(1)).existsByName(any(String.class));
        verify(currencyRepository, never()).save(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void deleteById_currencyExists_deletesCurrency() {
        doReturn(Optional.of(EXISTING_CURRENCY)).when(currencyRepository).findById(EXISTING_CURRENCY.getId());
        doReturn(Optional.of(NON_CURRENCY)).when(currencyRepository).findByLetterCode(NON_CURRENCY.getLetterCode());
        doReturn(List.of()).when(walletRepository).saveAll(EXISTING_CURRENCY.getWalletsWithCurrency());
        doReturn(List.of()).when(savingsAccountRepository).saveAll(EXISTING_CURRENCY.getSavingsAccountsWithCurrency());
        doNothing().when(currencyRepository).delete(EXISTING_CURRENCY);

        currencyService.deleteById(EXISTING_CURRENCY.getId());

        verify(currencyRepository, times(1)).findById(any(Long.class));
        verify(currencyRepository, times(1)).findByLetterCode(any(String.class));
        verify(currencyRepository, times(1)).delete(any(Currency.class));
        assertThatList(EXISTING_CURRENCY.getWalletsWithCurrency())
                .extracting(Wallet::getCurrency)
                .containsOnly(NON_CURRENCY);
        assertThatList(EXISTING_CURRENCY.getSavingsAccountsWithCurrency())
                .extracting(SavingsAccount::getCurrency)
                .containsOnly(NON_CURRENCY);
        verify(walletRepository, times(1)).saveAll(any(List.class));
        verify(savingsAccountRepository, times(1)).saveAll(any(List.class));
        verifyNoMoreInteractions(currencyRepository, walletRepository, savingsAccountRepository);
    }

    @Test
    public void deleteById_currencyNotExists_throwsException() {
        doReturn(Optional.empty()).when(currencyRepository).findById(EXISTING_CURRENCY.getId());

        assertThatThrownBy(() -> currencyService.deleteById(EXISTING_CURRENCY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(EXISTING_CURRENCY.getId().toString());
        verify(currencyRepository, times(1)).findById(any(Long.class));
        verify(currencyRepository, never()).delete(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }

    @Test
    public void deleteById_currencyEqualsNoN_throwsException() {
        doReturn(Optional.of(NON_CURRENCY)).when(currencyRepository).findById(NON_CURRENCY.getId());
        doReturn(Optional.of(NON_CURRENCY)).when(currencyRepository).findByLetterCode(NON_CURRENCY.getLetterCode());

        assertThatThrownBy(() -> currencyService.deleteById(NON_CURRENCY.getId()))
                .isInstanceOf(ResponseException.class)
                .hasMessageContaining(NON_CURRENCY.getId().toString());
        verify(currencyRepository, times(1)).findById(any(Long.class));
        verify(currencyRepository, times(1)).findByLetterCode(any(String.class));
        verify(currencyRepository, never()).delete(any(Currency.class));
        verifyNoMoreInteractions(currencyRepository);
    }
}
