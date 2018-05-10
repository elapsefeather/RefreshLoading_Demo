package featheryi.refreshloading;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaoyiting on 2018/5/10.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> datas;
    Context context;
    int normalType = 0;
    int footType = 1;
    boolean hasMore = true;
    boolean fadeTips = false;
    Handler mHandler = new Handler(Looper.getMainLooper());

    public Adapter(Context context, List<String> datas, boolean hasMore) {
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
    }

    public void setList(List<String> datas, boolean hasMore) {
        this.datas = datas;
        this.hasMore = hasMore;
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
//            最後ㄧ條提示項
            return footType;
        } else {
            return normalType;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
//            View.inflate(context, R.layout.layout_header, null); // 會不延展item
        if (viewType == normalType) {
            v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            return new NormalHolder(v);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.footview, parent, false);
            return new FootHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NormalHolder) {
            ((NormalHolder) holder).textView.setText(datas.get(position));
        } else {
            ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
            if (hasMore) {
                fadeTips = false;
                if (datas.size() > 0) {
                    ((FootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (datas.size() > 0) {
                    ((FootHolder) holder).tips.setText("没有更多数据了");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((FootHolder) holder).tips.setVisibility(View.GONE);
                            fadeTips = true;
//                            hasMore = true;
                        }
                    }, 500);
                }
            }
        }
    }

    class NormalHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public NormalHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;

        public FootHolder(View itemView) {
            super(itemView);
            tips = (TextView) itemView.findViewById(R.id.tips);
        }
    }

    public boolean isFadeTips() {
        return fadeTips;
    }

}
