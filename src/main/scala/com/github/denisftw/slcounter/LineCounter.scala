package com.github.denisftw.slcounter

import java.io.File
import scala.io.Source
import scala.collection.mutable.Map
import java.text.DecimalFormat


object LineCounter 
{
    val formatter = new DecimalFormat("##.##")
    
	def process(file: File, sourceCounter: Map[String, Int]) {
	    val keys = sourceCounter.keys.toArray
	    
	    def countLines(value: File, key: String): (String, Int) = {
            val count = Source.fromFile(value).getLines().size + sourceCounter(key)
            (key -> count)
	    }
	    
        if (file.isDirectory()) {
            file.listFiles().foreach( sub => process(sub, sourceCounter))
        } 
        else {
            val results = keys.map { 
                key => if (file.getName().endsWith(key)) Some(countLines(file, key)) else None 
            }
	  	    results.foreach(opt => opt.foreach( x => sourceCounter += x))
        }
	}
    
    def main(args: Array[String]) {
        val rootDir = new File("D:/Workspace/Bitbucket/dictionary/app")
        if (!rootDir.isDirectory())
            throw new RuntimeException("Not a directory!")
        
        val sourceCounter = Map("scala" -> 0, "js" -> 0, "html" -> 0, "less" -> 0)
        
        rootDir.listFiles().foreach {
            file => process(file, sourceCounter)
        }
        val total = sourceCounter.values.foldLeft(0)( (x,y) => x+y )
        val list = sourceCounter.keys.map {
            x => x + ": " + sourceCounter(x) + " (" + formatter.format(sourceCounter(x) * 100.0 / total)  + "%)"
        }
        list.foreach( x => println(x) )
        println("total: " + total)
    }
}
