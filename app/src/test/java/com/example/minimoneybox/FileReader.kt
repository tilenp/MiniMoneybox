package com.example.minimoneybox

import java.io.File
import java.io.FileNotFoundException
import java.util.Scanner

object FileReader {

    fun readFile(fileName: String): String {

        val result = StringBuilder()
        val classLoader = FileReader::class.java.classLoader
        val file = File(classLoader?.getResource(fileName)?.file)

        try {
            Scanner(file).use {
                while (it.hasNextLine()) {
                    val line = it.nextLine()
                    result.append(line).append("\n")
                }
            }
            return result.toString()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        throw RuntimeException("Cannot read file $fileName")
    }
}
