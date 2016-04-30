package in.iiitd.pcsma.coursecritic;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Interpolator;

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

public class Top50Activity extends AppCompatActivity {

    Integer bleh;

    String tempAcc = "", courseCodeGlobal = "";
    private ProgressDialog progressDialog;
    HashSet<String> courseInfo = new HashSet<String>();
    static String courseNames[], courseCodes[], instructorNames[], RatingArray[];


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel2> data;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;

    static String email = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top50);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*myOnClickListener = new MyOnClickListener(this);*/

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel2>();

        Bundle bundle = getIntent().getExtras();//String username = bundle.getString("username");
        email = bundle.getString("email");
        // System.out.println("USERNAME:"  + username);
        System.out.println("EMAIL: " + email);


        GetCourseFromDB getCourseFromDB = new GetCourseFromDB();

        try {
            tempAcc = getCourseFromDB.execute(email).get();
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
            //StringTokenizer st = new StringTokenizer(tempAcc);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(courseInfo);
        for (int i = 0; i < courseInfo.size(); i++) {
            data.add(new DataModel2(courseCodes[i], courseNames[i], instructorNames[i],RatingArray[i]));
        }
        adapter = new CustomAdapter2(data);
        recyclerView.setAdapter(adapter);
/*
        TextView textView2 = (TextView) recyclerView.findViewById(R.id.courseCode);
        if (textView2 == null) {
            System.out.println("Text view is still null, and you're fucked.");
        }
        final String getCourseCode = textView2.getText().toString();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate the Course");
        builder.setMessage("Enter the Rating");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        final RatingBar input = new RatingBar(this);
        input.setMax(5);
        input.setNumStars(5);
        input.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.setView(input);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                bleh = input.getProgress();
                SaveRatingtoDB saveRatingtoDB = new SaveRatingtoDB();
                saveRatingtoDB.execute(getCourseCode);
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
*/

    }

    /*public void showRating(View view) {


        View v = (View) view.getParent();
        TextView textView = (TextView) v.findViewById(R.id.courseCode);
        if (textView == null) {
            System.out.println("Fuck this, I am failing");
        } else {
            final String courseCode = textView.getText().toString();
            System.out.println(courseCode);
            String[] arraynew = courseCode.split(": ");
            courseCodeGlobal = arraynew[1];
            System.out.println("CourseCodeGlobal is" + courseCodeGlobal);



            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Rate the Course");
            builder.setMessage("Enter the Rating");
            builder.setIcon(android.R.drawable.ic_dialog_alert);

//            final RatingBar input = new RatingBar(this);
//            input.setStepSize(1f);
//            input.setNumStars(10);
//            input.setMax(10);
//            input.setFocusable(false);
//            input.setIsIndicator(false);


//            input.setMax(5);
//            input.setNumStars(5);
//            input.setStepSize(1);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.rating_bar_view, null, false);
            //final RatingBar input = (RatingBar) layout.findViewById(R.id.rating);
//            inflater.infla
            //            editor.setView(layout);
//            editor.show();


            //input.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            builder.setView(layout);
            final RatingBar ratingBar = (RatingBar) layout.findViewById(R.id.rating);

            //builder.setView(input);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    System.out.println("Positive Button clicked SUCKERRRR");
//                    bleh = input.getProgress();
                    bleh = ratingBar.getProgress();
                    SaveRatingtoDB saveRatingtoDB = new SaveRatingtoDB();
                    saveRatingtoDB.execute(courseCodeGlobal);
                    dialog.dismiss();

                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    dialog.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();

        }

    }*/


    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    /*public void addCourseReview(View view) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }*/

    /*public class SaveRatingtoDB extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... arg0) {
            try {
                String courseCode = arg0[0];
                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
                MongoClient client = new MongoClient(uri);
                DB db = client.getDB(uri.getDatabase());
                DBCollection newcollection = db.getCollection("rating_collection");
                DBCollection doscollection = db.getCollection("course_collection");
                BasicDBObject alphaDoc = new BasicDBObject();
                BasicDBObject query = new BasicDBObject();
                query.put("course_id", courseCode);

                BasicDBObject updated = new BasicDBObject();
                updated.put("$inc", new BasicDBObject("aggregate", bleh).append("count", 1));

                doscollection.update(query, updated);



                alphaDoc.put("courseCode", courseCode);
                alphaDoc.put("rating", bleh);

                newcollection.insert(alphaDoc);
                return true;


            } catch (Exception e) {
                return false;
            }
        }
//
//    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
//    }

    }*/


    /*private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ReviewActivity.class);
            intent.putExtra("email", email);
            startActivityForResult(intent, 0);

        }*/

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
   // }


    public class GetCourseFromDB extends AsyncTask<String, Void, String> {
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
                        Integer aggregate = (Integer) dbObject1.get("aggregate");
                        Integer count = (Integer) dbObject1.get("count");
                        String temp = String.valueOf(aggregate);
                        float newaggregate = Float.parseFloat(temp);
                        float average = (newaggregate/count);
                        String sent = String.valueOf(average);




                        tempAcc = course_id + ";" + course_name + ";" + course_instructor + ";" + sent;
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
        }
    }





        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
}



