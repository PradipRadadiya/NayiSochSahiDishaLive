package com.vimalsagarji.vimalsagarjiapp.activity.competition;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.retrofit.APIClient;
import com.vimalsagarji.vimalsagarjiapp.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WinnerListActivity extends AppCompatActivity {

    private TextView txt_total_comp, txt_total_mark;
    private RecyclerView recycleView_winnerlist;
    LinearLayoutManager linearLayoutManager;
    RecyclerWinnerListAdapter recyclerWinnerListAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_list);
        linearLayoutManager = new LinearLayoutManager(WinnerListActivity.this);
        bindId();

        recycleView_winnerlist = findViewById(R.id.recycleView_winnerlist);
        recycleView_winnerlist.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        String start_date = intent.getStringExtra("start_date");
        String end_date = intent.getStringExtra("end_date");
        String percentage = intent.getStringExtra("percentage");
        String is_visible = intent.getStringExtra("is_visible");
        getWinnerList(start_date, end_date, percentage,"100");
    }

    private void bindId() {
        txt_total_comp = findViewById(R.id.txt_total_comp);
        txt_total_mark = findViewById(R.id.txt_total_mark);
        progressBar = findViewById(R.id.progressBar);
    }

    private void getWinnerList(String from_date, String to_date,String attended_competition, String percentage) {
        ApiInterface apiInterface = APIClient.getClient().create(ApiInterface.class);
        Call<CompetitionWinner> callApi = apiInterface.getCompetitionWinner(from_date, to_date,attended_competition, percentage);
        callApi.enqueue(new Callback<CompetitionWinner>() {
            @Override
            public void onResponse(@NonNull Call<CompetitionWinner> call, @NonNull Response<CompetitionWinner> response) {
                assert response.body() != null;
                Log.e("status", "-----------------" + response.body().getStatus());
                Log.e("total", "-----------------" + response.body().getTotalCompetition());

                if (response.isSuccessful()) {
                    assert response.body() != null;
                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                        progressBar.setVisibility(View.GONE);
                        
                        if (response.body().getData()==null){
                            Toast.makeText(WinnerListActivity.this, "No data found.", Toast.LENGTH_LONG).show();
                        }
                        else {
                            txt_total_comp.setText("Total Competition - " + response.body().getTotalCompetition());
                            txt_total_mark.setText("Total Marks - " + response.body().getTotalMarks());
                            recyclerWinnerListAdapter = new RecyclerWinnerListAdapter(WinnerListActivity.this, response.body().getData());
                            recycleView_winnerlist.setAdapter(recyclerWinnerListAdapter);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CompetitionWinner> call, @NonNull Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(WinnerListActivity.this, R.string.reopen, Toast.LENGTH_SHORT).show();
            }

        });

    }


}
