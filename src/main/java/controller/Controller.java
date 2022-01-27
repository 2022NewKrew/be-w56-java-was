package controller;

import domain.RequestLine;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Controller {

    void control(DataOutputStream dos, BufferedReader bufferedReader, RequestLine requestLine) throws IOException;
}
