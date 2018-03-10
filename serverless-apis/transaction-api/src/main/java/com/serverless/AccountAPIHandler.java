package com.serverless;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AccountAPIHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = Logger.getLogger(Handler.class);	
	static String OAUTH_OKEN_URL="<url>"; 
	private String token = "";
	String bankId = "";
	String accountNumber = "";
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		BasicConfigurator.configure();
	     Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
	     if(pathParameters!=null){
	     bankId = pathParameters.get("bankid");
	     accountNumber = pathParameters.get("accountnumber");
	     }
		token = generateToken();
		LOG.info("received: " + input);
		LOG.info("received bankId: " + bankId + " :: AccountNumber is :: "+accountNumber);
		String myString = "Default String";
		try {
			LOG.info("received bankId: before calling account details :::");
			myString = getAccountDetails();
			LOG.info("received bankId: after calling account details :::");
		} catch (Exception e) {
			myString = "Oops Exception occured";
			e.printStackTrace();
		}
		//myString=URLEncoder.encode(myString);
		Response responseBody = new Response(myString, input);
		
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("Content-Type", "application/json"))
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

		try {
			//rest.createConnectionAndInvokeGETREST(inputUrl, "GET");
			String headerInfo="<headerInfo>";
			String headerKey="<headerKeyName>";
			// transaction initiate screen will not pass ChannelContext.
			//rest.createConnectionAndInvokeREST(inputPostUrl,inputJSON,"","", "POST");
			//Confirmation screen passes credentials and header 
			createConnectionAndInvokeREST(inputPostUrl,inputJSON,headerInfo,headerKey, "POST");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public String createConnectionAndInvokeREST(String inputUrl,String inputJSON,String headerInfo,String headerKey, String methodType) throws IOException{
		HttpURLConnection conn=null;
		try{
			
		URL url = new URL(inputUrl);
		

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
			conn.setRequestProperty ("Authorization", token);
			if(headerKey!=null && headerKey.trim().length()>0){
				conn.setRequestProperty (headerKey, headerInfo);
			}
			JSONObject json = new JSONObject();
			if(isDelete){
				//conn.setRequestProperty("X-HTTP-Method-Override", "DELETE");
			}
			OutputStream os = conn.getOutputStream();
			os.write(inputJSON.getBytes());
			os.flush();
		if(conn.getResponseCode() == 200){
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
		
				String output;
				String result = "";
				String cacheValue="";
				while ((output = br.readLine()) != null) {
					result = result+output;
				}
					try {
						json= new JSONObject(result);
						
							// cacheValue = (String) ((JSONObject)json.get("data")).get("output");
							
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				
			//	return result;

			}
		else if(conn.getResponseCode() == 400){
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getErrorStream())));			
				String output;
				while ((output = br.readLine()) != null) {
					
					try {
					json = new JSONObject(output);
					JSONObject json1= (JSONObject)json.get("status");
					JSONObject json2= (JSONObject)json1.get("message");
					String errCode =  (String)json2.get("messageCode");
					String messageAdditionalInfo = (String)json2.get("messageAddlnInfo");
					String messageType = (String)json2.get("message_TYPE");
					//requestStatus="FAL";
					if(messageType=="CE"){
						
					}else{
						
					}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
				return output;
				
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

			System.out.println("Response header is>>>>"+conn.getHeaderField("<ResponseHeaderKey>"));
			
		//	return result;

		}
		
		else{
			/*if (conn.getResponseCode() != 200 && conn.getResponseCode() != 400) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}*/
				//requestStatus="FAL";
			 
		}
		
		return json.toString();
	  } catch (MalformedURLException e) {

		//e.printStackTrace();
		//requestStatus="FAL";
		throw e;

	  } catch (IOException e) {
		  //requestStatus="FAL";
		//e.printStackTrace();
		throw e;

	 }finally{
		 conn.disconnect();

	 }
	}
	
	
	public String createConnectionAndInvokeGETREST(String inputUrl,String methodType,String token1) throws IOException{
		HttpURLConnection conn=null;
		try{
		//inputUrl = "https://apisandbox.openbankproject.com/obp/v3.0.0/my/banks/in-bank-x-1/accounts/OPENACCOUNT/account";
		methodType="GET";
		URL url = new URL(inputUrl);
		String cacheValue = "";
		String acessToken="DirectLogin token=\""+token+"\"";
		LOG.info("received createConnectionAndInvokeGETREST 1");

	/*	if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
				+ conn.getResponseCode());
		}
		
	*/	
		 conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod(methodType);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty ("Authorization", acessToken);
			LOG.info("received createConnectionAndInvokeGETREST 2");
		if(conn.getResponseCode() == 200){
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
		
				String output;
				String result = "";
				LOG.info("received createConnectionAndInvokeGETREST 3");
				while ((output = br.readLine()) != null) {
					result = result+output;
		}
					JSONObject json = new JSONObject();
					try {
						LOG.info("received createConnectionAndInvokeGETREST 4");
						json = new JSONObject(result);
						
						/*JSONObject json1 = (JSONObject) ((JSONArray)json.get("data")).get(0);
						cacheValue = (String)(json1.getString("cacheValue"));
							System.out.println("Cache vakue is???"+cacheValue);*/
							
						LOG.info("received createConnectionAndInvokeGETREST 5");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						result ="JSON Exception occured";
					}
					LOG.info("received createConnectionAndInvokeGETREST 6");
				return json.toString();

			}
		else if(conn.getResponseCode() == 400){
			LOG.info("received createConnectionAndInvokeGETREST 7");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getErrorStream())));			
				String output;
				String result = "";
				while ((output = br.readLine()) != null) {
					result = result+output;
				}
				LOG.info("received createConnectionAndInvokeGETREST 8");
				return result;
				
		}else{
			/*if (conn.getResponseCode() != 200 && conn.getResponseCode() != 400) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}*/
				//requestStatus="FAL";
			  
		}
		

	  } catch (MalformedURLException e) {
		  LOG.info("received createConnectionAndInvokeGETREST 9");
		//e.printStackTrace();
		throw e;

	  } catch (IOException e) {
		//e.printStackTrace();
		  LOG.info("received createConnectionAndInvokeGETREST 10");
		throw e;

	 }finally{
		 conn.disconnect();

	 }
		LOG.info("received createConnectionAndInvokeGETREST 11");
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
		} catch (IOException e) {
			token = "failedToken";
			e.printStackTrace();
		}
		return actualToken;
	}
	private String getAccountDetails(){
		String accountNumberLocal = Math.random()+"";
		                  //https://apisandbox.openbankproject.com/obp/v3.0.0/banks/in-bank-x-1/accounts/OPENACCOUNT1
		if(bankId==null || bankId.trim().length()==0){
			bankId = "in-bank-x-1";
		}
		if(accountNumber==null || accountNumber.trim().length()==0){
			accountNumber = "ACCOUNT_ID";
		}
		String inputUrl ="https://apisandbox.openbankproject.com/obp/v3.0.0/my/banks/"+bankId+"/accounts/"+accountNumber+"/account";
		LOG.info("received inputUrl:  :::"+inputUrl);
		String inputJSON = "{  \"user_id\":\"\",  \"label\":\"MyOpenAccount\",  \"type\":\"SAVINGS\",  \"balance\":{    \"currency\":\"INR\",    \"amount\":\"0\"  },  \"branch_id\":\"1235\",  \"account_routing\":{    \"scheme\":\"OBP\",    \"address\":\"UK123457\"  }}";
		//String headerInfo = "DirectLogin username=\"raviraj\",password=\"Raviraj@007\",consumer_key=\"r0ukdhqdlimeymfk0efvztdbfgo1apz5srwwdivt\"";
		String headerInfo = "DirectLogin token=\""+token+"\"";
		String headerKey = "Authorization";
		String methodType ="PUT";
		String accountDetailsResponse = "";
		try {
			accountDetailsResponse = createConnectionAndInvokeGETREST(inputUrl,inputJSON,headerInfo);
		} catch (IOException e) {
			accountDetailsResponse = "failedToken";
			e.printStackTrace();
		}
		return accountDetailsResponse;
	}
	
}
