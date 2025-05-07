package pe.edu.vallegrande.VaccineAplication.dto;

import java.time.LocalDate;

public class CycleLifeDTO {
    private Long cycleLifeId;
    private LocalDate endDate;
    private String timesInWeeks;
    private String nameIto;  // Nuevo campo agregado

    // Getter y Setter para cycleLifeId
    public Long getCycleLifeId() {
        return cycleLifeId;
    }

    public void setCycleLifeId(Long cycleLifeId) {
        this.cycleLifeId = cycleLifeId;
    }

    // Getter y Setter para endDate
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Getter y Setter para timesInWeeks
    public String getTimesInWeeks() {
        return timesInWeeks;
    }

    public void setTimesInWeeks(String timesInWeeks) {
        this.timesInWeeks = timesInWeeks;
    }

    // Getter y Setter para nameIto
    public String getNameIto() {
        return nameIto;
    }

    public void setNameIto(String nameIto) {
        this.nameIto = nameIto;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "CycleLifeDTO{" +
               "cycleLifeId=" + cycleLifeId +
               ", endDate=" + endDate +
               ", timesInWeeks='" + timesInWeeks + '\'' +
               ", nameIto='" + nameIto + '\'' +  // Incluir el nuevo campo en toString
               '}';
    }
}
