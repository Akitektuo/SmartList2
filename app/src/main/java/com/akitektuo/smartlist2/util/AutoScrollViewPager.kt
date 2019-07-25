package com.akitektuo.smartlist2.util

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.animation.Interpolator
import android.widget.Scroller
import androidx.viewpager.widget.ViewPager
import java.lang.ref.WeakReference

class CustomDurationScroller : Scroller {

    var scrollFactor = 1.0

    constructor(context: Context) : super(context)

    constructor(context: Context, interpolator: Interpolator) : super(context, interpolator)

    constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(
        context,
        interpolator,
        flywheel
    )

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, (duration * scrollFactor).toInt())
    }

}

class AutoScrollViewPager : ViewPager {

    companion object {
        private const val SCROLL_WHAT = 0
    }

    private class CustomHandler(autoScrollViewPager: AutoScrollViewPager) : Handler() {

        private val pager = WeakReference(autoScrollViewPager)

        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)

            if (msg?.what != SCROLL_WHAT) {
                return
            }
            with(pager.get()) {
                if (this == null) {
                    return
                }
                scroller?.scrollFactor = autoScrollFactor
                scrollOnce()
                scroller?.scrollFactor = swipeScrollFactor
                sendScrollMessage((interval + scroller?.duration).toLong())
            }
        }

    }

    private val DEFAULT_INTERVAL = 1500
    private val LEFT = 0
    private val RIGHT = 1
    private val SLIDE_BORDER_MODE_NONE = 0
    private val SLIDE_BORDER_MODE_CYCLE = 1
    private val SLIDE_BORDER_MODE_TO_PARENT = 2

    var interval = DEFAULT_INTERVAL
    private var direction = RIGHT
    private var isCycle = true
    private var stopScrollWhenTouch = true
    private var slideBorderMode = SLIDE_BORDER_MODE_NONE
    private var isBorderAnimation = true
    private var autoScrollFactor = 1.0
    private var swipeScrollFactor = 1.0
    private var isAutoScroll = false
    private var isStopByTouch = false
    private var touchX = 0f
    private var downX = 0f
    private var touchY = 0f

    private var handler = CustomHandler(this)
    var scroller: CustomDurationScroller? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    init {
        setViewPagerScroller()
    }

    private fun setViewPagerScroller() {
        try {
            val scrollerField = ViewPager::class.java.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
            val interpolatorField = ViewPager::class.java.getDeclaredField("sInterpolator")
            interpolatorField.isAccessible = true

            scroller = CustomDurationScroller(context, interpolatorField.get(null) as Interpolator)
            scrollerField.set(this, scroller)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startAutoScroll() {
        isAutoScroll = true
        sendScrollMessage((interval + (scroller?.duration ?: 0) / autoScrollFactor * swipeScrollFactor).toLong())
    }

    fun startAutoScroll(delayTimeInMilliseconds: Long) {
        isAutoScroll = true
        sendScrollMessage(delayTimeInMilliseconds)
    }

    fun stopAutoScroll() {
        isAutoScroll = false
        handler.removeMessages(SCROLL_WHAT)
    }

    private fun sendScrollMessage(delayTimeInMilliseconds: Long) {
        handler.removeMessages(SCROLL_WHAT)
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMilliseconds)
    }

    private fun scrollOnce() {
        if (adapter == null) {
            return
        }
        val totalCount = adapter!!.count
        if (totalCount <= 1) {
            return
        }

        val nextItem = if (direction == LEFT) {
            currentItem - 1
        } else {
            currentItem + 1
        }
        if (nextItem < 0 && isCycle) {
            setCurrentItem(totalCount - 1, isBorderAnimation)
        } else if (nextItem == totalCount && isCycle) {
            setCurrentItem(0, isBorderAnimation)
        } else {
            setCurrentItem(nextItem, true)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val action = ev?.actionMasked

        if (stopScrollWhenTouch) {
            if (action == ACTION_DOWN && isAutoScroll) {
                isStopByTouch = true
                stopAutoScroll()
            } else if (ev?.action == ACTION_UP && isStopByTouch) {
                startAutoScroll()
            }
        }

        if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT || slideBorderMode == SLIDE_BORDER_MODE_CYCLE) {
            touchX = ev?.x ?: 0f
            if (ev?.action == ACTION_DOWN) {
                downX = touchX
            }
            val pageCount = if (adapter == null) {
                0
            } else {
                adapter?.count ?: 0
            }
            if ((currentItem == 0 && downX <= touchX) || (currentItem == pageCount - 1 && downX >= touchX)) {
                if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    if (pageCount > 1) {
                        setCurrentItem(pageCount - currentItem - 1, isBorderAnimation)
                    }
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

}

