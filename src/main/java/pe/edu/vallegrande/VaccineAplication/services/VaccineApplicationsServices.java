package pe.edu.vallegrande.VaccineAplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import pe.edu.vallegrande.VaccineAplication.model.VaccineApplicationsModel;
import pe.edu.vallegrande.VaccineAplication.repository.VaccineApplicationsRepository;
import pe.edu.vallegrande.VaccineAplication.dto.CycleLifeDTO;
import pe.edu.vallegrande.VaccineAplication.dto.ShedDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.MediaType;

@Service
public class VaccineApplicationsServices {

    @Autowired
    private VaccineApplicationsRepository vaccineApplicationsRepository;

    // WebClient para consumir datos de CycleLife
private final WebClient cycleLifeWebClient = WebClient.builder()
.baseUrl("https://nph-ciclodevida.onrender.com/cicloVida") // Ajusta la URL si cambia
.defaultHeader("Content-Type", "application/json")
.build();

// Método para consumir los datos de CycleLife desde otro microservicio
public Mono<CycleLifeDTO> getCycleLifeFromExternal(Long cycleLifeId) {
return cycleLifeWebClient.get()
    .uri("/{id}", cycleLifeId)
    .accept(MediaType.APPLICATION_JSON)
    .retrieve()
    .bodyToMono(CycleLifeDTO.class);
}

// WebClient para consumir datos de Shed
private final WebClient shedWebClient = WebClient.builder()
.baseUrl("https://vaccine-z4vj.onrender.com/NPH/sheds") // Ajusta la URL si cambia
.defaultHeader("Content-Type", "application/json")
.build();

// Método para consumir los datos de Shed desde otro microservicio
public Mono<ShedDTO> getShedFromExternal(Long shedId) {
return shedWebClient.get()
    .uri("/{id}", shedId) // Usa el shedId como parámetro en la URL
    .accept(MediaType.APPLICATION_JSON)
    .retrieve()
    .bodyToMono(ShedDTO.class);  // Retorna un Mono con el DTO de Shed
}



    // Guardar una nueva aplicación de vacuna, incluyendo datos de ciclo de vida
    public Mono<VaccineApplicationsModel> save(VaccineApplicationsModel application) {
        return getCycleLifeFromExternal(application.getCycleLifeId())
                .flatMap(cycle -> {
                    if (cycle.getTimesInWeeks() == null) {
                        // Aquí puedes lanzar una excepción si no quieres permitir el valor null
                        return Mono.error(new IllegalArgumentException("timesInWeeks no puede ser null"));
                    }
                    application.setEndDate(cycle.getEndDate());
                    application.setTimesInWeeks(cycle.getTimesInWeeks());
                    return vaccineApplicationsRepository.save(application);
                });
    }
    
    
    

    // Obtener una aplicación y completar datos del ciclo de vida
    public Mono<VaccineApplicationsModel> getApplicationById(Long id) {
        return vaccineApplicationsRepository.findById(id)
                .flatMap(app -> getCycleLifeFromExternal(app.getCycleLifeId())
                    .map(cycle -> {
                        app.setEndDate(cycle.getEndDate());
                        app.setTimesInWeeks(cycle.getTimesInWeeks());
                        return app;
                    }));
    }

    // Listar todas las aplicaciones sin datos externos
    public Flux<VaccineApplicationsModel> getAllApplications() {
        return vaccineApplicationsRepository.findAll();
    }

    // Actualizar una aplicación (también actualiza los datos del ciclo de vida)
    public Mono<VaccineApplicationsModel> updateApplication(Long id, VaccineApplicationsModel application) {
        return getCycleLifeFromExternal(application.getCycleLifeId())
                .flatMap(cycle -> {
                    application.setEndDate(cycle.getEndDate());
                    application.setTimesInWeeks(cycle.getTimesInWeeks());
                    return vaccineApplicationsRepository.save(application);
                });
    }

    // Desactivar una aplicación (cambia estado a "I")
    public Mono<VaccineApplicationsModel> deactivateApplication(Long id) {
        return vaccineApplicationsRepository.findById(id)
                .flatMap(existingApplication -> {
                    existingApplication.setActive("I");
                    return vaccineApplicationsRepository.save(existingApplication);
                });
    }

    // Activar una aplicación (cambia estado a "A")
    public Mono<VaccineApplicationsModel> activateApplication(Long id) {
        return vaccineApplicationsRepository.findById(id)
                .flatMap(existingApplication -> {
                    existingApplication.setActive("A");
                    return vaccineApplicationsRepository.save(existingApplication);
                });
    }

    // Buscar por ciclo de vida (local, sin datos externos)
    public Mono<VaccineApplicationsModel> getCycleLifeIdById(Long cycleLifeId) {
        return vaccineApplicationsRepository.findByCycleLifeId(cycleLifeId);
    }
}
