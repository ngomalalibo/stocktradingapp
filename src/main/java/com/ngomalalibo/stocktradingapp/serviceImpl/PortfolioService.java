package com.ngomalalibo.stocktradingapp.serviceImpl;

import com.ngomalalibo.stocktradingapp.entity.ClientPortfolio;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entity.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.entity.StockQuote;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import com.ngomalalibo.stocktradingapp.exception.InsufficientCaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

// @Slf4j
@Service
@Qualifier("portfolio")
//@RequiredArgsConstructor
public class PortfolioService implements TransactionService
{
    Logger log = LoggerFactory.getLogger(PortfolioService.class);
    
    @Override
    public Object service(Map<String, Object> params)
    {
        List<ClientTransaction> allClientTransactions = (List<ClientTransaction>) params.get("allClientTransactions");
        String username = params.get("username").toString();
    
        ClientPortfolio portfolio = new ClientPortfolio();
        Double totalAmountInvested = 0D;
        AtomicReference<Double> totalValueOfPortfolio = new AtomicReference<>(0D);
        Double profitFromSales = 0D;
        LocalDateTime firstTransactionDate;
        
        String evaluation;
        
        //if no transactions, return with message of no transactions for current user
        if (allClientTransactions == null || allClientTransactions.size() == 0)
        {
            throw new InsufficientCaseException(new RuntimeException("No transactions for current user"));
        }
        
        //total amount invested in buying stocks
        totalAmountInvested = allClientTransactions.stream().filter(trans -> trans.getTransactionType().equals(TransactionType.BUY))
                                                   .map(ClientTransaction::getTransactionAmount).reduce(totalAmountInvested, Double::sum);
        portfolio.setTotalAmountInvested(totalAmountInvested); // add total amount invested to portfolio
        
        // get all unique client stocks and add to portfolio summary by calling
        Map<String, Object> stocks = new HashMap<String, Object>()
        {{
            put("username", username);
        }};
        portfolio.setStocks((Set<String>) new ClientStocksService().service(stocks));
        
        //get no of units owned per stock, get current stock price. Multiply both value for each stock to get value of portfolio
        if (portfolio.getStocks() != null)
        {
            Map<String, Object> req = new HashMap<String, Object>();
            portfolio.getStocks().forEach(stock ->
                                          {
                                              Integer noOfUnits = allClientTransactions.stream() // stream all client transactions
                                                                                       .filter(trans -> stock.equals(trans.getStockQuote().getSecurityName())) // get transactions for current stock
                                                                                       .map(ClientTransaction::getNoOfUnits).reduce(Integer::sum).orElse(0); // get no of units
                
                                              req.putIfAbsent("companyname", stock);
                                              StockQuote stockQuote1 = (StockQuote) new StockQuoteService().service(req);// get current price of stock
                                              if (stockQuote1 != null)
                                              {
                                                  Double currentPrice = stockQuote1.getUnitSharePrice();
                                                  // calculate value of stock and accumulate value of all stocks through each repetition of the foreach loop.
                                                  totalValueOfPortfolio.updateAndGet(accumulatedTotal -> accumulatedTotal + (noOfUnits * currentPrice));
                                              }
                                              /*else
                                              {
                                                  throw new CustomNullPointerException("Error while calculating polio value. Issue with stock: " + stock);
                                              }*/
                                          });
            portfolio.setCurrentValueOfPortfolio(totalValueOfPortfolio.get());
        }
        
        
        // get all transactions of type SELL and add them together to get profit from sales
        allClientTransactions.stream().filter(trans -> trans.getTransactionType().equals(TransactionType.SELL)).map(ClientTransaction::getTransactionAmount).reduce(profitFromSales, Double::sum);
        log.info("profit from sales -> " + profitFromSales);
        portfolio.setProfitFromSales(profitFromSales);
        
        // get date of first Transaction or todays date if unavailable
        firstTransactionDate = allClientTransactions.stream().map(PersistingBaseEntity::getCreatedDate).min(Comparator.naturalOrder()).orElse(LocalDateTime.now());
        portfolio.setDateOfAcquisition(firstTransactionDate);
        portfolio.setUsername(username);
        portfolio.setEvaluation("To be evaluated");
        
        ClientPortfolio savedPortfolio = (ClientPortfolio) portfolio.save(portfolio);// persist portfolio to database
        
        if (savedPortfolio != null)
        {
            return savedPortfolio;
        }
        return null;
    }
}
