package kr.pe.paran.myeyes.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.database.EstimateSQLiteOpenHelper;
import kr.pe.paran.myeyes.model.Customer;
import kr.pe.paran.myeyes.model.Estimate;
import kr.pe.paran.myeyes.model.ProductPrice;

public class ReportActivity extends AppCompatActivity {

    public static final String ASSET_PATH = "file:///android_asset/";

    private static final String TAG = ReportActivity.class.getSimpleName();
    private static final String ARG_CUSTOMER = "customer";

    private WebView mWebView;
    private StringBuilder mHtmlTagBuilder;

    private Customer        mCustomer;
    private long mTotalPrice = 0;


    public static Intent getIntent(Context context, Customer customer) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra(ARG_CUSTOMER, customer);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mCustomer   = (Customer) getIntent().getSerializableExtra(ARG_CUSTOMER);

//        final Estimate estimate = (Estimate) getIntent().getSerializableExtra("Estimate");
//        final Estimate estimate = getEstimate();

//        String reg_date = getIntent().getStringExtra("Estimate");
        if (mCustomer.reg_date == null) {
            Utility.showMessage(this, "상품정보가 없습니다.");
            finish();
        }
        EstimateSQLiteOpenHelper estimateSQLiteOpenHelper = new EstimateSQLiteOpenHelper(this);
        final Estimate estimate = estimateSQLiteOpenHelper.getEsitmate(mCustomer.reg_date);


        setTitle(estimate.custmer);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makePdfFile(estimate.custmer);
            }
        });

        mWebView = findViewById(R.id.webView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        ArrayList<Integer> cntCategory = new ArrayList<>();
        int start = 0;
        int count = 0;
        for (int i = 0; i < estimate.productPrices.size(); i++) {
            cntCategory.add(0);
            ProductPrice productPrice = estimate.productPrices.get(i);
            if (i > 0) {
                ProductPrice bProductPrice = estimate.productPrices.get(i - 1);
                if (!productPrice.category.equals(bProductPrice.category)) {
                    cntCategory.remove(start);
                    cntCategory.add(start, count);
                    start = i;
                    count = 0;
                }
            }
            count++;
        }
        cntCategory.remove(start);
        cntCategory.add(start, count);

        int cntSub = 0; long sumSub = 0; int subUnit = 0;
        mHtmlTagBuilder = new StringBuilder();
        for (int i = 0; i < estimate.productPrices.size(); i++) {

            ProductPrice productPrice = estimate.productPrices.get(i);
            mTotalPrice += productPrice.sum;
            String strPrice = productPrice.product.equals("인건비") ? Utility.getFormated(productPrice.count) + "명 x " + Utility.getFormated(productPrice.subCount) + "일" : Utility.getFormated(productPrice.count);
            productPrice.category += productPrice.category.equals("서비스") ? "<br> (월정액)" : "";

            if (cntCategory.get(i) > 0) {
                cntSub = productPrice.count * (productPrice.subCount > 0 ? productPrice.subCount : 1);
                sumSub = productPrice.sum;
                mHtmlTagBuilder.append(String.format(row_first,
                        cntCategory.get(i) + 1,
                        productPrice.category,
                        productPrice.product,
                        productPrice.standard,
                        strPrice,
                        Utility.getFormated(productPrice.price),
                        Utility.getFormated(productPrice.sum)));
            } else {
                cntSub += productPrice.count * (productPrice.subCount > 0 ? productPrice.subCount : 1);
                sumSub += productPrice.sum;
                mHtmlTagBuilder.append(String.format(row_middle,
                        productPrice.product,
                        productPrice.standard,
                        strPrice,
                        Utility.getFormated(productPrice.price),
                        Utility.getFormated(productPrice.sum)));
            }
            //...................
            if ((i + 1) == cntCategory.size() || cntCategory.get(i + 1) > 0) {
                mHtmlTagBuilder.append(String.format(row_subsum,
                        cntSub,
                        "",
                        Utility.getFormated(sumSub)));
            }
        }
        loadHtmlFile();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        finish();

        return super.onOptionsItemSelected(item);
    }

    private void loadHtmlFile() {

        String content = null;
        try {
            AssetManager assetManager = getAssets();
            InputStream in = assetManager.open("Report.html");
            byte[] bytes = readFully(in);
            content = new String(bytes, "UTF-8");
        } catch (IOException e) {
        }


        // Make HTML Tag,......
        content = String.format(content, "약정기간 : " + mCustomer.period,
                "고객명 : " + mCustomer.customer,
                mHtmlTagBuilder.toString(),
                Utility.getFormated(mTotalPrice),
                Utility.getCurrentDateTime(),
                Utility.getWaterMark(this));

        mWebView.loadDataWithBaseURL(ASSET_PATH, content.replace("!", "%"), "text/html", "utf-8", null);
        mWebView.requestFocusFromTouch();

    }

    private static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }


    private static String row_subsum = "<tr>" +
            "<td colspan=2>소 계</td>" +
            "<td>%s</td>" +
            "<td class='value_price'>%s</td>" +
            "<td class='value_price'>%s</td>" +
            "</tr>";

    private static String row_first = "<tr>" +
            "<td rowspan='%d'>%s</td>" +
            "<td>%s</td>" +
            "<td>%s</td>" +
            "<td>%s</td>" +
            "<td class='value_price'>%s</td>" +
            "<td class='value_price'>%s</td>" +
            "</tr>";

    private static String row_middle = "<tr>" +
            "<td>%s</td>" +
            "<td>%s</td>" +
            "<td>%s</td>" +
            "<td class='value_price'>%s</td>" +
            "<td class='value_price'>%s</td>" +
            "</tr>";


    private void makePdfFile(String customer) {

        Log.i(TAG, "WebView>" + mWebView.getWidth() + ", " + mWebView.getHeight());
        String filename = Utility.getFilenName(customer);

        PdfDocument pdfDocument = new PdfDocument();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(mWebView.getWidth(), mWebView.getHeight(), 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        mWebView.draw(page.getCanvas());

        pdfDocument.finishPage(page);
        try {
            pdfDocument.writeTo(getOutputStream(filename));
            Utility.showMessage(this, customer + "고객님의 GiGAEyes 견적서가 생성 되었습니다.");
            sendEstimate(filename);
        } catch (IOException e) {
            e.printStackTrace();
            Utility.showMessage(this, "PDF 파일 생성 중 오류 발생");
        }
        pdfDocument.close();

    }

    private void sendEstimate(String filename) {

        File fileDir = new File(Environment.getExternalStorageDirectory().toString(), "GiGAEyes");
        File filePDF = new File(fileDir, filename);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "GiGAEyes 견적서");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePDF.toString()));
        intent.setData(Uri.parse("mailto:"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    private FileOutputStream getOutputStream(String filename) {

        String dir = Environment.getExternalStorageDirectory().toString();

        Log.i(TAG, "Directory>" + dir);
        File fileDir = new File(dir, "GiGAEyes");
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File filePDF = new File(fileDir, filename);
        if (filePDF.exists()) {
            filePDF.delete();
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(filePDF);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileOutputStream;
    }

}
