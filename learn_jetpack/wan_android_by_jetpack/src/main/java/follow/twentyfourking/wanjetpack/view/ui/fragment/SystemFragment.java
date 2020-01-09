package follow.twentyfourking.wanjetpack.view.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import follow.twentyfourking.wanjetpack.R;
import follow.twentyfourking.wanjetpack.view.ui.activity.ChildSystemArtActivity;
import follow.twentyfourking.wanjetpack.view.ui.adapter.SystemArticleAdapter;
import follow.twentyfourking.wanjetpack.viewmodel.data.viewmodel.MainViewModel;
import follow.twentyfourking.wanjetpack.viewmodel.protocol.bean.TechnicalSystemBean;

public class SystemFragment extends Fragment implements SystemArticleAdapter.ISystemArticleCallback {
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;
    private ISystemCallback mCallback;
    private Context mContext;
    private Map<String, TechnicalSystemBean> mMainData;
    private SystemArticleAdapter mAdapter;

    public static Fragment newInstance(ISystemCallback callback) {
        SystemFragment fragment = new SystemFragment();
        fragment.setCallback(callback);
        return fragment;
    }

    private void setCallback(ISystemCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        observeViewModel();
        mMainData = new HashMap<>();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.from(container.getContext()).inflate(R.layout.fragment_system, null, false);
        initContent(view);
        return view;
    }

    private void initContent(View view) {
        mTabLayout = view.findViewById(R.id.tab_fragment_tech_system_main);
        mRecyclerView = view.findViewById(R.id.rcv_fragment_system_article);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mContext,
                manager.getOrientation());
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.item_drawable_1);
        itemDecoration.setDrawable(drawable);
        mRecyclerView.addItemDecoration(itemDecoration);
        mAdapter = new SystemArticleAdapter();
        mAdapter.setCallback(this);
        mRecyclerView.setAdapter(mAdapter);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() != null && !TextUtils.isEmpty(tab.getTag().toString())) {
                    TechnicalSystemBean bean = mMainData.get(tab.getTag().toString());
                    mAdapter.setData(bean.getChildren());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void observeViewModel() {
        mCallback.onGetMainViewModel().getChildListData().observe(getActivity(), new Observer<List<TechnicalSystemBean>>() {
            @Override
            public void onChanged(List<TechnicalSystemBean> technicalSystemList) {
                if (technicalSystemList != null && technicalSystemList.size() > 0) {
                    if (mMainData != null) {
                        mMainData.clear();
                    }
                    for (TechnicalSystemBean bean : technicalSystemList) {
                        mMainData.put(bean.getName(), bean);
                    }
                    Set<String> titles = mMainData.keySet();
                    createTabs(titles);
                }
            }
        });
    }

    private void createTabs(Set<String> data) {
        List<TabLayout.Tab> tabList = new ArrayList<>();
        for (String title : data) {
            TabLayout.Tab tab = mTabLayout.newTab().setCustomView(createView(title));
            tab.setTag(title);
            mTabLayout.addTab(tab);
            tabList.add(tab);
        }
        Log.d("TTT", "");
    }

    private View createView(String str) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.view_system_article_tab_item,
                        null, false);
        ((TextView) (view.findViewById(R.id.tv_system_tab_title))).setText(str);
        return view;
    }

    @Override
    public void startActivity(int cId) {
        Intent intent = new Intent();
        intent.putExtra("cid", cId);
        intent.setClass(mContext, ChildSystemArtActivity.class);
        getActivity().startActivity(intent);
    }

    public interface ISystemCallback {
        MainViewModel onGetMainViewModel();
    }

}
