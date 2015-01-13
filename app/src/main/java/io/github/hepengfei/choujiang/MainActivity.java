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

    private ChouJiangInterface chou;

    private Button button;
    private Button verify;
    private TextView hint;

    private TextView showPersonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showPersonView = (TextView) findViewById(R.id.showView);
        button = (Button) findViewById(R.id.button);
        verify = (Button) findViewById(R.id.verify);
        hint = (TextView) findViewById(R.id.hint);

        chou = new ChouJiangRandom();
        chou.init(PersonManager.initialPersonList);

        verify.setOnClickListener(verifyListener);
        verify.setEnabled(false);
        showPersonView.setText("");
        updateHint();

        initForStart();
    }

    private void updateHint() {
        String message = "总共" +
                chou.countTotal() +
                "人，已抽奖" +
                chou.countGot() +
                "人，剩余" +
                chou.countLeft() +
                "人。";
        hint.setText(message);
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
        if (id == R.id.action_reset) {
            reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reset() {
        showPersonView.setText("");
        chou = new ChouJiangRandom();
        chou.init(PersonManager.initialPersonList);

        verify.setEnabled(false);
        verify.setText("确认领奖");
        updateHint();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {

            if (message.what == 1) {
                if (isStop()) {
                    return;
                }

                chou.next();
                showPersonView.setText(chou.chosenForDisplay());
                handler.sendEmptyMessageDelayed(1, DELAY_MILLIS);
            }
        }
    };

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
            showPersonView.setText(chou.chosen());
            verify.setText("确认" + chou.chosen() + "领奖");
            verify.setOnClickListener(verifyListener);

            initForStart();
        }
    };

    private View.OnClickListener verifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chou.gotIt();
            String personName = chou.chosen();
            if (personName.isEmpty()) {
                return;
            }
            verify.setText("取消" + personName + "领奖");

            showMessage("恭喜 " + personName + " 中奖！");
            updateHint();

            verify.setOnClickListener(verifyListenerGiveUp);
        }
    };

    private View.OnClickListener verifyListenerGiveUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chou.giveUp();
            String personName = chou.chosen();
            verify.setText("确认" + personName + "领奖");
            showMessage("已取消 " + personName + " 领奖！");
            updateHint();

            verify.setOnClickListener(verifyListener);
        }
    };

    private void showMessage(String s) {
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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
        verify.setText("确认领奖");
    }
}
