package org.transactions.sync.entities;


import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Keep Records of all migration launched
 */
@Entity(name = "migration_history")
public class MigrationHistory {

    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private OffsetDateTime date;

    private Long duration;

    @Column(name = "nb_record_migrated")
    private Long nbRecordMigrated;

    private String status;

    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MigrationData> migrationData;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getNbRecordMigrated() {
        return nbRecordMigrated;
    }

    public void setNbRecordMigrated(Long nbRecordMigrated) {
        this.nbRecordMigrated = nbRecordMigrated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MigrationData> getMigrationData() {
        return migrationData;
    }

    public void setMigrationData(List<MigrationData> migrationData) {
        this.migrationData = migrationData;
    }
}
