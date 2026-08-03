package org.transactions.transactionssyncprocess.clients.transactions;

import org.springframework.stereotype.Component;
import org.transactions.clients.transactions.api.TransactionsApi;
import org.transactions.clients.transactions.model.Transaction;

import java.util.List;

@Component
public class TransactionRestClient {

    private final TransactionsApi transactionsApi;

    public TransactionRestClient(TransactionsApi transactionsApi) {
        this.transactionsApi = transactionsApi;
    }

    public Transaction createTransaction(Transaction transaction){
        // TODO : Handle errors
        return transactionsApi.createTransaction(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionsApi.getAll();
    }
}
