package com.ngomalalibo.stocktradingapp.config;

import com.ngomalalibo.stocktradingapp.entity.Client;
import com.ngomalalibo.stocktradingapp.entity.ClientAccount;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import com.ngomalalibo.stocktradingapp.serviceImpl.BuyService;
import com.ngomalalibo.stocktradingapp.serviceImpl.FundAccountService;
import com.ngomalalibo.stocktradingapp.serviceImpl.SellService;
import com.ngomalalibo.stocktradingapp.serviceImpl.TransactionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataConfig
{
    
    @Bean
    public GenericDataRepository userDataRepository()
    {
        return new GenericDataRepository(new User());
    }
    
    @Bean
    public GenericDataRepository clientDataRepository()
    {
        return new GenericDataRepository(new Client());
    }
    
    @Bean
    public GenericDataRepository clientAccountDataRepository()
    {
        return new GenericDataRepository(new ClientAccount());
    }
    
    @Bean
    public GenericDataRepository clientTransactionsDataRepository()
    {
        return new GenericDataRepository(new ClientTransaction());
    }
    
    @Bean
    public TransactionService buyService(@Qualifier("userDataRepository") GenericDataRepository userDataRepository,
                                         @Qualifier("clientDataRepository") GenericDataRepository clientDataRepository,
                                         @Qualifier("clientAccountDataRepository") GenericDataRepository clientAccountDataRepository,
                                         @Qualifier("clientTransactionsDataRepository") GenericDataRepository clientTransactionDataRepository)
    {
        return new BuyService(userDataRepository, clientDataRepository, clientAccountDataRepository, clientTransactionDataRepository);
    }
    
    @Bean
    public TransactionService sellService(@Qualifier("userDataRepository") GenericDataRepository userDataRepository,
                                          @Qualifier("clientDataRepository") GenericDataRepository clientDataRepository,
                                          @Qualifier("clientAccountDataRepository") GenericDataRepository clientAccountDataRepository,
                                          @Qualifier("clientTransactionsDataRepository") GenericDataRepository clientTransactionDataRepository)
    {
        return new SellService(userDataRepository, clientDataRepository, clientAccountDataRepository, clientTransactionDataRepository);
    }
    
    @Bean
    public TransactionService fundAccountService(@Qualifier("userDataRepository") GenericDataRepository userDataRepository,
                                                 @Qualifier("clientDataRepository") GenericDataRepository clientDataRepository,
                                                 @Qualifier("clientAccountDataRepository") GenericDataRepository clientAccountDataRepository)
    {
        return new FundAccountService(userDataRepository, clientDataRepository, clientAccountDataRepository);
    }
}
