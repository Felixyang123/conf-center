package com.wly.config.server.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class DigestUtils {

    public static void main(String[] args) {
        String originalMessage = "Hello, World!";

        String token = SHA256(originalMessage);
        System.out.println(token);
    }

    public static String SHA256(String originalMessage) {
        try {
            // 1. 获取 MessageDigest 实例，指定算法为 SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 2. 将字符串数据转换为字节数组并传入 update 方法
            //    对于一次性输入所有数据，也可以直接使用 digest(byte[] input)
            byte[] encodedhash = digest.digest(originalMessage.getBytes());

            // 3. 将生成的哈希字节数组转换为十六进制字符串
            return bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            // 如果传入的算法名称不被支持，会抛出此异常
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * 辅助方法：将字节数组转换为十六进制字符串
     * 这是非常标准且高效的实现方式。
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String md5(String originalMessage) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(originalMessage.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 algorithm not found", e);
            throw new RuntimeException(e);
        }
    }
}