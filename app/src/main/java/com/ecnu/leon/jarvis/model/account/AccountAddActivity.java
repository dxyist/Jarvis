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
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;

import java.util.Timer;
import java.util.TimerTask;

public class AccountAddActivity extends AppCompatActivity {
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
    }

    View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.Rlayout_addAccount_category: {
                    Toast.makeText(AccountAddActivity.this, "功能施工中~", Toast.LENGTH_SHORT).show();
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


    public void initView() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setTitle("增加一条账目");
        }

        categoryTextView = (TextView) findViewById(R.id.txt_addAccount_category);
    }
}
