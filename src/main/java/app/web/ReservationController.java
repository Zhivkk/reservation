package app.web;

import app.service.ReservationService;
import app.web.DTO.ReservationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    ReservationController(ReservationService reservationService) {

        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<String> makeReservation(@RequestBody ReservationRequest reservationRequest) {

        reservationService.createReservation(reservationRequest);
        return ResponseEntity.ok("Reservation created successfully");
    }
}


