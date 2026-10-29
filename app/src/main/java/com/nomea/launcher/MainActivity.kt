package com.nomea.launcher

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
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

    private val selectedMods = mutableListOf<Uri>()
    private var selectedVersion = "1.21.11"

    companion object {
        private const val PICK_MOD_REQUEST = 101
    }

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

    private fun showHome() {
        content.removeAllViews()

        addText("WELCOME TO NOMEA", 24f, red, true)
        addText("MINECRAFT JAVA LAUNCHER", 14f, white)
        addText("Selected version: $selectedVersion", 16f, gray)

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
            showPage(
                "SETTINGS",
                "Nomea Launcher",
                "Theme: Black & Red\nApp Version: 1.0"
            )
        }

        addButton("👤 ACCOUNT") {
            showPage(
                "ACCOUNT",
                "Player Account",
                "Microsoft sign-in is not implemented yet."
            )
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

    private fun showMods() {
        content.removeAllViews()

        addText("MODS MANAGER", 24f, red, true)
        addText(
            "Selected files: ${selectedMods.size}",
            15f,
            white
        )

        addText(
            "Choose compatible .jar mod files.",
            13f,
            gray
        )

        addButton("+ ADD MOD FILE") {
            pickModFile()
        }

        if (selectedMods.isEmpty()) {
            addText("No mod files selected.", 14f, gray)
        } else {
            selectedMods.forEachIndexed { index, uri ->
                val name = getFileName(uri)

                addText(
                    "${index + 1}. $name",
                    14f,
                    white
                )

                addButton("REMOVE $name") {
                    selectedMods.remove(uri)
                    showMods()
                }
            }
        }

        addText(
            "Note: selecting a file does not install it into Minecraft.",
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

        try {
            startActivityForResult(intent, PICK_MOD_REQUEST)
        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Unable to open file picker.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    @Deprecated("Deprecated by Android API; kept for broad compatibility")
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_MOD_REQUEST &&
            resultCode == RESULT_OK
        ) {
            val uri = data?.data ?: return
            val name = getFileName(uri)

            if (!name.endsWith(".jar", ignoreCase = true)) {
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
                uri,
                null,
                null,
                null,
                null
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

    private fun showPage(
        title: String,
        subtitle: String,
        description: String
    ) {
        content.removeAllViews()

        addText(title, 24f, red, true)
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
            val intent =
                packageManager.getLaunchIntentForPackage(packageName)

            if (intent != null) {
                try {
                    startActivity(intent)
                } catch (_: Exception) {
                    Toast.makeText(
                        this,
                        "Unable to open Minecraft Launcher.",
                        Toast.LENGTH_LONG
                    ).show()
                }
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
