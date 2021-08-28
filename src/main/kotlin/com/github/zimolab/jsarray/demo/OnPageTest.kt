package com.github.zimolab.jsarray.demo

import com.github.zimolab.jsarray.JsArrayInterface
import com.github.zimolab.jsarray.execute
import com.github.zimolab.jsarray.invoke
import javafx.scene.web.WebEngine
import kotlinx.html.*
import kotlinx.html.dom.append
import netscape.javascript.JSObject
import org.w3c.dom.Document
import org.w3c.dom.Element

class OnPageTest(private val engine: WebEngine) {
    private val doc: Document = engine.document
    private val global = engine.execute("window") as JSObject
    private val titleElement = doc.getElementById("title")!!
    private val contentElement = doc.getElementById("content")!!

    var collapseId = 0

    fun setTitle(title: String) {
        titleElement.textContent = title
    }

    fun DIV.println(vararg s: Any) {
        if (s.isEmpty()) {
            br()
            return
        }
        s.forEach {
            p {
                text(it.toString())
            }
            br()
        }
    }

    fun DIV.print(vararg s: Any) {
        s.forEach {
            p {
                text(it.toString())
            }
        }
    }

    fun DIV.snippet(block: DIV.()->String) {
        div {
            p {
                span {
                    classes = setOf("badge", "rounded-pill", "bg-info", "text-dark")
                    text("Code Snippet")
                }
            }
            code {
                pre {
                    text(this@div.block())
                }
            }
            hr()
        }
    }

    fun DIV.output(block: DIV.() -> Any?) {
        div {
            p {
                span {
                    classes = setOf("badge", "rounded-pill", "bg-info", "text-dark")
                    text("Output")
                }
            }
            val ret = this@div.block()
            if (ret != Unit) {
                p {
                    text(ret.toString())
                }
            }
        }
    }

    fun collapseBlock(id: String, api: String, block: TagConsumer<Element>.()->Unit): List<Element> {
        return (global.invoke("createCollapseElement", id, api) as Element).append {
            block()
        }
    }

    fun footBlock(block: TagConsumer<Element>.()->Unit): List<Element> {
        return contentElement.append{
            block()
        }
    }

    inline fun <reified T> test(array: JsArrayInterface<T>, api: String, crossinline block: DIV.(printer: DIV) -> Any?) {
        collapseId++
        var ret: Any? = Unit
        collapseBlock("collapse-$collapseId", api) {
            p {
                span {
                    classes = setOf("badge", "rounded-pill", "bg-secondary")
                    text("输入：$array")
                }
            }
            div {
                classes = setOf("card")
                div {
                    classes = setOf("card-body")
                    ret = this.block(this)
                }
            }
            if (ret != Unit) {
                p {
                    text("测试结果：$ret")
                }
            }
        }
    }
}