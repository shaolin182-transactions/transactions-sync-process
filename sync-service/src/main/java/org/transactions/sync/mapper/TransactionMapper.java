package org.transactions.sync.mapper;

import org.mapstruct.Mapper;
import org.model.transactions.Transaction;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // Transaction
    org.transactions.clients.transactions.model.Transaction transactionToRest(Transaction transaction);
    Transaction transactionFromRest(org.transactions.clients.transactions.model.Transaction transaction);

}
