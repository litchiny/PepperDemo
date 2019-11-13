package cn.softbankrobotics.testapi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity

abstract class BaseActivity() : RobotActivity(),RobotLifecycleCallbacks, View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getResLayoutId())
        QiSDK.register(this,this)
        init()
    }

    override fun onDestroy() {
        QiSDK.unregister(this)
        super.onDestroy()
    }

    abstract fun getResLayoutId(): Int
    abstract fun init()

    override fun onRobotFocusLost() {

    }

    override fun onRobotFocusRefused(reason: String?) {

    }

    override fun onClick(v: View?) {

    }
}
