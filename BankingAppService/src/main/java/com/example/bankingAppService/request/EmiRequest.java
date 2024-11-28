package com.example.bankingAppService.request;
import lombok.Data;

// @Data
// public class EmiRequest {
//     private double principal;
//     private double rateOfInterest;
//     private int tenure;
// }


public record EmiRequest(double principal, double rateOfInterest, int tenure) {
}
