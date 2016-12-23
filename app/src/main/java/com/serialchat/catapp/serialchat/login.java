package com.serialchat.catapp.serialchat;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import static android.Manifest.permission.GET_ACCOUNTS;

public class login extends AppCompatActivity {

    @BindView(R.id.et_User) AutoCompleteTextView etUser;
    @BindView(R.id.activity_login) RelativeLayout rlLogin;
    @BindView(R.id.et_Password) EditText etPass;
    @BindView(R.id.tv_ResetPass) TextView tvReset;
    @BindView(R.id.bt_Login) Button btLogin;


    private final Pattern patron = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    private final int MY_PERMISISONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        myRequestPermission();

        etUser.setAdapter(getEmailAddressAdapter(getApplicationContext()));

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUser.getText().toString();
                String pass = etPass.getText().toString();
                if (!user.isEmpty() && !pass.isEmpty()) {
                    if (patron.matcher(user).matches()) {
                        Toast.makeText(login.this, "Es correo", Toast.LENGTH_SHORT).show();
                        startMain();
                    }else{
                        Toast.makeText(login.this, "No es correo", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @SuppressWarnings("MissingPermission")
    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        if(!myRequestPermission()) {
            Toast.makeText(context, "Error al leer accounts", Toast.LENGTH_SHORT).show();
        }
        Account[] accounts = AccountManager.get(context).getAccounts();
        String[] addresses = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            addresses[i] = accounts[i].name;
        }
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, addresses);
    }
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
    private void startMain(){
        Intent intent = new Intent(this, chatMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISISONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permiso aceptado
                    Toast.makeText(this, "Permiso aceptado", Toast.LENGTH_SHORT).show();
                } else {
                    // Permiso denegado
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private boolean myRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        if (shouldShowRequestPermissionRationale(GET_ACCOUNTS)){
            Snackbar.make(rlLogin, "Permiso necesario para la aplicación", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(View v) {
                            requestPermissions(new String[]{GET_ACCOUNTS}, MY_PERMISISONS);
                        }
                    }).show();
        } else {
            requestPermissions(new String[]{GET_ACCOUNTS}, MY_PERMISISONS);
        }
        return false;
    }
}
