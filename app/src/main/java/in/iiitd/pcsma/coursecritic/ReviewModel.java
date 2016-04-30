package in.iiitd.pcsma.coursecritic;

/**
 * Created by Apple on 01/05/16.
 */
public class ReviewModel {
    //String courseName, courseCode, instructorName;
    String question, answer;
    int id_;

    public ReviewModel(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

}