package view.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kree.keehoo.mdpro.R;
import com.kree.keehoo.mdpro.presenter.MainPresenter;

import view.Activities.interfaces.MainActivityInterface;
import view.RVAdapters.SimpleViewAdapter;


public class DataListActivity extends AppCompatActivity implements MainActivityInterface {

    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private MainPresenter mainPresenter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(mainPresenter.saveCurrentScreenState());
    }

    protected void afterConfigurationChange(Bundle savedInstanceState) {
        mainPresenter.afterConfigurationChange(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        initViews();
        setUpTwoPaneMode();
        mainPresenter = new MainPresenter(this);
        afterConfigurationChange(savedInstanceState);
        doNotShowDetailOnLandscapeInTablet();
        mainPresenter.showPreviousScreen();

        if (noConnectivity()) {
            showRetryScreen();
            //TODO: additional presenter for connectivity screen
        } else {
            mainPresenter.downloadTappticValues();
        }
    }

    private void showRetryScreen() {
        Intent intent = new Intent(this, WaitForConnectivityActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean noConnectivity() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo networkInfo : info) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.data_list);
    }

    private void setUpTwoPaneMode() {
        if (findViewById(R.id.data_detail_container) != null) {  // data_detail_container jest w layoucie activity_data_detail i jest to kontrolka NestedSctollView
            mTwoPane = true;
        }
    }

    private void doNotShowDetailOnLandscapeInTablet() {
        if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mTwoPane = false;
            View v = findViewById(R.id.data_detail_container);
            if (null != v) {
                v.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean isTwoPane() {
        return mTwoPane;
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showTappticScreen() {

    }

    @Override
    public void showTappticList(SimpleViewAdapter adapter) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void startProperActivity(Intent intent) {
        startActivity(intent);
    }
}
