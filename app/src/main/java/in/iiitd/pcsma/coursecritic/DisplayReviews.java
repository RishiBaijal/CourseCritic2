package in.iiitd.pcsma.coursecritic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;

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


    String courseCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        courseCode = bundle.getString("courseCode");

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
