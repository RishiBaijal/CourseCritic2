//<<<<<<< HEAD
//package in.iiitd.pcsma.coursecritic;
//
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//
//public class YourFriendsActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_your_friends);
////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
////
////        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
////        fab.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
////            }
////        });
//    }
//
//}
//=======
package in.iiitd.pcsma.coursecritic;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.google.gdata.client.*;
import com.google.gdata.client.contacts.*;
import com.google.gdata.data.*;
import com.google.gdata.data.contacts.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class YourFriendsActivity extends AppCompatActivity {

    ArrayList<String> nameArray = new ArrayList<String>();
    ArrayList<String> emailArray = new ArrayList<String>();
    HashMap<String, String> nameEmailMapping = new HashMap<String, String>();


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<ContactModel> contacts;
    static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_friends);
        readContacts();
        System.out.println("NAME ARRAY: " + nameArray);
        System.out.println("EMAIL ARRAY: " + emailArray);

        myOnClickListener = new MyOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.contacts_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        contacts = new ArrayList<ContactModel>();
        int i;
        for (i = 0; i < nameArray.size(); i++)
        {
            contacts.add(new ContactModel(nameArray.get(i), emailArray.get(i)));
        }
        adapter = new ContactAdapter(contacts);
        recyclerView.setAdapter(adapter);

    }

    public void readContacts() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    System.out.println("name : " + name + ", ID : " + id);

                    // get email and type

                    Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCur.moveToNext()) {
                        String email = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        String emailType = emailCur.getString(
                                emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        System.out.println("Email " + email + " Email Type : " + emailType);
                        nameArray.add(name);
                        emailArray.add(email);
                    }
                    emailCur.close();
                }
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
            //System.out.println("The sluts cardview is clicked.");
            TextView  textView = (TextView) v.findViewById(R.id.contactEmailTextView);
            String email = textView.getText().toString();
            Intent intent = new Intent(v.getContext(), GetReviewInfoActivity.class);
            intent.putExtra("email", email);
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
//
//    public class GetCourseInfoFromDB extends AsyncTask<String, Void, String> {
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
//    }

//
//    public TextView outputText;
//    @Override
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        outputText = (TextView) findViewById(R.id.textView1);
//        fetchContacts();
//    }
//    public void fetchContacts() {
//        String phoneNumber = null;
//        String email = null;
//        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
//        String _ID = ContactsContract.Contacts._ID;
//        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
//        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
//        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
//        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
//        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
//
//        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
//
//        String DATA = ContactsContract.CommonDataKinds.Email.DATA;
//
//        StringBuffer output = new StringBuffer();
//        ContentResolver contentResolver = getContentResolver();
//        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
//        if (cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
//                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
//                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
//                if (hasPhoneNumber > 0) {
//                    output.append("\n First Name:" + name);
//                    // Query and loop for every phone number of the contact
//
//                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
//
//
//
//                    while (phoneCursor.moveToNext()) {
//
//                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
//                        output.append("\n Phone number:" + phoneNumber);
//
//                    }
//
//                    phoneCursor.close();
//                    // Query and loop for every email of the contact
//
//                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
//                    while (emailCursor.moveToNext()) {
//                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
//                        output.append("\nEmail:" + email);
//                    }
//
//                    emailCursor.close();
//
//                }
//                output.append("\n");
//
//            }
//
//            outputText.setText(output);
//
//        }
//    }
//
////    URL feedUrl;
////    String username = "", email = "";
////    ContactsService contactsService;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_your_friends);
////
////        Bundle bundle = getIntent().getExtras();
////        username = bundle.getString("username");
////        email = bundle.getString("email");
////
////        String url = "https://www.google.com/m8/feeds/contacts/" + username + "/full";
////        try {
////            this.feedUrl = new URL(url);
////        } catch (MalformedURLException e) {
////            e.printStackTrace();
////        }
////        new GetTask().execute();
////
////    }

}
//>>>>>>> 62b3cf80c5cafac8109a3be8345d65164af4ba21
