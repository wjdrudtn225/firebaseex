package com.example.wjdru.firebaseex;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;



public class MainActivity extends AppCompatActivity {
    private   ListView list_view;//리스트 뷰 생성
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("message");// 파이어베이스 경로 설정;
    private Button btn_send;
    private ArrayAdapter<String> arrayAdapter;
    private AppCompatEditText editText;
    private String str_name;
    private String str_msg;
    private String chat_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_view = (ListView)findViewById(R.id.list_view);
        btn_send = (Button)findViewById(R.id.btn_send);//전송 버튼 생성
        editText = (AppCompatEditText)findViewById(R.id.editText);

        arrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item);
        list_view.setAdapter(arrayAdapter);

        list_view.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        str_name= "Guest" + new Random().nextInt(1000);


        btn_send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Map<String, Object> map = new HashMap<String, Object>();

            String key = myRef.push().getKey();
            myRef.updateChildren(map);

            DatabaseReference dbRef = myRef.child(key);

            Map<String, Object> objectMap = new HashMap<String, Object>();

            objectMap.put("str_name", str_name);
            objectMap.put("text",editText.getText().toString());

            dbRef.updateChildren(objectMap);
            editText.setText("");



        }
    });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatListener(dataSnapshot);
        }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                chatListener(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void chatListener(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();

        while(i.hasNext()){
            chat_user =(String)((DataSnapshot)i.next()).getValue();
            str_msg = (String)((DataSnapshot)i.next()).getValue();

            arrayAdapter.add(chat_user + " : " + str_msg);
    }
        arrayAdapter.notifyDataSetChanged();
    }
}
