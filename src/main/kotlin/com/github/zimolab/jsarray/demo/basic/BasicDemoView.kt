package com.github.zimolab.jsarray.demo.basic

import com.github.zimolab.jsarray.JsArray
import com.github.zimolab.jsarray.demo.OnPageTest
import javafx.concurrent.Worker
import javafx.scene.layout.Priority
import javafx.scene.web.WebEngine
import netscape.javascript.JSObject
import tornadofx.*
import java.lang.Math.random

class BasicDemoView : View("基础API测试") {
    private val url = javaClass.getResource("/index.html")!!.toExternalForm()
    override val root = vbox {
        webview {
            vgrow = Priority.ALWAYS
            engine.loadWorker.stateProperty().addListener { _, _, state ->
                println(state)
                if (state == Worker.State.SUCCEEDED) {
                    println("网页加载成功！")
                    doTest(engine)
                }
            }
            engine.load(url)
        }
    }

    fun doTest(engine: WebEngine) {
        OnPageTest(engine).apply {
            setTitle(title)
            // 从JSObject创建JsArray对象
            val rawObject = engine.executeScript("int_array")
            val intJsArray = JsArray.intArrayOf(rawObject as JSObject)
            test(intJsArray, "toString()") {
                snippet { "intJsArray.toString()" }
                output {
                    intJsArray.toString()
                }
            }

            test(intJsArray, "join()") {
                snippet {
                    "intJsArray.join(\";\")"
                }
                output {
                    intJsArray.join(";")
                }
            }

            test(intJsArray, "fill()&&concat()") {
                snippet {
                    "" +
                            "val intJsArray2 = JsArray.newJsIntArray(rawObject, 10)\n" +
                            "intJsArray2?.fill(10)\n" +
                            "it.println(\"intArray2: \$intJsArray2\")\n" +
                            "intJsArray2?.concat(intJsArray)\n"
                }
                output {
                    val intJsArray2 = JsArray.newJsIntArray(rawObject, 10)
                    intJsArray2?.fill(10)
                    print("intArray2: $intJsArray2")
                    intJsArray2?.concat(intJsArray)
                }
            }

            test(intJsArray, "reverse()") {
                snippet {
                    "" +
                            "val a = intJsArray.reverse()\n" +
                            "print(\"a:\$a\")\n" +
                            "print(\"intJsArray: \$intJsArray\")\n" +
                            "print(\"a==intJsArray: \${a == intJsArray}\")"
                }
                output {
                    val a = intJsArray.reverse()
                    print("a:$a")
                    print("intJsArray: $intJsArray")
                    print("a==intJsArray: ${a == intJsArray}")
                }
            }

            test(intJsArray, "pop()") {
                snippet {
                    "" +
                            "intJsArray.pop()\n" +
                            "print(\"intJsArray: \$intJsArray\")"
                }
                output {
                    intJsArray.pop()
                    print("intJsArray: $intJsArray")
                }
            }

            test(intJsArray, "push()") {
                snippet {
                    "" +
                            "intJsArray.push(-1, -2, -3)\n" +
                            "println(\"intJsArray: \$intJsArray\")"
                }
                output {
                    intJsArray.push(-1, -2, -3)
                    println("intJsArray: $intJsArray")
                }
            }

            test(intJsArray, "shift()") {
                snippet {
                    "" +
                            "intJsArray.shift()\n" +
                            "print(\"intJsArray: \$intJsArray\")"
                }
                output {
                    intJsArray.shift()
                    print("intJsArray: $intJsArray")
                }
            }

            test(intJsArray, "unshift()") {
                snippet {
                    "" +
                            "val data = (0..10).map { (random() * 10).toInt() }\n" +
                            "print(\"data: \$data\")\n" +
                            "intJsArray.unshift(*data.toTypedArray())\n" +
                            "print(\"intJsArray: \$intJsArray\")"
                }

                output {
                    val data = (0..10).map { (random() * 10).toInt() }
                    print("data: $data")
                    intJsArray.unshift(*data.toTypedArray())
                    print("intJsArray: $intJsArray")
                }
            }

            test(intJsArray, "slice()") {
                snippet {
                    "" +
                            "val ret = intJsArray.slice(2)\n" +
                            "print(\"ret: \$ret\")\n" +
                            "println(\"intJsArray: \$intJsArray\")"
                }
                output {
                    val ret = intJsArray.slice(2)
                    print("ret: $ret")
                    print("intJsArray: $intJsArray")
                }

            }

            test(intJsArray, "splice()") {
                snippet {
                    "" +
                            "val ret = intJsArray.splice(2, 5)\n" +
                            "print(\"ret: \$ret\")\n" +
                            "print(\"intJsArray: \$intJsArray\")"
                }

                output {
                    val ret = intJsArray.splice(2, 5)
                    print("ret: $ret")
                    print("intJsArray: $intJsArray")
                }

            }

            test(intJsArray, "include()") {
                snippet {
                    "" +
                            "print(\"intJsArray: \$intJsArray\")\n" +
                            "intJsArray.includes(0)"
                }
                output {
                    print("intJsArray: $intJsArray")
                    intJsArray.includes(0)
                }
            }

            test(intJsArray, "indexOf()") {
                snippet {
                    "" +
                            "print(\"intJsArray: \$intJsArray\")\n" +
                            "val index = intJsArray.indexOf(0)\n" +
                            "print(\"index: \$index\")"
                }
                output {
                    print("intJsArray: $intJsArray")
                    val index = intJsArray.indexOf(0)
                    print("index: $index")
                }
            }

            test(intJsArray, "lastIndexOf()") {
                snippet {
                    "" +
                            "print(\"intJsArray: \$intJsArray\")\n" +
                            "val index = intJsArray.lastIndexOf(0)\n" +
                            "print(\"index: \$index\")\n"
                }
                output {
                    print("intJsArray: $intJsArray")
                    val index = intJsArray.lastIndexOf(0)
                    print("index: $index")

                }
            }

        }

    }
}
