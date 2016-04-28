package in.iiitd.pcsma.coursecritic;

import android.app.ProgressDialog;
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
import java.util.*;
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
    HashSet<String> courseInfo = new HashSet<String>();
    static String courseNames[], courseCodes[], instructorNames[];



    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel>();

//        TextView textView = (TextView) findViewById(R.id.courseView);
//        TextView textView1 = (TextView) findViewById(R.id.courseName);
//        TextView textView2 = (TextView) findViewById(R.id.courseCode);



        Bundle bundle = getIntent().getExtras();//String username = bundle.getString("username");
        String email = bundle.getString("email");
        // System.out.println("USERNAME:"  + username);
        System.out.println("EMAIL: " + email);

        GetCourseFromDB getCourseFromDB = new GetCourseFromDB();

        try {
            tempAcc = getCourseFromDB.execute(email).get();
            int length = courseInfo.size();
            courseNames = new String[length];
            courseCodes = new String[length];
            instructorNames = new String[length];

            Iterator iterator = courseInfo.iterator();
            int c = 0;
            while (iterator.hasNext())
            {
                String nextElement = (String) iterator.next();
                StringTokenizer st = new StringTokenizer(nextElement);
                System.out.println("Next element: " + nextElement);
                if (st.countTokens() == 3) {
                    String courseCode = st.nextToken();
                    String courseName = st.nextToken();
                    String instructor = st.nextToken();
                    courseCodes[c] = courseCode;
                    courseNames[c] = courseName;
                    instructorNames[c] = instructor;
                    c++;
                }
            }
            c = 0;
            int i;
            for (i = 0; i < 3; i++) {
                System.out.println("Course code array element #: " + i + " " + courseCodes[i]);
                System.out.println("Course Names array element #: " + i + " " + courseNames[i]);
                System.out.println("Instructor names array element #: " + i + " " + instructorNames[i]);
            }



            System.out.println("TEMPACC KI VALUE HAI: " + tempAcc);
            //StringTokenizer st = new StringTokenizer(tempAcc);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(courseInfo);
        for (int i = 0; i < courseInfo.size(); i++)
        {
            data.add(new DataModel(courseCodes[i], courseNames[i], instructorNames[i]));
        }
        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);

    }


    @Override
    protected void onPause()
    {
        super.onPause();
        if(progressDialog != null)
            progressDialog.dismiss();
    }

    public void addCourseReview(View view)
    {

    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ReviewActivity.class);
            startActivityForResult(intent, 0);

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
                    }
                    //tempAcc = tempAcc + ";";
                    courseInfo.add(tempAcc);

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
