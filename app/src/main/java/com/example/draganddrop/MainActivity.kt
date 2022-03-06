package com.example.draganddrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent

import android.widget.Toast

import android.widget.LinearLayout

import android.view.ViewGroup

import android.widget.TextView

import android.view.View.OnDragListener

import android.content.ClipData

import android.content.ClipDescription
import android.util.Log
import android.view.View

import android.view.View.OnLongClickListener
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources.getDrawable

class MainActivity : AppCompatActivity() {
    private var mImg: ImageView? = null
    private val IMAGEVIEW_TAG = "드래그 이미지"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mImg = findViewById<View>(R.id.image) as ImageView
        mImg!!.tag = IMAGEVIEW_TAG
        mImg!!.setOnLongClickListener(LongClickListener())
        findViewById<View>(R.id.toplinear).setOnDragListener(
            DragListener()
        )
        findViewById<View>(R.id.bottomlinear).setOnDragListener(
            DragListener()
        )
    }

    private class LongClickListener : OnLongClickListener {
        override fun onLongClick(view: View): Boolean {
            // 태그 생성
            val item = ClipData.Item(
                view.tag as CharSequence
            )
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(view.tag.toString(), mimeTypes, item)
            val shadowBuilder = View.DragShadowBuilder(view)
            view.startDrag(
                data,  // data to be dragged
                shadowBuilder,  // drag shadow
                view,  // 드래그 드랍할  Vew
                0 // 필요없은 플래그
            )
            view.visibility = View.INVISIBLE
            return true
        }

    }

    internal class DragListener : OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            val normalShape = getDrawable(v.context, R.drawable.normal_shape)!!
            val targetShape = getDrawable(v.context, R.drawable.target_shape)
            // 이벤트 시작
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> Log.d("DragClickListener", "ACTION_DRAG_STARTED")
                DragEvent.ACTION_DRAG_ENTERED -> {
                    Log.d("DragClickListener", "ACTION_DRAG_ENTERED")
                    // 이미지가 들어왔다는 것을 알려주기 위해 배경이미지 변경
                    v.background = targetShape
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    Log.d("DragClickListener", "ACTION_DRAG_EXITED")
                    v.background = normalShape
                }
                DragEvent.ACTION_DROP -> {
                    Log.d("DragClickListener", "ACTION_DROP")
                    when (v) {
                        v.findViewById<View>(R.id.bottomlinear) -> {
                            val view: View = event.localState as View
                            val viewgroup = view
                                .parent as ViewGroup
                            viewgroup.removeView(view)

                            // change the text
                            val text = v.findViewById(R.id.text) as TextView
                            text.text = "이미지가 드랍되었습니다."
                            val containView = v as LinearLayout
                            containView.addView(view)
                            view.visibility = View.VISIBLE
                        }
                        v.findViewById<View>(R.id.toplinear) -> {
                            val view: View = event.localState as View
                            val viewgroup = view
                                .parent as ViewGroup
                            viewgroup.removeView(view)
                            val containView = v as LinearLayout
                            containView.addView(view)
                            view.visibility = View.VISIBLE
                        }
                        else -> {
                            val view: View = event.localState as View
                            view.visibility = View.VISIBLE
                        }
                    }
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    Log.d("DragClickListener", "ACTION_DRAG_ENDED")
                    v.background = normalShape // go back to normal shape
                }
            }
            return true
        }
    }
}