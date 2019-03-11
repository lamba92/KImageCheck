package it.lamba.tests

import it.lamba.utils.getImageData
import it.lamba.utils.getResource
import org.junit.Test
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Tests {

    @Test
    fun testBrokenImage(){
        val file = getResource("truncated.jpg")
        val data = file.getImageData()
        assertTrue { data.isImage }
        assertTrue { data.isTruncated }
    }

    @Test
    fun testGoodImage(){
        val file = getResource("good.png")
        val data = file.getImageData()
        assertTrue { data.isImage }
        assertFalse { data.isTruncated }
    }

    @Test
    fun testNotImage(){
        val file = getResource("nope.txt")
        val data = file.getImageData()
        assertFalse { data.isImage }

        val dir = File(System.getProperty("user.dir"))
        val data2 = dir.getImageData()
        assertFalse { data2.isImage }
    }
}