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


public class ChouActivity extends ActionBarActivity {

    private static final long DELAY_MILLIS = 30;
    public static String initialPersonList [] = new String[] {
            "昌睿",
            "周泉龙",
            "李建利",
            "泰福",
            "王长财",
            "红良",
            "张昌逊",
            "卓臻",
            "昌哲",
            "贺承哲",
            "卓逊",
            "武臻",
            "肖武睿",
            "昌羲",
            "昌臻",
            "彦睿",
            "思源"
    };

    private ChouJiangInterface chou;

    private Button button;
    private Button verify;
    private TextView hint;

    private TextView showPersonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chou);

        showPersonView = (TextView) findViewById(R.id.showView);
        button = (Button) findViewById(R.id.button);
        verify = (Button) findViewById(R.id.verify);
        hint = (TextView) findViewById(R.id.hint);

        initActionBar();
        initChouJiang();
        initView();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void initChouJiang() {
        chou = new ChouJiangRandomRound();
        chou.init(initialPersonList);
    }

    private void initView() {
        showPersonView.setText("");

        button.setText(R.string.button_start);
        button.setOnClickListener(startListener);

        verify.setVisibility(View.INVISIBLE);

        updateHint();
    }

    private void updateHint() {
        String message = "总共" + chou.countTotal() + "人，已开奖" + chou.countGot() +
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
            button.setText(R.string.button_stop);
            button.setOnClickListener(stopListener);

            verify.setVisibility(View.INVISIBLE);

            handler.sendEmptyMessage(1);
        }
    };

    private View.OnClickListener stopListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String chosen = chou.chosen();
            chou.gotIt();
            updateHint();

            showPersonView.setText(chosen);

            verify.setText("取消此次抽奖结果");
            verify.setOnClickListener(verifyListenerGiveUp);
            verify.setVisibility(View.VISIBLE);

            button.setText(R.string.button_start);
            button.setOnClickListener(startListener);
        }
    };

    private View.OnClickListener verifyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chou.gotIt();
            updateHint();

            verify.setText("取消此次抽奖结果");
            verify.setOnClickListener(verifyListenerGiveUp);
        }
    };

    private View.OnClickListener verifyListenerGiveUp = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chou.giveUp();
            updateHint();

            verify.setText("恢复此次抽奖结果");
            verify.setOnClickListener(verifyListener);
        }
    };

}
