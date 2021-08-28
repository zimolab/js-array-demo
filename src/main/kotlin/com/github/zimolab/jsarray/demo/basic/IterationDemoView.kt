package com.github.zimolab.jsarray.demo.basic

import com.github.zimolab.jsarray.JsArray
import com.github.zimolab.jsarray.TypedIteratorCallback
import com.github.zimolab.jsarray.demo.OnPageTest
import javafx.concurrent.Worker
import javafx.scene.layout.Priority
import javafx.scene.web.WebEngine
import kotlinx.html.hr
import netscape.javascript.JSObject
import tornadofx.*

class IterationDemoView : View("迭代相关API测试") {
    private val url = javaClass.getResource("/index.html")!!.toExternalForm()
    override val root = vbox {
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

            test(jsArray, "forEach()") {
                snippet {
                    print("使用JsArrayIteratorCallback")
                    "" +
                            "jsArray.forEach(object : TypedIteratorCallback<Int?, Unit>{\n" +
                            "    override fun call(currentValue: Int?, index: Int, total: Int?, arr: Any?) {\n" +
                            "        print(\"index: \$index, value: \$currentValue\")\n" +
                            "    }\n" +
                            "})"
                }
                output {
                    jsArray.forEach(object : TypedIteratorCallback<Int?, Unit>{
                        override fun call(currentValue: Int?, index: Int, total: Int?, arr: Any?) {
                            print("index: $index, value: $currentValue")
                        }
                    })
                }
                hr()
                snippet {
                    print("使用lambda函数")
                    "" +
                            "jsArray.forEach { index, value ->\n" +
                            "    print(\"index: \$index, value: \$value\")\n" +
                            "}"
                }
                output {
                    jsArray.forEach { index, value ->
                        print("index: $index, value: $value")
                    }
                }
            }

            test(jsArray, "forEachAny()") {
                output {
                    jsArray.findAny { (index, value)->
                        value is Int && value >= 3
                    }
                }
            }
        }
    }
}
