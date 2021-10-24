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
    SharePref sharePref;

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
        pdfView = findViewById(R.id.pdfView);

        sharePref =  SharePref.getInstance(this);
        scrollMode = sharePref.getScrollMode();
        nightMode = sharePref.getNightMode();

        pdfView.fromAsset("books" + File.separator + pdfFileName)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(scrollMode == ScrollMode.horizontal)
                .pageSnap(scrollMode == ScrollMode.horizontal)
                .defaultPage(nsypage - 1)
                .scrollHandle(new DefaultScrollHandle(this))
                .pageFitPolicy(FitPolicy.WIDTH)
                .nightMode(nightMode)
                .load();


        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionVisibleState) {
                    getSupportActionBar().hide();
                    actionVisibleState = false;
                } else {
                    getSupportActionBar().show();
                    actionVisibleState = true;
                }
            }
        });
    }

//    @Override
//    protected void onNightModeChanged(int mode) {
//        SharePref sharePref = SharePref.getInstance(this);
//        if (sharePref.getNightMode()) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }
//        super.onNightModeChanged(mode);
//    }

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
    private void swapIcon(MenuItem item){
        if( scrollMode == ScrollMode.vertical) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_swap_horiz_24));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_baseline_swap_vert_24));
        }

    }

//    private void swapNightModeIcon(MenuItem item){
//
//        if( !nightMode) {
//            item.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
//
//        } else {
//            item.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
//        }
//
//    }

    private  void changeNightMode(MenuItem item){
        if(nightMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            item.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            pdfView.setNightMode(false);
            pdfView.loadPages();
            nightMode = false;
            sharePref.setNightModeState(false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            item.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            pdfView.setNightMode(true);
            pdfView.loadPages();
            nightMode = true;
            sharePref.setNightModeState(true);
        }
    }

    private void changePageMode(){

        int currentPage = pdfView.getCurrentPage();
        if( scrollMode == ScrollMode.vertical) {
            pdfView.fromAsset(assetName)
                    .enableSwipe(true) // allows to block changing pages using swipe
                    .swipeHorizontal(true)
                    .pageSnap(true)
                    .autoSpacing(true)
                    .pageFling(true)
                    .pageFitPolicy(FitPolicy.BOTH)
                    .defaultPage(currentPage)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .nightMode(nightMode)
                    .load();
            scrollMode = ScrollMode.horizontal;
            sharePref.setScrollMode(scrollMode);
        } else {
            pdfView.fromAsset(assetName)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .defaultPage(currentPage)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .pageFitPolicy(FitPolicy.WIDTH)
                    .nightMode(nightMode)
                    .load();
            scrollMode = ScrollMode.vertical;
            sharePref.setScrollMode(scrollMode);
        }

    }

}