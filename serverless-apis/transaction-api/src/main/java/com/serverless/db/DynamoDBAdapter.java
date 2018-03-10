package com.serverless.db;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.serverless.data.KYCDocuments;
import com.serverless.data.Transaction;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDBAdapter {

    private Logger logger = Logger.getLogger(this.getClass());

    private final static DynamoDBAdapter adapter = new DynamoDBAdapter();

    private final AmazonDynamoDB client;

    private DynamoDBAdapter() {
        client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("https://dynamodb.us-east-1.amazonaws.com", "us-east-1"))
                .build();
        logger.info("Created DynamoDB client");
    }

    public static DynamoDBAdapter getInstance(){
        return adapter;
    }

    public List<Transaction> getTransactions(String accountId) throws IOException {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Map<String, AttributeValue> vals = new HashMap<>();
        vals.put(":val1",new AttributeValue().withS(accountId));
        DynamoDBQueryExpression<Transaction> queryExpression = new DynamoDBQueryExpression<Transaction>()
                .withKeyConditionExpression("account_id = :val1 ")
                .withExpressionAttributeValues(vals);
        return mapper.query(Transaction.class, queryExpression);
    }

    
    public List<KYCDocuments> getKYC(String kyctype) throws IOException {
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        Map<String, AttributeValue> vals = new HashMap<>();
        vals.put(":val1",new AttributeValue().withS(kyctype));
        DynamoDBQueryExpression<KYCDocuments> queryExpression = new DynamoDBQueryExpression<KYCDocuments>()
                .withKeyConditionExpression("kycdocument = :val1 ")
                .withExpressionAttributeValues(vals);

        return mapper.query(KYCDocuments.class, queryExpression);
    }


    public void putTransaction(Transaction transaction) throws IOException{
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(transaction);
    }
    

    public void putTransaction(KYCDocuments transaction) throws IOException{
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(transaction);
    }
    
    public void deleteTransaction(KYCDocuments transaction) throws IOException{
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.delete(transaction);
    }

}