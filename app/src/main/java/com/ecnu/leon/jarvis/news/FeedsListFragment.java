package com.ecnu.leon.jarvis.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cootek.feedsnews.item.FeedsItem;
import com.cootek.feedsnews.util.FeedsConst;
import com.cootek.feedsnews.view.widget.FeedsChannelView;
import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.mylibrary.sdkTestActivity;


public class FeedsListFragment extends Fragment {

    private View mRootView;
    private RelativeLayout mRoot;
    FeedsChannelView mFeedsChannelView;
    ImageView mRedpacketView;


    public FeedsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FeedsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedsListFragment newInstance() {
        FeedsListFragment fragment = new FeedsListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_feeds_list, container, false);
            mRoot = (RelativeLayout) mRootView.findViewById(R.id.container);
            mFeedsChannelView = new FeedsChannelView(getActivity(), getActivity().getIntent());
            mFeedsChannelView.setFeedsChannelDelegate(new FeedsListViewListener());
            mFeedsChannelView.setFrom(FeedsConst.FROM_FEEDS);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            mRoot.addView(mFeedsChannelView, 0, params);
        }

        // Inflate the layout for this fragment
        return mRootView;
    }

    private class FeedsListViewListener implements FeedsChannelView.FeedsChannelDelegate {
        @Override
        public void onFirstAdapterUpdate(@NonNull View view) {

        }

        @Override
        public void getFirstVisibleItemIndex(int i, int i1) {

        }

        @Override
        public void onRefreshComplete(int i, int i1, String s) {

        }

        @Override
        public boolean onFeedsItemClick(int i, int i1, FeedsItem feedsItem) {
            return false;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }


}
