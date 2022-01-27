package controller;

import dto.RequestInfo;

import java.io.DataOutputStream;

public interface Controller {
    void handleRequest(RequestInfo requestInfo, DataOutputStream dos);
    default void doPost(RequestInfo requestInfo, DataOutputStream dos) {}
    default void doGet(RequestInfo requestInfo, DataOutputStream dos) {}
}
