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
import android.widget.RatingBar;
import android.widget.TextView;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DisplayReviews extends AppCompatActivity {

    ArrayList<String> questionArray = new ArrayList<String>();
    ArrayList<String> answerArray = new ArrayList<String>();
    ArrayList<String> ratingArray = new ArrayList<String>();


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<ReviewModel> data;
    static View.OnClickListener myOnClickListener;

    String courseCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.display_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<ReviewModel>();
        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        courseCode = bundle.getString("courseCode");

        myOnClickListener = new MyOnClickListener(this);


        GetReviewFromDB getReviewFromDB = new GetReviewFromDB();
        String rating = null;
        try {
            rating = getReviewFromDB.execute(email).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setNumStars(Integer.parseInt(rating));
        System.out.println("QuestionArray: " + questionArray);
        System.out.println("Answer array: " + answerArray);
        for (int i = 0; i < questionArray.size(); i++) {
            data.add(new ReviewModel(questionArray.get(i), answerArray.get(i)));
        }
        adapter = new ReviewDisplayAdapter(data);
        recyclerView.setAdapter(adapter);

    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            System.out.println("FROM DisplayReviewsActivity");
//            TextView textView = (TextView) v.findViewById(R.id.courseCode);
//            String courseCodeLocal = textView.getText().toString();
//            Intent intent = new Intent(v.getContext(), ReviewActivity.class);
//            intent.putExtra("email", email);
//            intent.putExtra("currentCourseCode", courseCodeLocal);
//            startActivityForResult(intent, 0);

        }
    }
    public class GetReviewFromDB extends AsyncTask<String, Void, String> {
        String rating = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(Top50Activity.this, "Fetching your courses", "Contacting the server. Please wait...", true, false);

        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String tempAcc = "";
                String email = arg0[0];
                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection newcollection = db.getCollection("review_collection");
                BasicDBObject searchQuery = new BasicDBObject();
                List<BasicDBObject> list = new ArrayList<BasicDBObject>();
                list.add(new BasicDBObject("Email", email));
                list.add(new BasicDBObject("courseCode", courseCode));
                searchQuery.put("$and", list);
                DBCursor dbCursor = newcollection.find(searchQuery);
                while (dbCursor.hasNext()) {
                    DBObject dbObject = dbCursor.next();
                    String question = (String) dbObject.get("question");
                    String answer = (String) dbObject.get("answer");
                    questionArray.add(question);
                    answerArray.add(answer);

                }
                DBCollection ratingCollection = db.getCollection("rating_collection");
                DBCursor dbCursor1 = ratingCollection.find(searchQuery);
                while (dbCursor1.hasNext())
                {
                    DBObject dbObject = dbCursor1.next();
                    rating = String.valueOf((Integer) dbObject.get("rating"));
                }
                return rating;


            } catch (Exception e) {
                e.printStackTrace();
                return "NULLA";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
        }
    }


}
