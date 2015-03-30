package com.liberic.bitcoinwallet.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.liberic.bitcoinwallet.R;
import com.liberic.bitcoinwallet.util.Constant;
import com.liberic.bitcoinwallet.util.Globals;
import com.liberic.bitcoinwallet.util.Security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends Activity {
    private EditText username;
    private EditText password;
    private Button login;
    private ProgressBar spinner;
    private CheckBox saveCredentials;
    private static LoginActivity mApp;

    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!checkIfCredentialsExists()) {
            setScreenLoginForm();

            username = (EditText) findViewById(R.id.username);
            password = (EditText) findViewById(R.id.password);

            saveCredentials = (CheckBox) findViewById(R.id.save_credentials);

            login = (Button) findViewById(R.id.login);
        } else {
            setScreenLoginLoading();
            //TODO Delete '|| true'
            if (checkCredentials()  || true) {
                goMainActivity();
            } else {
                SharedPreferences pref = getSharedPreferences(Constant.PREF_GENERAL, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove(Constant.USER);
                editor.remove(Constant.PASS);
                editor.apply();

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show();
                setScreenLoginForm();
            }
        }

        mApp = this;
    }

    private void setScreenLoginForm() {
        setContentView(R.layout.activity_login);
    }

    private void setScreenLoginLoading() {
        setContentView(R.layout.activity_login_loading);
    }

    public void login(View view) throws Exception {
        Globals.user = username.getText().toString();
        Globals.pass = Security.encrypt(password.getText().toString());
        password.setText("");

        setScreenLoginLoading();

        //TODO Delete '|| true'
        if (checkCredentials() || true) {
            if(saveCredentials.isChecked()) {
                SharedPreferences pref = getSharedPreferences(Constant.PREF_GENERAL, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constant.USER, Globals.user);
                editor.putString(Constant.PASS, Globals.pass);
                editor.apply();

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_with),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_without), Toast.LENGTH_SHORT).show();
            }

            goMainActivity();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_credentials), Toast.LENGTH_SHORT).show();
            setScreenLoginForm();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    private boolean checkIfCredentialsExists() {
        SharedPreferences pref = getPreferences(MODE_PRIVATE);
        Globals.user = pref.getString(Constant.USER, null);
        Globals.pass = pref.getString(Constant.PASS, null);
        return Globals.user != null && Globals.pass != null;
    }

    private boolean checkCredentials(){
        boolean success = false;
        if (connectToDatabase()) {
            try{
                String sql = "SELECT " + Constant.USER_TABLE_MYSQL + ", " + Constant.PASS_TABLE_MYSQL + " FROM " + Constant.TABLE_USER_MYSQL + " WHERE " + Constant.USER_TABLE_MYSQL + " = " + Globals.user;
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()) {
                    success = Globals.user.equals(rs.getString(Constant.USER_TABLE_MYSQL)) && Globals.pass.equals(rs.getString(Constant.PASS_TABLE_MYSQL));
                }
                st.close();
                rs.close();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
        return success;
    }

    private boolean connectToDatabase() {
        boolean success = false;
        if(connection == null) {
            //TODO Create mysql server
            String url = "jdbc:mysql//" + Constant.IP_MYSQL + ":" + Constant.PORT_MYSQL + "/" + Constant.TABLE_USER_MYSQL;
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                connection = DriverManager.getConnection(url, Constant.USER_MYSQL, Constant.PASS_MYSQL);
                success = true;
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) {
                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return success;
    }

    public static Context getContext() { return mApp.getApplicationContext(); }

    public static void logout(Activity act) {
        SharedPreferences pref = mApp.getSharedPreferences(Constant.PREF_GENERAL, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(Constant.USER);
        editor.remove(Constant.PASS);
        editor.apply();
        Globals.user = null;
        Globals.pass = null;

        Intent intent = new Intent(act.getApplicationContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        act.startActivity(intent);
        Toast.makeText(getContext(), act.getResources().getString(R.string.action_logout),Toast.LENGTH_SHORT).show();
    }

    private void goMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public static SharedPreferences getPreferencesStatic(String pref, int mode) {
        return mApp.getSharedPreferences(pref, mode);
    }
}
