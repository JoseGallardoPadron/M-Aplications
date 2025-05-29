package pe.edu.vallegrande.VaccineAplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.VaccineAplication.model.VaccineApplicationsModel;
import pe.edu.vallegrande.VaccineAplication.repository.VaccineApplicationsRepository;
import pe.edu.vallegrande.VaccineAplication.dto.CycleLifeDTO;
import pe.edu.vallegrande.VaccineAplication.webclient.client.CycleLifeClient; // Importar el cliente
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VaccineApplicationsServices {

    @Autowired
    private VaccineApplicationsRepository vaccineApplicationsRepository;

    @Autowired
    private CycleLifeClient cycleLifeClient; // Inyectar el cliente

    // Guardar una nueva aplicación de vacuna, incluyendo datos de ciclo de vida
    public Mono<VaccineApplicationsModel> save(VaccineApplicationsModel application) {
        return cycleLifeClient.getCycleLifeFromExternal(application.getCycleLifeId())
                .flatMap(cycle -> {
                    if (cycle.getTimesInWeeks() == null) {
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
                .flatMap(app -> cycleLifeClient.getCycleLifeFromExternal(app.getCycleLifeId())
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
        return cycleLifeClient.getCycleLifeFromExternal(application.getCycleLifeId())
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
