package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.entity.ClientPortfolio;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entity.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.exception.InsufficientCaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
//@RequiredArgsConstructor
public class PeriodicPortfolioService implements TransactionService
{
    private PersistingBaseEntity persistingBaseEntity;
    private PortfolioService portfolioService;
    private ClientTransactionsService clientTransactionsService;
    
    @Autowired
    public PeriodicPortfolioService(PersistingBaseEntity persistingBaseEntity, PortfolioService portfolioService, ClientTransactionsService clientTransactionsService)
    {
        this.persistingBaseEntity=persistingBaseEntity;
        this.portfolioService=portfolioService;
        this.clientTransactionsService=clientTransactionsService;
        
    }
    
    @Override
    public Object service(Map<String, Object> params)
    {
        LocalDateTime from = LocalDateTime.parse(params.get("from").toString());
        LocalDateTime to = LocalDateTime.parse(params.get("to").toString());
        String username = params.get("username").toString();
        
        Map<String, Object> alltransMap = new HashMap<String, Object>()
        {{
            put("username", username);
        }};
        List<ClientTransaction> allClientTransactions = (List<ClientTransaction>) clientTransactionsService.service(alltransMap);
        if (from.isAfter(to))
        {
            throw new InsufficientCaseException(new RuntimeException("Please provide a correct date range to get portfolio for period"));
        }
        List<ClientTransaction> periodicListOfClientTrans
                = allClientTransactions.stream().filter(trans -> trans.getCreatedDate().isAfter(from) && trans.getCreatedDate().isBefore(to)).collect(Collectors.toList());
        
        Map<String, Object> periodictransMap = new HashMap<String, Object>()
        {{
            put("periodicListOfClientTrans", periodicListOfClientTrans);
            put("username", username);
        }};
        ClientPortfolio portfolio = (ClientPortfolio) portfolioService.service(periodictransMap);
        ClientPortfolio savedPortfolio = (ClientPortfolio) persistingBaseEntity.save(portfolio);
        if (savedPortfolio != null)
        {
            return savedPortfolio;
        }
        return null;
    }
}
