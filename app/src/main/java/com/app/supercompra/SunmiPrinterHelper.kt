package com.app.supercompra

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.sunmi.peripheral.printer.InnerPrinterCallback
import com.sunmi.peripheral.printer.InnerPrinterManager
import com.sunmi.peripheral.printer.SunmiPrinterService

class SunmiPrintHelper(private val context: Context) {

    private var sunmiPrinterService: SunmiPrinterService? = null
    private var isConnecting = false

    private val innerPrinterCallback = object : InnerPrinterCallback() {
        override fun onConnected(service: SunmiPrinterService) {
            sunmiPrinterService = service
            isConnecting = false
            Log.d("SunmiPrintHelper", "Impressora Sunmi Conectada!")
            Toast.makeText(context, "Impressora Sunmi Conectada!", Toast.LENGTH_SHORT).show()
        }

        override fun onDisconnected() {
            sunmiPrinterService = null
            isConnecting = false
            Log.d("SunmiPrintHelper", "Impressora Sunmi Desconectada!")
            Toast.makeText(context, "Impressora Sunmi Desconectada!", Toast.LENGTH_SHORT).show()
        }
    }

    fun initPrinter() {
        if (sunmiPrinterService != null || isConnecting) {
            Log.d(
                "SunmiPrintHelper",
                "Já conectado ou em processo de conexão. Ignorando initPrinter."
            )
            return
        }

        isConnecting = true
        try {
            val result =
                InnerPrinterManager.getInstance().bindService(context, innerPrinterCallback)
            if (result) {
                Log.d("SunmiPrintHelper", "Chamada bindService bem-sucedida.")
            } else {
                Log.e("SunmiPrintHelper", "Falha ao chamar bindService.")
                isConnecting = false
                Toast.makeText(
                    context,
                    "Falha ao iniciar conexão com a impressora.",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isConnecting = false
            Log.e("SunmiPrintHelper", "Erro ao tentar conectar à impressora: ${e.message}")
            Toast.makeText(
                context,
                "Erro ao conectar à impressora: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun deinitPrinter() {
        if (sunmiPrinterService == null) {
            Log.d("SunmiPrintHelper", "Impressora já desconectada. Ignorando deinitPrinter.")
            return
        }

        try {
            InnerPrinterManager.getInstance().unBindService(context, innerPrinterCallback)
            sunmiPrinterService = null
            isConnecting = false
            Log.d("SunmiPrintHelper", "Serviço da impressora desvinculado.")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("SunmiPrintHelper", "Erro ao desconectar da impressora: ${e.message}")
            Toast.makeText(
                context,
                "Erro ao desconectar da impressora: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun printText(text: String) {
        if (sunmiPrinterService == null) {
            Toast.makeText(
                context,
                "Impressora não conectada. Tentando reconectar...",
                Toast.LENGTH_SHORT
            ).show()
            initPrinter()
            return
        }

        try {
            sunmiPrinterService?.printText(text, null)
            sunmiPrinterService?.lineWrap(3, null)
            sunmiPrinterService?.cutPaper(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erro ao imprimir texto: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
    }

    fun printBitmap(bitmap: Bitmap) {
        if (sunmiPrinterService == null) {
            Toast.makeText(
                context,
                "Impressora não conectada. Tentando reconectar...",
                Toast.LENGTH_SHORT
            ).show()
            initPrinter()
            return
        }

        try {
            sunmiPrinterService?.printBitmap(bitmap, null)
            sunmiPrinterService?.lineWrap(3, null)
            sunmiPrinterService?.cutPaper(null)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Erro ao imprimir imagem: ${e.message}", Toast.LENGTH_LONG)
                .show()
        }
    }
}