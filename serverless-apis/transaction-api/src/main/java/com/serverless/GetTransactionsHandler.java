package com.serverless;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import com.fasterxml.*;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.data.Transaction;
import com.serverless.db.DynamoDBAdapter;

public class GetTransactionsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {LOG.info("received: " + input);
	  List<Transaction> tx;
	  try {
	      Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
	      String accountId = pathParameters.get("account_id");
	      LOG.info("Getting transactions for " + accountId);
	      tx = DynamoDBAdapter.getInstance().getTransactions(accountId);
	    } catch (Exception e) {
	      LOG.error(e, e);
	      Response responseBody = new Response("Failure getting transactions", input);
	      return ApiGatewayResponse.builder()
	      .setStatusCode(500)
	      .setObjectBody(responseBody)
	      .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
	      .build();
	   }
	    return ApiGatewayResponse.builder()
	    .setStatusCode(200)
	    .setObjectBody(tx)
	    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
	    .build();}
}
	