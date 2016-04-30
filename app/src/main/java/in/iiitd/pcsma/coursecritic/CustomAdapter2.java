package in.iiitd.pcsma.coursecritic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Udai on 4/30/2016.
 */
public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyViewHolder> {

    private ArrayList<DataModel2> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView courseView;
        TextView courseName;
        TextView courseCode;
        TextView courseRating;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.courseView = (TextView) itemView.findViewById(R.id.courseView);
            this.courseName = (TextView) itemView.findViewById(R.id.courseName);
            this.courseCode = (TextView) itemView.findViewById(R.id.courseCode);
            this.courseRating = (TextView) itemView.findViewById(R.id.courseRating);
        }
    }

    public CustomAdapter2(ArrayList<DataModel2> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards2_layout, parent, false);

        view.setOnClickListener(Top50Activity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView courseView = holder.courseView;
        TextView courseName = holder.courseName;
        TextView courseCode = holder.courseCode;
        TextView courseRating = holder.courseRating;

        courseView.setText("Instructor name: " + dataSet.get(listPosition).getInstructorName());
        courseName.setText("Course Name: " + dataSet.get(listPosition).getCourseName());
        courseCode.setText("Course code: " + dataSet.get(listPosition).getCourseCode());
        courseRating.setText("Course Rating: " + dataSet.get(listPosition).getRating());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
