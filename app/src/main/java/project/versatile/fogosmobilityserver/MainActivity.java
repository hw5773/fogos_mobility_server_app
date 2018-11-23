package project.versatile.fogosmobilityserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import project.versatile.flexid.FlexID;
import project.versatile.flexid.FlexIDFactory;

public class MainActivity extends AppCompatActivity {
    Server server;
    EditText editText;
    Button startBtn;
    TextView textView1;
    TextView textView2;
    FlexID myID;
    FlexIDFactory factory;
    static final int port = 3333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        factory = new FlexIDFactory();

        editText = (EditText) findViewById(R.id.editText);
        startBtn = (Button) findViewById(R.id.startBtn);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
    }

    public void onButtonClicked(View v) {
        int bytes = Integer.parseInt(editText.getText().toString());
        server = new Server(this, port);
        textView1.setText("Listening on " + server.getIpAddress() + ":" + server.getPort());
        textView2.setText("서버에서 클라이언트에게 " + bytes + " 바이트를 전송합니다.");
    }
}
