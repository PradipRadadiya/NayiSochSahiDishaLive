package com.vimalsagarji.vimalsagarjiapp.activity.competition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vimalsagarji.vimalsagarjiapp.R;
import com.vimalsagarji.vimalsagarjiapp.common.CommonMethod;

public class CompetitionFinalResultNote extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_final_result_note);
        final Intent intent=getIntent();

        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_description = findViewById(R.id.txt_description);

        txt_title.setText(CommonMethod.decodeEmoji(intent.getStringExtra("title")));
        txt_description.setText(CommonMethod.decodeEmoji(intent.getStringExtra("description")));

        findViewById(R.id.btn_view_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(CompetitionFinalResultNote.this, WinnerListActivity.class);
                intent1.putExtra("start_date",intent.getStringExtra("start_date"));
                intent1.putExtra("end_date",intent.getStringExtra("end_date"));
                intent1.putExtra("percentage",intent.getStringExtra("percentage"));
                intent1.putExtra("is_visible",intent.getStringExtra("is_visible"));
                startActivity(intent1);
            }
        });

    }
}
