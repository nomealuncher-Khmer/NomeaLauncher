package com.nomea.launcher

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
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
    private val gray = Color.LTGRAY

    private lateinit var content: LinearLayout

    private val versions = listOf(
        "1.21.11",
        "1.21.10",
        "1.21.8",
        "1.21.4",
        "1.20.4",
        "1.19.4",
        "1.18.2",
        "1.16.5"
    )

    private var selectedVersion = "1.21.11"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedVersion = getPreferences(MODE_PRIVATE)
            .getString("selected_version", "1.21.11")
            ?: "1.21.11"

        window.statusBarColor = black
        window.navigationBarColor = black

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(black)
        }

        val header = TextView(this).apply {
            text = "✦ NOMEA ✦"
            textSize = 32f
            setTextColor(red)
            typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setPadding(12, 24, 12, 24)
        }

        root.addView(header)

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

    private fun addText(
        value: String,
        size: Float,
        color: Int
    ) {
        val text = TextView(this).apply {
            this.text = value
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
            setPadding(8, 12, 8, 12)
        }

        content.addView(text)
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

    private fun showHome() {
        content.removeAllViews()

        addText("WELCOME TO NOMEA", 24f, red)
        addText("MINECRAFT JAVA LAUNCHER", 14f, white)
        addText("Selected version: $selectedVersion", 16f, gray)

        addButton("▶ PLAY MINECRAFT") {
            openMinecraft()
        }

        addButton("▣ VERSIONS") {
            showVersions()
        }

        addButton("✦ MODS") {
            showPage(
                "MODS",
                "Mod management",
                "Mod installation is not available yet."
            )
        }

        addButton("⚙ SETTINGS") {
            showPage(
                "SETTINGS",
                "Nomea Launcher",
                "Theme: Black & Red\nVersion: 1.0"
            )
        }

        addButton("👤 ACCOUNT") {
            showPage(
                "ACCOUNT",
                "Player account",
                "Official Microsoft sign-in is not implemented yet."
            )
        }

        addText("NOMEA • BLACK & RED", 12f, red)
    }

    private fun showVersions() {
        content.removeAllViews()

        addText("MINECRAFT VERSIONS", 23f, red)
        addText("Choose a version", 15f, white)
        addText("Current: $selectedVersion", 16f, gray)

        versions.forEach { version ->
            addButton(
                if (version == selectedVersion) {
                    "✓ Minecraft $version"
                } else {
                    "Minecraft $version"
                }
            ) {
                selectedVersion = version

                getPreferences(MODE_PRIVATE)
                    .edit()
                    .putString("selected_version", version)
                    .apply()

                Toast.makeText(
                    this,
                    "Selected $version",
                    Toast.LENGTH_SHORT
                ).show()

                showVersions()
            }
        }

        addButton("← BACK TO HOME") {
            showHome()
        }
    }

    private fun showPage(
        title: String,
        subtitle: String,
        description: String
    ) {
        content.removeAllViews()

        addText(title, 24f, red)
        addText(subtitle, 17f, white)
        addText(description, 14f, gray)

        addButton("← BACK TO HOME") {
            showHome()
        }
    }

    private fun openMinecraft() {
        val packages = listOf(
            "net.kdt.pojavlaunch",
            "net.kdt.pojavlaunch.debug"
        )

        for (packageName in packages) {
            val intent: Intent? =
                packageManager.getLaunchIntentForPackage(packageName)

            if (intent != null) {
                startActivity(intent)
                return
            }
        }

        Toast.makeText(
            this,
            "Install a compatible Minecraft Java launcher first.",
            Toast.LENGTH_LONG
        ).show()
    }
}
