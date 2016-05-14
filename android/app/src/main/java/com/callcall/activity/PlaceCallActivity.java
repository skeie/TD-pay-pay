package com.callcall.activity;

import com.sinch.android.rtc.calling.Call;
import com.callcall.R;
import com.callcall.SinchService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceCallActivity extends BaseActivity {

    private Button mCallButton;
    private Button mCallUserButton;
    private EditText mCallName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCallName = (EditText) findViewById(R.id.callName);
        mCallButton = (Button) findViewById(R.id.callButton);
        mCallButton.setEnabled(false);
        mCallButton.setOnClickListener(buttonClickListener);

        mCallUserButton = (Button) findViewById(R.id.callUserButton);
        mCallUserButton.setEnabled(false);
        mCallUserButton.setOnClickListener(buttonClickListener);


        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onServiceConnected() {
        TextView userName = (TextView) findViewById(R.id.loggedInName);
        userName.setText(getSinchServiceInterface().getUserName());
        mCallButton.setEnabled(true);
        mCallUserButton.setEnabled(true);
    }

    private void stopButtonClicked() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        finish();
    }

    private void callButtonClicked(boolean callPhone) {
        String phoneNumber = mCallName.getText().toString();
        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a number to call", Toast.LENGTH_LONG).show();
            return;
        }

        Call call = call(phoneNumber, callPhone);
        String callId = call.getCallId();

        Intent callScreen = new Intent(this, CallScreenActivity.class);
        callScreen.putExtra(SinchService.CALL_ID, callId);
        startActivity(callScreen);
    }

    private Call call(String destination, boolean callPhone) {
        return callPhone ? getSinchServiceInterface().callPhoneNumber(destination) : getSinchServiceInterface().callUser(destination);
    }

    private OnClickListener buttonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.callButton:
                    callButtonClicked(true);
                    break;

                case R.id.stopButton:
                    stopButtonClicked();
                    break;
                case R.id.callUserButton:
                    callButtonClicked(false);
                    break;
            }
        }
    };
}
