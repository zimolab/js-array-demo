package com.github.zimolab.jsarray.demo.basic

import com.github.zimolab.jsarray.JsArray
import com.github.zimolab.jsarray.demo.OnPageTest
import javafx.concurrent.Worker
import javafx.scene.layout.Priority
import javafx.scene.web.WebEngine
import netscape.javascript.JSObject
import tornadofx.*

class IterationDemoView : View("迭代相关API测试") {
    private val url = javaClass.getResource("/index.html")!!.toExternalForm()
    override val root = vbox {
        this.alignment
        webview {
            vgrow = Priority.ALWAYS
            engine.loadWorker.stateProperty().addListener { _,_, state->
                println(state)
                if (state == Worker.State.SUCCEEDED) {
                    doTest(engine)
                }
            }
            engine.load(url)
        }
    }

    fun doTest(engine: WebEngine) {
        OnPageTest(engine).apply {
            setTitle(title)
            val raw = engine.executeScript("int_array") as JSObject
            val jsArray = JsArray.intArrayOf(raw)

            test(jsArray, "join()") {
                snippet {
                    "jsArray.join(\";\")"
                }
                output {
                    jsArray.join(",")
                }
            }
        }
    }
}
