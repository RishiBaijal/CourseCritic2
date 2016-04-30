//<<<<<<< HEAD
package in.iiitd.pcsma.coursecritic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Rating;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.w3c.dom.Text;

import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class YourCoursesActivity extends AppCompatActivity {


    Integer bleh;

    String tempAcc = "", courseCodeGlobal = "";
    private ProgressDialog progressDialog;
    HashSet<String> courseInfo = new HashSet<String>();
    static String courseNames[], courseCodes[], instructorNames[];


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    static View.OnClickListener myOnClickListener;

    static String email = "";


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
            //Comment out c here
            //c = 0;
            int i;
            //Make 3 -> c here
            for (i = 0; i < c; i++) {
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
        for (int i = 0; i < courseInfo.size(); i++) {
            data.add(new DataModel(courseCodes[i], courseNames[i], instructorNames[i]));
        }
        adapter = new CustomAdapter(data);
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

    public void showRating(View view) {


        View v = (View) view.getParent();
        TextView textView = (TextView) v.findViewById(R.id.courseCode);
        if (textView == null) {
            System.out.println("Fuck this, I am failing");
        } else {
            final String courseCode = textView.getText().toString();
            System.out.println(courseCode);
            String[] arraynew = courseCode.split(":");
            courseCodeGlobal = arraynew[1].substring(1);
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

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public void addCourseReview(View view) {
        View v = (View) view.getParent();

        TextView textView = (TextView) v.findViewById(R.id.courseCode);
        String courseCodeLocal = textView.getText().toString();
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("currentCourseCode", courseCodeLocal);
        startActivity(intent);
    }

    public class SaveRatingtoDB extends AsyncTask<String, Void, Boolean> {


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

    }


    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v.findViewById(R.id.courseCode);
            String courseCodeLocal = textView.getText().toString();
            Intent intent = new Intent(v.getContext(), ReviewActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("currentCourseCode", courseCodeLocal);
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
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(YourCoursesActivity.this, "Fetching your courses", "Contacting the server. Please wait...", true, false);

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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
        }
    }

}
//=======
//package in.iiitd.pcsma.coursecritic;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.media.Rating;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.RatingBar;
//import android.widget.TextView;
//
//import java.util.*;
//
//import com.mongodb.BasicDBObject;
//import com.mongodb.DB;
//import com.mongodb.DBCollection;
//import com.mongodb.DBCursor;
//import com.mongodb.DBObject;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//
//import org.w3c.dom.Text;
//
//import java.util.StringTokenizer;
//import java.util.concurrent.ExecutionException;
//
//public class YourCoursesActivity extends AppCompatActivity {
//
//
//    Integer bleh;
//
//    String tempAcc = "";
//    private ProgressDialog progressDialog;
//    HashSet<String> courseInfo = new HashSet<String>();
//    static String courseNames[], courseCodes[], instructorNames[];
//
//
//    private static RecyclerView.Adapter adapter;
//    private RecyclerView.LayoutManager layoutManager;
//    private static RecyclerView recyclerView;
//    private static ArrayList<DataModel> data;
//    static View.OnClickListener myOnClickListener;
//    private static ArrayList<Integer> removedItems;
//
//    static String email = "", currentCourseCode = "";
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_your_courses);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        myOnClickListener = new MyOnClickListener(this);
//
//        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
//        recyclerView.setHasFixedSize(true);
//
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        data = new ArrayList<DataModel>();
//
////        TextView textView = (TextView) findViewById(R.id.courseView);
////        TextView textView1 = (TextView) findViewById(R.id.courseName);
//
//        Bundle bundle = getIntent().getExtras();//String username = bundle.getString("username");
//        email = bundle.getString("email");
//        // System.out.println("USERNAME:"  + username);
//        System.out.println("EMAIL: " + email);
//
//
//        GetCourseFromDB getCourseFromDB = new GetCourseFromDB();
//
//        try {
//            tempAcc = getCourseFromDB.execute(email).get();
//            int length = courseInfo.size();
//            courseNames = new String[length];
//            courseCodes = new String[length];
//            instructorNames = new String[length];
//
//            Iterator iterator = courseInfo.iterator();
//            int c = 0;
//            while (iterator.hasNext()) {
//                String nextElement = (String) iterator.next();
//                StringTokenizer st = new StringTokenizer(nextElement, ";");
//                System.out.println("Next element: " + nextElement);
//                if (st.countTokens() == 3) {
//                    String courseName = st.nextToken();
//                    String courseCode = st.nextToken();
//                    String instructor = st.nextToken();
//                    courseCodes[c] = courseCode;
//                    courseNames[c] = courseName;
//                    instructorNames[c] = instructor;
//                    c++;
//                }
//            }
//          //  c = 0;
//            int i;
//            for (i = 0; i < c; i++) {
//                System.out.println("Course code array element #: " + i + " " + courseCodes[i]);
//                System.out.println("Course Names array element #: " + i + " " + courseNames[i]);
//                System.out.println("Instructor names array element #: " + i + " " + instructorNames[i]);
//            }
//
//
//            System.out.println("TEMPACC KI VALUE HAI: " + tempAcc);
//            //StringTokenizer st = new StringTokenizer(tempAcc);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        System.out.println(courseInfo);
//        for (int i = 0; i < courseInfo.size(); i++) {
//            data.add(new DataModel(courseCodes[i], courseNames[i], instructorNames[i]));
//        }
//        adapter = new CustomAdapter(data);
//        recyclerView.setAdapter(adapter);
//
///*
//        TextView textView2 = (TextView) recyclerView.findViewById(R.id.courseCode);
//        if (textView2 == null) {
//            System.out.println("Text view is still null, and you're fucked.");
//        }
//        final String getCourseCode = textView2.getText().toString();
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Rate the Course");
//        builder.setMessage("Enter the Rating");
//        builder.setIcon(android.R.drawable.ic_dialog_alert);
//        final RatingBar input = new RatingBar(this);
//        input.setMax(5);
//        input.setNumStars(5);
//        input.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        builder.setView(input);
//        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                bleh = input.getProgress();
//                SaveRatingtoDB saveRatingtoDB = new SaveRatingtoDB();
//                saveRatingtoDB.execute(getCourseCode);
//                dialog.dismiss();
//
//            }
//        });
//        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//                // User cancelled the dialog
//                dialog.cancel();
//            }
//        });
//        final AlertDialog dialog = builder.create();
//*/
//
//    }
//
//    public void showRating(View view) {
//
//
//        View v = (View) view.getParent();
//        TextView textView = (TextView) v.findViewById(R.id.courseCode);
//        currentCourseCode = textView.getText().toString();
//        if (textView == null) {
//            System.out.println("Fuck this, I am failing");
//        } else {
//            final String courseCode = textView.getText().toString();
//
//            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle("Rate the Course");
//            builder.setMessage("Enter the Rating");
//            builder.setIcon(android.R.drawable.ic_dialog_alert);
//
////            final RatingBar input = new RatingBar(this);
////            input.setStepSize(1f);
////            input.setNumStars(10);
////            input.setMax(10);
////            input.setFocusable(false);
////            input.setIsIndicator(false);
//
//
////            input.setMax(5);
////            input.setNumStars(5);
////            input.setStepSize(1);
//            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//
//            View layout = inflater.inflate(R.layout.rating_bar_view, null, false);
//            //final RatingBar input = (RatingBar) layout.findViewById(R.id.rating);
////            inflater.infla
//            //            editor.setView(layout);
////            editor.show();
//
//
//            //input.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            builder.setView(layout);
//            final RatingBar ratingBar = (RatingBar) layout.findViewById(R.id.rating);
//
//            //builder.setView(input);
//            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    System.out.println("Positive Button clicked SUCKERRRR");
////                    bleh = input.getProgress();
//                    bleh = ratingBar.getProgress();
//                    SaveRatingtoDB saveRatingtoDB = new SaveRatingtoDB();
//                    saveRatingtoDB.execute(courseCode);
//                    dialog.dismiss();
//
//                }
//            });
//            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // User cancelled the dialog
//                    dialog.cancel();
//                }
//            });
//            final AlertDialog dialog = builder.create();
//            dialog.show();
//
//        }
//
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (progressDialog != null)
//            progressDialog.dismiss();
//    }
//
//    public void addCourseReview(View view) {
//        View v = (View) view.getParent();
//        TextView textView = (TextView) v.findViewById(R.id.courseCode);
//        currentCourseCode = textView.getText().toString();
//        Intent intent = new Intent(this, ReviewActivity.class);
//        intent.putExtra("email", email);
//        intent.putExtra("currentCourseCode", currentCourseCode);
//        startActivity(intent);
//    }
//
//    public class SaveRatingtoDB extends AsyncTask<String, Void, Boolean> {
//
//
//        @Override
//        protected Boolean doInBackground(String... arg0) {
//            try {
//                String courseCode = arg0[0];
//                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
//                MongoClient client = new MongoClient(uri);
//                DB db = client.getDB(uri.getDatabase());
//                DBCollection newcollection = db.getCollection("rating_collection");
//                StringTokenizer st = new StringTokenizer(courseCode, ":");
//                String nextToken1 = st.nextToken();
//                String nextToken2 = st.nextToken();
//                BasicDBObject alphaDoc = new BasicDBObject();
//
//                alphaDoc.put("courseCode", nextToken2.substring(1));
//                alphaDoc.put("Email", email);
//                alphaDoc.put("rating", bleh);
//
//                newcollection.insert(alphaDoc);
//                return true;
//
//
//            } catch (Exception e) {
//                return false;
//            }
//        }
////
////    protected void onProgressUpdate(Integer... progress) {
////        setProgressPercent(progress[0]);
////    }
//
//    }
//
//
//    private class MyOnClickListener implements View.OnClickListener {
//
//        private final Context context;
//
//        private MyOnClickListener(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        public void onClick(View v) {
//            View parentView = (View)v.getParent();
//            TextView textView = (TextView) v.findViewById(R.id.courseCode);
//            currentCourseCode = (String) textView.getText().toString();
//
//            Intent intent = new Intent(v.getContext(), ReviewActivity.class);
//            intent.putExtra("email", email);
//            intent.putExtra("currentCourseCode", currentCourseCode);
//            System.out.println("The value of courseCode cocksucka is (from YOURCOURSESACTIVITY): " + currentCourseCode);
//
//            startActivityForResult(intent, 0);
//
//        }
//
////        private void removeItem(View v) {
////            int selectedItemPosition = recyclerView.getChildPosition(v);
////            RecyclerView.ViewHolder viewHolder
////                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
////            TextView textViewName
////                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewName);
////            String selectedName = (String) textViewName.getText();
////            int selectedItemId = -1;
////            for (int i = 0; i < MyData.nameArray.length; i++) {
////                if (selectedName.equals(MyData.nameArray[i])) {
////                    selectedItemId = MyData.id_[i];
////                }
////            }
////            removedItems.add(selectedItemId);
////            data.remove(selectedItemPosition);
////            adapter.notifyItemRemoved(selectedItemPosition);
////        }
//    }
//
//
//    public class GetCourseFromDB extends AsyncTask<String, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = ProgressDialog.show(YourCoursesActivity.this, "Fetching your courses", "Contacting the server. Please wait...", true, false);
//
//        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            try {
//                String tempAcc = "";
//                String email = arg0[0];
//                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
//                MongoClient client = new MongoClient(uri);
//                DB db = client.getDB(uri.getDatabase());
//                DBCollection newcollection = db.getCollection("student_collection");
//                BasicDBObject searchQuery = new BasicDBObject();
//                searchQuery.put("email", email);
//                DBCursor dbCursor = newcollection.find(searchQuery);
//                while (dbCursor.hasNext()) {
//                    DBObject dbObject = dbCursor.next();
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
//                        tempAcc = course_id + ";" + course_name + ";" + course_instructor;
//                        System.out.println("THE VALUE OF TEMPACC IS: " + tempAcc);
//                    }
//                    //tempAcc = tempAcc + ";";
//                    if (!(tempAcc.equals("")))
//                        courseInfo.add(tempAcc);
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
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            progressDialog.dismiss();
//        }
//    }
//
//}
//>>>>>>> 62b3cf80c5cafac8109a3be8345d65164af4ba21
