package com.ngomalalibo.stocktradingapp.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngomalalibo.stocktradingapp.entities.Stock;
import de.undercouch.bson4jackson.BsonFactory;
import org.bson.BsonBinaryWriter;
import org.bson.Document;
import org.bson.codecs.DocumentCodec;
import org.bson.codecs.EncoderContext;
import org.bson.io.BasicOutputBuffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

//MongoDB Document to JSonNode
public class DocumentToJsonNodeConverter
{
    public static JsonNode documentToJsonNode(final Document document) throws IOException
    {
        BasicOutputBuffer outputBuffer = new BasicOutputBuffer();
        BsonBinaryWriter writer = new BsonBinaryWriter(outputBuffer);
        
        //This codec builder is essential for the working of this method
        new DocumentCodec().encode(writer, document, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
        
        InputStream inputStream = new ByteArrayInputStream(outputBuffer.toByteArray());
        
        ObjectMapper mapper = new ObjectMapper(new BsonFactory());
        mapper.writerWithDefaultPrettyPrinter();
        return mapper.readTree(inputStream);
    }
    
    public static void main(String[] args)
    {
        try
        {
            Stock note = new Stock("nflx", 500D);
            Document doc = POJOToDocumentConverter.pojoToDocumentConverter(note);
            JsonNode jsonNode = documentToJsonNode(doc);
            System.out.println("jsonnode -> " + jsonNode);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}

/*

public static InputStream documentToInputStream(final Document document) {
    BasicOutputBuffer outputBuffer = new BasicOutputBuffer();
    BsonBinaryWriter writer = new BsonBinaryWriter(outputBuffer);
    new DocumentCodec().encode(writer, document, EncoderContext.builder().isEncodingCollectibleDocument(true).build());
    return new ByteArrayInputStream(outputBuffer.toByteArray());
}

public static JsonNode documentToJsonNode(final Document document) throws IOException {
    ObjectMapper mapper = new ObjectMapper(new BsonFactory());
    InputStream is = documentToInputStream(document);
    return mapper.readTree(is);
}
*/
