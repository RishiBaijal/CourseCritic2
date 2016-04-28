package in.iiitd.pcsma.coursecritic;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Apple on 28/04/16.
 */public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView courseView;
        TextView courseName;
        TextView courseCode;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.courseView = (TextView) itemView.findViewById(R.id.courseView);
            this.courseName = (TextView) itemView.findViewById(R.id.courseName);
            this.courseCode = (TextView) itemView.findViewById(R.id.courseCode);
        }
    }

    public CustomAdapter(ArrayList<DataModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);

        view.setOnClickListener(YourCoursesActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView courseView = holder.courseView;
        TextView courseName = holder.courseName;
        TextView courseCode = holder.courseCode;

        courseView.setText("Instructor name: " + dataSet.get(listPosition).getInstructorName());
        courseName.setText("Course Name: " + dataSet.get(listPosition).getCourseName());
        courseCode.setText("Course code: " + dataSet.get(listPosition).getCourseCode());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
