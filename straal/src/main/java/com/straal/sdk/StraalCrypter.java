/*
 * StraalCrypter.java
 * Created by Konrad Kowalewski on 26.01.18
 * Straal SDK for Android
 * Copyright 2018 Straal Sp. z o. o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.straal.sdk;

import android.support.annotation.NonNull;

import com.straal.sdk.exceptions.EncryptionException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

class StraalCrypter {
    private final static char[] HEX_ARRAY = "0123456789abcdef".toCharArray();
    private static final String TRANSFORMATION = "AES/CFB/NoPadding";
    private static final String ALGORITHM = "AES";
    private static final String CHARSET_NAME = "UTF-8";
    private final CryptKey mCryptKey;

    StraalCrypter(String cryptKey) {
        mCryptKey = new CryptKey(cryptKey);
    }

    String encryptString(String msg)
            throws EncryptionException,IllegalStateException {
        if (msg == null) {
            return null;
        }
        String result = mCryptKey.getId();
        try {
            Cipher cipher = getCipher(mCryptKey.getIv1(), Cipher.ENCRYPT_MODE);
            byte[] msgWithPadding = getBytesWithZeroPadding(msg.getBytes(CHARSET_NAME), 16);
            byte[] encrypted = cipher.doFinal(msgWithPadding);
            result += bytesToHex(encrypted);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                NoSuchPaddingException | UnsupportedEncodingException e) {
            throw new IllegalStateException("Unexpected error encoding msg, algorithm not supported", e);
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            throw new EncryptionException("Wrong encryption parameter", e);
        }
        return result;
    }


    private byte[] getBytesWithZeroPadding(byte[] bytes, int blockSize) {
        int mod = bytes.length % blockSize;
        byte[] result = new byte[bytes.length + blockSize - mod];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        return result;
    }

    String decryptString(String encryptedMsg)
            throws EncryptionException, IllegalStateException {
        if (encryptedMsg == null) {
            return null;
        }
        String encryptedMsgText = removeKeyIdFromMsg(encryptedMsg);
        String result;
        try {
            Cipher cipher = getCipher(mCryptKey.getIv2(), Cipher.DECRYPT_MODE);
            byte[] decrypted = removeEndPadding(cipher.doFinal(hexToBytes(encryptedMsgText)));
            result = new String(decrypted, CHARSET_NAME);
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                NoSuchPaddingException | UnsupportedEncodingException e) {
            throw new IllegalStateException("Unexpected error decoding msg, algorithm not supported", e);
        } catch (BadPaddingException | InvalidKeyException | IllegalBlockSizeException e) {
            throw new EncryptionException("Wrong decryption Parameter", e);
        }
        return result.trim();
    }

    @NonNull
    private String removeKeyIdFromMsg(String encryptedMsg) {
        return encryptedMsg.substring(16);
    }

    @NonNull
    private Cipher getCipher(String iv, int encryptMode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(hexToBytes(iv));
        SecretKeySpec sks = new SecretKeySpec(hexToBytes(mCryptKey.getKey()), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(encryptMode, sks, ivParameterSpec);
        return cipher;
    }

    private byte[] removeEndPadding(byte[] bytes) {
        int endPaddingCount = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] == 0) {
                endPaddingCount++;
            } else {
                break;
            }
        }
        int arraySize = bytes.length - endPaddingCount;
        byte[] result = new byte[arraySize];
        System.arraycopy(bytes, 0, result, 0, arraySize);
        return result;
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xff;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0f];
        }
        return new String(hexChars);
    }

    private byte[] hexToBytes(String s) {
        if (s == null) {
            return null;
        }
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    static class CryptKey {

        private final String key;

        private final String iv1;
        private final String iv2;

        CryptKey(String cryptKey) {
            id = parseId(cryptKey);
            key = parseKey(cryptKey);
            iv1 = parseIv1(cryptKey);
            iv2 = parseIv2(cryptKey);
        }

        private String parseIv2(String hex) {
            if (hex == null) {
                return null;
            }
            return hex.substring(112);
        }

        private String parseIv1(String hex) {
            if (hex == null) {
                return null;
            }
            return hex.substring(80, 112);
        }

        private String parseKey(String hex) {
            if (hex == null) {
                return null;
            }
            return hex.substring(16, 80);
        }

        private String parseId(String hex) {
            if (hex == null) {
                return null;
            }
            return hex.substring(0, 16);
        }

        private final String id;

        public String getId() {
            return id;
        }

        public String getKey() {
            return key;
        }

        public String getIv1() {
            return iv1;
        }

        public String getIv2() {
            return iv2;
        }
    }
}
