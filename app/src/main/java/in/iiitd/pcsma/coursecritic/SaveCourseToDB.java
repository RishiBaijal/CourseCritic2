package in.iiitd.pcsma.coursecritic;

import android.os.AsyncTask;
import java.util.*;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

/**
 * Created by Apple on 21/04/16.
 */
public class SaveCourseToDB extends AsyncTask<String, Void, Boolean> {

    @Override
    protected Boolean doInBackground(String... arg0) {
        try
        {
            String studentInfo = arg0[0];
            System.out.println("STUDENT INFO: " + studentInfo);
            MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
            MongoClient client = new MongoClient(uri);
            DB db = client.getDB(uri.getDatabase());
            DBCollection newcollection = db.getCollection("student_collection");
            StringTokenizer st = new StringTokenizer(studentInfo, ":");
            String username = st.nextToken();
            String email = st.nextToken();
            String course_id = st.nextToken();
            BasicDBObject alphaDoc = new BasicDBObject();
            alphaDoc.put("username", username);
            alphaDoc.put("email", email);
            alphaDoc.put("course_id", course_id);
//            alphaDoc.put("course_id", "");
//            alphaDoc.put("coursename", "");
//            alphaDoc.put("course_instructor", "");

            newcollection.insert(alphaDoc);
            return true;


        }
        catch (Exception e)
        {
            return false;
        }
    }
//
//    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
//    }

}