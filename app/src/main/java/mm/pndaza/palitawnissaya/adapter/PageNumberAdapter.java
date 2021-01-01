package mm.pndaza.palitawnissaya.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mm.pndaza.palitawnissaya.R;

public class PageNumberAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Integer> list;

    public PageNumberAdapter(Context context, ArrayList<Integer> list) {

        this.context = context;
        this.list = list;
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.tv_list_item);
        textView.setText(ConvertToMyanmarNumber(list.get(position)));

        return convertView;
    }

    private String ConvertToMyanmarNumber(int number) {

        return String.valueOf(number)
                .replace("0", "၀")
                .replace("1", "၁")
                .replace("2", "၂")
                .replace("3", "၃")
                .replace("4", "၄")
                .replace("5", "၅")
                .replace("6", "၆")
                .replace("7", "၇")
                .replace("8", "၈")
                .replace("9", "၉");
    }
}
