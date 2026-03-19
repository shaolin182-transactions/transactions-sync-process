package org.transactions.sync.repositories;

import org.springframework.data.repository.CrudRepository;
import org.transactions.sync.entities.MigrationHistory;

import java.util.UUID;

public interface MigrationHistoryRepository extends CrudRepository<MigrationHistory, UUID> {
}
