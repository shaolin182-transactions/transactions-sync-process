package org.transactions.sync.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.transactions.sync.entities.MigrationData;

import java.util.Optional;
import java.util.UUID;

public interface MigrationDataRepository extends CrudRepository<MigrationData, UUID> {

    @Query("SELECT d FROM migration_data d WHERE d.fromIdRecord = ?1 AND d.status = ?2")
    Optional<MigrationData> findByFromIdAndStatus(String fromId, String status);
}
