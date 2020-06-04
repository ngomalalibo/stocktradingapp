package com.ngomalalibo.stocktradingapp.controller;

import com.ngomalalibo.stocktradingapp.exception.ApiResponse;
import com.ngomalalibo.stocktradingapp.serviceImpl.BuyService;
import com.ngomalalibo.stocktradingapp.serviceImpl.FundAccountService;
import com.ngomalalibo.stocktradingapp.serviceImpl.SellService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
public class TransactionController
{
    @Autowired
    BuyService buyService;
    
    @Autowired
    SellService sellService;
    
    @Autowired
    FundAccountService fundAccountService;
    
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
        boolean successful;
        Object transactionType = request.get("transactiontype");
        if (transactionType != null)
        {
            if (transactionType.toString().equalsIgnoreCase("buy"))
            {
                successful = buyService.buy(request.get("companyname").toString(), request.get("username").toString(), Integer.parseInt(request.get("units").toString()));
                
            }
            else if (transactionType.toString().equalsIgnoreCase("sell"))
            {
                successful = sellService.sell(request.get("companyname").toString(), request.get("username").toString(), Integer.parseInt(request.get("units").toString()));
            }
            else if (transactionType.toString().equalsIgnoreCase("fundaccount"))
            {
                successful = fundAccountService.fundAccount(request.get("user").toString(), Double.parseDouble(request.get("deposit").toString()));
                if (successful)
                {
                    ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Account funded successfully", HttpStatus.OK.getReasonPhrase());
                    return ok(apiResponse);
                }
                else
                {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Account funding failed");
                }
            }
            else
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide a valid transaction type. Transaction types Supported are buy or sell.");
            }
            if (successful)
            {
                ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Transaction completed successfully", HttpStatus.OK.getReasonPhrase());
                return ok(apiResponse);
            }
            else
            {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Transaction failed");
            }
        }
        else
        {
            return ResponseEntity.badRequest().body("Please provide a valid transaction type. Transaction types Supported are buy or sell");
        }
    }
}
