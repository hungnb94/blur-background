package com.hb.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import leoh.screenshot.protector.extension.activity

private const val TAG = "WindowFocusLayout"

class WindowFocusLayout
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0,
        defStyleRes: Int = 0,
    ) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
        override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
            super.onWindowFocusChanged(hasWindowFocus)
            Log.d(TAG, "onWindowFocusChanged: $hasWindowFocus")
        }

        override fun drawableStateChanged() {
            super.drawableStateChanged()
            Log.d(TAG, "drawableStateChanged: ")
        }

        override fun refreshDrawableState() {
            super.refreshDrawableState()
            Log.d(TAG, "refreshDrawableState: ")
        }

        override fun childDrawableStateChanged(child: View) {
            super.childDrawableStateChanged(child)
            Log.d(TAG, "childDrawableStateChanged: ${hasWindowFocus()} $child")
        }

        override fun dispatchDraw(canvas: Canvas) {
            Log.d(TAG, "dispatchDraw: $activity $canvas ${hasWindowFocus()} ${hasFocusable()}")
            super.dispatchDraw(canvas)
        }

        override fun jumpDrawablesToCurrentState() {
            super.jumpDrawablesToCurrentState()
            Log.d(TAG, "jumpDrawablesToCurrentState")
        }

        override fun getBackground(): Drawable? {
            Log.d(TAG, "getBackground")
            return super.getBackground()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            Log.d(TAG, "onDraw: $canvas")
        }

        override fun draw(canvas: Canvas) {
            super.draw(canvas)
            Log.d(TAG, "draw: $canvas")
        }

        override fun drawChild(
            canvas: Canvas,
            child: View?,
            drawingTime: Long,
        ): Boolean {
            Log.d(TAG, "drawChild: $child")
            return super.drawChild(canvas, child, drawingTime)
        }

        override fun getDrawingCache(): Bitmap {
            Log.d(TAG, "getDrawingCache")
            return super.getDrawingCache()
        }

        override fun getDrawingCache(autoScale: Boolean): Bitmap? {
            Log.d(TAG, "getDrawingCache: $autoScale ${hasWindowFocus()} ${hasFocus()}")
            return super.getDrawingCache(autoScale)
        }

        override fun getForeground(): Drawable? {
            Log.d(TAG, "getForeground: ")
            return super.getForeground()
        }

        override fun invalidateDrawable(drawable: Drawable) {
            super.invalidateDrawable(drawable)
            Log.d(TAG, "invalidateDrawable: $drawable")
        }

        override fun scheduleDrawable(
            who: Drawable,
            what: Runnable,
            `when`: Long,
        ) {
            super.scheduleDrawable(who, what, `when`)
            Log.d(TAG, "scheduleDrawable: $who")
        }

        override fun unscheduleDrawable(
            who: Drawable,
            what: Runnable,
        ) {
            super.unscheduleDrawable(who, what)
            Log.d(TAG, "unscheduleDrawable: $who")
        }

        override fun unscheduleDrawable(who: Drawable?) {
            super.unscheduleDrawable(who)
            Log.d(TAG, "unscheduleDrawable: $who")
        }

        override fun setVerticalScrollbarThumbDrawable(drawable: Drawable?) {
            super.setVerticalScrollbarThumbDrawable(drawable)
            Log.d(TAG, "setVerticalScrollbarThumbDrawable: $drawable")
        }

        override fun setVerticalScrollbarTrackDrawable(drawable: Drawable?) {
            super.setVerticalScrollbarTrackDrawable(drawable)
            Log.d(TAG, "setVerticalScrollbarTrackDrawable: ")
        }

        override fun setHorizontalScrollbarThumbDrawable(drawable: Drawable?) {
            super.setHorizontalScrollbarThumbDrawable(drawable)
            Log.d(TAG, "setHorizontalScrollbarThumbDrawable: ")
        }

        override fun setHorizontalScrollbarTrackDrawable(drawable: Drawable?) {
            super.setHorizontalScrollbarTrackDrawable(drawable)
            Log.d(TAG, "setHorizontalScrollbarTrackDrawable: ")
        }

        override fun getVerticalScrollbarThumbDrawable(): Drawable? {
            return super.getVerticalScrollbarThumbDrawable()
            Log.d(TAG, "getVerticalScrollbarThumbDrawable: ")
        }

        override fun getVerticalScrollbarTrackDrawable(): Drawable? {
            return super.getVerticalScrollbarTrackDrawable()
            Log.d(TAG, "getVerticalScrollbarTrackDrawable: ")
        }

        override fun getHorizontalScrollbarThumbDrawable(): Drawable? {
            return super.getHorizontalScrollbarThumbDrawable()
            Log.d(TAG, "getHorizontalScrollbarThumbDrawable: ")
        }

        override fun getHorizontalScrollbarTrackDrawable(): Drawable? {
            return super.getHorizontalScrollbarTrackDrawable()
            Log.d(TAG, "getHorizontalScrollbarTrackDrawable: ")
        }

        override fun getDrawingCacheQuality(): Int {
            Log.d(TAG, "getDrawingCacheQuality: ")
            return super.getDrawingCacheQuality()
        }

        override fun setDrawingCacheQuality(quality: Int) {
            super.setDrawingCacheQuality(quality)
            Log.d(TAG, "setDrawingCacheQuality: ")
        }

        override fun setWillNotDraw(willNotDraw: Boolean) {
            super.setWillNotDraw(willNotDraw)
            Log.d(TAG, "setWillNotDraw: ")
        }

        override fun willNotDraw(): Boolean {
            Log.d(TAG, "willNotDraw: ")
            return super.willNotDraw()
        }

        override fun setWillNotCacheDrawing(willNotCacheDrawing: Boolean) {
            super.setWillNotCacheDrawing(willNotCacheDrawing)
            Log.d(TAG, "setWillNotCacheDrawing: ")
        }

        override fun willNotCacheDrawing(): Boolean {
            Log.d(TAG, "willNotCacheDrawing: ")
            return super.willNotCacheDrawing()
        }

        override fun getDrawingRect(outRect: Rect?) {
            super.getDrawingRect(outRect)
            Log.d(TAG, "getDrawingRect: ")
        }

        override fun getDrawingTime(): Long {
            Log.d(TAG, "getDrawingTime: ")
            return super.getDrawingTime()
        }

        override fun setDrawingCacheEnabled(enabled: Boolean) {
            super.setDrawingCacheEnabled(enabled)
            Log.d(TAG, "setDrawingCacheEnabled: ")
        }

        override fun isDrawingCacheEnabled(): Boolean {
            Log.d(TAG, "isDrawingCacheEnabled: ")
            return super.isDrawingCacheEnabled()
        }

        override fun destroyDrawingCache() {
            super.destroyDrawingCache()
            Log.d(TAG, "destroyDrawingCache: ")
        }

        override fun setDrawingCacheBackgroundColor(color: Int) {
            super.setDrawingCacheBackgroundColor(color)
            Log.d(TAG, "setDrawingCacheBackgroundColor: ")
        }

        override fun getDrawingCacheBackgroundColor(): Int {
            Log.d(TAG, "getDrawingCacheBackgroundColor: ")
            return super.getDrawingCacheBackgroundColor()
        }

        override fun buildDrawingCache() {
            super.buildDrawingCache()
            Log.d(TAG, "buildDrawingCache: ")
        }

        override fun buildDrawingCache(autoScale: Boolean) {
            super.buildDrawingCache(autoScale)
            Log.d(TAG, "buildDrawingCache: ")
        }

        override fun verifyDrawable(who: Drawable): Boolean {
            Log.d(TAG, "verifyDrawable: ")
            return super.verifyDrawable(who)
        }

        override fun drawableHotspotChanged(
            x: Float,
            y: Float,
        ) {
            super.drawableHotspotChanged(x, y)
            Log.d(TAG, "drawableHotspotChanged: ")
        }

        override fun setBackground(background: Drawable?) {
            super.setBackground(background)
            Log.d(TAG, "setBackground: ")
        }

        override fun setBackgroundDrawable(background: Drawable?) {
            super.setBackgroundDrawable(background)
            Log.d(TAG, "setBackgroundDrawable: ")
        }

        override fun setForeground(foreground: Drawable?) {
            super.setForeground(foreground)
            Log.d(TAG, "setForeground: ")
        }

        override fun onDrawForeground(canvas: Canvas) {
            super.onDrawForeground(canvas)
            Log.d(TAG, "onDrawForeground: ")
        }

        override fun getUniqueDrawingId(): Long {
            Log.d(TAG, "getUniqueDrawingId: ")
            return super.getUniqueDrawingId()
        }

        override fun dispatchDrawableHotspotChanged(
            x: Float,
            y: Float,
        ) {
            super.dispatchDrawableHotspotChanged(x, y)
            Log.d(TAG, "dispatchDrawableHotspotChanged: ")
        }

        override fun onCreateDrawableState(extraSpace: Int): IntArray {
            return super.onCreateDrawableState(extraSpace)
            Log.d(TAG, "onCreateDrawableState: ")
        }

        override fun setChildrenDrawingCacheEnabled(enabled: Boolean) {
            super.setChildrenDrawingCacheEnabled(enabled)
            Log.d(TAG, "setChildrenDrawingCacheEnabled: ")
        }

        override fun getChildDrawingOrder(
            childCount: Int,
            drawingPosition: Int,
        ): Int {
            Log.d(TAG, "getChildDrawingOrder: ")
            return super.getChildDrawingOrder(childCount, drawingPosition)
        }

        override fun isAlwaysDrawnWithCacheEnabled(): Boolean {
            Log.d(TAG, "isAlwaysDrawnWithCacheEnabled: ")
            return super.isAlwaysDrawnWithCacheEnabled()
        }

        override fun setAlwaysDrawnWithCacheEnabled(always: Boolean) {
            super.setAlwaysDrawnWithCacheEnabled(always)
            Log.d(TAG, "setAlwaysDrawnWithCacheEnabled: ")
        }

        override fun isChildrenDrawnWithCacheEnabled(): Boolean {
            Log.d(TAG, "isChildrenDrawnWithCacheEnabled: ")
            return super.isChildrenDrawnWithCacheEnabled()
        }

        override fun setChildrenDrawnWithCacheEnabled(enabled: Boolean) {
            super.setChildrenDrawnWithCacheEnabled(enabled)
            Log.d(TAG, "setChildrenDrawnWithCacheEnabled: ")
        }

        override fun isChildrenDrawingOrderEnabled(): Boolean {
            Log.d(TAG, "isChildrenDrawingOrderEnabled: ")
            return super.isChildrenDrawingOrderEnabled()
        }

        override fun setChildrenDrawingOrderEnabled(enabled: Boolean) {
            super.setChildrenDrawingOrderEnabled(enabled)
            Log.d(TAG, "setChildrenDrawingOrderEnabled: ")
        }

        override fun getPersistentDrawingCache(): Int {
            Log.d(TAG, "getPersistentDrawingCache: ")
            return super.getPersistentDrawingCache()
        }

        override fun setPersistentDrawingCache(drawingCacheToKeep: Int) {
            super.setPersistentDrawingCache(drawingCacheToKeep)
            Log.d(TAG, "setPersistentDrawingCache: ")
        }
    }
