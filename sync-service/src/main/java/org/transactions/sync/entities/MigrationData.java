package org.transactions.sync.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "migration_data")
public class MigrationData {

    @Id
    @Column(columnDefinition = "uuid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "from_id")
    private String fromIdRecord;

    @Column(name = "to_id")
    private String toIdRecord;

    private String status;

    @ManyToOne
    @JoinColumn(name = "history_id")
    private MigrationHistory history;



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFromIdRecord() {
        return fromIdRecord;
    }

    public void setFromIdRecord(String fromIdRecord) {
        this.fromIdRecord = fromIdRecord;
    }

    public String getToIdRecord() {
        return toIdRecord;
    }

    public void setToIdRecord(String toIdRecord) {
        this.toIdRecord = toIdRecord;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MigrationHistory getHistory() {
        return history;
    }

    public void setHistory(MigrationHistory history) {
        this.history = history;
    }
}
