package com.serverless;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import com.fasterxml.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.serverless.data.KYCDocuments;
import com.serverless.data.Transaction;
import com.serverless.db.DynamoDBAdapter;

public class AddKYCHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) { LOG.info("received: " + input);
    
    try{
      ObjectMapper mapper = new ObjectMapper();
      Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
      
      KYCDocuments tx = new KYCDocuments();
     
      JsonNode body = mapper.readTree((String) input.get("body"));
 
      String type = body.get("kyctype").asText();
      tx.setKyctype(type);
      JsonNode nodedata=body.get("data");
      String nodeData = nodedata.toString();
      System.out.println("JSONNode----->"+nodeData);
      
      tx.setData(nodeData);

      tx.setKycdocument("document");
   
      DynamoDBAdapter.getInstance().putTransaction(tx);
    } catch(Exception e){
      LOG.error(e,e);
      Response responseBody = new Response("Failure putting KYC details", input);
      return ApiGatewayResponse.builder()
      .setStatusCode(500)
      .setObjectBody(responseBody)
      .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
      .build();
    }
    Response responseBody = new Response("KYC   added successfully!", input);
    return ApiGatewayResponse.builder()
    .setStatusCode(200)
    .setObjectBody(responseBody)
    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
    .build();
    }
}
	