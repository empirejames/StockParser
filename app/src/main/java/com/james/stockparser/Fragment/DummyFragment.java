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
    private Resources res ;

    private static RecyclerView recyclerView;
    private ArrayList eps = new ArrayList();
    private ArrayList guli = new ArrayList();
    private ArrayList gushi = new ArrayList();
    private ArrayList present = new ArrayList();

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
        String [] titleEPS = res.getStringArray(R.array.hisEPS);
        String [] titleGuli = res.getStringArray(R.array.hisGuLi);
        String [] titleGuShi = res.getStringArray(R.array.hisGuShi);


        String[] epsResult = eps.toString().replace("[[", "").replace("]]", "").split(",");
        String[] guliResult = guli.toString().replace("[[", "").replace("]]", "").split(",");
        String[] gushiResult = gushi.toString().replace("[[", "").replace("]]", "").split(",");
        String[] presentResult = present.toString().replace("[[", "").replace("]]", "").split(",");
        ArrayList<String> arrayList = new ArrayList<>();

        //Log.e(TAG,epsResult.length + " - " + titleEPS.length );
        Log.e(TAG,guliResult.length + " - " + titleGuShi.length);
        for (int i=0 ; i<guliResult.length;i++){
            Log.e(TAG,guliResult[i]);
        };
        for (int i=0 ; i<titleGuShi.length;i++){
            Log.e(TAG,titleGuShi[i]);
        };

        //Log.e(TAG,gushiResult.length + " - " + titleGuli.length);



        if (classcal.equals("eps")) {
            for (int j = 3; j < epsResult.length; j++) {
                arrayList.add( titleEPS[j]+ " : " + epsResult[j]);
            }
        } else if (classcal.equals("guli")) {
            for (int j = 3; j < guliResult.length; j++) {
                arrayList.add(titleGuShi[j] + " : " + guliResult[j]);

            }
        } else if (classcal.equals("gushi")) {
            for (int j = 3; j < gushiResult.length; j++) {
                arrayList.add(titleGuli[j] + " : " + gushiResult[j]);
            }
        } else if (classcal.equals("present")) {
        }
        RecyclerView_Adapter adapter = new RecyclerView_Adapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview

    }
}
