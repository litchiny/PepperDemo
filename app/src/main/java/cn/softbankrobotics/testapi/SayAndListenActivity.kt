package cn.softbankrobotics.testapi

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.BodyLanguageOption
import com.aldebaran.qi.sdk.`object`.conversation.Phrase
import com.aldebaran.qi.sdk.`object`.locale.Language
import com.aldebaran.qi.sdk.`object`.locale.Locale
import com.aldebaran.qi.sdk.`object`.locale.Region
import com.aldebaran.qi.sdk.builder.ListenBuilder
import com.aldebaran.qi.sdk.builder.PhraseSetBuilder
import com.aldebaran.qi.sdk.builder.SayBuilder


class SayAndListenActivity : BaseActivity() {
    override fun init() {
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
//        initSay(qiContext)
        startListenAndSay(qiContext)
    }

    private fun startListenAndSay(qiContext: QiContext?) {
        val phraseSet = PhraseSetBuilder.with(qiContext)
                .withTexts("你叫什么", "你的名字是什么")
                .build()

        val listen = ListenBuilder.with(qiContext)
                .withPhraseSet(phraseSet)
                .withPhraseSets()
                .build()

        val futureListen = listen.async().run()
        futureListen.thenConsume {
            startSay(qiContext)
        }
    }

    private fun startSay(qiContext: QiContext?) {
        val say = SayBuilder.with(qiContext)
                .withResource(R.string.hello)
                .build()

        val futureSay = say.async().run()
        futureSay.thenConsume {
            //继续写Listen...
        }
    }

    private fun initListen1(qiContext: QiContext?) {
        val listen = ListenBuilder.with(qiContext)
                .withBodyLanguageOption(BodyLanguageOption.NEUTRAL)
                .build()
        val futureListen = listen.async().run()
        //Listen 开始的监听
        listen.addOnStartedListener {

        }

        futureListen.requestCancellation()
        // Listen过程被强制打断时的操作,一般是在onRobotFocusLost中调用
        // listen.removeAllOnStartedListeners()
        // futureListen.cancel(true)
    }

    private fun initSay1(qiContext: QiContext?) {
        val say = SayBuilder.with(qiContext)
                .withText("你好呀，我是Pepper")
                .build()

        say.async().run()
    }

    private fun initSay2(qiContext: QiContext?) {
        val say = SayBuilder.with(qiContext)
                .withResource(R.string.hello)
                .build()

        say.async().run()
    }

    override fun getResLayoutId(): Int {
        return R.layout.activity_say_and_listen
    }


}
