package com.ngomalalibo.stocktradingapp.controller;

import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.exception.ApiResponse;
import com.ngomalalibo.stocktradingapp.factory.TransactionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
public class TransactionController
{
    @Autowired
    TransactionFactory transactionFactory;
    /*@Qualifier("getApplicationContext")
    @Autowired
    private ApplicationContext applicationContext;
    */
    /**
     * @Getter class Pojo {
     * String type;
     * }
     * <p>
     * interface Transctn{
     * String apply(String param);
     * }
     * <p>
     * private Map<String,Transctn> txnMap  = new HashMap<>();
     * @PostConstruct public void init(){
     * txnMap.put("buy", (e ) -> "");
     * txnMap.put("sell", (e ) -> "");
     * txnMap.put("fund", (e ) -> "");
     * }
     * @PostMapping public ResponseEntity<?> txn(Pojo pojo) {
     * <p>
     * txnMap.get(pojo.getType()).apply("");
     * <p>
     * <p>
     * }
     */
    
    @PostMapping("/transaction")
    public ResponseEntity<Object> transaction(@RequestBody HashMap<String, Object> request)
    {
        ClientTransaction transaction = transactionFactory.createTransaction(request);
        // ClientTransaction transaction = applicationContext.getBean(TransactionFactory.class).createTransaction(request);
        if (transaction != null)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Transaction completed successfully", HttpStatus.OK.getReasonPhrase());
            return ResponseEntity.ok().body(apiResponse);
        }
        else
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Transaction failed", HttpStatus.BAD_REQUEST.getReasonPhrase());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        }
    }
}
