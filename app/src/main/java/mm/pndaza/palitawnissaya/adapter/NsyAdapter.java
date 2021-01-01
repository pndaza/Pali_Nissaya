package mm.pndaza.palitawnissaya.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import mm.pndaza.palitawnissaya.R;
import mm.pndaza.palitawnissaya.model.Nsy;
import mm.pndaza.palitawnissaya.utils.MDetect;

public class NsyAdapter extends RecyclerView.Adapter<NsyAdapter.ViewHolder> {

    private ArrayList<Nsy> list;
    private Context context;

    private OnItemClickListener onItemClickListener;

    public NsyAdapter(ArrayList<Nsy> list, OnItemClickListener onItemClickListener) {
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View wordListItemView = inflater.inflate(R.layout.nsy_list_item, parent, false);
        return new ViewHolder(wordListItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cover.setImageResource(getResId("cover_" + list.get(position).getId(), R.drawable.class));
        holder.tvName.setText(MDetect.getDeviceEncodedText(list.get(position).getName()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView cover;
        TextView tvName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            cover = itemView.findViewById(R.id.cover);
            tvName = itemView.findViewById(R.id.tv_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(list.get(getAdapterPosition()));
        }
    }


    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Nsy nsy);
    }

}
