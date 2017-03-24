/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

import static android.os.Build.VERSION_CODES.M;


public class MainActivity extends AppCompatActivity {

  EditText username;
  EditText password;
  Button signUpButton;
  TextView changeSignUpMode;
  Boolean signUpModeActive;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    username = (EditText) findViewById(R.id.username);
    password = (EditText) findViewById(R.id.password);
    signUpButton = (Button) findViewById(R.id.signUpButton);
    changeSignUpMode = (TextView) findViewById(R.id.changeSignUpMode);
    signUpModeActive = true;

    signUpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(TextUtils.isEmpty(username.getText().toString())){
          Toast.makeText(MainActivity.this, "Username cannot be blank", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password.getText().toString())){
          Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        if(signUpModeActive){
        ParseUser user = new ParseUser();
          user.setUsername(username.getText().toString());
          user.setPassword(password.getText().toString());
          user.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {
                  if(e == null){
                      Toast.makeText(MainActivity.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                  }
                  else {
                      e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();

                  }
              }
          });
        }
        else{
          ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
              if(user != null){
                Toast.makeText(MainActivity.this, "LogIn Successful", Toast.LENGTH_SHORT).show();
              }
              else{
                //Fix the error by seeing the documentation
                Log.v("Error", e.getMessage());
                e.printStackTrace();
              }
            }
          });
        }
      }
    });

    changeSignUpMode.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v){
        if(signUpModeActive){
          signUpModeActive = false;
          signUpButton.setText("Log In");
          changeSignUpMode.setText("Sign Up");
        }
        else{
          signUpModeActive = true;
          signUpButton.setText("Sign Up");
          changeSignUpMode.setText("Log In");
        }
      }
    });

    /*password.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
      }
    });*/
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
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
}
