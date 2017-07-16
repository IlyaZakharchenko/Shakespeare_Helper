package itis11_601.shakespearehelper.adapters;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import itis11_601.shakespearehelper.R;
import itis11_601.shakespearehelper.models.Diary;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private List<Diary> diaries;
    private OnDiaryClickListener listener;

    public DiaryAdapter(List<Diary> diaries, OnDiaryClickListener listener) {
        this.diaries = diaries;
        this.listener = listener;
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder {

        TextView tittle;
        TextView creation_date;
        TextView editing_date;
        RelativeLayout relativeLayout;

        DiaryViewHolder(View itemView) {
            super(itemView);
            tittle = (TextView) itemView.findViewById(R.id.tv_tittle);
            editing_date = (TextView) itemView.findViewById(R.id.tv_editing_date);
            creation_date = (TextView) itemView.findViewById(R.id.tv_creation_date);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
        }
    }

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiaryViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiaryViewHolder holder, int position) {
        holder.tittle.setText(diaries.get(position).getTittle());
        holder.creation_date.setText(diaries.get(position).getCreationDate());
        holder.editing_date.setText(diaries.get(position).getEditingDate());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDiaryClick(diaries.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return diaries.size();
    }

    public interface OnDiaryClickListener {
        void onDiaryClick(Diary diary);
    }

    public int getPosition(Diary diary) {
        return diaries.indexOf(diary);
    }
}
