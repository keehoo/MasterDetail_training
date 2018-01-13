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
    private String result;
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
        } else {
            mainPresenter.downloadTappticValues();

       //     getSupportLoaderManager().initLoader(R.id.string_loader_id, null, listLoaderCallbacks);
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
        //      consts.saveTwoPane(mTwoPane);
    }

 /*   public void setupRecyclerView(List<ElementOfTheTappticList> values, final boolean mTwoPane) {
*//*        final SimpleViewAdapter adapter = new SimpleViewAdapter(this, values, mTwoPane);
        adapter.setListener(new SimpleViewAdapter.OnElementClickListener() {
            @Override
            public void onClick(ElementOfTheTappticList currentObject, int currentPosition) {
                mainPresenter.showDetailScreen(mTwoPane, currentObject);
            }
        });

        adapter.setFocusListener(new SimpleViewAdapter.OnElementFocusListener() {
            @Override
            public void onFocus(View v, boolean hasFocus, int position) {
                if (hasFocus) {
                    //           consts.saveCurrentFocusId(position);
                }
            }
        });*//*
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }*/


   /* public void parseReceivedData() {
        // TODO move to presenter
        List<ElementOfTheTappticList> values = new ArrayList<>();

        try {
            JSONArray ja = new JSONArray(getResult());
            values.clear();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                values.add(new ElementOfTheTappticList(jo.getString("name"), jo.getString("image")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(DataListActivity.this, "JSONArray Exception", Toast.LENGTH_SHORT).show();
        }
        setupRecyclerView(values, mTwoPane);
    }*/

/*    private LoaderManager.LoaderCallbacks<String> listLoaderCallbacks = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            return new StringLoader(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            Log.d("onLoadFinished", "data = " + data);
            setResult(data);
         //   parseReceivedData();
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            setupRecyclerView(Collections.<ElementOfTheTappticList>emptyList(), mTwoPane);
        }
    };*/

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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
