package in.iiitd.pcsma.coursecritic;

/**
 * Created by Apple on 30/04/16.
 */
public class ContactModel {
    //String courseName, courseCode, instructorName;
    String contactName, contactEmail;
    int id_;

    public ContactModel(String contactName, String contactEmail) {
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

}
