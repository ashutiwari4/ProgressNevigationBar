package com.progressindicator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.library.NavigationBar;
import com.library.NvTab;

public class MainActivity extends AppCompatActivity implements NavigationBar.OnTabSelected, NavigationBar.OnTabClick {
    private NavigationBar bar;
    private int position = 0;
    private EditText etNumberOfCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumberOfCount = (EditText) findViewById(R.id.et_count);
        bar = (NavigationBar) findViewById(R.id.navBar);
        bar.setOnTabSelected(this);
        bar.setOnTabClick(this);
        setup(true, 4);
    }


    private void setup(boolean reset, int count) {
        if (reset)
            bar.resetItems();
        bar.setTabCount(count);
        bar.animateView(3000);
        bar.setCurrentPosition(position <= 0 ? 0 : position);
    }

    @Override
    public void onTabSelected(int touchPosition, NvTab prev, NvTab nvTab) {
        Toast.makeText(getApplicationContext(), "Selected position: " + touchPosition, Toast.LENGTH_LONG).show();
    }

    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.btn_reset:
                setup(true, 4);
                etNumberOfCount.setText("");
                break;
            case R.id.btn_next:
                bar.setCurrentPosition(++position);
                break;
            case R.id.btn_prev:
                bar.setCurrentPosition(--position);
                break;
            case R.id.btn_submit:
                setup(false, Integer.parseInt(etNumberOfCount.getText().toString()));
                break;
        }
    }

    @Override
    public void onTabClick(int touchPosition, NvTab prev, NvTab nvTab) {
        Toast.makeText(getApplicationContext(), "You clicked on: " + touchPosition, Toast.LENGTH_LONG).show();
    }
}
