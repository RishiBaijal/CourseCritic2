package in.iiitd.pcsma.coursecritic;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/*public class DiscussionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
    }


}*/

public class DiscussionActivity extends Activity {

    Context context = this;
    String messagehistory = "";
    String username = "";
    String email = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        setupConnectionFactory();
        publishToAMQP();
        setupPubButton();

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");
        email = bundle.getString("email");
        TextView lel = (TextView) findViewById(R.id.textView);

        lel.setText(readFromFile());
        /*EnterUsernameGroup al = new EnterUsernameGroup();*/

        System.out.println("Till Here");

        /*TextView alpha = (TextView) findViewById(R.id.editTextduos);
        final String user = alpha.getText().toString();*/


        final Handler incomingMessageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = msg.getData().getString("msg");
                TextView tv = (TextView) findViewById(R.id.textView);
                Date now = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
                tv.append(ft.format(now)  + ":" + ' ' + message + '\n');
                String temp = ft.format(now) + ":" + " " + message + '\n';

                writeToFile(temp);
            }
        };
        subscribe(incomingMessageHandler);
    }

    void setupPubButton() {
        Button button = (Button) findViewById(R.id.publish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                EditText et = (EditText) findViewById(R.id.text);
                publishMessage(et.getText().toString());
                et.setText("");
            }
        });
    }

    Thread subscribeThread;
    Thread publishThread;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        publishThread.interrupt();
        subscribeThread.interrupt();
    }

    private BlockingDeque<String> queue = new LinkedBlockingDeque<String>();

    void publishMessage(String message) {
        //Adds a message to internal blocking queue
        try {
            Log.d("", "[q] " + message);
            queue.putLast(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*void publishMessage2 (String Message) {
        *//*String message = msg.getData().getString("msg");*//*
        TextView tv = (TextView) findViewById(R.id.textView);
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss");
        tv.append(ft.format(now)  + ":" + ' ' + Message + '\n');

    }*/

    ConnectionFactory factory = new ConnectionFactory();

    private void setupConnectionFactory() {
        String uri = "amqp://kcmhmxfn:gepGpoFPhwoZCr1rHaD93s2RmnOzPdfg@fox.rmq.cloudamqp.com/kcmhmxfn";
        try {
            factory.setAutomaticRecoveryEnabled(false);
            factory.setUri(uri);
        } catch (KeyManagementException | NoSuchAlgorithmException | URISyntaxException e1) {
            e1.printStackTrace();
        }
    }

    void subscribe(final Handler handler) {
        subscribeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel channel = connection.createChannel();
                        channel.basicQos(1);
                        AMQP.Queue.DeclareOk q = channel.queueDeclare();
                        channel.queueBind(q.getQueue(), "amq.direct", "discussion");
                        QueueingConsumer consumer = new QueueingConsumer(channel);
                        channel.basicConsume(q.getQueue(), true, consumer);
                        /*Map<String, Object> args = new HashMap<String, Object>();
                        args.put("x-recent-history-length", 60);
                        channel.exchangeDeclare("chat", "x-recent-history", false, false, args);*/

                        // Process deliveries
                        while (true) {
                            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                            String message = new String(delivery.getBody());
                            Log.d("", "[r] " + message);

                            Message msg = handler.obtainMessage();
                            Bundle bundle = new Bundle();

                            bundle.putString("msg", message);
                            /*bundle.putString("msg", messageequis);
                            System.out.println("LOOOOOOL :" + messageequis);*/
                            msg.setData(bundle);
                            handler.sendMessage(msg);

                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e1) {
                        Log.d("", "Connection broken: " + e1.getClass().getName());
                        try {
                            Thread.sleep(4000); //sleep and then try again
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            }
        });
        subscribeThread.start();
    }

    public void publishToAMQP() {
        publishThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Connection connection = factory.newConnection();
                        Channel ch = connection.createChannel();
                        ch.confirmSelect();

                        while (true) {
                            String message = queue.takeFirst();
                            message = username + " :" + message;
                            /*messageequis = message;*/
                            try {
                                ch.basicPublish("amq.direct", "discussion", null, message.getBytes());
                                Log.d("", "[s] " + message);
                                ch.waitForConfirmsOrDie();
                            } catch (Exception e) {
                                Log.d("", "[f] " + message);
                                queue.putFirst(message);
                                /*writeToFile(message);*/
                                throw e;
                            }
                        }
                    } catch (InterruptedException e) {
                        break;
                    } catch (Exception e) {
                        Log.d("", "Connection broken: " + e.getClass().getName());
                        try {
                            Thread.sleep(5000); //sleep and then try again
                        } catch (InterruptedException e1) {
                            break;
                        }
                    }
                }
            }
        });
        publishThread.start();
    }

    private void writeToFile(String data) {
        try {
            File path = context.getFilesDir();
            File file = new File(path, "discussion");
            /*OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(groupname, Context.MODE_PRIVATE));*/
            if(!file.exists()) {
                file.createNewFile();
                /*outputStreamWriter.write(data);
                outputStreamWriter.write(System.lineSeparator());*/ }

            FileWriter fileWriter = new FileWriter(file, true);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();




            /*outputStreamWriter.append(data);
            outputStreamWriter.append(System.lineSeparator());
            outputStreamWriter.close();*/
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("discussion");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString + System.lineSeparator());
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

}