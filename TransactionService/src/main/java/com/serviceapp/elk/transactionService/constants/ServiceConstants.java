package com.serviceapp.elk.transactionService.constants;

public class ServiceConstants {
    // Base URL for Account Service
    public static final String ACCOUNT_SERVICE_BASE_URL = "http://localhost:9097/account";

    // Endpoints
    public static final String WITHDRAW_ENDPOINT = ACCOUNT_SERVICE_BASE_URL + "/withdraw/{accountNumber}";
    public static final String DEPOSIT_ENDPOINT = ACCOUNT_SERVICE_BASE_URL + "/depositAmount/{accountNumber}";
    public static final String GET_ACCOUNT_DETAILS_ENDPOINT = ACCOUNT_SERVICE_BASE_URL + "/getById/{accountNumber}";

}
