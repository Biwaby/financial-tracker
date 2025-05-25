package com.biwaby.financialtracker.dto.update;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateDto {

    @Size(min = 3, max = 255, message = "The <name> must be between 3 and 255 characters long.")
    private String name;

    @Size(min = 3, max = 255, message = "The <type> must be between 3 and 255 characters long.")
    private String type;

    private String description;
}
