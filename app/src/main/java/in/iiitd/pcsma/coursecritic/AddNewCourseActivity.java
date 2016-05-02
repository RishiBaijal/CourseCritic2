package in.iiitd.pcsma.coursecritic;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class AddNewCourseActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    public void onSubmit(View view)
    {
        View v = (View) view.getParent();
        EditText instructorNameText = (EditText) v.findViewById(R.id.instructorNameText);
        EditText courseNameText = (EditText) v.findViewById(R.id.courseNameText);
        EditText courseCodeText = (EditText) v.findViewById(R.id.courseCodeText);

        String instructorName = instructorNameText.getText().toString();
        String courseName = courseNameText.getText().toString();
        String courseCode = courseCodeText.getText().toString();

        SubmitCourseInfoToDb obj = new SubmitCourseInfoToDb();
        obj.execute(instructorName, courseName, courseCode, "0", "0");


    }

    public class SubmitCourseInfoToDb extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(AddNewCourseActivity.this);
            progressDialog.setMessage("Saving...");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                String instructorName = arg0[0];
                String courseName = arg0[1];
                String courseCode = arg0[2];
                Integer aggregate = Integer.parseInt(arg0[3]);
                Integer count = Integer.parseInt(arg0[4]);
                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection newcollection = db.getCollection("course_collection");
//                DBCollection doscollection = db.getCollection("course_collection");
                BasicDBObject alphaDoc = new BasicDBObject();
                alphaDoc.put("course_id", courseCode);
                alphaDoc.put("course_name", courseName);
                alphaDoc.put("course_instructor", instructorName);
                alphaDoc.put("aggregate", aggregate);
                alphaDoc.put("count", count);
                newcollection.insert(alphaDoc);
                return true;


            } catch (Exception e) {
                return false;
            }
        }
        @Override
        protected void onPostExecute(Boolean a)
        {
            super.onPostExecute(a);
            progressDialog.dismiss();
        }

    }

}
