package in.iiitd.pcsma.coursecritic;

import android.os.AsyncTask;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.StringTokenizer;

/**
 * Created by Apple on 21/04/16.
 */
public class GetCourseFromDB extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... arg0) {
        try
        {
            System.out.println("CUMS HERE. ");
            String tempAcc = "";
            String email = arg0[0];
         //   StringTokenizer st = new StringTokenizer(userInfo, ":");
            //String email = st.nextToken();
            MongoClientURI uri = new MongoClientURI("mongodb://rishi:ThunderAndSparks8@ds013881.mlab.com:13881/course_critic");
            MongoClient client = new MongoClient(uri);
            DB db = client.getDB(uri.getDatabase());
            DBCollection newcollection = db.getCollection("student_collection");
            BasicDBObject searchQuery = new BasicDBObject();
            searchQuery.put("email", email);
            DBCursor dbCursor = newcollection.find(searchQuery);
            System.out.println("CUMS HERE TOO. ");
            while (dbCursor.hasNext())
            {
                DBObject dbObject = dbCursor.next();
                String course_id = (String) dbObject.get("course_id");
                DBCollection courseCollection = db.getCollection("course_collection");
//                tempAcc = course_id;
                System.out.println("COURSE_ID: " + course_id);
                System.out.println("TEMPACC: " + tempAcc);

                BasicDBObject searchQuery2 = new BasicDBObject();
                searchQuery2.put("course_id", course_id);
                DBCursor dbCursor1 = courseCollection.find(searchQuery2);

                while (dbCursor1.hasNext()) {

                    DBObject dbObject1 = dbCursor1.next();
                    String course_name = (String) dbObject1.get("course_name");
                    String course_instructor = (String) dbObject1.get("course_instructor");
                    tempAcc = course_id + " " + course_name + " " + course_instructor;
                    System.out.println("THE VALUE OF TEMPACC IS: " + tempAcc);
                    tempAcc = tempAcc + "\n";
                }
            }
//            BasicDBObject alphaDoc = new BasicDBObject();
//            alphaDoc.put("course_id", "");
//            alphaDoc.put("coursename", "");
//            alphaDoc.put("course_instructor", "");
//            newcollection.insert(alphaDoc);
//            DBCursor dbCursor = newcollection.find();
//            while (dbCursor.hasNext())
//            {
//                DBObject dbObject = dbCursor.next();
//                String course_id = (String) dbObject.get("course_id");
//                String course_name = (String) dbObject.get("course_name");
//                String course_instructor = (String) dbObject.get("course_instructor");
//            }
            return tempAcc;


        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "NULLA";
        }
    }
}