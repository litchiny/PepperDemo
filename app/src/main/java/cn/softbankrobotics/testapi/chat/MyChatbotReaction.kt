package cn.softbankrobotics.testapi.chat

import com.aldebaran.qi.Future
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.aldebaran.qi.sdk.`object`.conversation.BaseChatbotReaction
import com.aldebaran.qi.sdk.`object`.conversation.Say
import com.aldebaran.qi.sdk.`object`.conversation.SpeechEngine

class MyChatbotReaction internal constructor(context: QiContext, private val answer: String) : BaseChatbotReaction(context) {
    private var fSay: Future<Void>? = null

    override fun runWith(speechEngine: SpeechEngine) {
        if (answer.isEmpty()) return
        val say = SayBuilder.with(speechEngine).withText(answer).build()
        fSay = say.async().run()
        try {
            fSay?.get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stop() {
        fSay?.cancel(true)
        fSay?.requestCancellation()
    }
}
