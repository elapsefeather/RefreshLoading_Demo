package featheryi.refreshloading;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoyiting on 2018/5/10.
 */

public class MainModel {

    MainPresenter mainPresenter;
    List<String> list;
    List<String> all_list;
    int all_count = 20;
    int page_count = 15;

    public MainModel(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;
        this.list = new ArrayList<>();
        this.all_list = new ArrayList<>();
    }

    public void getData(int page) {

        list.clear();

        int first = page_count * (page - 1);
        int end = page_count * (page - 1) + page_count;

        if (end > all_count) {
            end = all_count;
        }
//        新資料筆
        for (int i = first; i < end; i++) {
            list.add("" + i);
        }

//        合併所有資料筆
        all_list.addAll(list);
        mainPresenter.notifylist();
    }
}
