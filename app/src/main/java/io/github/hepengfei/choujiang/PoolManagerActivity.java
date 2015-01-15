package io.github.hepengfei.choujiang;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class PoolManagerActivity extends ActionBarActivity {

    private ListView listView;
    private EditText editText;
    private ImageButton addButton;
    private List<String> personList;
    private ArrayAdapter adapter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_manager);

        personList = new LinkedList<>();
        for (int i=0; i<ChouActivity.initialPersonList.length; ++i) {
            personList.add(ChouActivity.initialPersonList[i]);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, personList);

        initActionBar();

        editText = (EditText) findViewById(R.id.editText);
        addButton = (ImageButton) findViewById(R.id.addButton);
        textView = (TextView) findViewById(R.id.textView);
        listView = (ListView)findViewById(R.id.listView);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String input = editText.getText().toString().trim();
                if (input.isEmpty()) {
                    Toast.makeText(PoolManagerActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                String newPersonList[] = input.split("[,，;；]");
                int numberOfAdded = 0;
                int numberOfIgnored = 0;
                for (int i=0; i<newPersonList.length; ++i) {
                    String person = newPersonList[i].trim();
                    if (person.isEmpty()) {
                        continue;
                    }
                    if (personList.contains(person)) {
                        numberOfIgnored ++;
                        continue;
                    }
                    personList.add(0, person);
                    numberOfAdded ++;
                }
                String message = "";
                if (numberOfIgnored > 0) {
                    message = message + "忽略已存在的" + numberOfIgnored + "人";
                }
                if (numberOfAdded > 0) {
                    message = message + (message.isEmpty() ? "" : "，") +
                            "成功增加" + numberOfAdded + "人";
                }
                if (! message.isEmpty()) {
                    Toast.makeText(PoolManagerActivity.this, message, Toast.LENGTH_LONG).show();
                }
                updateView();
                editText.setText("");
            }
        });

        listView.setAdapter(adapter);

        updateView();
    }

    private void updateView() {
        adapter.notifyDataSetChanged();
        if (personList.isEmpty()) {
            textView.setText("当前名单为空，请添加抽奖人员");
        } else {
            textView.setText("当前名单总计" + personList.size() + "人：");
        }
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pool_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            personList.clear();
            updateView();
            return true;
        }

        if (id == android.R.id.home) {
            String tmp[] = new String[personList.size()];
            Iterator<String> it = personList.iterator();
            int i = 0;
            while (it.hasNext()) {
                tmp[i] = it.next();
                i++;
            }
            ChouActivity.initialPersonList = tmp;
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
