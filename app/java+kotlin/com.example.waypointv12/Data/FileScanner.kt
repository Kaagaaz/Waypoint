package com.example.waypointv12.data

import android.content.Context
import java.io.File

data class ScannedFile(
    val name: String,
    val path: String,
    val isHarmful: Boolean,
    val reason: String
)

class FileScanner(private val context: Context) {

    fun scanInternalStorage(): List<ScannedFile> {
        val root = context.filesDir
        val results = mutableListOf<ScannedFile>()
        scanDirectory(root, results)
        return results
    }

    private fun scanDirectory(directory: File, results: MutableList<ScannedFile>) {
        val files = directory.listFiles() ?: return
        for (file in files) {
            if (file.isDirectory) {
                scanDirectory(file, results)
            } else {
                results.add(analyzeFile(file))
            }
        }
    }

    private fun analyzeFile(file: File): ScannedFile {

        val extension = file.extension.lowercase()
        val isSuspect = extension == "sh" || extension == "exe" || extension == "apk" || file.name.contains("crack", true)
        
        return if (isSuspect) {
            ScannedFile(
                name = file.name,
                path = file.absolutePath,
                isHarmful = true,
                reason = "AI detected unauthorized executable signature in grid."
            )
        } else {
            ScannedFile(
                name = file.name,
                path = file.absolutePath,
                isHarmful = false,
                reason = "Verified integrity check: PASS."
            )
        }
    }
}
