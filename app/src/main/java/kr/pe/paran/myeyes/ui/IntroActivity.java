package kr.pe.paran.myeyes.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.pe.paran.myeyes.MainActivity;
import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.model.Properties;
import kr.pe.paran.myeyes.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity implements DownloadDialog.OnDialogDismissLisenter {

    private static final String TAG     = IntroActivity.class.getSimpleName();

    public static final String  TAG_DOWNLOAD_DIALOG = "download_dialog";

    private static final int    PERMISSIONS_REQUEST = 1;

    private static final String[]   REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        findViewById(R.id.btn_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });

        if (isAllPermissionGranted()) {
            versionCheck();
        }
        else {
            findViewById(R.id.img_intro).setVisibility(View.GONE);
            findViewById(R.id.layout_permission).setVisibility(View.VISIBLE);
        }

    }

    private boolean isAllPermissionGranted() {

        boolean isGranted = true;

        for (String permission : REQUIRED_SDK_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }
        return isGranted;
    }

    protected void checkPermissions() {

        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions.toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(PERMISSIONS_REQUEST, REQUIRED_SDK_PERMISSIONS, grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
//                        Toast.makeText(this, "Required permission '" + permissions[index] + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        Utility.showMessage(this, "App 실행을 위한 권한 없이 종료합니다.");
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                versionCheck();
                break;
        }
    }

    private void nextActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void versionCheck() {

        Utility.showProgress(this);

        RetrofitService.getProperties(getApplicationContext()).get().enqueue(new Callback<Properties>() {
            @Override
            public void onResponse(Call<Properties> call, final Response<Properties> response) {
                Utility.hideProgress();
                try {
                    int currentVersion = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA).versionCode;
                    if (currentVersion < response.body().versionCode) {
                        showUpdateDialog(response.body());
                    }
                    else {
                        nextActivity();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    nextActivity();
                }
            }

            @Override
            public void onFailure(Call<Properties> call, Throwable t) {
                Utility.hideProgress();
                Log.i(TAG, t.getMessage());
                Utility.showMessage(getApplicationContext(), "인터넷 연결상태를 확인 해 주세요.");
                nextActivity();
            }
        });
    }


    private void showUpdateDialog(final Properties properties) {
        new AlertDialog.Builder(this)
                .setTitle(properties.title)
                .setMessage(properties.message)
                .setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showDownloadDialog(properties.fileName);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nextActivity();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }


    private void showDownloadDialog(String filename) {
        DownloadDialog downloadDialog = DownloadDialog.newInstance(filename);
        downloadDialog.show(getSupportFragmentManager(), TAG_DOWNLOAD_DIALOG);
    }


    @Override
    public void onDialogDismiss(String result, String tag) {
        if (!result.toLowerCase().equals("fail")) {
            installApp(Utility.getDownloadFile(result));
        } else {
            nextActivity();
        }
    }

    private void installApp(File file) {
        Log.i(TAG, "install_app>" + file.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.i(TAG, "install_app>");
            Uri fileUri = FileProvider.getUriForFile(getBaseContext(), getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } else {
            Log.i(TAG, "install_app<");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

}
