import android.os.Build;
import android.util.JsonReader;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Random;

import javax.crypto.Cipher;

public class GifExporter {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void exportGif (int cMode, String path) throws IOException, JSONException, ParseException {
        String jsonKey = path.replace("/", "_");

        // Generate key if encryption mode
        String key = new String();
        if (cMode == Cipher.ENCRYPT_MODE) {
            key = getAlphaNumericString(16);
        }
        // Get key from file if decryption mode
        else if (cMode == Cipher.DECRYPT_MODE) {
            // FileReader reader = new FileReader("/sdcard/keys.json");
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("/sdcard/keys.json"));
            JSONObject jsonObject = (JSONObject) obj;
            key= (String) jsonObject.get(jsonKey);
            Log.d("KEY FOUND:", key);
        }

        File inFile = new File(path);
        File encryptedFile = new File(path);
        File decryptedFile = new File(path);

        // Writing "dir/file.ext":"key" to a file
        Pair<String, String> keyValPair = new Pair<>(jsonKey, key); // Create key value pair

        Gson gson = new Gson();

        FileReader reader = new FileReader("/sdcard/keys.json");

        Map<String, String> jsonToMap = gson.fromJson(reader, Map.class);
        jsonToMap.put(keyValPair.first, keyValPair.second);

        FileWriter writer = new FileWriter("/sdcard/keys.json");
        gson.toJson(jsonToMap, writer);
        writer.close();
        reader.close();

        // Cipher Operation
        try {
            if (cMode == Cipher.ENCRYPT_MODE) {
                Locker.encrypt(key, inFile, encryptedFile);
            }
            else if (cMode == Cipher.DECRYPT_MODE) {
                Locker.decrypt(key, encryptedFile, decryptedFile);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getAlphaNumericString(int n)
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
