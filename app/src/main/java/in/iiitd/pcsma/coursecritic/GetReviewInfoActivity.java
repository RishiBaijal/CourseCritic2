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

public class GetReviewInfoActivity extends AppCompatActivity {

    HashSet<String> courseInfo = new HashSet<String>();
    static String courseNames[], courseCodes[], instructorNames[];


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_review_info);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.review_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel>();

        GetCourseInfoFromDB obj = new GetCourseInfoFromDB();
        try {
            String tempAcc = obj.execute(email).get();
            System.out.println("tempAcc from GetReviewInfoActivity: " + tempAcc);
            int length = courseInfo.size();
            courseNames = new String[length];
            courseCodes = new String[length];
            instructorNames = new String[length];

            Iterator iterator = courseInfo.iterator();
            int c = 0;
            while (iterator.hasNext()) {
                String nextElement = (String) iterator.next();
                StringTokenizer st = new StringTokenizer(nextElement, ";");
                System.out.println("Next element: " + nextElement);
                if (st.countTokens() == 3) {
                    String courseName = st.nextToken();
                    String courseCode = st.nextToken();
                    String instructor = st.nextToken();
                    /*String aggregate = st.nextToken();
                    String count = st.nextToken();*/

                    courseCodes[c] = courseCode;
                    courseNames[c] = courseName;
                    instructorNames[c] = instructor;
                    c++;
                }
            }
            int i;
            for (i = 0; i < c; i++) {
                System.out.println("Course code array element #: " + i + " " + courseCodes[i]);
                System.out.println("Course Names array element #: " + i + " " + courseNames[i]);
                System.out.println("Instructor names array element #: " + i + " " + instructorNames[i]);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < courseInfo.size(); i++) {
            data.add(new DataModel(courseCodes[i], courseNames[i], instructorNames[i]));
        }
        adapter = new ViewReviewAdapter(data);
        recyclerView.setAdapter(adapter);

    }

    public class GetCourseInfoFromDB extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String tempAcc = "";
                String email = arg0[0];
                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection newcollection = db.getCollection("student_collection");
                BasicDBObject searchQuery = new BasicDBObject();
                searchQuery.put("email", email);
                DBCursor dbCursor = newcollection.find(searchQuery);
                while (dbCursor.hasNext()) {
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
                        tempAcc = course_id + ";" + course_name + ";" + course_instructor;
                        System.out.println("THE VALUE OF TEMPACC IS: " + tempAcc);
                    }
                    //tempAcc = tempAcc + ";";
                    if (!(tempAcc.equals("")))
                        courseInfo.add(tempAcc);

                }
                return tempAcc;


            } catch (Exception e) {
                e.printStackTrace();
                return "NULLA";
            }
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            System.out.println("The slut's cardview is clicked. ");

        }

    }


}
