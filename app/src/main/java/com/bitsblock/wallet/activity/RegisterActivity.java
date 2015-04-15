package com.bitsblock.wallet.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bitsblock.wallet.R;
import com.bitsblock.wallet.util.Constant;
import com.bitsblock.wallet.util.Globals;
import com.bitsblock.wallet.util.Interface;
import com.bitsblock.wallet.util.Security;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegisterActivity extends Activity {

    private EditText username;
    private EditText password;
    private EditText email;
    private Connection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView loginScreen = (TextView) findViewById(R.id.link_login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);

        loginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void register(View view) throws Exception {
        Globals.user = username.getText().toString();
        Globals.pass = Security.encrypt(password.getText().toString());
        password.setText("");

        //TODO Delete '|| true'
        if(!checkCredentials() || true) {
            Toast.makeText(getApplicationContext(), "Register complete", Toast.LENGTH_SHORT).show();
            goMainActivity();
        } else {
            Toast.makeText(getApplicationContext(), "Register incomplete", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkCredentials(){
        boolean success = false;
        if (connectToDatabase()) {
            try{
                String sql = "SELECT " + Constant.USER_TABLE_MYSQL + " FROM " + Constant.TABLE_USER_MYSQL + " WHERE " + Constant.USER_TABLE_MYSQL + " = " + Globals.user;
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if(rs.next()) {
                    success = Globals.user.equals(rs.getString(Constant.USER_TABLE_MYSQL));
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

    private void goMainActivity() {
        SharedPreferences pref = getSharedPreferences(Constant.PREF_CURRENT_USER, MODE_PRIVATE);
        Interface.getRates(this, false);
        loadDefaultPreferences(pref);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void loadDefaultPreferences(SharedPreferences pref) {
        SharedPreferences.Editor editor = pref.edit();
        if (pref.getString(Constant.CURRENCY_TYPE,null) == null) {
            editor.putString(Constant.CURRENCY_TYPE, "");
            editor.putFloat(Constant.CURRENCY_VALUE, 1.0f);
        }

        editor.apply();
    }
}
