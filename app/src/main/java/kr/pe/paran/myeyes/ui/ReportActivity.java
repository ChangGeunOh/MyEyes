package kr.pe.paran.myeyes.ui;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kr.pe.paran.myeyes.R;
import kr.pe.paran.myeyes.Utility;
import kr.pe.paran.myeyes.model.Estimate;
import kr.pe.paran.myeyes.model.ProductPrice;
import kr.pe.paran.myeyes.model.UnitPrice;

public class ReportActivity extends AppCompatActivity {

    private final String     TAG     = getClass().getSimpleName();

    public static final String ASSET_PATH = "file:///android_asset/";

    private WebView         mWebView;
    private StringBuilder   mHtmlTagBuilder;
    private long            mTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Estimate estimate = getEstimate();
        setTitle(estimate.custmer);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mWebView = findViewById(R.id.webView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        ArrayList<Integer> cntCategory = new ArrayList<>();
        int start = 0; int count = 0;
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

        mHtmlTagBuilder = new StringBuilder();
        for (int i = 0; i < estimate.productPrices.size(); i++) {
            ProductPrice productPrice = estimate.productPrices.get(i);
            mTotalPrice += productPrice.sum;
            if (cntCategory.get(i) > 0) {
                mHtmlTagBuilder.append(String.format(row_first,
                        cntCategory.get(i),
                        productPrice.category,
                        productPrice.product,
                        productPrice.standard,
                        Utility.getFormated(productPrice.price),
                        productPrice.count,
                        Utility.getFormated(productPrice.sum)));
            }
            else {
                mHtmlTagBuilder.append(String.format(row_middle,
                        productPrice.product,
                        productPrice.standard,
                        Utility.getFormated(productPrice.price),
                        productPrice.count,
                        Utility.getFormated(productPrice.sum)));
            }
        }
        loadChart();
    }


    private void loadChart() {

        String content = null;
        try {
            AssetManager assetManager = getAssets();
            InputStream in = assetManager.open("Report.html");
            byte[] bytes = readFully(in);
            content = new String(bytes, "UTF-8");
        } catch (IOException e) {
        }

        content = String.format(content, mHtmlTagBuilder.toString(), Utility.getFormated(mTotalPrice));

        mWebView.loadDataWithBaseURL(ASSET_PATH, content.replace("!", "%"), "text/html", "utf-8", null);
        mWebView.requestFocusFromTouch();

    }

    private static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1;) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }



    private static String  row_first = "<tr>" +
            "<td rowspan='%d'>%s</td>" +
            "<td>%s</td>" +
            "<td>%s</td>" +
            "<td class='value_price'>%s</td>" +
            "<td>%d</td>" +
            "<td class='value_price'>%s</td>" +
            "</tr>";

    private static String  row_middle= "<tr>" +
            "<td>%s</td>" +
            "<td>%s</td>" +
            "<td class='value_price'>%s</td>" +
            "<td>%d</td>" +
            "<td class='value_price'>%s</td>" +
            "</tr>";

    private Estimate getEstimate() {

        Estimate estimate = new Estimate("테스트");

        ProductPrice productPrice = new ProductPrice();
        productPrice.category   = "노무비";
        productPrice.product    = "인건비";
        productPrice.standard   = "중급기술자";
        productPrice.unit       = "인";
        productPrice.price      = 180000;
        productPrice.count      = 1;
        productPrice.sum        = productPrice.count * productPrice.price;
        estimate.addProduct(productPrice);

        productPrice = new ProductPrice();
        productPrice.category   = "노무비";
        productPrice.product    = "인건비";
        productPrice.standard   = "초급기술자";
        productPrice.unit       = "인";
        productPrice.price      = 180000;
        productPrice.count      = 2;
        productPrice.sum        = productPrice.count * productPrice.price;
        estimate.addProduct(productPrice);

        productPrice = new ProductPrice();
        productPrice.category   = "서비스";
        productPrice.product    = "인터넷";
        productPrice.standard   = "기가인터넷";
        productPrice.unit       = "월";
        productPrice.price      = 35000;
        productPrice.count      = 3;
        productPrice.sum        = productPrice.count * productPrice.price;
        estimate.addProduct(productPrice);

        productPrice = new ProductPrice();
        productPrice.category   = "서비스";
        productPrice.product    = "인터넷";
        productPrice.standard   = "컴팩트인터넷";
        productPrice.unit       = "월";
        productPrice.price      = 30000;
        productPrice.count      = 2;
        productPrice.sum        = productPrice.count * productPrice.price;
        estimate.addProduct(productPrice);

        productPrice = new ProductPrice();
        productPrice.category   = "서비스";
        productPrice.product    = "CCTV";
        productPrice.standard   = "9-50";
        productPrice.unit       = "월";
        productPrice.price      = 9000;
        productPrice.count      = 5;
        productPrice.sum        = productPrice.count * productPrice.price;
        estimate.addProduct(productPrice);

        productPrice = new ProductPrice();
        productPrice.category   = "자재";
        productPrice.product    = "브라켓";
        productPrice.standard   = "돔";
        productPrice.unit       = "개";
        productPrice.price      = 3000;
        productPrice.count      = 2;
        productPrice.sum        = productPrice.count * productPrice.price;
        estimate.addProduct(productPrice);

        productPrice = new ProductPrice();
        productPrice.category   = "장비";
        productPrice.product    = "POE";
        productPrice.standard   = "48p";
        productPrice.unit       = "개";
        productPrice.price      = 636000;
        productPrice.count      = 1;
        productPrice.sum        = productPrice.count * productPrice.price;
        estimate.addProduct(productPrice);


        return estimate;
    }
}