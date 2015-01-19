package io.github.hepengfei.choujiang;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.Iterator;
import java.util.List;


public class PoolManagerActivity extends ActionBarActivity {

    private ListView listView;
    private EditText editText;
    private ImageButton addButton;
    private BaseAdapter adapter;
    private TextView textView;

    private ChouJiangInterface chou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_manager);

        chou = ChouJiangRandomRound.getInstance();

        adapter = new PersonListAdapter();

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
                    final int n = chou.add(person);
                    if (n == 0) {
                        numberOfIgnored ++;
                    } else {
                        numberOfAdded ++;
                    }
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("onItemLongClick", "position=" + position + ",id=" + id + ",countOfChecked=" + listView.getCheckedItemCount());
                return true;
            }
        });

        updateView();
    }

    private void updateView() {
        adapter.notifyDataSetChanged();
        if (chou.countTotal() == 0) {
            textView.setText("当前名单为空，请添加抽奖人员");
        } else {
            textView.setText("当前名单总计" + chou.countTotal() + "人：");
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
            chou.clear();
            updateView();
            return true;
        }

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        if (id == R.id.action_copy) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("名单", chou.getAllNames());
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(PoolManagerActivity.this, "已复制" + chou.countTotal() + "人的名单到剪贴板", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    class PersonItemHolder {
        TextView textView;
        ImageButton imageButton;
    }

    class PersonListAdapter extends BaseAdapter {

        private ImageButton lastToggledButton;

        public PersonListAdapter() {

        }

        @Override
        public int getCount() {
            return chou.countTotal();
        }

        @Override
        public Object getItem(int position) {
            return chou.getName(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PersonItemHolder holder;

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.person_item, null);
                holder = new PersonItemHolder();
                holder.textView = (TextView) convertView.findViewById(R.id.textView);
                holder.imageButton = (ImageButton) convertView.findViewById(R.id.imageButton);
                holder.imageButton.setTag(holder);
                convertView.setTag(holder);
            } else {
                holder = (PersonItemHolder)convertView.getTag();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonItemHolder holder = (PersonItemHolder)v.getTag();
                    if (lastToggledButton != null && lastToggledButton != holder.imageButton) {
                        lastToggledButton.setVisibility(View.INVISIBLE);
                    }

                    if (holder.imageButton.getVisibility() == View.VISIBLE) {
                        holder.imageButton.setVisibility(View.INVISIBLE);
                    } else {
                        holder.imageButton.setVisibility(View.VISIBLE);
                    }

                    lastToggledButton = holder.imageButton;
                }
            });
            holder.textView.setText(chou.getName(position));

            holder.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonItemHolder holder = (PersonItemHolder)v.getTag();
                    removePerson(holder.textView.getText().toString());

                    lastToggledButton = null; // lastToggledButton == holder.imageButton == v
                }
            });

            holder.imageButton.setVisibility(View.INVISIBLE);

            return convertView;
        }
    }

    private void removePerson(String person) {
        chou.remove(person);
        updateView();
    }

}
