package app;

import app.model.Reservation;
import app.repository.ReservationRepository;
import app.service.ReservationService;
import app.web.DTO.ReservationRequest;
import app.web.ReservationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationControllerApiTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    void testMakeReservation() {

        ReservationRequest reservationRequest = new ReservationRequest("John Doe", "123456789", "john.doe@example.com", null, 2, "Test message");
        when(reservationService.createReservation(any())).thenReturn(Optional.of(new Reservation()));

        ResponseEntity<String> response = reservationController.makeReservation(reservationRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Reservation created successfully", response.getBody());
    }

    @Test
    void testMakeReservation_InvalidRequest() {

        ReservationRequest reservationRequest = new ReservationRequest(null, null, null, null, 0, null);

        ResponseEntity<String> response = reservationController.makeReservation(reservationRequest);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetAllReservations() {

        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(reservations);

        ResponseEntity<List<Reservation>> response = reservationController.getAllReservations();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(reservations, response.getBody());
    }
}
