package com.espresso.black_box.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.espresso.black_box.R;


/**
 * Created by Karnaukh Roman on 16.08.2016.
 */
@TargetApi(16)
public class EspressoTestActivity extends Activity implements View.OnClickListener{
    Button notify;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        notify = (Button) findViewById(R.id.makeNotify);

        editText = (EditText) findViewById(R.id.editText);

        editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        editText.setImeActionLabel("Show",  EditorInfo.IME_ACTION_DONE);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Intent intent = new Intent("ua.espresso.ap24.tests.showtoast");
                intent.putExtra("message", editText.getText().toString());
                startService(intent);
                return true;
            }
        });

        notify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.makeNotify:
                Intent intent = new Intent("ua.espresso.ap24.tests.showtoast");
                intent.putExtra("message", editText.getText().toString());
                startService(intent);
                break;
        }

    }


}
