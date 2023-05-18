package com.example.tkey_android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class DeCryptor {
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";


//    init with key pair
    String decryptData(final String alias, final byte[] encryptedData, KeyStore keyStore) throws UnrecoverableEntryException, KeyStoreException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        KeyStore.Entry entry = keyStore.getEntry(alias, null);
        if(entry instanceof KeyStore.PrivateKeyEntry) {
            PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(encryptedData), "UTF-8");
        } else {
            throw  new RuntimeException();
        }
    }

//    String decryptData(final String alias, final byte[] encryptedData, final byte[] encryptionIv, KeyStore keyStore) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, UnrecoverableEntryException, KeyStoreException, InvalidAlgorithmParameterException, InvalidKeyException {
//        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
//        final GCMParameterSpec spec = new GCMParameterSpec(128, encryptionIv);
//        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(alias, keyStore), spec);
//        return new String(cipher.doFinal(encryptedData), "UTF-8");
//    }
//
//    private SecretKey getSecretKey(final String alias, KeyStore keyStore) throws UnrecoverableEntryException, KeyStoreException, NoSuchAlgorithmException {
//        return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
//    }

    void deleteEntry(String alias, KeyStore keyStore) throws KeyStoreException {
        keyStore.deleteEntry(alias);
    }
}
