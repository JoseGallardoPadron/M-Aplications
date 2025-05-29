package pe.edu.vallegrande.VaccineAplication.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pe.edu.vallegrande.VaccineAplication.model.VaccineApplicationsModel;
import pe.edu.vallegrande.VaccineAplication.repository.VaccineApplicationsRepository;
import pe.edu.vallegrande.VaccineAplication.webclient.client.CycleLifeClient;
import pe.edu.vallegrande.VaccineAplication.dto.CycleLifeDTO;
import java.time.LocalDate;

public class VaccineApplicationsServicesTest {

    @InjectMocks
    private VaccineApplicationsServices vaccineApplicationsServices;

    @Mock
    private VaccineApplicationsRepository vaccineApplicationsRepository;

    @Mock
    private CycleLifeClient cycleLifeClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveApplication() {
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setCycleLifeId(1L);
        CycleLifeDTO cycleLife = new CycleLifeDTO();
        cycleLife.setEndDate(LocalDate.parse("2025-12-31")); // Asegúrate de que sea LocalDate
        cycleLife.setTimesInWeeks("4"); // Usar String si el modelo lo requiere

        when(cycleLifeClient.getCycleLifeFromExternal(application.getCycleLifeId())).thenReturn(Mono.just(cycleLife));
        when(vaccineApplicationsRepository.save(application)).thenReturn(Mono.just(application));

        Mono<VaccineApplicationsModel> result = vaccineApplicationsServices.save(application);

        StepVerifier.create(result)
            .expectNextMatches(res -> {
                assertEquals(cycleLife.getEndDate(), res.getEndDate());
                assertEquals(cycleLife.getTimesInWeeks(), res.getTimesInWeeks());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testSaveApplicationWithNullTimesInWeeks() {
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setCycleLifeId(1L);
        CycleLifeDTO cycleLife = new CycleLifeDTO();
        cycleLife.setEndDate(LocalDate.parse("2025-12-31"));
        cycleLife.setTimesInWeeks(null); // Establecer como null

        when(cycleLifeClient.getCycleLifeFromExternal(application.getCycleLifeId())).thenReturn(Mono.just(cycleLife));

        Mono<VaccineApplicationsModel> result = vaccineApplicationsServices.save(application);

        StepVerifier.create(result)
            .expectErrorMatches(error -> error instanceof IllegalArgumentException)
            .verify();
    }

    @Test
    public void testGetApplicationById() {
        Long id = 1L;
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setCycleLifeId(1L);

        CycleLifeDTO cycleLife = new CycleLifeDTO();
        cycleLife.setEndDate(LocalDate.parse("2025-12-31")); // Asegúrate de que sea LocalDate
        cycleLife.setTimesInWeeks("4"); // Usar String si el modelo lo requiere

        when(vaccineApplicationsRepository.findById(id)).thenReturn(Mono.just(application));
        when(cycleLifeClient.getCycleLifeFromExternal(application.getCycleLifeId())).thenReturn(Mono.just(cycleLife));

        Mono<VaccineApplicationsModel> result = vaccineApplicationsServices.getApplicationById(id);

        StepVerifier.create(result)
            .expectNextMatches(res -> {
                assertEquals(cycleLife.getEndDate(), res.getEndDate());
                assertEquals(cycleLife.getTimesInWeeks(), res.getTimesInWeeks());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetAllApplications() {
        VaccineApplicationsModel application1 = new VaccineApplicationsModel();
        VaccineApplicationsModel application2 = new VaccineApplicationsModel();
        Flux<VaccineApplicationsModel> applications = Flux.just(application1, application2);

        when(vaccineApplicationsRepository.findAll()).thenReturn(applications);

        Flux<VaccineApplicationsModel> result = vaccineApplicationsServices.getAllApplications();

        StepVerifier.create(result.collectList())
            .expectNextMatches(res -> {
                assertEquals(2, res.size());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testUpdateApplication() {
        Long id = 1L;
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setCycleLifeId(1L);
        CycleLifeDTO cycleLife = new CycleLifeDTO();
        cycleLife.setEndDate(LocalDate.parse("2025-12-31")); // Asegúrate de que sea LocalDate
        cycleLife.setTimesInWeeks("4"); // Usar String si el modelo lo requiere

        when(cycleLifeClient.getCycleLifeFromExternal(application.getCycleLifeId())).thenReturn(Mono.just(cycleLife));
        when(vaccineApplicationsRepository.save(application)).thenReturn(Mono.just(application));

        Mono<VaccineApplicationsModel> result = vaccineApplicationsServices.updateApplication(id, application);

        StepVerifier.create(result)
            .expectNextMatches(res -> {
                assertEquals(cycleLife.getEndDate(), res.getEndDate());
                assertEquals(cycleLife.getTimesInWeeks(), res.getTimesInWeeks());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testDeactivateApplication() {
        Long id = 1L;
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setActive("A");

        when(vaccineApplicationsRepository.findById(id)).thenReturn(Mono.just(application));
        when(vaccineApplicationsRepository.save(application)).thenReturn(Mono.just(application));

        Mono<VaccineApplicationsModel> result = vaccineApplicationsServices.deactivateApplication(id);

        StepVerifier.create(result)
            .expectNextMatches(res -> {
                assertEquals("I", res.getActive());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testActivateApplication() {
        Long id = 1L;
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setActive("I");

        when(vaccineApplicationsRepository.findById(id)).thenReturn(Mono.just(application));
        when(vaccineApplicationsRepository.save(application)).thenReturn(Mono.just(application));

        Mono<VaccineApplicationsModel> result = vaccineApplicationsServices.activateApplication(id);

        StepVerifier.create(result)
            .expectNextMatches(res -> {
                assertEquals("A", res.getActive());
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void testGetCycleLifeIdById() {
        Long cycleLifeId = 1L;
        VaccineApplicationsModel application = new VaccineApplicationsModel();
        application.setCycleLifeId(cycleLifeId);

        when(vaccineApplicationsRepository.findByCycleLifeId(cycleLifeId)).thenReturn(Mono.just(application));

        Mono<VaccineApplicationsModel> result = vaccineApplicationsServices.getCycleLifeIdById(cycleLifeId);

        StepVerifier.create(result)
            .expectNextMatches(res -> {
                assertEquals(application, res);
                return true;
            })
            .verifyComplete();
    }
}
