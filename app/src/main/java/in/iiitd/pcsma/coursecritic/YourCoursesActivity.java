package in.iiitd.pcsma.coursecritic;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class YourCoursesActivity extends AppCompatActivity {

    String tempAcc = "";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.courseView);
        TextView textView1 = (TextView) findViewById(R.id.courseName);
        TextView textView2 = (TextView) findViewById(R.id.courseCode);

        Bundle bundle = getIntent().getExtras();//String username = bundle.getString("username");
        String email = bundle.getString("email");
        // System.out.println("USERNAME:"  + username);
        System.out.println("EMAIL: " + email);

        GetCourseFromDB getCourseFromDB = new GetCourseFromDB();

        try {
            tempAcc = getCourseFromDB.execute(email).get();
            StringTokenizer st = new StringTokenizer(tempAcc);
            String courseCode = st.nextToken();
            String courseName = st.nextToken();
            String instructor = st.nextToken();
            textView.setText("Course code: " + courseCode);
            textView1.setText("Name of the course: " + courseName);
            textView2.setText("Instructor : " + instructor);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    protected void onPause()
    {
        super.onPause();
        if(progressDialog != null)
            progressDialog.dismiss();

    }

    public class GetCourseFromDB extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(YourCoursesActivity.this, "Fetching your courses", "Contacting the server. Please wait...", true, false);

        }

        @Override
        protected String doInBackground(String... arg0) {
            try
            {
                String tempAcc = "";
                String email = arg0[0];
                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection newcollection = db.getCollection("student_collection");
                BasicDBObject searchQuery = new BasicDBObject();
                searchQuery.put("email", email);
                DBCursor dbCursor = newcollection.find(searchQuery);
                while (dbCursor.hasNext())
                {
                    DBObject dbObject = dbCursor.next();
                    String course_id = (String) dbObject.get("course_id");
                    System.out.println("COURSE ID: " + course_id);
                    DBCollection courseCollection = db.getCollection("course_collection");
                    BasicDBObject searchQuery2 = new BasicDBObject();
                    searchQuery2.put("course_id", course_id);
                    DBCursor dbCursor1 = courseCollection.find(searchQuery2);

                    while (dbCursor1.hasNext()) {

                        DBObject dbObject1 = dbCursor1.next();
                        String course_name = (String) dbObject1.get("course_name");
                        String course_instructor = (String) dbObject1.get("course_instructor");
                        tempAcc = course_id + " " + course_name + " " + course_instructor;
                        System.out.println("THE VALUE OF TEMPACC IS: " + tempAcc);
                        tempAcc = tempAcc + "\n";
                    }
                }
                return tempAcc;


            }
            catch (Exception e)
            {
                e.printStackTrace();
                return "NULLA";
            }
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
    }

}
