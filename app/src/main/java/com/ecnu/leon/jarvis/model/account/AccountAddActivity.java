package com.ecnu.leon.jarvis.model.account;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;

import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class AccountAddActivity extends AppCompatActivity {
    int REQUEST_CODE = 1;

    TextView categoryTextView;
    Button saveButton;
    Button saveAndStayButton;
    EditText costNumberEditText;
    EditText remarkEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_add);


        initView();
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.Rlayout_addAccount_category);
        relativeLayout.setOnClickListener(clickListener);

        saveButton = (Button) findViewById(R.id.btn_addAccount_save);
        saveButton.setOnClickListener(clickListener);
        saveAndStayButton = (Button) findViewById(R.id.btn_addAccount_save_And_AddAcount);
        saveAndStayButton.setOnClickListener(clickListener);

        costNumberEditText = (EditText) findViewById(R.id.edt_addAccount_number);
        remarkEditText = (EditText) findViewById(R.id.edt_addAccount_remark);

        categoryTextView = (TextView) findViewById(R.id.txt_addAccount_category);

        // 自动弹出软键盘
        costNumberEditText.setFocusable(true);
        costNumberEditText.setFocusableInTouchMode(true);
        costNumberEditText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) costNumberEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(costNumberEditText, 0);
            }

        }, 400);

        this.autoHint();

    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Rlayout_addAccount_category: {
                    // intent很重要，是两个Activity之间的纽带
                    Intent intent = new Intent(AccountAddActivity.this, CategoryActivity.class);

                    // 需要传出去的数据字串
                    String hello = categoryTextView.getText().toString().trim();
                    // 我们把要传出去的字串放到bundle中
                    Bundle extras = new Bundle();
                    // 第一个参数是key值，取的通过这个key就可以拿到这个bundle中的数据了
                    extras.putString("category", hello);
                    // 将bundle放进Intent中
                    intent.putExtras(extras);
                    // 跳转，这边代码有变化
                    // startActivity(in);
                    startActivityForResult(intent, REQUEST_CODE);
                    break;
                }

                case R.id.btn_addAccount_save: {
                    if (costNumberEditText.getText().toString().trim().equalsIgnoreCase("") || Float.valueOf(costNumberEditText.getText().toString().trim()) == 0) {
                        Toast.makeText(getApplicationContext(), "请输入有效金额", Toast.LENGTH_SHORT).show();
                        break;
                    }
//                    if (remarkEditText.getText().toString().trim() == "选择总类")
//                    {
//                        Toast.makeText(getApplicationContext(), "请选择消费总类", Toast.LENGTH_SHORT).show();
//                        break;
//                    }

                    AccountItem accountItem = new AccountItem(AccountManager.getNewAccountID(), Float.valueOf(costNumberEditText.getText().toString().trim()));
                    if (!remarkEditText.getText().toString().trim().equalsIgnoreCase("")) {
                        accountItem.setRemark(remarkEditText.getText().toString().trim());
                    }

                    if (!categoryTextView.getText().toString().trim().equalsIgnoreCase("")) {
                        accountItem.setCategory(categoryTextView.getText().toString().trim());
                    }

                    AccountManager.getInstance(getApplicationContext()).addNewAccountItem(accountItem);
                    Toast.makeText(getApplicationContext(), "已加入账目", Toast.LENGTH_SHORT).show();
                    // 关闭Activity
                    finish();
                    break;
                }
                case R.id.btn_addAccount_save_And_AddAcount: {
                    if (costNumberEditText.getText().toString().trim().equalsIgnoreCase("") || Float.valueOf(costNumberEditText.getText().toString().trim()) == 0) {
                        Toast.makeText(getApplicationContext(), "请输入有效金额", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    AccountItem accountItem = new AccountItem(AccountManager.getNewAccountID(), Float.valueOf(costNumberEditText.getText().toString().trim()));
                    if (!remarkEditText.getText().toString().trim().equalsIgnoreCase("")) {
                        accountItem.setRemark(remarkEditText.getText().toString().trim());
                    }

                    if (!categoryTextView.getText().toString().trim().equalsIgnoreCase("")) {
                        accountItem.setCategory(categoryTextView.getText().toString().trim());
                    }

                    AccountManager.getInstance(getApplicationContext()).addNewAccountItem(accountItem);
                    Toast.makeText(getApplicationContext(), "已加入账目", Toast.LENGTH_SHORT).show();
                    // 关闭Activity
                    break;
                }

                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                String show = data.getStringExtra("return");
                // 让textView显示取出来的数据
                if (show != null)
                {
                    categoryTextView.setText(show);
                } else
                {
                }
            }
        }
    }

    private void autoHint()
    {
        GregorianCalendar nowCalendar = new GregorianCalendar();
        int hour = nowCalendar.get(GregorianCalendar.HOUR_OF_DAY);
        if (hour >= 5 && hour < 10)
        {
            categoryTextView.setText("早餐");
        } else if (hour >= 10 && hour <= 14)
        {
            categoryTextView.setText("午餐");
        } else if (hour >= 16 && hour <= 20)
        {
            categoryTextView.setText("晚餐");
        } else if (hour >= 21 && hour <= 24)
        {
            categoryTextView.setText("夜宵");
        }
    }

    public void initView() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setTitle("增加一条账目");
        }

        categoryTextView = (TextView) findViewById(R.id.txt_addAccount_category);
    }
}
