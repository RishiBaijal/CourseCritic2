package in.iiitd.pcsma.coursecritic;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context = this;


    public String username1 = "";
    public String email1 = "";

    Thread subscribeThread;
    Thread publishThread;

    private ProgressDialog mProgressDialog;

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<RecommendationsModel> data;
    static View.OnClickListener myOnClickListener;

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

//    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();



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
        setupConnectionFactory();


        myOnClickListener = new MyOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.newsFeed_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<RecommendationsModel>();

        String fileData = readFromFile();
        StringTokenizer st = new StringTokenizer(fileData, System.lineSeparator());
        int len = st.countTokens();
        String recosArray[] = new String[len];
        String timeStampArray[] = new String[len];
        int i;
        for (i = 0; i< len; i++)
        {
            String w = st.nextToken();
            //StringTokenizer st1 = new StringTokenizer(w, ":");
            int firstOccOfSpace = w.indexOf(" ");
            recosArray[i] = w.substring(firstOccOfSpace);
            timeStampArray[i] = w.substring(0, firstOccOfSpace);
        }
        System.out.println("RECOSARRAY: " + recosArray);
        System.out.println("TIMESTAMPARRAY: " + timeStampArray);
        for (i = 0; i < recosArray.length; i++) {
            data.add(new RecommendationsModel(recosArray[i], timeStampArray[i]));
        }

        adapter = new RecommendationsAdapter(data);
        recyclerView.setAdapter(adapter);

/*TODO: GET this shit to work
        TextView kek = (TextView)findViewById(R.id.newsFeed);
        kek.setText(readFromFile());*/


//        LinearLayout layout = (LinearLayout) findViewById(R.id.nav_header_navigation);
//        TextView textView1 = (TextView) findViewById(R.id.textView1);
//        textView1.setText(username);
        Bundle bundle = getIntent().getExtras();
        final String username = bundle.getString("displayName");
        final String email = bundle.getString("email");
        email1 = email;
        username1 = username;


        final Handler incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                //TextView tv = (TextView) findViewById(R.id.newsFeed);

                Date now = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                String formatted = ft.format(now)  + ":" + ' ' + message + '\n';
                //StringTokenizer st2 = new StringTokenizer(formatted, ":");
                data.add(new RecommendationsModel(message, ft.format(now) + ""));
                //tv.append();
                String temp = ft.format(now) + ":" + " " + message + '\n';

                writeToFile(temp);
                adapter = new RecommendationsAdapter(data);
                recyclerView.setAdapter(adapter);
            }
        };

        subscribe(incomingMessageHandler);


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
            //showProgressDialog();
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

    ConnectionFactory factory = new ConnectionFactory();

    private void setupConnectionFactory() {
        String uri = "amqp://kcmhmxfn:gepGpoFPhwoZCr1rHaD93s2RmnOzPdfg@fox.rmq.cloudamqp.com/kcmhmxfn";
        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUri(uri);
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        publishThread.interrupt();
        subscribeThread.interrupt();
    }

    void subscribe(final Handler handler) {
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.basicQos(1);
                        AMQP.Queue.DeclareOk q = channel.queueDeclare();
                        channel.queueBind(q.getQueue(), "amq.direct", "chat");
                        QueueingConsumer consumer = new QueueingConsumer(channel);
                        channel.basicConsume(q.getQueue(), true, consumer);

                        // Process deliveries
                        while (true) {
                            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                            String message = new String(delivery.getBody());
                            Log.d("", "[r] " + message);

                            Message msg = handler.obtainMessage();
                            Bundle bundle = new Bundle();

                            bundle.putString("msg", message);
                            /*bundle.putString("msg", messageequis);
                            System.out.println("LOOOOOOL :" + messageequis);*/
                            msg.setData(bundle);
                            handler.sendMessage(msg);

                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e1) {
                        Log.d("", "Connection broken: " + e1.getClass().getName());
                        try {
                            Thread.sleep(4000); //sleep and then try again
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            }
        });
        subscribeThread.start();
    }
    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("Reccos");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString + System.lineSeparator());
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private void writeToFile(String data) {
        try {
            File path = context.getFilesDir();
            File file = new File(path, "Reccos");
            /*OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(groupname, Context.MODE_PRIVATE));*/
            if(!file.exists()) {
                file.createNewFile();
                /*outputStreamWriter.write(data);
                outputStreamWriter.write(System.lineSeparator());*/ }

            FileWriter fileWriter = new FileWriter(file, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();




            /*outputStreamWriter.append(data);
            outputStreamWriter.append(System.lineSeparator());
            outputStreamWriter.close();*/
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            System.out.println("The slut's cardview is clicked (from NavigationActivity)");
            TextView recoView = (TextView) v.findViewById(R.id.recoView);
            TextView timeStampView = (TextView) v.findViewById(R.id.timeStampView);
            String reco = recoView.getText().toString();
            String timeStamp = timeStampView.getText().toString();
            Intent intent = new Intent(v.getContext(), DetailedRecoViewActivity.class);
            intent.putExtra("reco", reco);
            intent.putExtra("timeStamp", timeStamp);
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
}
