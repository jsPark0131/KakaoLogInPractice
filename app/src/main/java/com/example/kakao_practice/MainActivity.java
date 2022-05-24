package com.example.kakao_practice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kakao.auth.AuthType;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;

public class MainActivity extends AppCompatActivity {
    private Button btn_custom_login;
    private Button btn_custom_login_out;
    private SessionCallBack sessionCallback = new SessionCallBack();
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_custom_login = (Button) findViewById(R.id.btn_custom_login);
        btn_custom_login_out = (Button) findViewById(R.id.btn_custom_login_out);


        if (Session.getCurrentSession().checkAndImplicitOpen()) {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onSuccess(MeV2Response result) {
                    Log.e("###", "kakao automatic log in");
                    Intent intent = new Intent(MainActivity.this, NextActivity.class);
                    startActivity(intent);
                    finish();
                }
                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }
            });
        }

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

       /* btn_custom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.open(AuthType.KAKAO_LOGIN_ALL, MainActivity.this);
            }
        });*/

        /*btn_custom_login_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Log.e("###", "logout");
                            }
                        });
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}