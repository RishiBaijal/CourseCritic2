package in.iiitd.pcsma.coursecritic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Apple on 02/05/16.
 */
public class AllReviewsAdapter extends RecyclerView.Adapter<AllReviewsAdapter.MyViewHolder> {

    private ArrayList<AllReviewsModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView emailView, questionView, answerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.emailView = (TextView) itemView.findViewById(R.id.emailView);
            this.questionView = (TextView) itemView.findViewById(R.id.questionView);
            this.answerView = (TextView) itemView.findViewById(R.id.answerView);
        }
    }

    public AllReviewsAdapter(ArrayList<AllReviewsModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_reviews_card_layout, parent, false);

        view.setOnClickListener(GetAllReviewsForThisCourseActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView emailView = holder.emailView;
        TextView questionView = holder.questionView;
        TextView answerView = holder.answerView;

        emailView.setText(dataSet.get(listPosition).getEmailId());
        questionView.setText(dataSet.get(listPosition).getQuestion());
        answerView.setText(dataSet.get(listPosition).getAnswer());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}