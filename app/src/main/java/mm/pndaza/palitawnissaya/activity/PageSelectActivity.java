package mm.pndaza.palitawnissaya.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import mm.pndaza.palitawnissaya.R;
import mm.pndaza.palitawnissaya.adapter.PageIndexNumberAdapter;
import mm.pndaza.palitawnissaya.adapter.PageNumberAdapter;
import mm.pndaza.palitawnissaya.database.DBOpenHelper;
import mm.pndaza.palitawnissaya.model.PaliBook;
import mm.pndaza.palitawnissaya.utils.MDetect;

public class PageSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_select);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        PaliBook paliBook = intent.getParcelableExtra("pali_book");

        MDetect.init(this);
        setTitle(MDetect.getDeviceEncodedText(paliBook.getName()));

        int firstPage = paliBook.getFirstPage();
        int lastPage = paliBook.getLastPage();
        int pageCount = (lastPage - firstPage) + 1;

        ArrayList<Integer> pageNumbers = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            pageNumbers.add( i + firstPage);
        }

        PageNumberAdapter adapter = new PageNumberAdapter(this, pageNumbers);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pageNumber = pageNumbers.get(position) ;

                if ( isNsyExist(paliBook.getId(), pageNumber)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("book_id", paliBook.getId());
                    bundle.putInt("page_number", pageNumber);

                    Intent intent = new Intent(PageSelectActivity.this, NsySelectActivity.class);
                    intent.putExtras(bundle);
                    PageSelectActivity.this.startActivity(intent);

                } else {

                    showMessage(getString(R.string.empty_nsy));
                }
            }
        });


        ArrayList<Integer> indexNumbers = new ArrayList<>();
        indexNumbers.add(firstPage);
        int step = 40;
        for(int i= step; i < lastPage; i = i + step ){
            if(i > firstPage) {
                indexNumbers.add(i);
            }
        }
        indexNumbers.add(lastPage);
        PageIndexNumberAdapter indexNumberAdapter = new PageIndexNumberAdapter(this, indexNumbers);
        ListView list_view_index = findViewById(R.id.list_view_index);
        list_view_index.setAdapter(indexNumberAdapter);


        list_view_index.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pageNumber = (Integer) parent.getItemAtPosition(position);

                listView.setSelection(pageNumbers.indexOf(pageNumber));

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void showMessage(String msg){
        Toast toast = Toast.makeText(this, MDetect.getDeviceEncodedText(msg), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private boolean isNsyExist(String bookid, int pageNumber){

        return DBOpenHelper.getInstance(this).isNsyExist(bookid, pageNumber);
    }
}
