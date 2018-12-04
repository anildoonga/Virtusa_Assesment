package com.virtusa.assesment.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.virtusa.assesment.R;
import com.virtusa.assesment.adapters.FeedActivityRecyclerAdapter;
import com.virtusa.assesment.interfaces.WipServiceInterface;
import com.virtusa.assesment.model.Row;
import com.virtusa.assesment.remote.ApiUtils;
import com.virtusa.assesment.utility.AlertUtil;
import com.virtusa.assesment.utility.Utility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * this Activity displays the json data extracted from json feed in recycler viewer
 */
public class MainFeedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar progressBar;
    private RecyclerView mRecyclerView;

    private List<Row> mVirArray = new ArrayList<>();
    private FeedActivityRecyclerAdapter mFeedAdapter;
    private WipServiceInterface mService;
    private List<Row> mRows = null;
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {//Swipe Refresh handler
            getData();
            mSwipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }, 1000);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Set Back Icon on Activity
        mService = ApiUtils.getWipService();
        if (Utility.CheckInternet(MainFeedActivity.this)) {
            getData();
        } else {
            AlertUtil.alert_Msg(MainFeedActivity.this, getResources().getString(R.string.network_error));
        }
        loadRecyclerView();
        mFeedAdapter = new FeedActivityRecyclerAdapter(MainFeedActivity.this);
       /* mFeedAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mRows != null) {
                    Intent myIntent = new Intent(MainFeedActivity.this, DisplayImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(getString(R.string.Cell_Position), position);
                    myIntent.putExtras(bundle);
                    startActivity(myIntent);
                }
            }
        });*/
        // SwipeRefreshLayout
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_orange_dark,
                android.R.color.holo_green_dark,
                android.R.color.holo_blue_dark);
        mRecyclerView.setAdapter(mFeedAdapter);
    }

    /**
     * Loading of Recycler Viewer
     */
    private void loadRecyclerView() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * making the call using enqueue()
     * it takes callback interface as an argument
     * on success it will call onResponse else onFailure
     */
    private void getData() {
        {
            mService.getServerData().enqueue(new Callback<List<Row>>() {//getFinalData
                @SuppressWarnings("ConstantConditions")
                @Override
                public void onResponse(Call<List<Row>> call, Response<List<Row>> response) {
                    if (response.isSuccessful()) {
                        //Log.e("response", "response------------------>" + response.body());
                        mVirArray = response.body();
                        mFeedAdapter.mRecyclerViewAdapterData(mVirArray);
                        mFeedAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        int statusCode = response.code();
                        Log.e("RETROFIT", String.valueOf(statusCode));
                        // error case
                        switch (response.code()) {
                            case 404:
                                Toast.makeText(getApplicationContext(), R.string.Server_Not_Found, Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(getApplicationContext(), R.string.Server_Broken, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), R.string.Unknown_Error, Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Row>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("response", "response------------------>" + t.getMessage());
                    progressBar.setVisibility(View.GONE);
                    AlertUtil.alert_Msg(MainFeedActivity.this, getResources().getString(R.string.network_error));
                    Log.e("RETROFIT", t.toString());
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reset:
                loadRecyclerView();
                getData();
                return (true);
            case android.R.id.home:
                finish();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onRefresh() {

        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                mHandler.sendEmptyMessage(0);
            }
        }, 1000);
    }

}
