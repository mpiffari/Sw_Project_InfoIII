package com.bc.bookcrossing.bookcrossing.RequestManager;

public enum RequestType {
    BOOK_REGISTRATION_MANUAL("0"),
    BOOK_RESERVATION("1"),
    LOGIN("2"),
    SIGN_IN("3"),
    BOOK_REGISTRATION_AUTOMATIC("4"),
    PROFILE_INFO("5"),
    TAKEN_BOOKS("6"),
    PICK_UP("7"),
    BOOK_SEARCH("8");

    public String description;

    RequestType(String desc) {
        this.description = desc;
    }

    public static RequestType getEnumReqType (String desc) {
        for (RequestType r : RequestType.values()) {
            if (r.description.equalsIgnoreCase(desc)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
