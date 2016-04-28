package in.iiitd.pcsma.coursecritic;

/**
 * Created by Apple on 28/04/16.
 */
public class DataModel {
    String courseName, courseCode, instructorName;
    int id_;

    public DataModel(String courseName, String courseCode, String instructorName) /*, int id_)*/ {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.instructorName = instructorName;
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
}
