package in.iiitd.pcsma.coursecritic;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class YourCoursesActivity extends AppCompatActivity {

    String tempAcc = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.courseView);

        Bundle bundle = getIntent().getExtras();//String username = bundle.getString("username");
        String email = bundle.getString("email");
       // System.out.println("USERNAME:"  + username);
        System.out.println("EMAIL: " +email);

        GetCourseFromDB getCourseFromDB = new GetCourseFromDB();

        try {
            tempAcc = getCourseFromDB.execute(email).get();
            System.out.println("Idhar aaya maa ka louda." + tempAcc);
            textView.setText(tempAcc + "boobs");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
