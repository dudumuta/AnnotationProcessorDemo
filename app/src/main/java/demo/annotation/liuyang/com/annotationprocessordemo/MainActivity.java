package demo.annotation.liuyang.com.annotationprocessordemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import demo.annotation.liuyang.com.appannotation.BindView;
import demo.annotation.liuyang.com.appapi.LCJViewBinder;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.test)
    TextView mTestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LCJViewBinder.bind(this);

        mTestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LCJViewBinder.unBind(this);
    }
}
