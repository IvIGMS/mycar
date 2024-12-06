package com.mycar.business.controllers.dto.issue;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IssueCreateDTO {
    @NotNull(message = "El campo 'name' es obligatorio.")
    @Size(min = 10, max = 100, message = "El título debe tener entre 10 y 100 caracteres")
    private String name;

    @Size(min = 25, max = 250, message = "La descripción debe tener entre 25 y 250 caracteres")
    private String description;

    @Min(value = 1, message = "El número de días de aviso no puede ser menor a 1 día")
    @Max(value = 3650, message = "El número de días de aviso no puede ser mayor a 3650 días")
    private Integer notificationDateDays;

    @Min(value = 50, message = "El número de km de aviso no puede ser menor a 50 km")
    @Max(value = 150000, message = "El número de km de aviso no puede ser mayor a 150.000 km")
    private Integer notificationDistance;

    @Min(value = 0, message = "El número de km del vehículo no puede ser menor a 0 km")
    @Max(value = 2000000, message = "El número de km del vehículo no puede ser mayor a 2.000.000 de km")
    private Integer currentDistance;

    @NotNull(message = "El campo 'typeId' es obligatorio.")
    @Min(value = 1, message = "El type debe ser 1 (Issues medidos con distancia) o 2 (Issues medidos con fecha)")
    @Max(value = 2, message = "El type debe ser 1 (Issues medidos con distancia) o 2 (Issues medidos con fecha)")
    private Long typeId;

    @NotNull(message = "El campo 'carId' es obligatorio.")
    @Min(value = 0, message = "El id del vehículo debe ser un número natural")
    private Long carId;
}
