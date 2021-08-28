@file:Suppress("FunctionName")

package com.github.zimolab.jsarray.demo.advanced

import com.github.zimolab.jsarray.JsArray
import com.github.zimolab.jsarray.JsArrayIteratorCallback
import com.github.zimolab.jsarray.demo.OnPageTest
import com.github.zimolab.jsarray.execute
import javafx.concurrent.Worker
import javafx.scene.layout.Priority
import javafx.scene.web.WebEngine
import kotlinx.html.hr
import kotlinx.html.output
import netscape.javascript.JSObject
import tornadofx.*

class MultiTypesDemoView: View("多类型元素与稀疏数组测试") {
    private val url = javaClass.getResource("/index.html")!!.toExternalForm()
    override val root = vbox {
        webview {
            vgrow = Priority.ALWAYS
            engine.loadWorker.stateProperty().addListener { _,_, state->
                println(state)
                if (state == Worker.State.SUCCEEDED) {
                    //doTest_AsAnyType(engine)
                    doTest_asIntType(engine)
                }
            }
            engine.load(url)
        }
    }

    private fun doTest_AsAnyType(engine: WebEngine) {
        val rawArray = engine.execute("multi_types_array") as JSObject
        val anyJsArray = JsArray.anyArrayOf(rawArray)

        OnPageTest(engine).apply {
            test(anyJsArray, "forEach()") {
                snippet {
                    "" +
                            "anyJsArray.forEach { index, value ->\n" +
                            "    print(\"\"\"index: \$index, value: \$value  (type: \${value?.let { it::class }})\"\"\")\n" +
                            "}"
                }
                output {
                    anyJsArray.forEach { index, value ->
                        print("""index: $index, value: $value  (type: ${value?.let { it::class }})""")
                    }
                }
            }
        }
    }

    private fun doTest_asIntType(engine: WebEngine) {
        val rawArray = engine.execute("multi_types_array") as JSObject
        val intJsArray = JsArray.intArrayOf(rawArray)
        OnPageTest(engine).apply {
            test(intJsArray, "get()/getAny()") {
                snippet {
                    "" +
                            "// 下面的代码将引发异常\n" +
                            "// val a = intJsArray[3]\n" +
                            "// print(\"a = \$a\")\n" +
                            "// 使用getAny()\n" +
                            "print(intJsArray[1])\n" +
                            "print(intJsArray.getAny(3))"
                }

                output {
                    // 下面的代码将引发异常
                    // val a = intJsArray[3]
                    // print("a = $a")
                    // 使用getAny()
                    print(intJsArray[1])
                    print(intJsArray.getAny(3))
                }

            }

            test(intJsArray, "forEach()") {
                snippet {
                    "" +
                            "//以下代码将引发类型转换异常，使用下面的方法\n" +
                            "//intJsArray.forEach { index, value ->\n" +
                            "//    print(\"index: \$index, value: \$value\")\n" +
                            "//}"
                }
                snippet {
                    "intJsArray.forEach(object : JsArrayIteratorCallback<Int?, Unit>{\n" +
                            "    override fun call(currentValue: Int?, index: Int, total: Int?, arr: Any?) {\n" +
                            "        print(\"index: \$index, value: \$currentValue, \${currentValue?.let { it::class }}\")\n" +
                            "    }\n" +
                            "})"
                }

                output {
                    intJsArray.forEach(object : JsArrayIteratorCallback<Int?, Unit>{
                        override fun call(currentValue: Int?, index: Int, total: Int?, arr: Any?) {
                            print("index: $index, value: $currentValue, ${currentValue?.let { it::class }}")
                        }
                    })
                    hr()
                }

                snippet {
                    "" +
                            "intJsArray.forEach {(index, value)->\n" +
                            "    print(\"index: \$index, value: \$value, \${value?.let { it::class }}\")\n" +
                            "}"
                }

                output {
                    intJsArray.forEach {(index, value)->
                        print("index: $index, value: $value, ${value?.let { it::class }}")
                    }
                }
            }
        }
    }
}