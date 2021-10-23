package mm.pndaza.palitawnissaya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mm.pndaza.palitawnissaya.R;
import mm.pndaza.palitawnissaya.model.Category;
import mm.pndaza.palitawnissaya.model.PaliBook;
import mm.pndaza.palitawnissaya.utils.MDetect;

public class PaliBookAdapter extends BaseAdapter {

    private static final int BOOK = 0;
    private static final int HEADER = 1;

    private Context context;
    private ArrayList<Object> list;

    public PaliBookAdapter(Context context, ArrayList<Object> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        // id of header is added as empty string
        if (list.get(position) instanceof Category){
            return HEADER;
        } else {
            return BOOK;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            switch (getItemViewType(position)){
                case HEADER:
                    convertView = LayoutInflater.from(context).inflate(R.layout.list_header, parent, false);
                    break;
                case BOOK:
                    convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
                    break;
            }
        }


        switch (getItemViewType(position)){
            case HEADER:
                TextView tvHeader = convertView.findViewById(R.id.tv_list_item);
                // display as zawgyi
                final Category category = (Category)list.get(position);
                final String categoryName = MDetect.getDeviceEncodedText(category.getName());
                tvHeader.setText(categoryName);
                break;
            case BOOK:
                TextView tvBook = convertView.findViewById(R.id.tv_list_item);
                // display as zawgyi
                final PaliBook paliBook = (PaliBook)list.get(position);
                final String bookName = MDetect.getDeviceEncodedText(paliBook.getName());
                tvBook.setText(bookName);
                break;
        }

        return convertView;
    }

}
