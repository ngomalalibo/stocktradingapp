package com.ngomalalibo.stocktradingapp.apiclient;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ngomalalibo.stocktradingapp.entity.ClientAccount;
import com.ngomalalibo.stocktradingapp.entity.ClientTransaction;
import com.ngomalalibo.stocktradingapp.entity.StockQuote;
import com.ngomalalibo.stocktradingapp.entity.User;
import com.ngomalalibo.stocktradingapp.exception.CustomNullPointerException;
import com.ngomalalibo.stocktradingapp.repository.GenericDataRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.manager.RestRequest;
import pl.zankowski.iextrading4j.client.rest.request.IEXCloudV1RestRequest;
import pl.zankowski.iextrading4j.client.rest.request.stocks.PriceRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

@Data
@Slf4j
@RequiredArgsConstructor
public class StockQuoteApiClient implements IEXCloudV1RestRequest
{
    private static Client client;
    private static WebTarget target;
    
    public static String SECRET_STOCK_API_KEY = System.getenv().get("SECRET_STOCK_API_KEY");
    public static String PUBLISHABLE_STOCK_API_KEY = System.getenv().get("PUBLISHABLE_STOCK_API_KEY");
    public static String URL_ADD_COMPANY_TOKEN = "https://cloud-sse.iexapis.com/stable/stock/%s/quote?token=%s";
    public static String STOCK_URL = "";
    
    private static GenericDataRepository userGDS = new GenericDataRepository(new User());
    private static GenericDataRepository clientGDS = new GenericDataRepository(new com.ngomalalibo.stocktradingapp.entity.Client());
    private static GenericDataRepository clientAccountGDS = new GenericDataRepository(new ClientAccount());
    private static GenericDataRepository transactionsGDS = new GenericDataRepository(new ClientTransaction());
    
    protected void setCompanyURL(String company)
    {
        client = ClientBuilder.newClient();
        STOCK_URL = String.format(URL_ADD_COMPANY_TOKEN.trim(), company, PUBLISHABLE_STOCK_API_KEY);
        target = client.target(URL_ADD_COMPANY_TOKEN);
    }
    
    public StockQuote getStock(String company) throws CustomNullPointerException
    {
        if (company == null)
        {
            throw new CustomNullPointerException("Please provide a company name to get its stock price");
        }
        // provide page number to url
        setCompanyURL(company);
        
        URL url = null;
        
        //get stock result with list of stocks
        
        //date formatter to retrieve updatedDate
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        
        try
        {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            
            // send GET request to retrieve data from API as a request for JSON
            // log.info("STOCK URL -> " + STOCK_URL);
            url = new URL(STOCK_URL);
            URLConnection yc = url.openConnection();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("ACCEPT", "application/json");
            
            // log.warn("Response code -> " + conn.getResponseCode());
            // log.warn("Response message -> " + conn.getResponseMessage());
            
            // confirm that response is successful with status code 200
            if (conn.getResponseCode() == 200)
            {
                // Create buffered reader to read data
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String output;
                
                if ((output = in.readLine()) != null)
                {
                    // log.info("....");
                    JsonElement je = jp.parse(output);
                    String prettyJsonResponse = gson.toJson(je);
                    log.info("output -> \n" + prettyJsonResponse);
                    
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
                    objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
                    
                    return objectMapper.readValue(output, StockQuote.class);
                    
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        return null;
        
    }
    
    public static Quote getQuote()
    {
        final IEXCloudClient cloudClient = IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_V1/*IEX_CLOUD_BETA_SANDBOX*/,
                                                                   new IEXCloudTokenBuilder()
                                                                           .withPublishableToken(PUBLISHABLE_STOCK_API_KEY)
                                                                           .withSecretToken(SECRET_STOCK_API_KEY)
                                                                           .build());
        final Quote quote = cloudClient.executeRequest(new QuoteRequestBuilder()
                                                               .withSymbol("NFLX")
                                                               .build());
        System.out.println(quote);
        return quote;
    }
    
    @Override
    public RestRequest<BigDecimal> build()
    {
        log.info("Fetching stock price... please wait");
        final IEXCloudClient cloudClient = IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_V1,
                                                                   new IEXCloudTokenBuilder()
                                                                           .withPublishableToken(PUBLISHABLE_STOCK_API_KEY)
                                                                           .withSecretToken(SECRET_STOCK_API_KEY)
                                                                           .build());
        BigDecimal nflx = cloudClient.executeRequest(new PriceRequestBuilder()
                                                             .withSymbol("nflx")
                                                             .build());
        
        
        return null;
    }
    
    public static void main(String[] args)
    {
        log.info("Fetching stock data... please wait");
        
        System.out.println(getQuote().toString());
    }
}
