package com.ovs.VotingSystem.constatnt;

public enum Status {
    ACTIVE("A","Active"),
    INACTIVE("I", "Inactive"),
    UNKNOWN("U", "Unknown");


    private final String value;
    private final String code;

    Status(String code, String value) {
        this.code = code;
        this.value = value;
    }


    public String getValue() {
        return value;
    }
    public String getCode() {
        return code;
    }

    public static Status getByCode(String code) {
        for (Status status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }

}
