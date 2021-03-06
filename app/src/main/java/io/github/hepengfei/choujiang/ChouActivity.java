package io.github.hepengfei.choujiang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.HashMap;
import java.util.Map;


public class ChouActivity extends ActionBarActivity {

    private static final long DELAY_MILLIS = 30;

    private ChouJiangInterface chou;

    private Button button;
    private Button verify;
    private TextView hint;

    private TextView showPersonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chou);

        UmengUpdateAgent.update(this);

        chou = ChouJiangRandomRound.getInstance();

        showPersonView = (TextView) findViewById(R.id.showView);
        button = (Button) findViewById(R.id.button);
        verify = (Button) findViewById(R.id.verify);
        hint = (TextView) findViewById(R.id.hint);

        initActionBar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

        if (! isStop()) {
            stop();
        }
    }

    private void stop() {
        initView();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
    }


    private void initView() {
        showPersonView.setText(chou.chosen());

        button.setText(R.string.button_start);
        button.setOnClickListener(startListener);

        if (chou.chosen().isEmpty()) {
            verify.setVisibility(View.INVISIBLE);
        } else {
            verify.setVisibility(View.VISIBLE);
            if (chou.isChosenGot()) {
                verify.setText(R.string.button_giveup);
                verify.setOnClickListener(verifyListenerGiveUp);
            } else {
                verify.setText(R.string.button_got);
                verify.setOnClickListener(verifyListenerGot);
            }
        }

        updateHint();
    }

    private void updateHint() {
        String message = "总计" + chou.countTotal() + "人，已开奖" + chou.countGot() +
                "次，本轮剩余" + chou.countLeft() + "人";
        hint.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chou, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_pool_manager) {
            Intent intent = new Intent(this, PoolManagerActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            if (chou.countTotal() == 0) {
                Toast.makeText(ChouActivity.this, "名单为空，请点击右上角按扭添加。", Toast.LENGTH_LONG).show();
                return;
            }

            button.setText(R.string.button_stop);
            button.setOnClickListener(stopListener);

            verify.setVisibility(View.INVISIBLE);

            handler.sendEmptyMessage(1);


            Map<String, String > status = new HashMap<>();
            status.put("total", String.valueOf(chou.countTotal()));
            status.put("got", String.valueOf(chou.countGot()));
            status.put("left", String.valueOf(chou.countLeft()));
            MobclickAgent.onEvent(ChouActivity.this, "chou_start", status);
        }
    };

    private View.OnClickListener stopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String chosen = chou.chosen();
            chou.gotIt();
            updateHint();

            showPersonView.setText(chosen);

            verify.setText(R.string.button_giveup);
            verify.setOnClickListener(verifyListenerGiveUp);
            verify.setVisibility(View.VISIBLE);

            button.setText(R.string.button_start);
            button.setOnClickListener(startListener);


            Map<String, String > status = new HashMap<>();
            status.put("total", String.valueOf(chou.countTotal()));
            status.put("got", String.valueOf(chou.countGot()));
            status.put("left", String.valueOf(chou.countLeft()));
            MobclickAgent.onEvent(ChouActivity.this, "chou_stop");
        }
    };

    private View.OnClickListener verifyListenerGot = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chou.gotIt();
            updateHint();

            verify.setText(R.string.button_giveup);
            verify.setOnClickListener(verifyListenerGiveUp);
        }
    };

    private View.OnClickListener verifyListenerGiveUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chou.giveUp();
            updateHint();

            verify.setText(R.string.button_got);
            verify.setOnClickListener(verifyListenerGot);
        }
    };

}
