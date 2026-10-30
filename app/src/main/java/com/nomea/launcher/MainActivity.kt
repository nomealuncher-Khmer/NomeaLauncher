package com.nomea.launcher

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.util.UUID

class MainActivity : Activity() {

    private val black = Color.rgb(12, 12, 14)
    private val white = Color.WHITE
    private val gray = Color.LTGRAY

    private var red = Color.rgb(235, 35, 55)

    private lateinit var content: LinearLayout

    private var selectedVersion = "1.21.11"
    private var selectedRam = 2048
    private var playerName = ""

    private val selectedMods = mutableListOf<Uri>()

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

    companion object {
        private const val PICK_MOD_REQUEST = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getPreferences(MODE_PRIVATE)

        selectedVersion = prefs.getString(
            "selected_version", "1.21.11"
        ) ?: "1.21.11"

        selectedRam = prefs.getInt("ram", 2048)

        playerName = prefs.getString(
            "player_name", ""
        ) ?: ""

        val theme = prefs.getString("theme", "RED")

        red = if (theme == "PURPLE") {
            Color.rgb(170, 70, 255)
        } else {
            Color.rgb(235, 35, 55)
        }

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
        color: Int,
        bold: Boolean = false
    ) {
        val text = TextView(this).apply {
            this.text = value
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
            setPadding(8, 12, 8, 12)

            if (bold) {
                typeface = Typeface.DEFAULT_BOLD
            }
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

    // HOME

    private fun showHome() {
        content.removeAllViews()

        addText("WELCOME TO NOMEA", 24f, red, true)
        addText("MINECRAFT JAVA LAUNCHER", 14f, white)
        addText("Version: $selectedVersion", 16f, gray)
        addText("RAM: ${selectedRam}MB", 14f, gray)

        addText(
            if (playerName.isEmpty()) {
                "Player: Not set"
            } else {
                "Player: $playerName"
            },
            15f,
            white
        )

        addButton("▶ PLAY MINECRAFT") {
            openMinecraft()
        }

        addButton("▣ VERSIONS") {
            showVersions()
        }

        addButton("✦ MODS") {
            showMods()
        }

        addButton("⚙ SETTINGS") {
            showSettings()
        }

        addButton("👤 ACCOUNT") {
            showAccount()
        }

        addButton("ℹ ABOUT") {
            showPage(
                "ABOUT NOMEA",
                "NOMEA LAUNCHER",
                "Version 1.0\nBlack & Red Gaming Launcher"
            )
        }

        addText("NOMEA • BLACK & RED", 12f, red, true)
    }

    // VERSIONS

    private fun showVersions() {
        content.removeAllViews()

        addText("MINECRAFT VERSIONS", 23f, red, true)
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

                showVersions()
            }
        }

        addButton("← BACK TO HOME") {
            showHome()
        }
    }

    // MODS

    private fun showMods() {
        content.removeAllViews()

        addText("MODS MANAGER", 24f, red, true)

        addText(
            "Selected files: ${selectedMods.size}",
            15f,
            white
        )

        addButton("+ ADD MOD FILE") {
            pickModFile()
        }

        if (selectedMods.isEmpty()) {
            addText("No mod files selected.", 14f, gray)
        } else {
            selectedMods.forEachIndexed { index, uri ->

                val name = getFileName(uri)

                addText("${index + 1}. $name", 14f, white)

                addButton("REMOVE $name") {
                    selectedMods.remove(uri)
                    showMods()
                }
            }
        }

        addText(
            "Selecting a file does not install it into Minecraft.",
            12f,
            gray
        )

        addButton("← BACK TO HOME") {
            showHome()
        }
    }

    private fun pickModFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }

        startActivityForResult(intent, PICK_MOD_REQUEST)
    }

    @Deprecated("Deprecated by Android API")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (
            requestCode == PICK_MOD_REQUEST &&
            resultCode == RESULT_OK
        ) {
            val uri = data?.data ?: return
            val name = getFileName(uri)

            if (!name.endsWith(".jar", true)) {
                Toast.makeText(
                    this,
                    "Please select a .jar file.",
                    Toast.LENGTH_LONG
                ).show()
                return
            }

            if (!selectedMods.contains(uri)) {
                selectedMods.add(uri)
            }

            showMods()
        }
    }

    private fun getFileName(uri: Uri): String {
        var name = uri.lastPathSegment ?: "Unknown file"

        try {
            contentResolver.query(
                uri, null, null, null, null
            )?.use { cursor ->

                val index = cursor.getColumnIndex(
                    android.provider.OpenableColumns.DISPLAY_NAME
                )

                if (cursor.moveToFirst() && index >= 0) {
                    name = cursor.getString(index) ?: name
                }
            }
        } catch (_: Exception) {
        }

        return name
    }

    // SETTINGS

    private fun showSettings() {
        content.removeAllViews()

        addText("⚙ SETTINGS", 25f, red, true)
        addText("RAM SETTINGS", 20f, white, true)

        addText("Current RAM: ${selectedRam}MB", 16f, gray)

        listOf(512, 1024, 2048, 4096).forEach { value ->

            addButton(
                if (selectedRam == value) {
                    "✓ ${value}MB RAM"
                } else {
                    "${value}MB RAM"
                }
            ) {
                selectedRam = value

                getPreferences(MODE_PRIVATE)
                    .edit()
                    .putInt("ram", value)
                    .apply()

                showSettings()
            }
        }

        addText("THEME SETTINGS", 20f, white, true)

        addButton("🔴 RED THEME") {
            changeTheme("RED")
        }

        addButton("🟣 PURPLE THEME") {
            changeTheme("PURPLE")
        }

        addText(
            "RAM setting is saved in this app. It is not connected to Minecraft yet.",
            12f,
            gray
        )

        addButton("← BACK TO HOME") {
            showHome()
        }
    }

    private fun changeTheme(theme: String) {
        red = if (theme == "PURPLE") {
            Color.rgb(170, 70, 255)
        } else {
            Color.rgb(235, 35, 55)
        }

        getPreferences(MODE_PRIVATE)
            .edit()
            .putString("theme", theme)
            .apply()

        showSettings()
    }

    // ACCOUNT

    private fun showAccount() {
        content.removeAllViews()

        addText("👤 ACCOUNT", 25f, red, true)

        addText(
            "PLAYER PROFILE",
            18f
