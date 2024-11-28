package com.example.bankingAppService.constants;

public class ServiceConstants {
    
    public static final String ACCOUNT_SERVICE_BASE_URL = "http://localhost:9097/account";
    public static final String GET_ACCOUNT_BY_ID_URL = ACCOUNT_SERVICE_BASE_URL + "/getById/{accountNumber}";
    public static final String GET_ALL_ACCOUNTS_URL = ACCOUNT_SERVICE_BASE_URL + "/getAllAccount";
    public static final String WITHDRAW_URL = ACCOUNT_SERVICE_BASE_URL + "/withdraw/{accountNumber}";
    public static final String DEPOSIT_URL = ACCOUNT_SERVICE_BASE_URL + "/depositAmount/{accountNumber}";

}
