package me.nakeeb.almezan.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.nakeeb.almezan.R;
import me.nakeeb.almezan.model.DateItem;

/**
 * Created by mamdouhelnakeeb on 12/13/17.
 */

public class DateRVAdapter extends RecyclerView.Adapter<DateRVAdapter.ViewHolder> {

    Context context;
    ArrayList<DateItem> dates;

    public DateRVAdapter(Context context, ArrayList<DateItem> dates){

        this.context = context;
        this.dates = dates;
    }

    @Override
    public DateRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());

        ViewGroup viewGroup = (ViewGroup) mInflater.inflate(R.layout.daterv_item, parent, false);

        return new ViewHolder(viewGroup);
    }

    @Override
    public void onBindViewHolder(DateRVAdapter.ViewHolder holder, int position) {

        String date = dates.get(position).date;

        holder.dateTV.setText(date);
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView dateTV;

        public ViewHolder(View itemView) {

            super(itemView);

            dateTV = itemView.findViewById(R.id.miladiDateTV);

        }
    }
}
