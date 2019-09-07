package com.bc.bookcrossing.src.ClientModels.Enums;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public enum LoginStatus {
    NONE("No logged in"),
    EMPTY_FIELDS("Fill all fields"),
    EMPTY_USERNAME("No username"),
    EMPTY_PASSWORD("No password"),
    // Status send back from server
    SUCCESS("Logged in"),
    COMMUNICATION_ERROR("Network error"),
    WRONG_USERNAME("Wrong username"),
    WRONG_PASSWORD("Wrong password");

    public String description;

    LoginStatus(String desc) {
        this.description = desc;
    }

    public String getDescription() {
        return description;
    }
}
