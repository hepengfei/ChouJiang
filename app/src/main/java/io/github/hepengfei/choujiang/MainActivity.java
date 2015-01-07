package io.github.hepengfei.choujiang;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private static final long DELAY_MILLIS = 30;

    private PersonManager personManager = PersonManager.getInstance();

    private Button button;
    private TextView chosenPersonView;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chosenPersonView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        verify = (Button) findViewById(R.id.verify);

        verify.setOnClickListener(verifyListener);
        verify.setEnabled(false);
        chosenPersonView.setText("");

        initForStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {

            if (message.what == 1) {
                recoverPersonIfNotVerify();
                String name = personManager.choosePerson();
                chosenPersonView.setText(name);

                if (! isStop()) {
                    handler.sendEmptyMessageDelayed(1, DELAY_MILLIS);
                }
            }
        }
    };

    private void recoverPersonIfNotVerify() {
        String personName = chosenPersonView.getText().toString();
        if (personName.isEmpty()) {
            return;
        }
        personManager.recoverPerson(personName);

    }

    private boolean isStop() {
        final String buttonText = button.getText().toString();
        return buttonText.equals(getResources().getString(R.string.button_start));
    }

    private View.OnClickListener startListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initForStop();

            handler.sendEmptyMessage(1);
        }
    };

    private View.OnClickListener stopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            initForStart();
        }
    };

    private View.OnClickListener verifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String personName = chosenPersonView.getText().toString();
            if (personName.isEmpty()) {
                return;
            }
            chosenPersonView.setText("");
            showMessage("恭喜 " + personName + " 中奖！");
        }
    };

    private void showMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void initForStart() {
        button.setText(R.string.button_start);
        button.setOnClickListener(startListener);

        verify.setEnabled(true);
    }

    private void initForStop() {
        button.setText(R.string.button_stop);
        button.setOnClickListener(stopListener);

        verify.setEnabled(false);
    }
}
