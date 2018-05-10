package featheryi.refreshloading;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    MainPresenter mainPresenter;

    LinearLayoutManager layoutManager;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    TextView nodata;

    int page = 1;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter(this);

        init();

        mainPresenter.getData(page);//初始
    }

    public void init() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("loading");

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainPresenter.adapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int firstVisibleItem, laseVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //判断RecyclerView的状态 是空闲时，同时，是第一个可见的ITEM时才加载
                if (newState == RecyclerView.SCROLL_STATE_IDLE && firstVisibleItem == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (mainPresenter.adapter.isFadeTips() == false && laseVisibleItem + 1 == mainPresenter.adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                page++;
                                mainPresenter.getData(page);
                            }
                        }, 500);
                    }
                    if (mainPresenter.adapter.isFadeTips() == true && laseVisibleItem + 2 == mainPresenter.adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                page++;
                                mainPresenter.getData(page);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //可見 item
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                laseVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        nodata = (TextView) findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
    }

    public void showProgressDialog() {
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onRefresh() {
//        下拉重來 初始化
        page = 1;
        mainPresenter.mainModel.all_list.clear();
        mainPresenter.getData(page);
    }
}
