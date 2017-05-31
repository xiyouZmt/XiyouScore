package com.zmt.boxin.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.NetworkThread.GetSession;
import com.zmt.boxin.NetworkThread.IdentifyThread;
import com.zmt.boxin.NetworkThread.ViewStateThread;
import com.zmt.boxin.R;
import com.zmt.boxin.View.ClearEditText;
import com.zmt.boxin.Utils.IsConnected;
import com.zmt.boxin.View.PasswordEditText;
import com.zmt.boxin.Utils.RequestUrl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private final String rootPath = Environment.getExternalStorageDirectory() + "/";
    private ProgressDialog progressdialog;
    private App app;
    @BindView(R.id.login) Button login;
    @BindView(R.id.remember) CheckBox checkbox;
    @BindView(R.id.username) ClearEditText username;
    @BindView(R.id.password) PasswordEditText password;
    @BindView(R.id.checkCodeText) ClearEditText checkCodeText;
    @BindView(R.id.checkCode) ImageView checkCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 标题栏透明
         */
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_login);
        initViews();
        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        if(sharedPreferences != null){
            String name = sharedPreferences.getString("username", "");
            String pwd = sharedPreferences.getString("password", "");
            username.setText(name);
            password.setText(pwd);
            checkbox.setChecked(true);
        }

        /**
         * 获取验证码
         */
        boolean allow = accessPermission();
        if(allow){
            RequestUrl url = new RequestUrl();
            IdentifyThread thread = new IdentifyThread(url.getIdentifyCode(), handler, app);
            Thread t = new Thread(thread, "IdentifyThread");
            t.start();
        }
    }

    private final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Intent intent = new Intent(); 
            switch (msg.what){
                case 0x000 :
                    Snackbar.make(login, "登陆成功!", Snackbar.LENGTH_SHORT).show();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    if(msg.obj != null){
                        intent.putExtra("content", msg.obj.toString());
                    } else {
                        intent.putExtra("content", "course is null");
                    }
                    startActivity(intent);
                    progressdialog.dismiss();
                    finish();
                    break;
                case 0x001 :
                    String checkCodePath = rootPath + "/Boxin/Images/checkCode.png";
                    Bitmap bitmap = BitmapFactory.decodeFile(checkCodePath);
                    checkCodeView.setImageBitmap(bitmap);
                    break;
                case 0x111 :
                    progressdialog.dismiss();
                    Snackbar.make(login, "用户名或密码错误!", Snackbar.LENGTH_SHORT).show();
                    break;
                case 0x222 :
                    progressdialog.dismiss();
                    Snackbar.make(login, "服务端连接失败,请重新登陆!", Snackbar.LENGTH_SHORT).show();
                    break;
                case 0x333 :
                    progressdialog.dismiss();
                    Snackbar.make(login, "网络连接错误!", Snackbar.LENGTH_SHORT).show();
                    break;
                case 0x444 :
                    progressdialog.dismiss();
                    Toast.makeText(LoginActivity.this, "你还没有对本学期的课程进行评价, 请先评价", Toast.LENGTH_SHORT).show();
                    intent.setClass(LoginActivity.this, com.zmt.boxin.Activity.WebView.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @OnClick({R.id.login, R.id.checkCode})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login :
                IsConnected connected = new IsConnected();
                if(username.getText().toString().equals("")){
                    Snackbar.make(login, "用户名不能为空!", Snackbar.LENGTH_SHORT).show();
                } else if(password.getText().toString().equals("")){
                    Snackbar.make(login, "密码不能为空!", Snackbar.LENGTH_SHORT).show();
                } else if(checkCodeText.getText().toString().equals("")){
                    Snackbar.make(login, "验证码不能为空!", Snackbar.LENGTH_SHORT).show();
                } else {
                    String number = username.getText().toString();
                    String pwd = password.getText().toString();
                    String checkCode = checkCodeText.getText().toString();
                    if(checkbox.isChecked()){
                        /**
                         * 记住密码
                         */
                        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", number);
                        editor.putString("password", pwd);
                        editor.apply();
                    } else{
                        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                        if(sharedPreferences != null){
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("username");
                            editor.remove("password");
                            editor.apply();
                        }
                    }
                    if(!connected.checkNetwork(this)){
                        Snackbar.make(login, "网络未连接，请先连接网络!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        progressdialog.show();
                        RequestUrl url = new RequestUrl();
                        ViewStateThread thread = new ViewStateThread(url.getIP(), number, pwd, checkCode, app, handler);
                        Thread t = new Thread(thread, "NetWorkThread");
                        t.start();
                    }
                }
                break;
            case R.id.checkCode :
                /**
                 * 重新获取验证码
                 */
                RequestUrl url = new RequestUrl();
                IdentifyThread thread = new IdentifyThread(url.getIdentifyCode(), handler, app);
                Thread t = new Thread(thread, "IdentifyThread");
                t.start();
                break;
        }
    }

    public void initViews(){
        app = (App)getApplication();
        ButterKnife.bind(this);
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("正在登陆...");
        progressdialog.setCancelable(false);
    }

    public boolean accessPermission(){
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case 1 :
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    /**
                     * 授予权限，
                     */
                    RequestUrl url = new RequestUrl();
                    IdentifyThread thread = new IdentifyThread(url.getIdentifyCode(), handler, app);
                    Thread t = new Thread(thread, "IdentifyThread");
                    t.start();
                } else {
                    /**
                     * 拒绝授予
                     */
                    Toast.makeText(LoginActivity.this, "请在系统设置中对本应用开启存储权限!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
