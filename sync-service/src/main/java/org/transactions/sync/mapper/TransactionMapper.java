package org.transactions.sync.mapper;

import org.mapstruct.Mapper;
import org.model.transactions.BankAccount;
import org.model.transactions.Transaction;
import org.model.transactions.TransactionCategory;
import org.model.transactions.TransactionDetails;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // Transaction
    org.transactions.clients.transactions.model.Transaction transactionToRest(Transaction transaction);
    Transaction transactionFromRest(org.transactions.clients.transactions.model.Transaction transaction);

}
