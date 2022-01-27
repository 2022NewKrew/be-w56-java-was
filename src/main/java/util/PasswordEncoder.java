package util;

import util.exception.NoInstanceException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
    public static String encrypt(String password) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(md.digest());
        } catch (Exception e){
            e.printStackTrace();
            throw new NoInstanceException("Instance 를 찾을 수 없습니다.");
        }
    }

    private static String bytesToHex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static boolean match(String rawPassword, String encodedPassword) throws NoSuchAlgorithmException {
        return encrypt(rawPassword).equals(encodedPassword);
    }
}
