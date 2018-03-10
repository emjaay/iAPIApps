package com.serverless.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "kyc_detais")
public class KYCDocuments {

	@DynamoDBAttribute(attributeName = "data")
public String data;


	public String getData() {
	return data;
}



public void setData(String data) {
	this.data = data;
}

	@DynamoDBRangeKey(attributeName = "kyctype")
    String kyctype; //Hash Key

    public String getKyctype() {
		return kyctype;
	}



	public void setKyctype(String kyctype) {
		this.kyctype = kyctype;
	}

	  
	
	@DynamoDBHashKey(attributeName = "kycdocument")
    String kycdocument; //Hash Key

   

	public String getKycdocument() {
		return kycdocument;
	}



	public void setKycdocument(String kycdocument) {
		this.kycdocument = kycdocument;
	}




}