package in.iiitd.pcsma.coursecritic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public String username1 = "";
    public String email1 = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();




//        LinearLayout layout = (LinearLayout) findViewById(R.id.nav_header_navigation);
//        TextView textView1 = (TextView) findViewById(R.id.textView1);
//        textView1.setText(username);
        Bundle bundle = getIntent().getExtras();
        final String username = bundle.getString("displayName");
        final String email = bundle.getString("email");
        email1 = email;
        username1 = username;


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView textView1 = (TextView) header.findViewById(R.id.textView1);
        textView1.setText(username);
        TextView textView2 = (TextView) header.findViewById(R.id.textView2);
        textView2.setText(email);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Course");
        builder.setMessage("Enter the course Id");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        final EditText input = new EditText(this);
        input.setHint("Course ID");
        builder.setView(input);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String course_id = input.getText().toString();
                String colonSeparatedParam = username + ":" + email + ":" + course_id;

                SaveCourseToDB saveCourseToDB = new SaveCourseToDB();
                saveCourseToDB.execute(colonSeparatedParam);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
//        if (id == R.id.main_menu)
//        {
//            //SignInActivity.receivedExternalIntent = true;
//            Intent intent = new Intent(this, SignInActivity.class);
//            intent.putExtra("signOut", "pleaseSignOut");
//            startActivity(intent);
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_friends) {
            Intent intent = new Intent(this, YourFriendsActivity.class);
            intent.putExtra("username", username1);
            intent.putExtra("email", email1);
            startActivity(intent);
        } else if (id == R.id.nav_your_courses) {
            Intent intent = new Intent(this, YourCoursesActivity.class);
            intent.putExtra("username", username1);
            intent.putExtra("email", email1);
            startActivity(intent);

        }
//        else if (id == R.id.nav_past_course) {
//            Intent intent = new Intent(this, PastCoursesActivity.class);
//            intent.putExtra("username", username1);
//            intent.putExtra("email", email1);
//            startActivity(intent);
//
//        }
        else if (id == R.id.nav_top_50) {
            Intent intent = new Intent(this, Top50Activity.class);
            intent.putExtra("username", username1);
            intent.putExtra("email", email1);
            startActivity(intent);

        } else if (id == R.id.nav_recommendations) {
            Intent intent = new Intent(this, RecommendationsActivity.class);
            intent.putExtra("username", username1);
            intent.putExtra("email", email1);
            startActivity(intent);

        } else if (id == R.id.nav_discussions) {
            Intent intent = new Intent(this, DiscussionActivity.class);
            intent.putExtra("username", username1);
            intent.putExtra("email", email1);
            startActivity(intent);

        }
        else if (id == R.id.nav_feedback)
        {
            Intent intent = new Intent(this, FeedBackActivity.class);
            intent.putExtra("email", email1);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void addCourse(View view) {
    }
}
