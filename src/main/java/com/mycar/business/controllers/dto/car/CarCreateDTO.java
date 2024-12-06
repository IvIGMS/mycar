package com.mycar.business.controllers.dto.car;

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
public class CarCreateDTO {
    @NotNull(message = "El campo 'marca' es obligatorio.")
    @Size(min = 2, max = 20, message = "La marca del coche debe tener entre 2 y 20 caracteres")
    private String companyName;

    @NotNull(message = "El campo 'marca' es obligatorio.")
    @Size(min = 2, max = 30, message = "El modelo del coche debe tener entre 2 y 30 caracteres")
    private String modelName;

    @Min(value = 0, message = "El número mínimo de km es 0. No puedes tener un vehículo con menos de 0 km")
    @Max(value = 2000000, message = "El número máximo de km permitidos es 2.000.000")
    private Integer km;
}
