package com.kenportal.users;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kenportal.users.network_utils.VolleySingleton;
import com.kenportal.users.urls.ServerLinks;
import com.kenportal.users.utils.CustomPreference;

import org.json.JSONException;
import org.json.JSONObject;


public class UpdatesDetailActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView updateDetails;
    String user_id,update_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates_detail);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra("topic"));
        toolbar.setTitleTextColor(Color.WHITE);

        updateDetails = (TextView) findViewById(R.id.updates_detial);
        update_id=getIntent().getStringExtra("updt_id");
        updateDetails.setText(Html.fromHtml(getIntent().getStringExtra("detail")));
        updateDetails.setMovementMethod(LinkMovementMethod.getInstance());
        stripUnderlines(updateDetails);
        user_id= CustomPreference.with(this).getString("id", "");
        updateStatus();

    }

    private void updateStatus(){

        JSONObject param=new JSONObject();

        try {
            param.put("user_id",user_id);
            param.put("update_id",update_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.POST,
                ServerLinks.ServerUrl + "/update_read_status",
                param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
            });
        request.setRetryPolicy(new DefaultRetryPolicy(3000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance().addToRequestQueue(request, "update_status");
    }


    private void stripUnderlines(TextView textView) {
        Spannable s = new SpannableString(textView.getText());
        URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);

        for (URLSpan span: spans) {
            Log.i("atag", span.getURL());
        }
        textView.setText(s);
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
