package com.kenportal.users;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kenportal.users.adapter.MessengerAdapter;
import com.kenportal.users.datamodels.MessangerModel;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MessangerActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<MessangerModel> chatList;
    private MessengerAdapter mAdapter;

    private AppCompatEditText msgTxt;
    private AppCompatButton postBtn;
    MessangerModel mdl;
    Calendar c;
    SimpleDateFormat df;
    Socket socket;
//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messanger);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.chat_details);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        chatList=new ArrayList<>();

        try {
            socket = IO.socket("http://192.168.8.112:3000");
        } catch (URISyntaxException e) {
            Log.i("atag","exception "+e.getMessage());
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                Log.i("atag", "successfully connected.");

                socket.emit("foo", "hi");
//                socket.disconnect();
            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("atag","event.");
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                Log.i("atag","disconnect");
            }

        }).on("foo", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                JSONObject obj = (JSONObject)args[0];

                Log.i("atag",obj.toString());
            }

        });
        socket.connect();


//        private Socket socket;
//    {
//        try{
//            socket = IO.socket("http://192.168.8.103:3000/?tuser=487&fuser=395");
//        }catch(URISyntaxException e){
//            throw new RuntimeException(e);
//        }
//    }
        mAdapter = new MessengerAdapter(this, chatList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        msgTxt = (AppCompatEditText) findViewById(R.id.msgTxt);
        postBtn = (AppCompatButton) findViewById(R.id.postBtn);
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = msgTxt.getText().toString();
                if (txt.equals("")) {
                    Toast.makeText(MessangerActivity.this, "Please enter some text.", Toast.LENGTH_SHORT).show();
                } else {
                    mdl = new MessangerModel();
                    c = Calendar.getInstance();
                    df = new SimpleDateFormat("dd MMM yyyy hh:mm a");
                    String formattedDate = df.format(c.getTime());
                    Log.i("atag",formattedDate);
                    mdl.setMessage(txt);
                    mdl.setDate(formattedDate);
                    chatList.add(mdl);
                    mAdapter.notifyDataSetChanged();
                    msgTxt.setText("");
                }
            }
        });


//        socket.connect();
//        socket.on("message", handleIncomingMessages);

    }

    private Emitter.Listener handleIncomingMessages = new Emitter.Listener(){
        @Override
        public void call(final Object... args){
            MessangerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    String imageText;
                    try {
                        message = data.getString("text").toString();

                        mdl = new MessangerModel();
                        mdl.setMessage(message);
                        mdl.setDate("");
                        chatList.add(mdl);
                        mAdapter.notifyDataSetChanged();
//                        msgTxt.setText("");
//                        addMessage(message);

                    } catch (JSONException e) {
                        // return;
                    }
//                    try {
//                        imageText = data.getString("image");
//                        addImage(decodeImage(imageText));
//                    } catch (JSONException e) {
//                        //retur
//                    }

                }
            });
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
