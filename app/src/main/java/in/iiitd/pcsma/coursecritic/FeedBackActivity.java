package in.iiitd.pcsma.coursecritic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class  FeedBackActivity extends AppCompatActivity {
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");


    }
    public void submitToDatabase(View view)
    {
        View v = (View) view.getParent();
        TextView textView1 = (TextView) v.findViewById(R.id.question1);
        String question1 = textView1.getText().toString();
        TextView textView2 = (TextView) v.findViewById(R.id.question2);
        String question2 = textView2.getText().toString();
        TextView textView3 = (TextView) v.findViewById(R.id.question3);
        String question3 = textView3.getText().toString();
        TextView textView4 = (TextView) v.findViewById(R.id.question4);
        String question4 = textView4.getText().toString();
        TextView textView5 = (TextView) v.findViewById(R.id.question5);
        String question5 = textView5.getText().toString();

        EditText editText1 = (EditText) v.findViewById(R.id.answer1);
        EditText editText2 = (EditText) v.findViewById(R.id.answer2);
        EditText editText3 = (EditText) v.findViewById(R.id.answer3);
        EditText editText4 = (EditText) v.findViewById(R.id.answer4);
        EditText editText5 = (EditText) v.findViewById(R.id.answer5);

        String answer1 = editText1.getText().toString();
        String answer2 = editText2.getText().toString();
        String answer3 = editText3.getText().toString();
        String answer4 = editText4.getText().toString();
        String answer5 = editText5.getText().toString();

        if (answer1.equals("") || answer2.equals("") || answer3.equals("") || answer4.equals("") || answer5.equals(""))
        {
            Toast.makeText(v.getContext(), "Please fill out all the fields", Toast.LENGTH_SHORT);
        }
        else
        {
            SaveFeedBacktoDB saveFeedBacktoDB = new SaveFeedBacktoDB();
            saveFeedBacktoDB.execute(question1, answer1, question2, answer2, question3, answer3, question4, answer4, question5, answer5);
        }

    }
    public class SaveFeedBacktoDB extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection newcollection = db.getCollection("feedback");
//                DBCollection doscollection = db.getCollection("course_collection");
                BasicDBObject alphaDoc = new BasicDBObject();
                alphaDoc.put("Email", email);
                alphaDoc.put("question1", arg0[0]);
                alphaDoc.put("answer1", arg0[1]);
                alphaDoc.put("question2", arg0[2]);
                alphaDoc.put("answer2", arg0[3]);
                alphaDoc.put("question3", arg0[4]);
                alphaDoc.put("answer3", arg0[5]);
                alphaDoc.put("question4", arg0[6]);
                alphaDoc.put("answer4", arg0[7]);
                alphaDoc.put("question5", arg0[8]);
                alphaDoc.put("answer5", arg0[9]);
                newcollection.insert(alphaDoc);


//                BasicDBObject query = new BasicDBObject();
//                query.put("course_id", courseCode);
//
//                BasicDBObject updated = new BasicDBObject();
//                updated.put("$inc", new BasicDBObject("aggregate", bleh).append("count", 1));
//
//                doscollection.update(query, updated);
//
//
//                alphaDoc.put("Email", email);
//
//                alphaDoc.put("courseCode", courseCode);
//                alphaDoc.put("rating", bleh);
//
//                newcollection.insert(alphaDoc);
                return true;


            } catch (Exception e) {
                return false;
            }
        }
//
//    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
//    }

    }



}

