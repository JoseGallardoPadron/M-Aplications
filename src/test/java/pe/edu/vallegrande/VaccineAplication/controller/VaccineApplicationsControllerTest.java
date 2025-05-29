package pe.edu.vallegrande.VaccineAplication.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.edu.vallegrande.VaccineAplication.model.VaccineApplicationsModel;
import pe.edu.vallegrande.VaccineAplication.services.VaccineApplicationsServices;

public class VaccineApplicationsControllerTest {

    @InjectMocks
    private VaccineApplicationsController vaccineApplicationsController;

    @Mock
    private VaccineApplicationsServices vaccineApplicationsServices;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateApplication() {
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setApplicationId(1L);

        when(vaccineApplicationsServices.save(application)).thenReturn(Mono.just(application));

        Mono<ResponseEntity<VaccineApplicationsModel>> response = vaccineApplicationsController.createApplication(application);

        StepVerifier.create(response)
            .expectNextMatches(res -> {
                assertEquals(HttpStatus.CREATED, res.getStatusCode());
                assertEquals(application, res.getBody());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetAllApplications() {
        VaccineApplicationsModel application1 = new VaccineApplicationsModel();
        VaccineApplicationsModel application2 = new VaccineApplicationsModel();
        Flux<VaccineApplicationsModel> applications = Flux.just(application1, application2);

        when(vaccineApplicationsServices.getAllApplications()).thenReturn(applications);

        Flux<VaccineApplicationsModel> response = vaccineApplicationsController.getAllApplications();

        StepVerifier.create(response.collectList())
            .expectNextMatches(res -> {
                assertEquals(2, res.size());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetApplicationById() {
        Long id = 1L;
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setApplicationId(id);

        when(vaccineApplicationsServices.getApplicationById(id)).thenReturn(Mono.just(application));

        Mono<ResponseEntity<VaccineApplicationsModel>> response = vaccineApplicationsController.getApplicationById(id);

        StepVerifier.create(response)
            .expectNextMatches(res -> {
                assertEquals(HttpStatus.OK, res.getStatusCode());
                assertEquals(application, res.getBody());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testUpdateApplication() {
        Long id = 1L;
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setApplicationId(id);

        when(vaccineApplicationsServices.updateApplication(eq(id), any(VaccineApplicationsModel.class)))
                .thenReturn(Mono.just(application));

        Mono<ResponseEntity<VaccineApplicationsModel>> response = vaccineApplicationsController.updateApplication(id, application);

        StepVerifier.create(response)
            .expectNextMatches(res -> {
                assertEquals(HttpStatus.OK, res.getStatusCode());
                assertEquals(application, res.getBody());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testDeactivateApplication() {
        Long id = 1L;
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setApplicationId(id);

        when(vaccineApplicationsServices.deactivateApplication(id)).thenReturn(Mono.just(application));

        Mono<ResponseEntity<VaccineApplicationsModel>> response = vaccineApplicationsController.deactivateApplication(id);

        StepVerifier.create(response)
            .expectNextMatches(res -> {
                assertEquals(HttpStatus.OK, res.getStatusCode());
                assertEquals(application, res.getBody());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testActivateApplication() {
        Long id = 1L;
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setApplicationId(id);

        when(vaccineApplicationsServices.activateApplication(id)).thenReturn(Mono.just(application));

        Mono<ResponseEntity<VaccineApplicationsModel>> response = vaccineApplicationsController.activateApplication(id);

        StepVerifier.create(response)
            .expectNextMatches(res -> {
                assertEquals(HttpStatus.OK, res.getStatusCode());
                assertEquals(application, res.getBody());
                return true;
            })
            .verifyComplete();
    }
}
