package app.web;

import app.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reservations")
class ReservationController {

    private final ReservationService reservationService;

    ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public String makeReservation(@RequestParam String userName, @RequestParam String userPhone,
                                  @RequestParam String userEmail, @RequestParam String date, // Приемаме като String
                                  @RequestParam int guests, @RequestParam String message) {
        try {
            LocalDate parsedDate = LocalDate.parse(date); // Парсиране на датата
            return reservationService.createReservation(userName, userPhone, userEmail, parsedDate, guests, message)
                    .map(res -> "Reservation successful for table " + res.getTableId())
                    .orElse("No available table for the requested date and guests");
        } catch (Exception e) {
            return "Invalid date format. Please use YYYY-MM-DD.";
        }
    }


}


