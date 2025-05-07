package pe.edu.vallegrande.VaccineAplication.repository;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import pe.edu.vallegrande.VaccineAplication.model.VaccineApplicationsModel; // Cambia a VaccineModel
import reactor.core.publisher.Mono;




public interface VaccineApplicationsRepository extends ReactiveCrudRepository<VaccineApplicationsModel, Long> {
     // Método para encontrar aplicaciones por vaccineId
    Mono<VaccineApplicationsModel> findByCycleLifeId(Long cycleLifeId);
    

}