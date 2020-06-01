package com.ngomalalibo.stocktradingapp.service;

import com.ngomalalibo.stocktradingapp.entity.ClientPortfolio;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entity.PersistingBaseEntity;
import com.ngomalalibo.stocktradingapp.entity.StockQuote;
import com.ngomalalibo.stocktradingapp.enumeration.TransactionType;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.exception.InsufficientCaseException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

// @Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService
{
    Logger log = LoggerFactory.getLogger(PortfolioService.class);
    
    public ClientPortfolio getPortfolio(List<ClientTransaction> allClientTransactions, String username) throws CustomNullPointerException, InsufficientCaseException
    {
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
        portfolio.setStocks(new ClientStocksService().getClientStocks(username));
        
        //get no of units owned per stock, get current stock price. Multiply both value for each stock to get value of portfolio
        if (portfolio.getStocks() != null)
        {
            portfolio.getStocks().forEach(stock ->
                                          {
                                              Integer noOfUnits = allClientTransactions.stream() // stream all client transactions
                                                                                       .filter(trans -> stock.equals(trans.getStockQuote().getSecurityName())) // get transactions for current stock
                                                                                       .map(ClientTransaction::getNoOfUnits).reduce(Integer::sum).orElse(0); // get no of units
                                              StockQuote stockQuote1 = new StockQuoteService().getStock(stock);// get current price of stock
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
    
    public ClientPortfolio getPortfolioForPeriod(List<ClientTransaction> allClientTransactions, String username, LocalDateTime from, LocalDateTime to) throws InsufficientCaseException
    {
        if (from.isAfter(to))
        {
            throw new InsufficientCaseException(new RuntimeException("Please provide a correct date range to get portfolio for period"));
        }
        List<ClientTransaction> periodicListOfClientTrans
                = allClientTransactions.stream().filter(trans -> trans.getCreatedDate().isAfter(from) && trans.getCreatedDate().isBefore(to)).collect(Collectors.toList());
        
        ClientPortfolio portfolio = getPortfolio(periodicListOfClientTrans, username);
        ClientPortfolio savedPortfolio = (ClientPortfolio) portfolio.save(portfolio);
        if (savedPortfolio != null)
        {
            return savedPortfolio;
        }
        return null;
    }
}
