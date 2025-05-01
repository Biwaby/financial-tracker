package com.biwaby.financialtracker.service.impl;

import com.biwaby.financialtracker.dto.create.LimitCreateDto;
import com.biwaby.financialtracker.dto.update.LimitUpdateDto;
import com.biwaby.financialtracker.entity.Limit;
import com.biwaby.financialtracker.entity.User;
import com.biwaby.financialtracker.entity.Wallet;
import com.biwaby.financialtracker.enums.LimitType;
import com.biwaby.financialtracker.exception.ResponseException;
import com.biwaby.financialtracker.repository.LimitRepository;
import com.biwaby.financialtracker.service.LimitService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LimitServiceImpl implements LimitService {

    private final LimitRepository limitRepository;

    @Override
    @Transactional
    public Limit save(Limit limit) {
        return limitRepository.save(limit);
    }

    @Override
    @Transactional
    public Limit create(User user, Wallet wallet, LimitCreateDto dto) {
        if (limitRepository.existsByWallet(wallet)) {
            throw new ResponseException(
                    HttpStatus.BAD_REQUEST.value(),
                    "Limit already exists for wallet with id <%s>".formatted(wallet.getId())
            );
        }

        Limit limitToCreate = new Limit();
        limitToCreate.setUser(user);
        limitToCreate.setWallet(wallet);
        limitToCreate.setType(LimitType.getTypeByValue(dto.getType()));
        limitToCreate.setTargetAmount(dto.getTargetAmount());
        limitToCreate.setCreationDate(LocalDateTime.now());
        limitToCreate.setIsActive(Boolean.TRUE);

        return save(limitToCreate);
    }

    @Override
    public Limit getById(User user, Long id) {
        return limitRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Limit with id <%s> is not found or not allowed".formatted(id)
                )
        );
    }

    @Override
    public Limit getByWallet(User user, Wallet wallet) {
        return limitRepository.findByWalletAndUser(wallet, user).orElseThrow(
                () -> new ResponseException(
                        HttpStatus.NOT_FOUND.value(),
                        "Limit for wallet with id <%s> is not found or not allowed".formatted(wallet.getId())
                )
        );
    }

    @Override
    public Boolean existsByWallet(Wallet wallet) {
        return limitRepository.existsByWallet(wallet);
    }

    @Override
    public List<Limit> getAll(User user) {
        return limitRepository.findAllByUser(user);
    }

    @Override
    @Transactional
    public void updateForWallet(User user, Wallet wallet, BigDecimal amount) {
        Limit limitToUpdate = getByWallet(user, wallet);
        LocalDateTime nowDateTime = LocalDateTime.now();
        LimitType type = limitToUpdate.getType();
        LocalDateTime limitStartDateTime = limitToUpdate.getCreationDate();
        LocalDateTime limitEndDateTime = type.addTo(limitStartDateTime, 1);
        boolean limitIsActive;

        if (type.equals(LimitType.PERMANENT)) {
            limitIsActive = nowDateTime.isAfter(limitStartDateTime);
        } else {
            limitIsActive = nowDateTime.isAfter(limitStartDateTime) && nowDateTime.isBefore(limitEndDateTime);
        }

        limitToUpdate.setIsActive(limitIsActive);

        if (limitToUpdate.getIsActive()) {
            limitToUpdate.setCurrentAmount(limitToUpdate.getCurrentAmount().add(amount));
            if (limitToUpdate.getCurrentAmount().compareTo(limitToUpdate.getTargetAmount()) > 0) {
                limitToUpdate.setIsExceeded(Boolean.TRUE);
            }
        }

        save(limitToUpdate);
    }

    @Override
    @Transactional
    public Limit update(User user, Long id, LimitUpdateDto dto) {
        Limit limitToUpdate = getById(user, id);

        if (dto != null) {
            if (dto.getType() != null && !dto.getType().trim().isEmpty()) {
                limitToUpdate.setType(LimitType.getTypeByValue(dto.getType()));
            }
            if (dto.getTargetAmount() != null) {
                if (dto.getTargetAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new ResponseException(
                            HttpStatus.BAD_REQUEST.value(),
                            "The <targetAmount> must be equal or greater than zero"
                    );
                }
                limitToUpdate.setTargetAmount(dto.getTargetAmount());
            }
            if (dto.getCurrentAmount() != null) {
                if (dto.getCurrentAmount().compareTo(BigDecimal.ZERO) < 0) {
                    throw new ResponseException(
                            HttpStatus.BAD_REQUEST.value(),
                            "The <currentAmount> must be equal or greater than zero"
                    );
                }
                limitToUpdate.setCurrentAmount(dto.getCurrentAmount());
            }

            if (limitToUpdate.getCurrentAmount().compareTo(limitToUpdate.getTargetAmount()) > 0) {
                limitToUpdate.setIsExceeded(Boolean.TRUE);
            } else {
                limitToUpdate.setIsExceeded(Boolean.FALSE);
            }
        }
        return save(limitToUpdate);
    }

    @Override
    @Transactional
    public void deleteById(User user, Long id) {
        Limit limitToDelete = getById(user, id);
        limitRepository.delete(limitToDelete);
    }
}
