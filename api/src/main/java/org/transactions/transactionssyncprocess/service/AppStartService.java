package org.transactions.transactionssyncprocess.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.transactions.sync.ISyncService;

@Service
public class AppStartService {

    private final ISyncService syncService;

    @Autowired
    public AppStartService(ISyncService syncService){
        this.syncService = syncService;
    }

    public void start() {
        syncService.syncDatabase();
    }
}
