package in.iiitd.pcsma.coursecritic;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ReviewActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {


    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnSubmit;

    //String answerArray[] = new String[8];
    String answer = "", question = "";
    static String email = "", courseCode = "";
    static String other = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("email");
        courseCode = bundle.getString("currentCourseCode");
        System.out.println("The value of email is: " + email);
        System.out.println("The value of courseCode cocksucka is (from ReviewActivity): " + courseCode);

        addListenerOnRatingBar();
        addListenerOnButton();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> questionList = new ArrayList<String>();
        questionList.add("What is your attendance?");
        questionList.add("What is the amount of time per week that you put in?");
        questionList.add("What is your opinion of the instructor's ability to convey the key concepts? (Please be civil)");
        questionList.add("What is your opinion of the TA's ability to solve your doubts? (Please be civil)");
        questionList.add("How much did the course assignments help you in learning the course material?");
        questionList.add("How much did the course project help you in learning the course material?");
        questionList.add("Other");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, questionList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);





    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                String courseRating = String.valueOf(rating);
                System.out.println(";"+courseRating+";");
                if (courseRating.equals("1.0")) {
                    txtRatingValue.setText("God-awful.");
                }
                else if (courseRating.equals("2.0"))
                {
                    txtRatingValue.setText("Painfully average");
                }
                else if (courseRating.equals("3.0"))
                {
                    txtRatingValue.setText("Satisfactory");
                }
                else if (courseRating.equals("4.0"))
                {
                    txtRatingValue.setText("Will happily do it again.");
                }
                else
                {
                    txtRatingValue.setText("Awesometacular");
                }

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText visibleEditText = (EditText) findViewById(R.id.visibleEditText);
                String answer = (String) visibleEditText.getText().toString();
                if (question.equalsIgnoreCase("Other"))
                {
                    EditText hiddenEditText = (EditText) findViewById(R.id.hiddenEditText);
                    question = (String) hiddenEditText.getText().toString();
                }
                SaveAnswerToDB obj = new SaveAnswerToDB();
                obj.execute(question + ";" + answer);
            }

        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("onItemSelected gets executed. ");
        String item = parent.getItemAtPosition(position).toString();
        question = item;
        Toast.makeText(parent.getContext(), "Selected: " + position + " " + item, Toast.LENGTH_LONG).show();
        EditText visibleEditText = (EditText) findViewById(R.id.visibleEditText);
        EditText hiddenEditText = (EditText) findViewById(R.id.hiddenEditText);
        hiddenEditText.setVisibility(View.GONE);
        if (item.equalsIgnoreCase("Other"))
        {
            hiddenEditText.setVisibility(View.VISIBLE);
            other = (String) visibleEditText.getText().toString();
            System.out.println("The value of other is : " + other);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class SaveAnswerToDB extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... arg0) {
            try
            {
                String questionAns = arg0[0];
                MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
                MongoClient client = new MongoClient(uri);

                DB db = client.getDB(uri.getDatabase());
                DBCollection newcollection = db.getCollection("review_collection");
                System.out.println("STUDENT INFO: " + questionAns);

                StringTokenizer st = new StringTokenizer(questionAns, ";");
                String question = st.nextToken();
                String answer = st.nextToken();
                System.out.println("QUESTION" + question);
                System.out.println("Answer cocksucka:"  +answer);
                System.out.println("CourseCode cocksucka" + courseCode);
                StringTokenizer st1 = new StringTokenizer(courseCode, ":");
                String token1 = st1.nextToken();
                String token2 = st1.nextToken();
                if (token2.charAt(0) == ' ')
                    token2 = token2.substring(1);
                System.out.println("TOKEN 2: " + token2);
                BasicDBObject alphaDoc = new BasicDBObject();
                alphaDoc.put("Email", email);
                System.out.println("EMAIL:"+email);
                alphaDoc.put("courseCode", token2);
                alphaDoc.put("question", question);
                alphaDoc.put("answer", answer);
                newcollection.insert(alphaDoc);
                return true;


            }
            catch (Exception e)
            {
                return false;
            }
        }
//        @Override
//        protected void onPostExecute()
//        {
//            Toast.makeText(ReviewActivity.this, "Written successfully to database.", Toast.LENGTH_SHORT).show();
//
//        }
//
//    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
//    }

    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_review);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//    }

}
