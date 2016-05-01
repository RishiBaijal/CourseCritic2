package in.iiitd.pcsma.coursecritic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailedRecoViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_reco_view);

        TextView recoView = (TextView) findViewById(R.id.recoView);
        TextView timeStampView = (TextView) findViewById(R.id.timeStampView);

        Bundle bundle = getIntent().getExtras();
        String reco = bundle.getString("reco");
        String timeStamp = bundle.getString("timeStamp");

        recoView.setText(reco);
        timeStampView.setText(timeStamp);
    }
}
