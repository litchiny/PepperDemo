package cn.softbankrobotics.testapi

import android.util.Log
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.`object`.conversation.BaseQiChatExecutor
import com.aldebaran.qi.sdk.`object`.conversation.QiChatExecutor
import com.aldebaran.qi.sdk.`object`.conversation.QiChatbot
import com.aldebaran.qi.sdk.`object`.conversation.Bookmark
import com.aldebaran.qi.sdk.`object`.conversation.Chat
import com.aldebaran.qi.sdk.`object`.conversation.AutonomousReactionValidity
import com.aldebaran.qi.sdk.`object`.conversation.AutonomousReactionImportance
import com.aldebaran.qi.sdk.`object`.conversation.Topic
import com.aldebaran.qi.sdk.`object`.conversation.BookmarkStatus
import com.aldebaran.qi.sdk.`object`.actuation.Animate
import android.support.annotation.RawRes
import com.aldebaran.qi.sdk.builder.*


class BookMarkActivity : BaseActivity() {
    override fun init() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var qiChatbot: QiChatbot? = null
    private var chat: Chat? = null
    private var proposalBookmark: Bookmark? = null
    private var dogBookmarkStatus: BookmarkStatus? = null
    private var elephantBookmarkStatus: BookmarkStatus? = null

    //设置动作表演的参数以让动作转到对应的标签
    private fun sayProposal() {
        qiChatbot?.goToBookmark(proposalBookmark, AutonomousReactionImportance.HIGH, AutonomousReactionValidity.IMMEDIATE)
    }

    override fun onRobotFocusGained(qiContext: QiContext?) {
//        initBookMark(qiContext)
        startSay(qiContext)
    }

    private fun startSay(qiContext: QiContext?) {
        val say = SayBuilder.with(qiContext).withText("请看我表演小狗呀").build()
        val futureSay = say.async().run()

        //Say和Animation同时进行
        //mimic(R.raw.dog_a001, qiContext!!)

        //Say完成后再Animation
        futureSay.andThenConsume {
            mimic(R.raw.dog_a001, qiContext!!)
        }
        futureSay.requestCancellation()
    }

    private fun initBookMark(qiContext: QiContext?) {
        val topic = TopicBuilder.with(qiContext)
                .withResource(R.raw.mimic_animal)
                .build()
        qiChatbot = QiChatbotBuilder.with(qiContext)
                .withTopic(topic)
                .build()
        chat = ChatBuilder.with(qiContext)
                .withChatbot(qiChatbot)
                .build()

        chat?.addOnStartedListener(this::sayProposal);

        val bookmarks = topic.bookmarks
        //获取top文件内的mimic_proposal
        proposalBookmark = bookmarks["mimic_proposal"]
        val dogBookmark = bookmarks["dog_mimic"]
        val elephantBookmark = bookmarks["elephant_mimic"]

        dogBookmarkStatus = qiChatbot?.bookmarkStatus(dogBookmark)
        elephantBookmarkStatus = qiChatbot?.bookmarkStatus(elephantBookmark)
        //当语音回复成功后开始表演
        dogBookmarkStatus?.addOnReachedListener {
            mimicDog(qiContext!!);
        }
        //当语音回复成功后开始表演
        elephantBookmarkStatus?.addOnReachedListener {
            mimicElephant(qiContext!!)
        }

        chat?.async()?.run();
    }

    override fun onRobotFocusLost() {
        super.onRobotFocusLost()
        chat?.removeAllOnStartedListeners()
        dogBookmarkStatus?.removeAllOnReachedListeners()
        elephantBookmarkStatus?.removeAllOnReachedListeners()
    }

    private fun mimicDog(qiContext: QiContext) {
        Log.i(TAG, "Dog mimic.")
        mimic(R.raw.dog_a001, qiContext)
    }

    private fun mimicElephant(qiContext: QiContext) {
        Log.i(TAG, "Elephant mimic.")
        mimic(R.raw.elephant_a001, qiContext)
    }

    private fun mimic(@RawRes mimicResource: Int?, qiContext: QiContext) {
        val animation = AnimationBuilder.with(qiContext)
                .withResources(mimicResource)
                .build()

        val animate = AnimateBuilder.with(qiContext)
                .withAnimation(animation)
                .build()

        animate.async().run()
    }

    override fun getResLayoutId(): Int {
        return R.layout.activity_discuss
    }
    companion object {
        val TAG = "BookMarkActivity"
    }
}
