package app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableId;
    private String userName;
    private String userPhone;
    private String userEmail;
    private LocalDate date;
    private int guests;
    private String message;

}