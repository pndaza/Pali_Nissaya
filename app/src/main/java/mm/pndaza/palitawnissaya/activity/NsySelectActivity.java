package mm.pndaza.palitawnissaya.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

import mm.pndaza.palitawnissaya.R;
import mm.pndaza.palitawnissaya.adapter.NsyAdapter;
import mm.pndaza.palitawnissaya.database.DBOpenHelper;
import mm.pndaza.palitawnissaya.model.Nsy;
import mm.pndaza.palitawnissaya.utils.MDetect;

public class NsySelectActivity extends AppCompatActivity implements NsyAdapter.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nsy_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MDetect.init(this);
        String title = getString(R.string.choice_nsy);
        setTitle(MDetect.getDeviceEncodedText(title));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final String bookid = bundle.getString("book_id");
        int pageNumber = bundle.getInt("page_number");

        ArrayList<Nsy> nsyArrayList = DBOpenHelper.getInstance(this).getNsyBooks( bookid, pageNumber );
        NsyAdapter adapter = new NsyAdapter(nsyArrayList, this);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        TextView textView = findViewById(R.id.tv_empty);
        if(nsyArrayList.size() <= 0) {
            textView.setText(MDetect.getDeviceEncodedText(getString(R.string.empty_nsy)));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onItemClick(Nsy nsy) {

        Intent intent = new Intent(this, BookReaderActivity.class);
        intent.putExtra("nsyid", nsy.getId());
        intent.putExtra("nsypage", nsy.getPdfPageNumber());
        intent.putExtra("nsyname", nsy.getName());
        startActivity(intent);
    }
}
