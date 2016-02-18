package app.android.thientt.tudienchuyennganh;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.folderselector.FileChooserDialog;

import java.io.File;

import app.android.thientt.tudienchuyennganh.model.ExcelPoi;

public class AddActivity extends AppCompatActivity implements FileChooserDialog.FileCallback {
    private View view;

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
    }

    private MaterialDialog dialog;

    @Override
    public void onFileSelection(final File file) {
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
}


