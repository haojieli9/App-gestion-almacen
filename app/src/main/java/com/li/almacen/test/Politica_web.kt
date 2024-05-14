package com.li.almacen.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.li.almacen.databinding.ActivityPoliticaWebBinding

class Politica_web : AppCompatActivity() {
    private lateinit var binding: ActivityPoliticaWebBinding
    private val BASE_URL = "<html><body><h1>Lorem Ipsum</h1><p>Lorem ipsum dolor sit amet...</p></body></html>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoliticaWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.webview1.webChromeClient = object : WebChromeClient() {}
        binding.webview1.webViewClient = object : WebViewClient() {}

        val htmlContent = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Política de Privacidad</title>
            </head>
            <body>
                <h1>Recopilación de Información Personal</h1>
                <p>Nuestra aplicación recopila cierta información personal de los usuarios con el fin de proporcionar y mejorar nuestros servicios. Esta información puede incluir, pero no se limita a:</p>
                <ul>
                    <li>Nombre y apellidos</li>
                    <li>Dirección de correo electrónico</li>
                    <li>Ubicación geográfica</li>
                    <li>Información demográfica (como edad, género, etc.)</li>
                    <li>Datos de contacto (como números de teléfono)</li>
                </ul>
                <p>Esta información se recopila de diversas maneras, incluidas las interacciones directas del usuario con la aplicación, el registro de cuentas, formularios de contacto, encuestas y el uso de tecnologías de seguimiento como cookies y píxeles de seguimiento.</p>
                <p>Utilizamos esta información para los siguientes fines:</p>
                <ol>
                    <li>Personalizar la experiencia del usuario y proporcionar contenido y características adaptados a los intereses individuales.</li>
                    <li>Comprender mejor las necesidades y preferencias de nuestros usuarios para mejorar nuestros productos y servicios.</li>
                    <li>Enviar comunicaciones promocionales y de marketing relacionadas con nuestros productos y servicios, siempre que el usuario haya dado su consentimiento para recibir dichas comunicaciones.</li>
                </ol>
            </body>
            </html>
        """.trimIndent()

        binding.webview1.loadData(htmlContent, "text/html", "UTF-8")
    }

    override fun onBackPressed() {
        if (binding.webview1.canGoBack()) {
            binding.webview1.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
