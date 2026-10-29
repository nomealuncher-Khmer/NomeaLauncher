package com.nomea.launcher

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private val black = Color.rgb(12, 12, 14)
    private val red = Color.rgb(235, 35, 55)
    private val white = Color.WHITE
    private val gray = Color.rgb(170, 170, 180)

    private lateinit var root: LinearLayout
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = black
        window.navigationBarColor = black

        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(black)
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(20, 24, 20, 20)
        }

        val logo = TextView(this).apply {
            text = "NOMEA"
            textSize = 36f
            setTextColor(red)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "MINECRAFT JAVA LAUNCHER"
            textSize = 12f
            setTextColor(white)
            gravity = Gravity.CENTER
        }

        header.addView(logo)
        header.addView(subtitle)

        root.addView(
            header,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        val scroll = ScrollView(this)

        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 12, 22, 24)
        }

        scroll.addView(content)

        root.addView(
            scroll,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        setContentView(root)
        showHome()
    }

    private fun showHome() {
        content.removeAllViews()

        addText(
            "WELCOME TO NOMEA",
            23f,
            red,
            true
        )

        addText(
            "Your Minecraft journey starts here.",
            14f,
            gray
        )

        addText(
            "JAVA EDITION",
            14f,
            white,
            true
        )

        addButton("▶  PLAY MINECRAFT") {
            openMinecraft()
        }

        addButton("▣  VERSIONS") {
            showPage(
                "VERSIONS",
                "Manage Minecraft versions here.",
                "Version management will be added in a future update."
            )
        }

        addButton("✦  MODS") {
            showPage(
                "MODS",
                "Manage your Minecraft mods.",
                "Mod installation will be added in a future update."
            )
        }

        addButton("⚙  SETTINGS") {
            showPage(
                "SETTINGS",
                "Nomea Launcher Settings",
                "Theme: Black & Red\nApp Version: 1.0"
            )
        }

        addButton("👤  ACCOUNT") {
            showPage(
                "ACCOUNT",
                "Player Account",
                "Official Microsoft sign-in has not been implemented yet."
            )
        }

        addButton("←  ABOUT") {
            showPage(
                "ABOUT NOMEA",
                "NOMEA LAUNCHER",
                "Version 1.0\nBlack & Red Gaming Launcher"
            )
        }

        addText(
            "NOMEA  •  BLACK & RED",
            12f,
            red,
            true
        )
    }

    private fun showPage(
        title: String,
        subtitle: String,
        description: String
    ) {
        content.removeAllViews()

        addText(title, 25f, red, true)
        addText(subtitle, 16f, white, true)
        addText(description, 14f, gray)

        addButton("←  BACK TO HOME") {
            showHome()
        }
    }

    private fun addText(
        value: String,
        size: Float,
        color: Int,
        bold: Boolean = false
    ) {
        val text = TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
            setPadding(8, 12, 8, 12)

            if (bold) {
                typeface = Typeface.DEFAULT_BOLD
            }
        }

        content.addView(
            text,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
    }

    private fun addButton(
        label: String,
        action: () -> Unit
    ) {
        val button = Button(this).apply {
            text = label
            textSize = 15f
            setTextColor(white)
            isAllCaps = false
            setBackgroundColor(red)
            setOnClickListener { action() }
        }

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            58
        )

        params.setMargins(0, 8, 0, 8)
        content.addView(button, params)
    }

    private fun openMinecraft() {
        val packages = listOf(
            "net.kdt.pojavlaunch",
            "net.kdt.pojavlaunch.debug"
        )

        for (packageName in packages) {
            val intent =
                packageManager.getLaunchIntentForPackage(packageName)

            if (intent != null) {
                startActivity(intent)
                return
            }
        }

        Toast.makeText(
            this,
            "Please install a compatible Minecraft Java launcher first.",
            Toast.LENGTH_LONG
        ).show()
    }
}
