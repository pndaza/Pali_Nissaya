package mm.pndaza.palitawnissaya.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;

import mm.pndaza.palitawnissaya.R;
import mm.pndaza.palitawnissaya.utils.MDetect;
import mm.pndaza.palitawnissaya.utils.ScrollMode;
import mm.pndaza.palitawnissaya.utils.SharePref;

public class BookReaderActivity extends AppCompatActivity {

    private PDFView pdfView;
    private String assetName;
    private boolean actionVisibleState = true;
    private ScrollMode scrollMode;
    private boolean nightMode;
    private SharePref sharePref;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String nsyid = intent.getStringExtra("nsyid");
        int nsypage = intent.getIntExtra("nsypage", 1);
        String nsyName = intent.getStringExtra("nsyname");

        MDetect.init(this);
        setTitle(MDetect.getDeviceEncodedText(nsyName));

        String pdfFileName = nsyid + ".pdf";
        assetName = "books" + File.separator + pdfFileName;
        currentPage = nsypage - 1;
        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt("currentPage", 0);
        }
        pdfView = findViewById(R.id.pdfView);

        sharePref = SharePref.getInstance(this);
        scrollMode = sharePref.getScrollMode();
        nightMode = sharePref.getNightMode();

        loadPdf();


        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionVisibleState = !actionVisibleState;
                if (actionVisibleState) {
                    getSupportActionBar().show();
                } else {
                    getSupportActionBar().hide();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.

        savedInstanceState.putInt("currentPage", currentPage);
        // etc.
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_reader, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        switch (id) {
            case R.id.menu_scroll_mode:
                swapIcon(item);
                changePageMode();
                return true;
            case R.id.menu_night_mode:
                changeNightMode(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void swapIcon(MenuItem item) {
        if (scrollMode == ScrollMode.vertical) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_swap_horiz_24));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_swap_vert_24));
        }

    }

    private void changeNightMode(MenuItem item) {
        currentPage = pdfView.getCurrentPage();
        nightMode = !nightMode;
        sharePref.setNightMode(nightMode);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // changing night mode will load onCreate method again
        // doesn't need to load pdf
        // current page will be save in onSaveInstanceState method
    }

    private void changePageMode() {

        currentPage = pdfView.getCurrentPage();
        if (scrollMode == ScrollMode.vertical) {
            scrollMode = ScrollMode.horizontal;
        } else {
            scrollMode = ScrollMode.vertical;
        }
        sharePref.setScrollMode(scrollMode);

        loadPdf();

    }

    private void loadPdf() {
        pdfView.fromAsset(assetName)
                .defaultPage(currentPage)
                .enableSwipe(true)
                .pageFitPolicy(scrollMode == ScrollMode.horizontal ? FitPolicy.BOTH : FitPolicy.WIDTH)
                .swipeHorizontal(scrollMode == ScrollMode.horizontal)
                .pageSnap(scrollMode == ScrollMode.horizontal)
                .autoSpacing(true)// allows to block changing pages using swipe
                .pageFling(scrollMode == ScrollMode.horizontal)
                .scrollHandle(new DefaultScrollHandle(this))
                .nightMode(nightMode)
                .load();
    }

}