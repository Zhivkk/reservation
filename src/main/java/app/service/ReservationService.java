package app.service;

import app.EmailClient;
import app.model.Reservation;
import app.model.TableEntity;
import app.repository.ReservationRepository;
import app.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservationService {

    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;
    private final RestTemplate restTemplate;

    private static final String EMAIL_API_URL = "https://external-email-api.com/send";

    public ReservationService(TableRepository tableRepository, ReservationRepository reservationRepository, RestTemplate restTemplate) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
        this.restTemplate = restTemplate;
    }

    public Optional<Reservation> createReservation(String userName, String userPhone, String userEmail, LocalDate date, int guests, String message) {
        List<TableEntity> availableTables = tableRepository.findByCapacityGreaterThanEqualAndIsAvailableTrue(guests);
        if (availableTables.isEmpty()) {
            return Optional.empty();
        }
        TableEntity table = availableTables.get(0);
        table.setAvailable(false);
        tableRepository.save(table);

        Reservation reservation = new Reservation();
        reservation.setTableId(table.getId());
        reservation.setUserName(userName);
        reservation.setUserPhone(userPhone);
        reservation.setUserEmail(userEmail);
        reservation.setDate(date);
        reservation.setGuests(guests);
        reservation.setMessage(message);
        reservationRepository.save(reservation);

        return Optional.of(reservation);
    }

}