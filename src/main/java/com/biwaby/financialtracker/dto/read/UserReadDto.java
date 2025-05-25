package com.biwaby.financialtracker.dto.read;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReadDto implements Serializable {

    private Long id;
    private String username;
    private String roleName;
    private LocalDateTime registeredAt;
}