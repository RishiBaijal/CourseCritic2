package in.iiitd.pcsma.coursecritic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Apple on 01/05/16.
 */
public class RecommendationsAdapter extends RecyclerView.Adapter<RecommendationsAdapter.MyViewHolder> {

    private ArrayList<RecommendationsModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView recoView;
        TextView timeStampView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.recoView = (TextView) itemView.findViewById(R.id.recoView);
            this.timeStampView = (TextView) itemView.findViewById(R.id.timeStampView);
        }
    }

    public RecommendationsAdapter(ArrayList<RecommendationsModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommendations_layout, parent, false);

        view.setOnClickListener(DisplayReviews.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.recoView;
        TextView textViewEmail = holder.timeStampView;

        textViewName.setText(dataSet.get(listPosition).getRecoText());
        textViewEmail.setText(dataSet.get(listPosition).getTimestamp());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
