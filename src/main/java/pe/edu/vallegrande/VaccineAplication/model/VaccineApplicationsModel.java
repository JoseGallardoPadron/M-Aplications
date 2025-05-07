package pe.edu.vallegrande.VaccineAplication.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("vaccine_applications") // Asegúrate de que coincide con el nombre en la BD
public class VaccineApplicationsModel {

    @Id
    private Long applicationId; // Identificador único para cada aplicación

    @Column("end_date") // Fecha de finalización de la aplicación de la vacuna
    private LocalDate endDate;

    @Column("cycle_life_id") // Relación con la tabla cycle_life
    private Long cycleLifeId; // Cambiado de vaccineId a cycleLifeId

    @Column("shed_id") // Relación con la tabla shed
    private Long shedId;

    @Column("amount") // Cantidad de dosis aplicadas
    private Integer amount;

    @Column("cost_application") // Costo de la aplicación de la vacuna
    private BigDecimal costApplication;

    @Column("email") // Email del responsable o registrado
    private String email;

    @Column("date_registration") // Fecha en la que se registró el dato
    private LocalDate dateRegistration;

    @Column("quantity_birds") // Número de aves vacunadas
    private Integer quantityBirds;

    @Column("active") // Estado: 'A' = Activo, 'I' = Inactivo
    private String active; // O usa `Boolean active;` si en la BD es tipo boolean

    @Column("times_in_weeks") // Número de veces que se aplica la vacuna
    private String  timesInWeeks;

}
