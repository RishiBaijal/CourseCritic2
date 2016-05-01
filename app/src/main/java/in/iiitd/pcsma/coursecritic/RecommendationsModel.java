package in.iiitd.pcsma.coursecritic;

/**
 * Created by Apple on 02/05/16.
 */
public class RecommendationsModel {
    String recoText, timestamp;
    int id_;

    public RecommendationsModel(String recoText, String timestamp) {
        this.recoText = recoText;
        this.timestamp = timestamp;
    }

    public String getRecoText() {
        return recoText;
    }

    public String getTimestamp() {
        return timestamp;
    }

}