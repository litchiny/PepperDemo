package cn.softbankrobotics.testapi

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.SayBuilder

class LanguageActivity : BaseActivity() {
    val janStr = "＜司会＞\n" +
            "本日は、ご来場頂きまして、誠にありがとうございます。\n" +
            "次のセミナー講演に先立ちまして、お客様にお願い申し上げます。\n" +
            "携帯電話・時計のアラームなどは周りのお客様のご迷惑にならないよう、音が鳴らない設定になっているか今一度ご確認ください。また、会場内でのご不明点などはお近くのスタッフにお申し付けください。\n" +
            "なお、講演中、非常の際にはスタッフの指示に従って頂きますようお願い申し上げます。\n" +
            "講演開始まで、もうしばらくお待ちください。"
    val chStr = "非常感谢大家前来参加今天的论坛活动。开演前有些注意事项要告知大家。请将手机调至静音模式。\n" +
            "如遇到问题请联系场内就近的工作人员并听从工作人员的安排。\n" +
            "论坛演讲马上就要开始了，请大家稍作等待。"
    protected var qiContext: QiContext? = null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_chinese -> {
                Log.e("Litchiny", "onClick---btn_chinese")
                startSay(Locale(Language.CHINESE, Region.CHINA), chStr)
            }
            R.id.btn_jan -> {
                Log.e("Litchiny", "onClick---btn_jan")
                startSay(Locale(Language.JAPANESE, Region.JAPAN), janStr)
            }
        }
    }

    override fun init() {
        val btn_chinese = findViewById<Button>(R.id.btn_chinese)
        val btn_jan = findViewById<Button>(R.id.btn_jan)

        btn_jan.setOnClickListener(this)
        btn_chinese.setOnClickListener(this)
    }


    override fun getResLayoutId(): Int {
        return R.layout.activity_language
    }


    override fun onRobotFocusGained(qiContext: QiContext?) {
        this.qiContext = qiContext
        runOnUiThread {
            Toast.makeText(this, "onRobotFocusGained", Toast.LENGTH_SHORT).show()
        }
    }


     fun startSay(locale: Locale, text: String) {
        qiContext?.let {
            val say = SayBuilder.with(it).withLocale(locale).withText(text).buildAsync()
            val future = say.get().async().run()
            future.andThenConsume {
                Log.e("Litchiny", "startSay---andThenConsume")
            }
        }
    }
}
