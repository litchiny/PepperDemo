package cn.softbankrobotics.testapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.aldebaran.qi.sdk.`object`.human.Human
import com.aldebaran.qi.sdk.`object`.humanawareness.HumanAwareness
import com.aldebaran.qi.sdk.builder.EngageHumanBuilder
import com.aldebaran.qi.sdk.`object`.humanawareness.EngageHuman
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.`object`.conversation.Say


class EngageHumanActivity : RobotActivity(), RobotLifecycleCallbacks, View.OnClickListener {
    lateinit var humanAwareness: HumanAwareness
    lateinit var humanList: MutableList<Human>
    lateinit var qiContext: QiContext
    lateinit var recommendedHuman: Human
    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_engage_human) {
            startEngageHuman()
        } else if (v?.id == R.id.btn_engage_human_around) {
            startEngageHumanAround()
        }
    }

    private fun startEngageHumanAround() {
        if (humanList.size > 0) {
            val engageHuman = EngageHumanBuilder.with(qiContext)
                    .withHuman(humanList[0])
                    .buildAsync()
            engageHuman.get().addOnHumanIsDisengagingListener {
                val say = SayBuilder.with(qiContext)
                        .withText("Goodbye!")
                        .build()
                say.run()
                //打招呼
                runOnUiThread {
                    tv_msg.text = "${tv_msg.text} \n ${System.currentTimeMillis()} --- Around"
                }

            }
            engageHuman.get().async().run()
        }
    }

    private fun startEngageHuman() {
        val engageHuman = EngageHumanBuilder.with(qiContext)
                .withHuman(recommendedHuman)
                .buildAsync()
        engageHuman.get().addOnHumanIsDisengagingListener {
            val say = SayBuilder.with(qiContext)
                    .withText("Goodbye!")
                    .build()
            say.run()
            //打招呼
            runOnUiThread {
                tv_msg.text = "${tv_msg.text} \n ${System.currentTimeMillis()}"
            }
        }
        engageHuman.get().async().run()
    }

    lateinit var tv_msg: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this)
        setContentView(R.layout.activity_engage_human)
        tv_msg = findViewById(R.id.tv_msg)
        findViewById<Button>(R.id.btn_engage_human).setOnClickListener(this)
        findViewById<Button>(R.id.btn_engage_human_around).setOnClickListener(this)

    }

    override fun onDestroy() {
        QiSDK.unregister(this)
        super.onDestroy()
    }

    override fun onRobotFocusGained(qiContext: QiContext) {
        this.qiContext = qiContext
        humanAwareness = qiContext.humanAwareness


        humanAwareness.addOnEngagedHumanChangedListener {

        }

        humanAwareness.addOnHumansAroundChangedListener {
            humanList = humanAwareness.humansAround
            Log.e("Litchiny_Human", "humanList: ${humanList.size}")
        }

        humanAwareness.addOnRecommendedHumanToEngageChangedListener {
            recommendedHuman = humanAwareness.engagedHuman
            startEngageHuman()
            Log.e("Litchiny_Human", "recommendedHuman: estimatedAge: ${recommendedHuman.estimatedAge},gender: ${recommendedHuman.estimatedGender}")
        }

        runOnUiThread {
            Toast.makeText(this, "onRobotFocusGained", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRobotFocusLost() {

    }

    override fun onRobotFocusRefused(reason: String) {

    }
}
