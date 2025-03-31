package app;

import app.model.Reservation;
import app.model.TableEntity;
import app.repository.ReservationRepository;
import app.repository.TableRepository;
import app.service.ReservationService;
import app.web.DTO.ReservationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private TableRepository tableRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RestTemplate restTemplate;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                tableRepository,
                reservationRepository,
                restTemplate
        );
    }


    @Test
    void createReservation_WhenTableAvailable_ReturnsReservation() {

        ReservationRequest request = new ReservationRequest(
                "Иван", "0881234567", "ivan@test.com",
                LocalDate.now().plusDays(1), 4, "Специални изисквания"
        );

        TableEntity availableTable = new TableEntity();
        availableTable.setId(1L);
        availableTable.setCapacity(4);
        availableTable.setAvailable(true);

        when(tableRepository.findByCapacityGreaterThanEqualAndIsAvailableTrue(4))
                .thenReturn(List.of(availableTable));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<Reservation> result = reservationService.createReservation(request);


        assertTrue(result.isPresent());

        Reservation savedReservation = result.get();
        assertEquals(request.getUserName(), savedReservation.getUserName());
        assertEquals(availableTable.getId(), savedReservation.getTableId());
        assertFalse(availableTable.isAvailable());

        verify(tableRepository).save(availableTable);
        verify(reservationRepository).save(savedReservation);
    }

    @Test
    void createReservation_WhenNoTablesAvailable_ReturnsEmpty() {

        ReservationRequest request = new ReservationRequest(
                "Иван", "0881234567", "ivan@test.com",
                LocalDate.now().plusDays(1), 4, ""
        );

        when(tableRepository.findByCapacityGreaterThanEqualAndIsAvailableTrue(4))
                .thenReturn(List.of());


        Optional<Reservation> result = reservationService.createReservation(request);


        assertTrue(result.isEmpty());
        verify(reservationRepository, never()).save(any());
    }


    @Test
    void deleteOldReservations_WhenNoOldReservations_DoesNothing() {
        when(reservationRepository.findAll())
                .thenReturn(List.of(createTestReservation(LocalDate.now(), 3L)));

        reservationService.deleteOldReservations();

        verify(reservationRepository, never()).delete(any());
        verify(tableRepository, never()).save(any());
    }

    private Reservation createTestReservation(LocalDate date, Long tableId) {
        Reservation reservation = new Reservation();
        reservation.setDate(date);
        reservation.setTableId(tableId);
        return reservation;
    }
}
