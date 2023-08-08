package org.transactions.persistence.model;

import org.model.transactions.BankAccount;
import org.model.transactions.TransactionCategory;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.OffsetDateTime;
import java.util.Objects;

@Document(indexName = "#{@elasticSearchDatabaseConfig.getIndex()}")
public class TransactionES {

    private String id;

    private String description;

    private BankAccount from;

    private BankAccount to;

    private TransactionCategory category;

    private Float costDecimal;

    private Float costAbsDecimal;

    private Long cost;

    private Long costAbs;

    private Float income;

    private Float outcome;

    private OffsetDateTime dateTime;

    private String dateTimeFormatted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankAccount getFrom() {
        return from;
    }

    public void setFrom(BankAccount from) {
        this.from = from;
    }

    public BankAccount getTo() {
        return to;
    }

    public void setTo(BankAccount to) {
        this.to = to;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public Float getCostDecimal() {
        return costDecimal;
    }

    public void setCostDecimal(Float costDecimal) {
        this.costDecimal = costDecimal;
    }

    public Float getCostAbsDecimal() {
        return costAbsDecimal;
    }

    public void setCostAbsDecimal(Float costAbsDecimal) {
        this.costAbsDecimal = costAbsDecimal;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Float getIncome() {
        return income;
    }

    public void setIncome(Float income) {
        this.income = income;
    }

    public Float getOutcome() {
        return outcome;
    }

    public void setOutcome(Float outcome) {
        this.outcome = outcome;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(OffsetDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getCostAbs() {
        return costAbs;
    }

    public void setCostAbs(Long costAbs) {
        this.costAbs = costAbs;
    }

    public String getDateTimeFormatted() {
        return dateTimeFormatted;
    }

    public void setDateTimeFormatted(String dateTimeFormatted) {
        this.dateTimeFormatted = dateTimeFormatted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionES that = (TransactionES) o;
        return  Objects.equals(description, that.description) &&
                Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                Objects.equals(category, that.category) &&
                Objects.equals(costDecimal, that.costDecimal) &&
                Objects.equals(costAbsDecimal, that.costAbsDecimal) &&
                Objects.equals(cost, that.cost) &&
                Objects.equals(costAbs, that.costAbs) &&
                Objects.equals(income, that.income) &&
                Objects.equals(outcome, that.outcome) &&
                Objects.equals(dateTime, that.dateTime) &&
                Objects.equals(dateTimeFormatted, that.dateTimeFormatted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, from, to, category, costDecimal, costAbsDecimal, cost, costAbs, income, outcome, dateTime, dateTimeFormatted);
    }

    @Override
    public String toString() {
        return "TransactionES{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", category=" + category +
                ", costDecimal=" + costDecimal +
                ", costAbsDecimal=" + costAbsDecimal +
                ", cost=" + cost +
                ", costAbs=" + costAbs +
                ", income=" + income +
                ", outcome=" + outcome +
                ", dateTime=" + dateTime +
                '}';
    }
}
