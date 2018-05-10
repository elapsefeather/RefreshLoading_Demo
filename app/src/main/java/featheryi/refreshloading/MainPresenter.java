package featheryi.refreshloading;

import android.view.View;

/**
 * Created by chaoyiting on 2018/5/10.
 */

public class MainPresenter {

    MainActivity mainActivity;
    MainModel mainModel;
    Adapter adapter;

    public MainPresenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        this.mainModel = new MainModel(this);
        this.adapter = new Adapter(this.mainActivity, mainModel.all_list, true);
    }

    public void getData(int page) {
        mainActivity.showProgressDialog();
        mainModel.getData(page);
    }

    public void notifylist() {
        boolean hasmore = false;
        if (mainModel.all_list.size() > 0) {
            if (mainModel.all_list.size() < mainModel.all_count) {
                hasmore = true;
            }
            mainActivity.recyclerView.setVisibility(View.VISIBLE);
            mainActivity.nodata.setVisibility(View.GONE);

        } else {
            mainActivity.recyclerView.setVisibility(View.GONE);
            mainActivity.nodata.setVisibility(View.VISIBLE);
        }

        adapter.setList(mainModel.all_list, hasmore);
        adapter.notifyDataSetChanged();
        mainActivity.dismissProgressDialog();
    }
}
