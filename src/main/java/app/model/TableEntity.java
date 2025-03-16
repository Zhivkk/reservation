package app.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "tables")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int capacity;
    private boolean isAvailable;
}