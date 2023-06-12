package org.unibl.etf.mr.activityplanner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.unibl.etf.mr.activityplanner.R;
import org.unibl.etf.mr.activityplanner.database.dto.ActivityWithPictures;
import org.unibl.etf.mr.activityplanner.database.enums.ActivityType;

import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.BeanHolder> {

    private final List<ActivityWithPictures> list;
    private final LayoutInflater layoutInflater;
    private final Consumer<Integer> onActivityItemClick;

    public ActivityAdapter(List<ActivityWithPictures> list, Context context, Consumer<Integer> onActivityItemClick) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.onActivityItemClick = onActivityItemClick;
    }

    @NonNull
    @Override
    public BeanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.activity_list_item, parent, false);
        return new BeanHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BeanHolder holder, int position) {
        ActivityWithPictures activity = list.get(position);
        holder.titleTextView.setText(activity.getActivity().getTitle());
        holder.dateTextView.setText(new SimpleDateFormat("dd. MM. yyyy. HH:mm").format(activity.getActivity().getDate()));
        holder.descriptionTextView.setText(activity.getActivity().getDescription());
        ActivityType type = activity.getActivity().getType();
        switch (type) {
            case WORK:
                holder.typeChip.setText(R.string.activity_type_work);
                break;
            case FREE_TIME:
                holder.typeChip.setText(R.string.activity_type_free_time);
                break;
            case TRAVEL:
                holder.typeChip.setText(R.string.activity_type_travel);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BeanHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView;
        TextView dateTextView;
        TextView descriptionTextView;
        Chip typeChip;

        public BeanHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTextView = itemView.findViewById(R.id.titleTv);
            dateTextView = itemView.findViewById(R.id.dateTv);
            descriptionTextView = itemView.findViewById(R.id.descriptionTv);
            typeChip = itemView.findViewById(R.id.typeChip);
        }

        @Override
        public void onClick(View view) {
            onActivityItemClick.accept(getAdapterPosition());
        }
    }
}
