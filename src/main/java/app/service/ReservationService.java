package app.service;

import app.EmailClient;
import app.model.Reservation;
import app.model.TableEntity;
import app.repository.ReservationRepository;
import app.repository.TableRepository;
import app.web.DTO.ReservationRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ReservationService {

    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    private static final String EMAIL_API_URL = "https://external-email-api.com/send";

    public ReservationService(TableRepository tableRepository, ReservationRepository reservationRepository, RestTemplate restTemplate) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Optional<Reservation> createReservation(@RequestBody ReservationRequest reservationRequest) {
        List<TableEntity> availableTables = tableRepository.findByCapacityGreaterThanEqualAndIsAvailableTrue(reservationRequest.getGuests());
        if (availableTables.isEmpty()) {
            return Optional.empty();
        }

        TableEntity table = availableTables.get(0);
        table.setAvailable(false);
        tableRepository.save(table);

        Reservation reservation = new Reservation();
        reservation.setTableId(table.getId());
        reservation.setUserName(reservationRequest.getUserName());
        reservation.setUserPhone(reservationRequest.getUserPhone());
        reservation.setUserEmail(reservationRequest.getUserEmail());
        reservation.setDate(reservationRequest.getDate());
        reservation.setGuests(reservationRequest.getGuests());
        reservation.setMessage(reservationRequest.getMessage());

        table.setAvailable(false); //запазваме масата
        reservationRepository.save(reservation); //Записваме резервацията

        return Optional.of(reservation);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight
    public void deleteOldReservations () {
       reservationRepository.findAll().forEach(reservation -> {
           if (reservation.getDate().isBefore(LocalDate.now().minusDays(1))) {
               reservationRepository.delete(reservation);
               tableRepository.findById(reservation.getTableId()).ifPresent(table -> table.setAvailable(true));
           }
       });
    }

}