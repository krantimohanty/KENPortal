package com.kenportal.users;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import com.kenportal.users.adapter.EmployeeListAdapter;
import com.kenportal.users.local_db.DbHelper;
import com.kenportal.users.local_db.DbModel;

public class AllEmployeeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    DbHelper dbHelper;
    private RecyclerView mRecyclerView;
    private List<DbModel> empArrayList;
    private RecyclerView.LayoutManager mLayoutManager;
    private EmployeeListAdapter mAdapter;
    LinearLayout boottomLayt;
    Button buttonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employee);

        dbHelper = new DbHelper(AllEmployeeActivity.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        empArrayList=new ArrayList<DbModel>();
        empArrayList = dbHelper.getAllEmp();

        boottomLayt=(LinearLayout)findViewById(R.id.boottomLayt);
        boottomLayt.setVisibility(View.GONE);
        buttonOk=(Button)findViewById(R.id.buttonOk);

        mRecyclerView = (RecyclerView)findViewById(R.id.all_employee_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EmployeeListAdapter(empArrayList,mRecyclerView,boottomLayt,buttonOk,this);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_employee, menu);
        final MenuItem item = menu.findItem(R.id.employee_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent=new Intent(AllEmployeeActivity.this,MapsActivity.class);
            finish();
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AllEmployeeActivity.this,MapsActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<DbModel> filteredModelList = filter(empArrayList, newText);
        mAdapter.setModels(empArrayList);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return false;
    }

    private List<DbModel> filter(List<DbModel> models, String query) {
        query = query.toLowerCase();
        final List<DbModel> filteredModelList = new ArrayList<>();
        for (DbModel model : models) {
            final String text = model.getEmp_name().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

}
