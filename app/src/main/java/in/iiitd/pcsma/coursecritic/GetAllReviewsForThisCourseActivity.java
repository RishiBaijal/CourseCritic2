package in.iiitd.pcsma.coursecritic;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class GetAllReviewsForThisCourseActivity extends AppCompatActivity {

    HashSet<String> reviewInfo = new HashSet<String>();
    String tempAcc = "";
    String emailArray[], questionArray[], answerArray[];


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<AllReviewsModel> data;
    static View.OnClickListener myOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_reviews_for_this_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String courseCode = bundle.getString("courseCode");

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.all_courses_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<AllReviewsModel>();


        GetReviewsFromDB getReviewsFromDB = new GetReviewsFromDB();
        try {
            int c = 0;
            tempAcc = getReviewsFromDB.execute(courseCode).get();
            System.out.println("ReviewInfo is: " + reviewInfo);
            int len = reviewInfo.size();
            emailArray = new String[len];
            questionArray = new String[len];
            answerArray = new String[len];

            Iterator iterator = reviewInfo.iterator();
            while (iterator.hasNext()) {
                String temp = (String) iterator.next();
                System.out.println("The value of temp:" + temp);
                StringTokenizer st = new StringTokenizer(temp, ";");
                emailArray[c] = st.nextToken();
                questionArray[c] = st.nextToken();
                answerArray[c] = st.nextToken();
                c++;
            }
            for (int i = 0; i < c; i++)
            {
                System.out.println("ame here");
                System.out.println("EMAIL: " + emailArray[i]);

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        int i;
        for (i = 0; i < reviewInfo.size(); i++) {
            data.add(new AllReviewsModel(emailArray[i], questionArray[i], answerArray[i]));
        }

        adapter = new AllReviewsAdapter(data);
        recyclerView.setAdapter(adapter);

    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            System.out.println("If this doesn't work, I am fucked in the ass.");

        }

//        private void removeItem(View v) {
//            int selectedItemPosition = recyclerView.getChildPosition(v);
//            RecyclerView.ViewHolder viewHolder
//                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
//            TextView textViewName
//                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
//            String selectedName = (String) textViewName.getText();
//            int selectedItemId = -1;
//            for (int i = 0; i < MyData.nameArray.length; i++) {
//                if (selectedName.equals(MyData.nameArray[i])) {
//                    selectedItemId = MyData.id_[i];
//                }
//            }
//            removedItems.add(selectedItemId);
//            data.remove(selectedItemPosition);
//            adapter.notifyItemRemoved(selectedItemPosition);
//        }
    }

    public class GetReviewsFromDB extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String tempAcc = "";
                String courseCode = arg0[0];
                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection newcollection = db.getCollection("review_collection");
                BasicDBObject searchQuery = new BasicDBObject();
                searchQuery.put("courseCode", courseCode);
                DBCursor dbCursor = newcollection.find(searchQuery);
                while (dbCursor.hasNext()) {
                    DBObject dbObject = dbCursor.next();
                    String email = (String) dbObject.get("Email");
                    String question = (String) dbObject.get("question");
                    String answer = (String) dbObject.get("answer");


                    tempAcc = email + ";" + question + ";" + answer;
                    System.out.println("THE VALUE OF TEMPACC IS: " + tempAcc);
                    if (!(tempAcc.equals("")))
                        reviewInfo.add(tempAcc);
                }
                //tempAcc = tempAcc + ";";


            } catch (Exception e) {
                e.printStackTrace();
                return "NULLA";
            }
            return tempAcc;


        }


    }

}
