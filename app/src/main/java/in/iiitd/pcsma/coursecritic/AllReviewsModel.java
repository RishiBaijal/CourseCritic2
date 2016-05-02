package in.iiitd.pcsma.coursecritic;

/**
 * Created by Apple on 30/04/16.
 */
public class AllReviewsModel {
    String email, question, answer;
    int id_;

    public AllReviewsModel(String email, String question, String answer) {
        this.email = email;
        this.question = question;
        this.answer = answer;
    }

    public String getEmailId() {
        return email;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}