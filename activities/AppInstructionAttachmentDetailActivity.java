package com.devool.ucicareconnect.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devool.ucicareconnect.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class AppInstructionAttachmentDetailActivity extends AppCompatActivity implements View.OnClickListener{

    ImageView imgInstructionAttachment;
    WebView webViewAttachment;
    ProgressBar progressBar;
    private ProgressDialog mProgressDialog;
    String pdfPath;
    String pdfname;
    ImageView imgCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_instruction_attachment_detail);

        imgInstructionAttachment = findViewById(R.id.img_instruction_attachment);
        webViewAttachment = findViewById(R.id.wv_attachment);
        progressBar = findViewById(R.id.progress);
        imgCloseButton = findViewById(R.id.img_close_button);

        imgCloseButton.setOnClickListener(this);

        if (getIntent().getExtras().getString("file_extension").equalsIgnoreCase("jpg")) {
            imgInstructionAttachment.setVisibility(View.VISIBLE);
            webViewAttachment.setVisibility(View.GONE);
            Glide.with(AppInstructionAttachmentDetailActivity.this).
                    load(getIntent().getExtras().getString("attachment_url")).
                    placeholder(R.drawable.loading)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imgInstructionAttachment);

        } else if (getIntent().getExtras().getString("file_extension").equalsIgnoreCase("pdf")) {
            imgInstructionAttachment.setVisibility(View.GONE);
            webViewAttachment.setVisibility(View.VISIBLE);

            WebSettings settings = webViewAttachment.getSettings();
            webViewAttachment.setInitialScale(150);
            settings.setSupportZoom(true);
            settings.setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webViewAttachment.setWebContentsDebuggingEnabled(true);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                settings.setAllowFileAccessFromFileURLs(true);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                settings.setAllowUniversalAccessFromFileURLs(true);
            }
            settings.setBuiltInZoomControls(true);
            webViewAttachment.setWebChromeClient(new WebChromeClient());

            startDownload(getIntent().getExtras().getString("attachment_url"));
        }

    }

    private void startDownload(String url) {
        //  new DownloadFileAsync(activity);
        new DownloadFileAsync().execute(url);
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        Activity context;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(AppInstructionAttachmentDetailActivity.this);
            mProgressDialog.setMessage("Downloading file..");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0] );
                String[] separated = f_url[0].split("/");
                pdfname = separated[5];
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                File file = new File(AppInstructionAttachmentDetailActivity.this.getFilesDir(),"/UCIattachment");
                if(!file.exists()){
                    file.mkdirs();
                }
                OutputStream output = new FileOutputStream(file+"/"+pdfname+"");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // writing data to file
                    output.write(data, 0, count);
                }
                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ",""+ e.getMessage());
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            try {
            pdfPath = AppInstructionAttachmentDetailActivity.this.getFilesDir() + "/UCIattachment/"+pdfname;
            Log.e("pdfPath", pdfPath);
            //File file = new File(pdfPath);
            mProgressDialog.dismiss();
            File filess = new File(pdfPath);
            webViewAttachment.loadUrl("file:///android_asset/web/viewer.html?file=" + filess);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close_button:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

}
