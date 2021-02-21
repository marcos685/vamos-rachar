package com.example.vamosrachar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    lateinit var editValor: EditText
    lateinit var editPessoas: EditText
    lateinit var btShare: FloatingActionButton
    lateinit var btTTS: FloatingActionButton
    lateinit var resultField: TextView
    lateinit var ttsPlayer: TextToSpeech
    var pessoas: Int? = 1
    var valor: Double? = 0.0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editValor = findViewById(R.id.editValor)
        editPessoas = findViewById(R.id.editPessoas)
        btShare = findViewById(R.id.floatingShare)
        btTTS = findViewById(R.id.floatingTTS)
        resultField = findViewById(R.id.textView)

        editValor.addTextChangedListener(this)
        editPessoas.addTextChangedListener(this)

        btShare.setOnClickListener(this)
        btTTS.setOnClickListener(this)

        val ttsIntent = Intent()
        ttsIntent.action = TextToSpeech.Engine.ACTION_CHECK_TTS_DATA
        startActivityForResult(ttsIntent, 1122)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1122) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                ttsPlayer = TextToSpeech(this, this)
            } else {
                val installTTS = Intent()
                installTTS.action = (TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
                startActivity(installTTS)
            }
        }
    }



    override fun afterTextChanged(s: Editable?) {
        val df = DecimalFormat("#.00")

        pessoas =  editPessoas.text.toString().toIntOrNull()
        valor = editValor.text.toString().toDoubleOrNull()

        if(pessoas?:0 > 0 && valor != null) {
            resultField.text = "R$"+ df.format((valor?:0.0)/(pessoas?:1).toDouble())
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

    }

    override fun onClick(v: View?) {
        if (v==btShare){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, R.string.result_conta.toString() + resultField.text.toString())
            startActivity(intent)
        }

        if (v==btTTS) {
            if (ttsPlayer !=null ) {
                ttsPlayer.speak(R.string.result_conta.toString()+ resultField.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "ID1" )
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            Log.v("pdm", "sucesso")
        } else if (status == TextToSpeech.ERROR){
            Log.v("pdm", "deu ruim...")
        }
    }
}