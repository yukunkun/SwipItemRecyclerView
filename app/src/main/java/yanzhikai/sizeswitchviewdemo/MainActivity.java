package yanzhikai.sizeswitchviewdemo;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.widget.Toast;

import java.util.List;

import yanzhikai.sizeswitchview.BigDirectionKey;
import yanzhikai.sizeswitchview.SizeSwitchView;
import yanzhikai.sizeswitchviewdemo.swipeitem.Main2Activity;

public class MainActivity extends AppCompatActivity implements BigDirectionKey.OnKeyClickListener{
    private SizeSwitchView mSizeSwitchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSizeSwitchView = (SizeSwitchView) findViewById(R.id.ssv);
        mSizeSwitchView.setOnBigKeyClickListener(this);
    }


    @Override
    public void onKeyClick(int index) {
        switch (index){
            case 0:
                mSizeSwitchView.toSmallMode();
                break;
            case 1:
                makeToast("1");
                break;
            case 2:
                makeToast("2");
                Intent intentMain=new Intent(this,Main2Activity.class);
                startActivity(intentMain);
                break;
            case 3:
                makeToast("3");
                Intent intentLV=new Intent(this,ListViewActivity.class);
                startActivity(intentLV);
                break;
            case 4:
                makeToast("4");
                Intent intent=new Intent(this,RecyclerActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void makeToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
