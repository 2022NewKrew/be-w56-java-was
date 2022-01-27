package controller;

import dto.RequestInfo;

import java.io.DataOutputStream;
import java.util.Map;

public interface Controller {
    void handleRequest(RequestInfo requestInfo, DataOutputStream dos);
    default void doPost(RequestInfo requestInfo, DataOutputStream dos) {}
    default void doGet(RequestInfo requestInfo, DataOutputStream dos) {}
}
