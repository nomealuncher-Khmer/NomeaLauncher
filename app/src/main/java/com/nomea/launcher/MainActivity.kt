package com.nomea.launcher

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button

class MainActivity : Activity() {

    private val black = Color.rgb(15, 15, 15)
    private val red = Color.rgb(255, 45, 65)
    private val white = Color.WHITE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = black
        window.navigationBarColor = black

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 30, 28, 30)
            setBackgroundColor(black)
        }

        val title = TextView(this).apply {
            text = "NOMEA"
            textSize = 38f
            setTextColor(red)
            gravity = Gravity.CENTER
            setTypeface(null, android.graphics.Typeface.BOLD)
        }

        root.addView(
            title,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        val subtitle = TextView(this).apply {
            text = "MINECRAFT JAVA LAUNCHER"
            textSize = 13f
            setTextColor(white)
            gravity = Gravity.CENTER
        }

        root.addView(subtitle)

        addButton(root, "▶  PLAY MINECRAFT")
        addButton(root, "▣  VERSIONS")
        addButton(root, "✦  MODS")
        addButton(root, "⚙  SETTINGS")

        setContentView(root)
    }

    private fun addButton(
        root: LinearLayout,
        label: String
    ) {
        val button = Button(this).apply {
            text = label
            textSize = 16f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(190, 20, 45))
            isAllCaps = false
        }

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            58
        )

        params.setMargins(0, 18, 0, 0)
        root.addView(button, params)
    }
}
