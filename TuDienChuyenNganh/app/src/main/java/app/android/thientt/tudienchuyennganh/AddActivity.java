package app.android.thientt.tudienchuyennganh;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import app.android.thientt.tudienchuyennganh.model.ExcelPoi;
import app.android.thientt.tudienchuyennganh.model.InputStreamVolleyRequest;
import app.android.thientt.tudienchuyennganh.model.TLog;

public class AddActivity extends AppCompatActivity implements FileChooserDialog.FileCallback {
    private View view;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void chonfile(View view) {
        this.view = view;
        String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + "/Download";
        new FileChooserDialog.Builder(this)
                .initialPath(SDCARD_PATH)
                .mimeType("application/vnd.ms-excel")
                .show();

        // downLoadFile();
    }

    public void downLoadFile(String mUrl) {
//        String mUrl = "http://translateviet.com/?ddownload=738";
        mUrl = "http://translateviet.com/?ddownload=745";
//        String mUrl = "http://blogtraitim.info/wp-content/uploads/2015/06/Hinh-anh-dep-ve-tinh-yeu-lang-man-1-1024x768.jpg";
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, mUrl,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        // TODO handle the response
                        try {
                            if (response != null) {
                                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES), "True Moments");
                                if (!mediaStorageDir.exists()) {
                                    if (!mediaStorageDir.mkdirs()) {
                                        return;
                                    }
                                }
                                // Create a media file name
                                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                                File mediaFile;
                                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                                        "moi" + timeStamp + ".xls");
                                Uri aa = Uri.fromFile(mediaFile);
//                                FileInputStream fis = new FileInputStream(new File(name));
//                                fis.close();
                                FileOutputStream outputStream = new FileOutputStream(aa.getPath());
                                // FileOutputStream out = new FileOutputStream(file);
                                //outputStream = openFileOutput(aa.getPath(), Context.MODE_PRIVATE);

                                outputStream.write(response);
                                outputStream.close();
                                TLog.d("download xong");
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            TLog.e("error: " + e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO handle the error
                error.printStackTrace();
            }
        }, null);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(request);

    }

    @Override
    public void onFileSelection(@NonNull FileChooserDialog exdialog, @NonNull final File file) {
        dialog = new MaterialDialog.Builder(this)
                .title(R.string.process)
                .content(R.string.waiting)
                .progress(true, 0)
                .show();
        final String name = file.getName();
        Thread thread = new Thread() {
            @Override
            public void run() {
                String result = ExcelPoi.readExcelFile(AddActivity.this, file, name.substring(0, name.lastIndexOf(".")));
                dialog.dismiss();
                Snackbar.make(view, result, Snackbar.LENGTH_LONG).show();
            }
        };
        thread.start();
    }

    public void openWeb(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://translateviet.com/chia-se/tai-lieu/danh-sach-tu-vung-cho-ung-dung-android"));
        startActivity(browserIntent);
    }
}


