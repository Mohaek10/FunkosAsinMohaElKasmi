package dev.mohaek.models;

import lombok.*;

import dev.mohaek.services.IdGenerator;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funko {
    private long id;
    @Builder.Default
    private UUID uuid=UUID.randomUUID();
    @Getter
    private long myId;
    private String name;
    private Modelo modelo;
    private double precio;
    private LocalDate fecha_lanzamiento;

}