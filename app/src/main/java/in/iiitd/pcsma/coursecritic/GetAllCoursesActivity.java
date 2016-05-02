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

public class GetAllCoursesActivity extends AppCompatActivity {

    ArrayList<String> courseIdList = new ArrayList<String>();
    ArrayList<String> courseNameList = new ArrayList<String>();
    ArrayList<String> courseInstructorList = new ArrayList<String>();
    ArrayList<String> ratingList = new ArrayList<String>();
    HashSet<String> courseInfo = new HashSet<String>();
    static String courseNames[], courseCodes[], instructorNames[], RatingArray[];


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel2> data;
    static View.OnClickListener myOnClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        final String email = bundle.getString("email");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AddNewCourseActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        GetAllCoursesFromDB getAllCoursesFromDB = new GetAllCoursesFromDB();
        try {
            String tempAcc = getAllCoursesFromDB.execute("").get();
            int length = courseInfo.size();
            courseNames = new String[length];
            courseCodes = new String[length];
            instructorNames = new String[length];
            RatingArray = new String[length];

            Iterator iterator = courseInfo.iterator();
            int c = 0;
            while (iterator.hasNext()) {
                String nextElement = (String) iterator.next();
                StringTokenizer st = new StringTokenizer(nextElement, ";");
                System.out.println("Next element: " + nextElement);
                if (st.countTokens() == 4) {
                    String courseName = st.nextToken();
                    String courseCode = st.nextToken();
                    String instructor = st.nextToken();
                    String Rating = st.nextToken();
                    /*String aggregate = st.nextToken();
                    String count = st.nextToken();*/

                    courseCodes[c] = courseCode;
                    courseNames[c] = courseName;
                    instructorNames[c] = instructor;
                    RatingArray[c] = Rating;
                    c++;
                }
            }
            //Comment out c here
            //c = 0;
            int i;
            //Make 3 -> c here
            for (i = 0; i < c; i++) {
                System.out.println("Course code array element #: " + i + " " + courseCodes[i]);
                System.out.println("Course Names array element #: " + i + " " + courseNames[i]);
                System.out.println("Instructor names array element #: " + i + " " + instructorNames[i]);
                System.out.println("Rating array element #: " + i + " " + RatingArray[i]);
            }


            System.out.println("TEMPACC KI VALUE HAI: " + tempAcc);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        recyclerView = (RecyclerView) findViewById(R.id.all_courses_recycler_view);
        recyclerView.setHasFixedSize(true);
        myOnClickListener = new MyOnClickListener(this);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel2>();
        int i;
        for (i = 0; i < courseCodes.length; i++) {
            data.add(new DataModel2(courseCodes[i], courseNames[i], instructorNames[i], RatingArray[i]));
        }
        adapter = new CustomAdapter2(data);
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
            TextView courseCodeView = (TextView) v.findViewById(R.id.courseCode);
            String courseCode = courseCodeView.getText().toString();
            Intent intent = new Intent(v.getContext(), GetAllReviewsForThisCourseActivity.class);
            intent.putExtra("courseCode", courseCode.substring(13));
            System.out.println("THE VALUE OF COURSECODE IS:" + courseCode.substring(13) + ";");
            startActivity(intent);
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

    public class GetAllCoursesFromDB extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... arg0) {
            try {
                String tempAcc = "";
                String email = arg0[0];
                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection newcollection = db.getCollection("course_collection");
                BasicDBObject searchQuery = new BasicDBObject();
                //searchQuery.put("email", email);
                DBCursor dbCursor = newcollection.find();
                while (dbCursor.hasNext()) {
                    DBObject dbObject = dbCursor.next();
                    String course_id = (String) dbObject.get("course_id");
                    String course_name = (String) dbObject.get("course_name");
                    String course_instructor = (String) dbObject.get("course_instructor");
                    Integer aggregate = (Integer) dbObject.get("aggregate");
                    Integer count = (Integer) dbObject.get("count");
                    String temp = String.valueOf(aggregate);
                    float newaggregate = Float.parseFloat(temp);
                    float average = (newaggregate / count);
                    String sent = String.valueOf(average);
                    tempAcc = course_id + ";" + course_name + ";" + course_instructor + ";" + sent;
                    if (!(tempAcc.equals("")))
                        courseInfo.add(tempAcc);
                }
//                    String course_id = (String) dbObject.get("course_id");
//                    System.out.println("COURSE ID: " + course_id);
//                    DBCollection courseCollection = db.getCollection("course_collection");
//                    BasicDBObject searchQuery2 = new BasicDBObject();
//                    searchQuery2.put("course_id", course_id);
//                    DBCursor dbCursor1 = courseCollection.find(searchQuery2);
//
//                    while (dbCursor1.hasNext()) {
//
//                        DBObject dbObject1 = dbCursor1.next();
//                        String course_name = (String) dbObject1.get("course_name");
//                        String course_instructor = (String) dbObject1.get("course_instructor");
//                        Integer aggregate = (Integer) dbObject1.get("aggregate");
//                        Integer count = (Integer) dbObject1.get("count");
//                        String temp = String.valueOf(aggregate);
//                        float newaggregate = Float.parseFloat(temp);
//                        float average = (newaggregate/count);
//                        String sent = String.valueOf(average);
//
//
//
//
//                        tempAcc = course_id + ";" + course_name + ";" + course_instructor + ";" + sent;
//                        System.out.println("THE VALUE OF TEMPACC IS: " + tempAcc);
//                    }
//                    //tempAcc = tempAcc + ";";
//                    if (!(tempAcc.equals("")))
//                        courseInfo.add(tempAcc);
//
//                }
                return tempAcc;


            } catch (Exception e) {
                e.printStackTrace();
                return "NULLA";
            }
        }
//        protected String doInBackground(String... arg0) {
//            try {
//                String tempAcc = "";
//                //String email = arg0[0];
//                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
//                MongoClient client = new MongoClient(uri);
//                DB db = client.getDB(uri.getDatabase());
//                DBCollection newcollection = db.getCollection("course_collection");
//                BasicDBObject searchQuery = new BasicDBObject();
//                DBCursor dbCursor = newcollection.find(searchQuery);
//                while (dbCursor.hasNext()) {
//                    DBObject dbObject = dbCursor.next();
//                    String course_id = (String) dbObject.get("course_id");
//                    System.out.println("COURSE ID: " + course_id);
//                    String course_name = (String) dbObject.get("course_name");
//                    String course_instructor = (String) dbObject.get("course_instructor");
//                    courseIdList.add(course_id);
//                    courseNameList.add(course_name);
//                    courseInstructorList.add(course_instructor);
//                    float aggregate = Float.parseFloat(String.valueOf((Integer) dbObject.get("aggregate")));
//                    float count = Float.parseFloat(String.valueOf((Integer) dbObject.get("count")));
//                    float rating = aggregate / count;
//                    ratingList.add(String.valueOf(rating));
////                    DBCollection courseCollection = db.getCollection("course_collection");
//
////                    BasicDBObject searchQuery2 = new BasicDBObject();
////                    searchQuery2.put("course_id", course_id);
////                    DBCursor dbCursor1 = courseCollection.find(searchQuery2);
////
////                    while (dbCursor1.hasNext()) {
////
////                        DBObject dbObject1 = dbCursor1.next();
////                        String course_name = (String) dbObject1.get("course_name");
////                        String course_instructor = (String) dbObject1.get("course_instructor");
////                        tempAcc = course_id + ";" + course_name + ";" + course_instructor;
////                        System.out.println("THE VALUE OF TEMPACC IS: " + tempAcc);
////                    }
////                    //tempAcc = tempAcc + ";";
////                    if (!(tempAcc.equals("")))
////                        courseInfo.add(tempAcc);
//
//                }
//                return tempAcc;
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                return "NULLA";
//            }
//        }
    }

}
