package org.transactions.transactionssyncprocess.clients.transactions;

import org.springframework.stereotype.Component;
import org.transactions.clients.transactions.api.TransactionsApi;
import org.transactions.clients.transactions.model.Transaction;

@Component
public class TransactionRestClient {

    private final TransactionsApi transactionsApi;

    public TransactionRestClient(TransactionsApi transactionsApi) {
        this.transactionsApi = transactionsApi;
    }

    public Transaction createTransaction(Transaction transaction){
        return transactionsApi.createTransaction(transaction);
    }
}
