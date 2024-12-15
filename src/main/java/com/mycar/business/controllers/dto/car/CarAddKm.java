package com.mycar.business.controllers.dto.car;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CarAddKm {
    @NotNull(message = "El campo 'carId' es obligatorio.")
    @Min(value = 0, message = "El campo carId debe ser un número natural")
    private Long carId;

    @NotNull(message = "El campo 'message' es obligatorio.")
    @Min(value = 0, message = "El número mínimo de km es 0. No puedes tener un vehículo con menos de 0 km")
    @Max(value = 2000000, message = "El número máximo de km permitidos es 2.000.000")
    private Integer km;
}
