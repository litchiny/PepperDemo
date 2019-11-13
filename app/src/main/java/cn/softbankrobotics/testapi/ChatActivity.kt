package cn.softbankrobotics.testapi

import android.os.Bundle
import android.util.Log
import cn.softbankrobotics.testapi.chat.MyChatBot
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.builder.ChatBuilder
import com.aldebaran.qi.sdk.builder.QiChatbotBuilder
import com.aldebaran.qi.sdk.builder.TopicBuilder
import com.aldebaran.qi.sdk.design.activity.RobotActivity

class ChatActivity : RobotActivity(), RobotLifecycleCallbacks {
    override fun onRobotFocusGained(qiContext: QiContext?) {
        initChat(qiContext)
//        initChat2(qiContext)
    }

    private fun initChat(qiContext: QiContext?) {
        val bot = MyChatBot(qiContext!!)
        val chat = ChatBuilder.with(qiContext).withChatbot(bot).build()
        val futureChat = chat.async().run()
                //取消
        //        futureChat.cancel(true)
        //        futureChat.requestCancellation()
    }

    private fun initChat2(qiContext: QiContext?) {
        val topic = TopicBuilder.with(qiContext).withResource(R.raw.hello).build()
        val bot = QiChatbotBuilder.with(qiContext).withTopic(topic).build()
        val chat = ChatBuilder.with(qiContext).withChatbot(bot).build()
        val futureChat = chat.async().run()

        chat.addOnStartedListener {
            Log.e(TAG, "addOnStartedListener")
        }
        //监听所有识别出来的文字内容
        chat.addOnHeardListener {
            Log.e(TAG, "1-addOnHeardListener: it: ${it.text}")
        }
        //监听所有未识别出来
        chat.addOnNoPhraseRecognizedListener {
            Log.e(TAG, "2-addOnNoPhraseRecognizedListener")
        }
        //监听所有已回复的文字内容
        chat.addOnNormalReplyFoundForListener {
            Log.e(TAG, "3-addOnNormalReplyFoundForListener: it: ${it.text}")
        }
        //监听所有没有回复的文字内容
        chat.addOnNoReplyFoundForListener {
            Log.e(TAG, "4-addOnNoReplyFoundForListener: it: ${it.text}")
        }
        //监听所有回复的内容
        chat.addOnSayingChangedListener {
            Log.e(TAG, "5-addOnSayingChangedListener: it: ${it.text}")
        }
        //监听所有听状态的改变
        chat.addOnListeningChangedListener {
            Log.e(TAG, "6-addOnListeningChangedListener: it: $it")
        }
        //监听所有听状态的改变
        chat.addOnHearingChangedListener {
            Log.e(TAG, "7-addOnHearingChangedListener: it: $it")
        }

        //取消
//        futureChat.cancel(true)
//        futureChat.requestCancellation()
    }

    override fun onRobotFocusLost() {
    }

    override fun onRobotFocusRefused(reason: String?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        QiSDK.register(this, this);
    }

    override fun onDestroy() {
        QiSDK.unregister(this)
        super.onDestroy()
    }

    companion object {
        val TAG = "ChatActivity-L"
    }
}
