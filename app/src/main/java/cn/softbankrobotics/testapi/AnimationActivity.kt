package cn.softbankrobotics.testapi

import android.util.Log
import android.view.View

import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.builder.AnimateBuilder
import com.aldebaran.qi.sdk.builder.AnimationBuilder
import com.aldebaran.qi.sdk.builder.HolderBuilder
import com.aldebaran.qi.sdk.`object`.actuation.Animate
import com.aldebaran.qi.sdk.`object`.actuation.Animation
import com.aldebaran.qi.sdk.`object`.holder.AutonomousAbilitiesType
import com.aldebaran.qi.sdk.`object`.holder.Holder

import java.util.HashMap
import java.util.concurrent.ExecutionException

import cn.softbankrobotics.testapi.model.AnimModel

class AnimationActivity : BaseActivity() {
    private val modelMap = HashMap<Int, AnimModel>()
    private var startIndex = 0
    private var qiContext: QiContext? = null
    private var holder: Holder? = null
    private var endIndex = 3

    fun onNextAnimation(view: View) {
        if (qiContext == null) return
        try {
            startAnim(modelMap[startIndex]!!.resId)
            startIndex++
            if (startIndex > endIndex) startIndex = 0
            Log.d("Litchiny", "onNextAnimation: startIndex: $startIndex")
        } catch (e: ExecutionException) {
            e.printStackTrace()
        }
    }

    fun onPepperRelease(view: View) {
        if (qiContext == null || holder == null) return
        holder!!.async().release()
    }

    override fun getResLayoutId(): Int {
        return R.layout.activity_animation
    }

    override fun init() {
        startIndex = 0
        modelMap[0] = AnimModel(R.raw.update1)
        modelMap[1] = AnimModel(R.raw.update2)
        modelMap[2] = AnimModel(R.raw.update1_l)
        modelMap[3] = AnimModel(R.raw.update3)
        endIndex = modelMap.size - 1
    }

    override fun onRobotFocusGained(qiContext: QiContext) {
        this.qiContext = qiContext
        runOnUiThread { ToolUtil.showToast(this, "onRobotFocusGained...") }
    }

    private fun initHolder() {
        holder = HolderBuilder.with(qiContext).withAutonomousAbilities(
                AutonomousAbilitiesType.AUTONOMOUS_BLINKING,
                AutonomousAbilitiesType.BACKGROUND_MOVEMENT,
                AutonomousAbilitiesType.BASIC_AWARENESS
        ).build()
        holder!!.async().hold()
    }

    @Throws(ExecutionException::class)
    private fun startAnim(resId: Int) {
        if (holder == null) initHolder()
        val animation = AnimationBuilder.with(qiContext).withResources(resId).buildAsync()
        val animate = AnimateBuilder.with(qiContext).withAnimation(animation.get()).buildAsync()
        animate.get().async().run().thenConsume { animateFuture -> Log.d("Litchiny", "startAnim:thenConsume ") }
    }

}
