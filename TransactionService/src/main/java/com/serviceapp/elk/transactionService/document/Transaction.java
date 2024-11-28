// package com.serviceapp.elk.transactionService.document;

// import org.springframework.data.annotation.Id;
// import org.springframework.data.elasticsearch.annotations.Document;
// import org.springframework.data.elasticsearch.annotations.Field;
// import org.springframework.data.elasticsearch.annotations.FieldType;

// import lombok.Data;
// import java.util.UUID;
// import java.util.Date;

// @Data
// @Document(indexName = "transactions")
// public class Transaction {

// @Id
// private String transactionId;

// @Field(type = FieldType.Keyword)
// private UUID fromAccount;

// @Field(type = FieldType.Keyword)
// private UUID toAccount;

// @Field(type = FieldType.Double)
// private double amount;

// @Field(type = FieldType.Date, format = {}, pattern =
// "uuuu-MM-dd'T'HH:mm:ss.SSS'Z'")
// private Date transactionDate;

// @Field(type = FieldType.Keyword)
// private String transactionType; // "self-transfer", "account-to-account"

// @Field(type = FieldType.Keyword)
// private String status; // "success", "failed"
// }
