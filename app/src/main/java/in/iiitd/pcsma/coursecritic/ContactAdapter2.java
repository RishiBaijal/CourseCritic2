package in.iiitd.pcsma.coursecritic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Udai on 5/1/2016.
 */

public class ContactAdapter2 extends RecyclerView.Adapter<ContactAdapter2.MyViewHolder> {

    private ArrayList<ContactModel> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView contactName;
        TextView contactEmail;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.contactName = (TextView) itemView.findViewById(R.id.contactNameTextView);
            this.contactEmail = (TextView) itemView.findViewById(R.id.contactEmailTextView);
        }
    }

    public ContactAdapter2(ArrayList<ContactModel> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_layout, parent, false);

        view.setOnClickListener(ReccotoContacts.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.contactName;
        TextView textViewEmail = holder.contactEmail;

        textViewName.setText(dataSet.get(listPosition).getContactName());
        textViewEmail.setText(dataSet.get(listPosition).getContactEmail());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}