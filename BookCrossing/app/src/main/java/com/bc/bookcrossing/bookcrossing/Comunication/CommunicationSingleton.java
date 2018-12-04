package com.bc.bookcrossing.bookcrossing.Comunication;

class CommunicationSingleton implements SendRequests {
    public static final CommunicationSingleton singletonCommunication = new CommunicationSingleton();
    private CommunicationSingleton() {

    }

    @Override
    public void send(String data) {

    }
}
