package app.web;

import app.model.Reservation;
import app.repository.ReservationRepository;
import app.service.ReservationService;
import app.web.DTO.ReservationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;

    ReservationController(ReservationService reservationService, ReservationRepository reservationRepository) {

        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
    }

    @PostMapping
    public ResponseEntity<String> makeReservation(@RequestBody ReservationRequest reservationRequest) {

        reservationService.createReservation(reservationRequest);
        return ResponseEntity.ok("Reservation created successfully");
    }

    @GetMapping("/reserved-tables")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return ResponseEntity.ok(reservations);
    }

}


