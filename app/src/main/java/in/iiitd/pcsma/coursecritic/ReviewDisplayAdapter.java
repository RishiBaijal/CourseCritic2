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
public class ReviewDisplayAdapter extends RecyclerView.Adapter<ReviewDisplayAdapter.MyViewHolder> {

    private ArrayList<ReviewModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView questionView;
        TextView answerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.questionView = (TextView) itemView.findViewById(R.id.questionView);
            this.answerView = (TextView) itemView.findViewById(R.id.answerView);
        }
    }

    public ReviewDisplayAdapter(ArrayList<ReviewModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_review, parent, false);

        view.setOnClickListener(DisplayReviews.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.questionView;
        TextView textViewEmail = holder.answerView;

        textViewName.setText(dataSet.get(listPosition).getQuestion());
        textViewEmail.setText(dataSet.get(listPosition).getAnswer());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}