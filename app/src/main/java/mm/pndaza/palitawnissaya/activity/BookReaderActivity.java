package mm.pndaza.palitawnissaya.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;

import mm.pndaza.palitawnissaya.R;
import mm.pndaza.palitawnissaya.utils.MDetect;

public class BookReaderActivity extends AppCompatActivity {

    private boolean actionVisibleState = true;

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
        PDFView pdfView = findViewById(R.id.pdfView);

        //single pages like a ViewPager
        /*
        pdfView.fromAsset("books" + File.separator + filename)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .defaultPage(page-1)
                .scrollHandle(new DefaultScrollHandle(this))
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
        */
        pdfView.fromAsset("books" + File.separator + pdfFileName)
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .defaultPage(nsypage - 1)
                .scrollHandle(new DefaultScrollHandle(this))
                .pageFitPolicy(FitPolicy.WIDTH)
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}