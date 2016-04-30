package in.iiitd.pcsma.coursecritic;

/**
 * Created by Udai on 4/30/2016.
 */
public class DataModel2 {
    String courseName, courseCode, instructorName,rating;
    int id_;

    public DataModel2(String courseName, String courseCode, String instructorName, String rating) /*, int id_)*/ {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.instructorName = instructorName;
        this.rating = rating;
//        this.id_ = id_;
    }
//    public int getId() {
//        return id_;
//    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getRating() { return rating; }
}