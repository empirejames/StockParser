package com.james.stockparser.Fragment;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.stockparser.R;

import java.util.ArrayList;

public class DummyFragment extends Fragment {
    private View view;

    private String title;
    private String classcal;
    private String TAG = DummyFragment.class.getSimpleName();
    private int year;
    private Resources res;

    private static RecyclerView recyclerView;
    private ArrayList eps = new ArrayList();
    private ArrayList guli = new ArrayList();
    private ArrayList gushi = new ArrayList();
    private ArrayList present = new ArrayList();
    String[] epsResult;
    String[] guliResult;
    String[] gushiResult;
    String[] presentResult;

    public DummyFragment() {
    }

    @SuppressLint("ValidFragment")
    public DummyFragment(String title) {
        this.title = title;//Setting tab title
    }

    @SuppressLint("ValidFragment")
    public DummyFragment(int year, String classcal, ArrayList eps, ArrayList guli, ArrayList gushi, ArrayList present) {
        this.year = year;
        this.classcal = classcal;
        this.eps = eps;
        this.guli = guli;
        this.gushi = gushi;
        this.present = present;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dummy, container, false);

        setRecyclerView();
        return view;

    }

    //Setting recycler view
    public void setRecyclerView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//Linear Items
        res = getResources();
        String[] titleEPS = res.getStringArray(R.array.hisEPS);
        String[] titleGuli = res.getStringArray(R.array.hisGuLi);
        String[] titleGuShi = res.getStringArray(R.array.hisGuShi);
        String[] titlePresent = res.getStringArray(R.array.hisPresent);

        epsResult = eps.toString().replace("[[", "").replace("]]", "").split(",");
        guliResult = guli.toString().replace("[[", "").replace("]]", "").split(",");
        gushiResult = gushi.toString().replace("[[", "").replace("]]", "").split(",");
        presentResult = present.toString().replace("[[", "").replace("]]", "").split(",");
        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i < presentResult.length; i++) {
            Log.e(TAG, presentResult[i]);
        }
        if (classcal != null) {
            if (classcal.equals("eps")) {
                if (eps.size() == 0) {
                    arrayList.add("無該股票資料");
                } else {
                    if (titleEPS.length == epsResult.length) {
                        for (int j = 3; j < epsResult.length; j++) {
                            arrayList.add(titleEPS[j] + " : " + epsResult[j]);
                        }
                    } else {
                        arrayList.add("無該股票資料");
                    }
                }
            } else if (classcal.equals("guli")) {
                if (guli.size() == 0) {
                    arrayList.add("無該股票資料");
                } else {
                    if (titleGuShi.length == guliResult.length) {
                        for (int j = 3; j < guliResult.length; j++) {
                            arrayList.add(titleGuShi[j] + " : " + guliResult[j]);

                        }
                    } else {
                        arrayList.add("無該股票資料");
                    }
                }
            } else if (classcal.equals("gushi")) {
                if (gushi.size() == 0) {
                    arrayList.add("無該股票資料");
                } else {
                    if (titleGuli.length == gushiResult.length) {
                        for (int j = 3; j < gushiResult.length; j++) {
                            arrayList.add(titleGuli[j] + " : " + gushiResult[j]);
                        }
                    } else {
                        arrayList.add("無該股票資料");
                    }
                }
            } else if (classcal.equals("present")) {
                if (present.size() == 0) {
                    arrayList.add("無該股票資料");
                } else {
                    if (titlePresent.length == presentResult.length) {
                        arrayList.add(titlePresent[3] + " : " + presentResult[presentResult.length-1]);
                        for (int j = 4; j < presentResult.length; j++) {
                            arrayList.add(titlePresent[j] + " : " + presentResult[j-1]);
                        }
                    } else if(titlePresent.length > presentResult.length){
                        arrayList.add(titlePresent[3] + " : " + presentResult[presentResult.length-1]);
                        for (int j = 4; j < presentResult.length; j++) {
                            arrayList.add(titlePresent[j] + " : " + presentResult[j-1]);
                        }
                    }else{
                        arrayList.add("無該股票資料");
                    }
                }
            }
        }else{
            arrayList.add("無該股票資料");
        }

        RecyclerView_Adapter adapter = new RecyclerView_Adapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);
    }
}
