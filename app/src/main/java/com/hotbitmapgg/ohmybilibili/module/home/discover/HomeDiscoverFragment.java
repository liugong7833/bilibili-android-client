package com.hotbitmapgg.ohmybilibili.module.home.discover;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hotbitmapgg.ohmybilibili.R;
import com.hotbitmapgg.ohmybilibili.base.RxLazyFragment;
import com.hotbitmapgg.ohmybilibili.entity.discover.HotSearchTag;
import com.hotbitmapgg.ohmybilibili.module.entry.GameCentreActivity;
import com.hotbitmapgg.ohmybilibili.module.search.TotalStationSearchActivity;
import com.hotbitmapgg.ohmybilibili.network.RetrofitHelper;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 16/8/4 21:18
 * 100332338@qq.com
 * <p/>
 * 首页发现界面
 */
public class HomeDiscoverFragment extends RxLazyFragment
{

    @Bind(R.id.tags_layout)
    TagFlowLayout mTagFlowLayout;

    @Bind(R.id.hide_scroll_view)
    NestedScrollView mScrollView;

    @Bind(R.id.hide_tags_layout)
    TagFlowLayout mHideTagLayout;

    @Bind(R.id.more_layout)
    LinearLayout mMoreLayout;

    @Bind(R.id.tv_more)
    TextView mMoreText;

    private boolean isShowMore = true;

    private List<HotSearchTag.ListBean> hotSearchTags = new ArrayList<>();

    public static HomeDiscoverFragment newInstance()
    {

        return new HomeDiscoverFragment();
    }

    @Override
    public int getLayoutResId()
    {

        return R.layout.fragment_discover;
    }

    @Override
    public void finishCreateView(Bundle state)
    {

        mScrollView.setNestedScrollingEnabled(true);
        getTags();
    }

    private void getTags()
    {

        RetrofitHelper.getHotSearchTagsApi()
                .getHotSearchTags()
                .compose(bindToLifecycle())
                .map(HotSearchTag::getList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listBeans -> {

                    hotSearchTags.addAll(listBeans);
                    initTagLayout();
                }, throwable -> {

                });
    }


    private void initTagLayout()
    {

        //获取热搜标签集合前8个默认显示
        List<HotSearchTag.ListBean> frontTags = hotSearchTags.subList(0, 7);
        mTagFlowLayout.setAdapter(new TagAdapter<HotSearchTag.ListBean>(frontTags)
        {

            @Override
            public View getView(FlowLayout parent, int position, HotSearchTag.ListBean listBean)
            {

                TextView mTags = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.layout_tags_item, parent, false);
                mTags.setText(listBean.getKeyword());
                mTags.setOnClickListener(v -> TotalStationSearchActivity.launch(getActivity(), listBean.getKeyword()));

                return mTags;
            }
        });


        mHideTagLayout.setAdapter(new TagAdapter<HotSearchTag.ListBean>(hotSearchTags)
        {

            @Override
            public View getView(FlowLayout parent, int position, HotSearchTag.ListBean listBean)
            {

                TextView mTags = (TextView) LayoutInflater.from(getActivity())
                        .inflate(R.layout.layout_tags_item, parent, false);
                mTags.setText(listBean.getKeyword());
                mTags.setOnClickListener(v -> TotalStationSearchActivity.launch(getActivity(), listBean.getKeyword()));

                return mTags;
            }
        });
    }


    @OnClick(R.id.more_layout)
    void showAndHideMoreLayout()
    {

        if (isShowMore)
        {
            isShowMore = false;
            mScrollView.setVisibility(View.VISIBLE);
            mMoreText.setText("收起");
            mTagFlowLayout.setVisibility(View.GONE);
        } else
        {
            isShowMore = true;
            mScrollView.setVisibility(View.GONE);
            mMoreText.setText("查看更多");
            mTagFlowLayout.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 前往全区热门视频界面
     */
    @OnClick(R.id.ic_quanzi_layout)
    void startQuanziActivity()
    {

        startActivity(new Intent(getActivity(), AllHotVideoActivity.class));
    }


    /**
     * 前往话题中心界面
     */
    @OnClick(R.id.topic_center_layout)
    void startTopicCenterActivity()
    {

        startActivity(new Intent(getActivity(), TopicCenterActivity.class));
    }

    /**
     * 前往活动中心界面
     */
    @OnClick(R.id.activity_center_layout)
    void startActivityCenterActivity()
    {

    }

    /**
     * 前往全区排行榜界面
     */
    @OnClick(R.id.layout_all_rank)
    void startAllRankActivity()
    {

        startActivity(new Intent(getActivity(), AllareasRankActivity.class));
    }

    /**
     * 前往原创排行榜界面
     */
    @OnClick(R.id.layout_original)
    void startOriginalRankActivity()
    {

        startActivity(new Intent(getActivity(), OriginalRankActivity.class));
    }

    /**
     * 前往游戏中心界面
     */
    @OnClick(R.id.layout_game_center)
    void startGameCenterActivity()
    {

        startActivity(new Intent(getActivity(), GameCentreActivity.class));
    }

    /**
     * 前往搜索界面
     */
    @OnClick(R.id.card_view)
    void startSearchActivity()
    {

        startActivity(new Intent(getActivity(), TotalStationSearchActivity.class));
    }

    @Override
    protected void lazyLoad()
    {

    }
}
