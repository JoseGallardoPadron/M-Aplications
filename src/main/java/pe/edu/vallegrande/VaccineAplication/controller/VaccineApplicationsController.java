package pe.edu.vallegrande.VaccineAplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.VaccineAplication.model.VaccineApplicationsModel;
import pe.edu.vallegrande.VaccineAplication.services.VaccineApplicationsServices;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/vaccine-applications") // Ruta base para el controlador
public class VaccineApplicationsController {

    @Autowired
    private VaccineApplicationsServices vaccineApplicationsServices; // Usamos el servicio para gestionar las aplicaciones

    // Crear una nueva aplicación de vacuna
    @PostMapping("/create")
    public Mono<ResponseEntity<VaccineApplicationsModel>> createApplication(@RequestBody VaccineApplicationsModel application) {
        // Validación simple del henId eliminada

        return vaccineApplicationsServices.save(application)
            .map(savedApplication -> ResponseEntity.status(HttpStatus.CREATED).body(savedApplication))
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null))) // Usar null aquí
            .onErrorResume(e -> {
                System.err.println("Error creando la aplicación de vacuna: " + e.getMessage());
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)); // También devuelve null
            });
    }

    // Listar todas las aplicaciones de vacunas
    @GetMapping
    public Flux<VaccineApplicationsModel> getAllApplications() {
        return vaccineApplicationsServices.getAllApplications(); // Método en el servicio que puede usar el repositorio
    }

    // Obtener una aplicación de vacuna por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<VaccineApplicationsModel>> getApplicationById(@PathVariable Long id) {
        return vaccineApplicationsServices.getApplicationById(id)
                .map(application -> ResponseEntity.ok(application))
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Si no se encuentra, retorna Not Found
    }

    // Actualizar una aplicación de vacuna
    @PutMapping("/{id}")
    public Mono<ResponseEntity<VaccineApplicationsModel>> updateApplication(@PathVariable Long id, @RequestBody VaccineApplicationsModel application) {
        application.setApplicationId(id); // Establecer el ID correcto para la actualización
        return vaccineApplicationsServices.updateApplication(id, application) // Aquí se pasa el id junto con el objeto
                .map(updatedApplication -> ResponseEntity.ok(updatedApplication))
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Si no se encuentra, retorna Not Found
    }
    
    // Eliminar (inactivar) una aplicación de vacuna
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<VaccineApplicationsModel>> deactivateApplication(@PathVariable Long id) {
        return vaccineApplicationsServices.deactivateApplication(id) // Llama al servicio para desactivar
                .map(deactivatedApplication -> ResponseEntity.ok(deactivatedApplication)) // Retorna la aplicación desactivada
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Si no se encuentra, retorna Not Found
    }

    // Activar (revertir a activo) una aplicación de vacuna
    @PatchMapping("/activate/{id}") // Método PATCH para actualizar parcialmente
    public Mono<ResponseEntity<VaccineApplicationsModel>> activateApplication(@PathVariable Long id) {
        return vaccineApplicationsServices.activateApplication(id) // Llama al servicio para activar
                .map(activatedApplication -> ResponseEntity.ok(activatedApplication)) // Retorna la aplicación activada
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Si no se encuentra, retorna Not Found
    }
}
