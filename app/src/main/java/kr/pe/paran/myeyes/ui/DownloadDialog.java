package kr.pe.paran.myeyes.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.network.RetrofitService;
import kr.pe.paran.myeyes.network.RetrofitUrl;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadDialog extends DialogFragment {

    static final String TAG = DownloadDialog.class.getSimpleName();

    private static final String KEY_DOWNLOAD_FILENAME = "download_url";

    private TextView mPercent;
    private TextView mDetail;
    private ProgressBar mProgressBar;

    private OnDialogDismissLisenter mListener;

    private String mFileName;

    public interface OnDialogDismissLisenter {
        void onDialogDismiss(String result, String tag);
    }



    public static DownloadDialog newInstance(String filename) {
        DownloadDialog downloadDialog = new DownloadDialog();

        Bundle args = new Bundle();
        args.putString(KEY_DOWNLOAD_FILENAME, filename);
        downloadDialog.setArguments(args);

        return downloadDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = getActivity();
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OnDialogDismissLisenter) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mFileName = getArguments().getString(KEY_DOWNLOAD_FILENAME);

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_download, null);
        mPercent = view.findViewById(R.id.tv_percent);
        mDetail = view.findViewById(R.id.tv_detail);
        mProgressBar = view.findViewById(R.id.progressBar);
        mProgressBar.setMax(100);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        setCancelable(false);

        downloadFile();


        return builder.create();
    }

    private void downloadFile() {

        String strUrl = RetrofitUrl.API_BASE_URL + mFileName;

        RetrofitService.getRetrofit(getContext()).download(strUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DownloadFileTask downloadFileTask = new DownloadFileTask();
                downloadFileTask.execute(response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                onDismissDialog(-1);

            }
        });
    }

    private class DownloadFileTask extends AsyncTask<ResponseBody, Integer, Integer> {

        @Override
        protected Integer doInBackground(ResponseBody... responseBodies) {

            int result = 100;
            ResponseBody body = responseBodies[0];
            try {
                File destinationFile = Utility.getDownloadFile(mFileName);
                if (destinationFile.exists()) {
                    destinationFile.delete();
                }

                InputStream inputStream = null;
                OutputStream outputStream = null;

                try {
                    inputStream = body.byteStream();
                    outputStream = new FileOutputStream(destinationFile);
                    byte data[] = new byte[4096];
                    int count;
                    int progress = 0;
                    long fileSize = body.contentLength();
//                    Log.i(TAG, "File Size=" + fileSize);

                    while ((count = inputStream.read(data)) != -1) {
                        outputStream.write(data, 0, count);
                        progress += count;

                        publishProgress(progress, (int) fileSize);
                    }

                    outputStream.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                    result = -1;

                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            int percent = (int) ((float) values[0] / (float) values[1] * 100.f);
//            Log.i(TAG, "Percent>" + percent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mProgressBar.setProgress(percent, true);
            }
            else {
                mProgressBar.setProgress(percent);
            }
            mPercent.setText(percent + " %");
            mDetail.setText(String.format("%d KB / %d KB", values[0] / 1024, values[1] / 1024));
        }

        @Override
        protected void onPostExecute(final Integer value) {
            super.onPostExecute(value);

            onDismissDialog(value);
        }
    }

    private void onDismissDialog(final int result) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (result == 100) {
                    Utility.showMessage(getContext() ,"Download Completed...");
                    mListener.onDialogDismiss(getArguments().getString(KEY_DOWNLOAD_FILENAME), getTag());
                } else {
                    Utility.showMessage(getContext(), "Download Failed......");
                    mListener.onDialogDismiss("Fail", getTag());
                }
                dismiss();
            }
        }, 1000);
    }
}
