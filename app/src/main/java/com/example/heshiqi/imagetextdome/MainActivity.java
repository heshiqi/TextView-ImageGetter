package com.example.heshiqi.imagetextdome;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.heshiqi.imagetextdome.test.ImageTextView;

public class MainActivity extends AppCompatActivity implements ImageTextView.OnImageClickListener {


//    String jsonContent = "[{\"type\":1,\"value\":\"b|642|531|http://club2.autoimg.cn/g10/M0F/BE/DF/wKgH4FnvDSqABh8uAAEy3eTj0cM238_w642_h531.jpg\"},{\"type\":0,\"value\":\"[br]•后座区车窗的安全开关[br]•电动车窗[br]•外后视镜操作车灯[br]\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g13/M14/C0/4E/wKgH41nvLXuAIQIyAAAE2i3MI4M146_w40_h40.jpg\"},{\"type\":0,\"value\":\"G仪表照明\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g11/M11/BB/26/wKjBzFnvLZeAOr4SAAAEU6Oxtsk589_w40_h40.jpg\"},{\"type\":0,\"value\":\"前雾灯\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g16/M02/BF/07/wKgH5lnvLg6ASCByAAAEPBXEOdk147_w40_h40.jpg\"},{\"type\":0,\"value\":\"停车灯\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g8/M00/B9/BD/wKgHz1nvLhyAPiXNAAAEKy4jO20019_w40_h40.jpg\"},{\"type\":0,\"value\":\"近光灯[br]\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g8/M14/B7/14/wKjBz1nxREGAFkYYAAAEftNfEk8673_w40_h40.jpg\"},{\"type\":0,\"value\":\"自动行车灯控制 自适应弯道灯 远光灯辅助功能[br]\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g13/M10/C0/3A/wKjBylnxRHOAZd7wAAAET63Zah4401_w40_h40.jpg\"},{\"type\":0,\"value\":\"后雾灯[br]\"},{\"type\":1,\"value\":\"b|302|161|http://club2.autoimg.cn/g4/M11/C8/16/wKjB01n23SSAODS7AAAnFux-cBY974_w302_h161.jpg\"},{\"type\":0,\"value\":\"[br]解锁[br]\"},{\"type\":1,\"value\":\"s|18|16|http://club2.autoimg.cn/g15/M13/C3/7B/wKgH1ln23WaAGb7NAAACEU_TEA4259_w18_h16.jpg\"},{\"type\":0,\"value\":\"按压按钮汽车解锁。 迎宾灯、车内灯和车前区照明灯接通。 可以设置如何使汽车解锁。 设置存储在当前使用的遥控器上。[br]1. 设置[br]2. 车门锁[br]3. 开锁：\"}]";
    String jsonContent = "[{\"type\":1,\"value\":\"b|642|531|http://pic49.nipic.com/file/20140927/19617624_230415502002_2.jpg\"},{\"type\":0,\"value\":\"[br][br]\"},{\"type\":1,\"value\":\"b|642|531|http://pic2.ooopic.com/12/22/94/37bOOOPICc7_1024.jpg\"},{\"type\":0,\"value\":\"[br]•后座区车窗的安全开关[br]•电动车窗[br]•外后视镜操作车灯[br]\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g13/M14/C0/4E/wKgH41nvLXuAIQIyAAAE2i3MI4M146_w40_h40.jpg\"},{\"type\":0,\"value\":\"仪表照明\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g11/M11/BB/26/wKjBzFnvLZeAOr4SAAAEU6Oxtsk589_w40_h40.jpg\"},{\"type\":0,\"value\":\"前雾灯\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g16/M02/BF/07/wKgH5lnvLg6ASCByAAAEPBXEOdk147_w40_h40.jpg\"},{\"type\":0,\"value\":\"停车灯\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g8/M00/B9/BD/wKgHz1nvLhyAPiXNAAAEKy4jO20019_w40_h40.jpg\"},{\"type\":0,\"value\":\"近光灯[br]\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g8/M14/B7/14/wKjBz1nxREGAFkYYAAAEftNfEk8673_w40_h40.jpg\"},{\"type\":0,\"value\":\"自动行车灯控制 自适应弯道灯 远光灯辅助功能[br]\"},{\"type\":1,\"value\":\"s|40|40|http://club2.autoimg.cn/g13/M10/C0/3A/wKjBylnxRHOAZd7wAAAET63Zah4401_w40_h40.jpg\"},{\"type\":0,\"value\":\"后雾灯[br]\"},{\"type\":1,\"value\":\"b|302|161|http://club2.autoimg.cn/g4/M11/C8/16/wKjB01n23SSAODS7AAAnFux-cBY974_w302_h161.jpg\"},{\"type\":0,\"value\":\"[br]解锁[br]\"},{\"type\":1,\"value\":\"s|18|16|http://club2.autoimg.cn/g15/M13/C3/7B/wKgH1ln23WaAGb7NAAACEU_TEA4259_w18_h16.jpg\"},{\"type\":0,\"value\":\"按压按钮汽车解锁。 迎宾灯、车内灯和车前区照明灯接通。 可以设置如何使汽车解锁。 设置存储在当前使用的遥控器上。[br]1. 设置[br]2. 车门锁[br]3. 开锁：\"}]";
    private ImageTextView imageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageTextView = (ImageTextView) findViewById(R.id.ImageTextView);
        imageTextView.setOnImageClickListener(this);
        imageTextView.setContent(jsonContent);
        App.activity=this;
    }

    @Override
    public void onImageClick(View view, String sourceUrl) {
        Log.d("hh", sourceUrl);
        Toast.makeText(this, "大图点击:\n" + sourceUrl, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.activity=null;
    }
}
