package app.repository;

import app.model.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

    List<TableEntity> findByCapacityGreaterThanEqualAndIsAvailableTrue(int capacity);
}
