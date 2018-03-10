package com.serverless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransferAPIHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(Handler.class);	
	static String OAUTH_OKEN_URL="<url>"; 
	private String token = "";
	String bankId = "";
	String accountNumber = "";
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("INSIDE BASE CLASS");
	     Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
	     ObjectMapper mapper = new ObjectMapper();
	     JsonNode body = null;
	     if(mapper!=null){
		     try {
				 body = mapper.readTree((String) input.get("body"));
			} catch (JsonProcessingException e1) {
				body = null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				body = null;
			}
	     }
	     bankId = pathParameters.get("bankid");
	     accountNumber = pathParameters.get("accountnumber");
		BasicConfigurator.configure();
		token = generateToken();
		LOG.info("received token is: " + token);
		String myString = "Default String";
		try {
			LOG.info("beforecalling  transferRequest ");
			myString = transferRequest(body);
			LOG.info("After calling  transferRequest ");
		} catch (Exception e) {
			LOG.info("After calling  transferRequest inside exception ");
			e.printStackTrace();
			myString = "Oops Exception occured";
			e.printStackTrace();
		}
		Response responseBody = new Response(myString, input);
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
	}
	final String  ab="<url>";

	public void start(String[] args) {
		// TODO Auto-generated method stub
		if(false){
			//do nothing
		}else{

		String inputUrl = "http://<getUrl>";
		String inputPostUrl ="http://<postUrl>";
		String inputJSON = "{"+
				  "JsonData"+
				"}";

		/*try {*/
			//rest.createConnectionAndInvokeGETREST(inputUrl, "GET");
			String headerInfo="<headerInfo>";
			String headerKey="<headerKeyName>";
			// transaction initiate screen will not pass ChannelContext.
			//rest.createConnectionAndInvokeREST(inputPostUrl,inputJSON,"","", "POST");
			//Confirmation screen passes credentials and header 
			createConnectionAndInvokeREST(inputPostUrl,inputJSON,headerInfo,headerKey, "POST");
		/*} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		}
	}

	
	
	
		
	public String createConnectionAndInvokePUTREST(String inputUrl,String inputJSON) throws Exception{
		/*if(true){
			return "Hello this is me createConnectionAndInvokePUTREST returning response";
		}*/
		HttpURLConnection conn=null;
		String acessToken="oAuthToken";
		String result = "";
		String output;
		try{
			
			URL url = new URL(inputUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty ("Authorization", acessToken);
			OutputStream os = conn.getOutputStream();
			os.write(inputJSON.getBytes());
			os.flush();

			if(conn.getResponseCode() == 200 || conn.getResponseCode() == 201){
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
				while ((output = br.readLine()) != null){
					result = result + output;
				}
			}
			else {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getErrorStream())));			
				while ((output = br.readLine()) != null) {
					result = result + output;
				}
			}

		} catch (MalformedURLException e) {
			throw e;

		} catch (IOException e) {
			throw e;
		}finally{
			conn.disconnect();
		}
		return result;
	}
	public String createConnectionAndInvokeREST(String inputUrl,String inputJSON,String headerInfo,String headerKey, String methodType){
		HttpURLConnection conn=null;
		String outputVal = "1";
		try{
			LOG.info("inside createConnectionAndInvokeREST 1");	
		URL url = new URL(inputUrl);
		outputVal = "2";
		LOG.info("inside createConnectionAndInvokeREST 2");	
	/*	if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
		}
		
	*/	boolean isDelete =false;
		if(methodType == null || methodType.trim().length()==0 || methodType.equalsIgnoreCase("DELETE")){
		methodType =  "PUT";
		isDelete=true;
		}
		 conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(methodType);
			conn.setRequestProperty("Content-Type", "application/json");
			//conn.setRequestProperty ("Authorization", token);
			outputVal = "3";
			LOG.info("inside createConnectionAndInvokeREST 3");	
			if(headerKey!=null && headerKey.trim().length()>0){
				conn.setRequestProperty (headerKey, headerInfo);
			}
			JSONObject json = new JSONObject();
			outputVal = "4";
			LOG.info("inside createConnectionAndInvokeREST 4");	
			if(isDelete){
				//conn.setRequestProperty("X-HTTP-Method-Override", "DELETE");
			}
			OutputStream os = conn.getOutputStream();
			outputVal = "5";
			LOG.info("inside createConnectionAndInvokeREST 5");	
			os.write(inputJSON.getBytes());
			os.flush();
		if(conn.getResponseCode() == 200 || conn.getResponseCode() == 201){
			outputVal = "6";
			LOG.info("inside createConnectionAndInvokeREST 6");	
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
		
				String output;
				String result = "";
				String cacheValue="";
				outputVal = "7";
				LOG.info("inside createConnectionAndInvokeREST 7");	
				while ((output = br.readLine()) != null) {
					result = result+output;
				}
					try {
						outputVal = "8";
						LOG.info("inside createConnectionAndInvokeREST 8");	
						json= new JSONObject(result);
						outputVal=result;
							// cacheValue = (String) ((JSONObject)json.get("data")).get("output");
							
						
					} catch (JSONException e) {
						outputVal = "9";
						LOG.info("inside createConnectionAndInvokeREST 9");	
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				
			return result;

			}
		else if(conn.getResponseCode() == 400){
			outputVal = "10";
			LOG.info("inside createConnectionAndInvokeREST 10");	
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getErrorStream())));			
				String output;
				String result="";
				while ((output = br.readLine()) != null) {
					
					result = result+output;
				}
				return result;
				
		}else if(conn.getResponseCode() == 201){
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			System.out.println("Response header is>>>>"+conn.getHeaderField("<ResponseHeaderKey>"));
			String output;
			String result = "";
			String cacheValue="";
			while ((output = br.readLine()) != null) {
				result = result+output;
				System.out.println("result is;;;;"+result);
			}
				try {
					json = new JSONObject(result);
					
						// cacheValue = (String) ((JSONObject)json.get("data")).get("<JsonKey>");
						
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			
		//	return result;

		}else if(conn.getResponseCode() == 401){
			outputVal = "12";
			LOG.info("inside createConnectionAndInvokeREST 12");	
			System.out.println("Response header is>>>>"+conn.getHeaderField("<ResponseHeaderKey>"));
			
		//	return result;

		}
		
		else{
			/*if (conn.getResponseCode() != 200 && conn.getResponseCode() != 400) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}*/
				//requestStatus="FAL";
			outputVal = "13";
			LOG.info("inside createConnectionAndInvokeREST 13");	
		}
		if(outputVal==null){
			LOG.info("inside createConnectionAndInvokeREST EV");
			outputVal ="EV";
		}
		return outputVal;
	  } catch (MalformedURLException e) {
		  LOG.info("inside MalformedURLException");
		  return "MalformedURLException";
		//e.printStackTrace();
		//requestStatus="FAL";
		//throw e;

	  } catch (IOException e) {
		  //requestStatus="FAL";
		//e.printStackTrace();
		  LOG.info("inside IOException");
		  return "IOException";
		//throw e;

	 }catch(Exception r){
		 outputVal ="I AM EXCEPTION";
		 return outputVal;
	 }
		
		finally{
		 if(outputVal==null){
				LOG.info("inside createConnectionAndInvokeREST EV");
				outputVal ="FINALLY";
			}
		 conn.disconnect();

	 }
	}
	
	
	public String createConnectionAndInvokeGETREST(String inputUrl,String methodType,String token) throws IOException{
		HttpURLConnection conn=null;
		try{
		inputUrl = "https://apisandbox.openbankproject.com/obp/v3.0.0/users/current";
		methodType="GET";
		URL url = new URL(inputUrl);
		String cacheValue = "";
		String acessToken="DirectLogin token=\""+token+"\"";
		

	/*	if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
		}
		
	*/	
		 conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod(methodType);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty ("Authorization", acessToken);
		if(conn.getResponseCode() == 200){
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
		
				String output;
				String result = "";
				while ((output = br.readLine()) != null) {
					result = result+output;
		}
					JSONObject json = new JSONObject();
					try {
						json = new JSONObject(result);
						
						/*JSONObject json1 = (JSONObject) ((JSONArray)json.get("data")).get(0);
						cacheValue = (String)(json1.getString("cacheValue"));
							System.out.println("Cache vakue is???"+cacheValue);*/
							
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						result ="JSON Exception occured";
					}
				
				return result;

			}
		else if(conn.getResponseCode() == 400){
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getErrorStream())));			
				String output;
				while ((output = br.readLine()) != null) {
					JSONObject json;
					try {
					json = new JSONObject(output);
					JSONObject json1= (JSONObject)json.get("status");
					JSONArray json21= (JSONArray)json1.get("message");
					JSONObject json2= (JSONObject)json21.get(0);
					String errCode =  (String)json2.get("messageCode");
					String messageAdditionalInfo = (String)json2.get("messageAddlnInfo");
					String messageType = (String)json2.get("message_TYPE");
					//requestStatus="FAL";
					
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
				return output;
				
		}else{
			/*if (conn.getResponseCode() != 200 && conn.getResponseCode() != 400) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}*/
				//requestStatus="FAL";
			  
		}
		

	  } catch (MalformedURLException e) {

		//e.printStackTrace();
		throw e;

	  } catch (IOException e) {
		//e.printStackTrace();
		throw e;

	 }finally{
		 conn.disconnect();

	 }
		return null;
	}
	
	private String generateToken(){
		String inputUrl ="https://apisandbox.openbankproject.com/my/logins/direct";
		String inputJSON = "";
		String headerInfo="DirectLogin username=\"raviraj\",password=\"Raviraj@007\",consumer_key=\"r0ukdhqdlimeymfk0efvztdbfgo1apz5srwwdivt\"";
		String headerKey = "Authorization";
		String methodType ="POST";
		String token = "";
		String actualToken = "";
		try {
			token = createConnectionAndInvokeREST(inputUrl,inputJSON,headerInfo,headerKey, methodType);
			if(token!=null && token.toString().contains("token")){
				JSONObject obj = new JSONObject(token);
				actualToken = (String)obj.get("token");
			}
		} catch (Throwable e) {
			token = "failedToken";
			e.printStackTrace();
		}
		return actualToken;
	}
	private String transferRequest(JsonNode body){
		String localAccountNumber = Math.random()+"";
		                  //https://apisandbox.openbankproject.com/obp/v3.0.0/banks/in-bank-x-1/accounts/OPENACCOUNT1
		LOG.info("inside transferRequest ");
		String toBankId="";
		String toAccountId = "";
		String currency = "";
		String amount="";
		String description = "";
		try{
			if(body!=null){
				toBankId = (String)body.get("bank_id").asText();
				toAccountId = (String)body.get("account_id").asText();
				currency = (String)body.get("currency").asText();
				amount = (String)body.get("amount").asText();
				description = (String)body.get("description").asText();
			}else{
				toBankId = "gh.29.uk";
				toAccountId = "8ca8a7e4-6d02-48e3-a029-0b2bf89de9f0";
				currency = "EUR";
				amount="90";
				description = "Gift Contribution";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		if(toBankId==null ||toBankId.trim().length()==0 ){
			toBankId = "gh.29.uk";
		}
		if(toAccountId==null ||toAccountId.trim().length()==0 ){
			toAccountId = "8ca8a7e4-6d02-48e3-a029-0b2bf89de9f0";
		}
		if(currency==null ||currency.trim().length()==0 ){
			currency = "EUR";
		}
		if(amount==null ||amount.trim().length()==0 ){
			amount = "90";
		}
		if(description==null ||description.trim().length()==0 ){
			description = "Gift Contribution";
		}	
		if(bankId==null || bankId.trim().length()==0){
			bankId = "in-bank-x-1";
		}
		if(accountNumber==null || accountNumber.trim().length()==0){
			accountNumber = "ACCOUNT_ID";
		}
		Random r = new Random();
		int Low = 10;
		int High = 100;
		int Result = r.nextInt(High-Low) + Low;
		LOG.info("inside transferRequest afetr Result");
		String inputUrl ="https://apisandbox.openbankproject.com/obp/v3.0.0/banks/"+bankId+"/accounts/"+accountNumber+"/owner/transaction-request-types/SANDBOX_TAN/transaction-requests";
		LOG.info("inside transferRequest inputUrl"+inputUrl);

		String inputJSON = "{  \"to\":{    \"bank_id\":\""+toBankId+"\",    \"account_id\":\""+toAccountId+"\"  },  \"value\":{    \"currency\":\""+currency+"\",    \"amount\":\""+amount+"\"  },  \"description\":\""+description+"\"}";
		//String headerInfo = "DirectLogin username=\"raviraj\",password=\"Raviraj@007\",consumer_key=\"r0ukdhqdlimeymfk0efvztdbfgo1apz5srwwdivt\"";
		LOG.info("inside transferRequest inputJSON"+inputJSON);
		String headerInfo = "DirectLogin token=\""+token+"\"";
		String headerKey = "Authorization";
		String methodType ="POST";
		String transferMoney = "";
		try {
			LOG.info("inside transferRequest before calling createConnectionAndInvokeREST");
			//transferMoney = createConnectionAndInvokeREST(inputUrl,inputJSON,headerInfo,headerKey, methodType);
			transferMoney=createConnectionAndInvokeREST(inputUrl,inputJSON,headerInfo,headerKey, methodType);
			LOG.info("inside transferRequest after calling createConnectionAndInvokeREST");
			if(transferMoney==null){
				LOG.info("inside transferRequest after calling transferMoney is NULLL");
				transferMoney="ERROR IN PROGRAM FUNC:: "+transferMoney;
			}else{
				transferMoney=""+transferMoney;
				LOG.info("inside transferRequest after calling transferMoney is NOT NULLL");
			}
		} catch (Throwable e) {
			transferMoney = "failedToken";
			e.printStackTrace();
		}
		return transferMoney;
	}
	
}
