package cn.softbankrobotics.testapi.chat

import android.os.Handler
import android.util.Log
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.*
import com.aldebaran.qi.sdk.`object`.locale.Locale

class MyChatBot(internal var qiContext: QiContext) : BaseChatbot(qiContext) {

    //处理回复的语义和其他逻辑
    override fun replyTo(phrase: Phrase?, locale: Locale): StandardReplyReaction {
        val isNotReply = phrase == null || phrase.text.isNullOrEmpty()
        var str = ""
        Handler().postDelayed({

        }, 10000)

        val reaction = MyChatbotReaction(qiContext, str)
        return StandardReplyReaction(reaction, ReplyPriority.NORMAL)
    }

    override fun acknowledgeHeard(phrase: Phrase?, locale: Locale?) {
        super.acknowledgeHeard(phrase, locale)
        Log.d(TAG, "acknowledgeHeard: phrase.text: ${phrase?.text}")
    }

    override fun acknowledgeSaid(phrase: Phrase?, locale: Locale?) {
        super.acknowledgeSaid(phrase, locale)
        Log.d(TAG, "acknowledgeSaid: phrase.text: ${phrase?.text}")
    }

    companion object {
        val TAG = "MyChatBot-L"
    }
}
