package com.ecnu.leon.jarvis.model.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.model.task.TaskManager;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    TextView incomeFloatTextView;
    TextView outcomeFloatTextView;
    TextView differenceFloatTextView;

    TextView todayCostFloatTextView;
    TextView todayCostTextView;

    RelativeLayout todayCostRelativeLayout;
    RelativeLayout incomeRelativeLayout;

    static final int ACTION_ADD_INCOME_DIALOG = 0;



    private OnFragmentInteractionListener mListener;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountFragment.newInstance();


    }

    @Override
    public void onPause() {
        super.onPause();
        AccountManager.getInstance(getContext()).saveContent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initView(view);
        // Inflate the layout for this fragment
        return view;

    }

    public void initView(View view) {
        Button button = (Button) view.findViewById(R.id.btn_account_add_newCount);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountAddActivity.class);
                startActivity(intent);
            }
        });


        incomeFloatTextView = (TextView) view.findViewById(R.id.txt_account_income_number_inThisMonth);
        outcomeFloatTextView = (TextView) view.findViewById(R.id.txt_account_outcome_number_inThisMonth);
        differenceFloatTextView = (TextView) view.findViewById(R.id.txt_account_difference_number_inThisMonth);

        todayCostTextView = (TextView) view.findViewById(R.id.txt_account_today_cost);
        todayCostFloatTextView = (TextView) view.findViewById(R.id.txt_account_today_cost_value);


        todayCostRelativeLayout = (RelativeLayout) view.findViewById(R.id.rlayout_account_today_cost);
        todayCostRelativeLayout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                AccountDetailsActivity.startActivity(getContext());


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();


        float totalIncome = AccountManager.getInstance(getActivity()).getTotalIncomeValue();
        incomeFloatTextView.setText(totalIncome + "");

        outcomeFloatTextView.setText("当月支出");
        outcomeFloatTextView.setText(AccountManager.getInstance(getActivity()).getMonthlyCost(new Date()) + "");
        float difference = AccountManager.getInstance(getContext()).getTotalIncomeValue() - AccountManager.getInstance(getContext()).getTotalCost();
        // 取2位精度
        difference = ((float) (Math.round(difference * 100)) / 100);

        differenceFloatTextView.setText(difference + "");
        if (AccountManager.getInstance(getContext()).getOneDayCostNumber(new Date()) != 0)
        {
            todayCostTextView.setText("今日支出" + AccountManager.getInstance(getContext()).getOneDayCostNumber(new Date()) + "笔");
            todayCostFloatTextView.setText("" + AccountManager.getInstance(getContext()).getOneDayCost(new Date()));
            todayCostFloatTextView.setTextColor(Color.RED);
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
