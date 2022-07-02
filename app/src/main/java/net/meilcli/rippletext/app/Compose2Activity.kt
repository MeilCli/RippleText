package net.meilcli.rippletext.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.google.android.material.composethemeadapter.MdcTheme
import net.meilcli.rippletext.compose.RippleText

class Compose2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose2)

        val compose = findViewById<ComposeView>(R.id.compose)
        compose.setContent {
            MdcTheme {
                Sample()
            }
        }
    }

    @Composable
    private fun Sample() {
        val text = buildAnnotatedString {
            val addClickableText = {
                val index = pushStringAnnotation(tag = "click", annotation = "clickable")
                pushStyle(SpanStyle(color = Color.Blue))
                append("This text is clickable and effective.")
                pop(index)
            }
            val addNonClickableText = {
                append("This text is not clickable.")
            }
            for (i in 0 until 15) {
                for (j in 0..i) {
                    addNonClickableText()
                }
                append(' ')
                addClickableText()
                append('\n')
            }
        }
        val scrollState = rememberScrollState()
        RippleText(
            text = text,
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState),
            style = MaterialTheme.typography.body1,
            onClick = {
                val annotations = text.getStringAnnotations("click", start = it, end = it)
                for (annotation in annotations) {
                    val clickedText = text.substring(annotation.start, annotation.end)
                    showToast("Clicked: $clickedText, ${annotation.start}~${annotation.end}")
                }
            }
        )
    }
}