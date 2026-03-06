package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var btnLoad: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate started")

        try {
            setContentView(R.layout.activity_main)
            Log.d(TAG, "setContentView completed")

            // Инициализация View
            btnLoad = findViewById(R.id.btnLoad)
            Log.d(TAG, "btnLoad found")

            recyclerView = findViewById(R.id.recyclerView)
            Log.d(TAG, "recyclerView found")

            tvStatus = findViewById(R.id.tvError)
            Log.d(TAG, "tvError found")

            recyclerView.layoutManager = LinearLayoutManager(this)
            Log.d(TAG, "layoutManager set")

            btnLoad.setOnClickListener {
                Log.d(TAG, "Load button clicked")
                loadData()
            }

            // Для теста сразу показываем сообщение
            tvStatus.text = "Приложение готово. Нажмите кнопку"

            Log.d(TAG, "onCreate completed successfully")

        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadData() {
        Log.d(TAG, "loadData started")

        tvStatus.text = "Загрузка..."
        btnLoad.isEnabled = false

        lifecycleScope.launch {
            try {
                Log.d(TAG, "Starting API call")

                val countries = withContext(Dispatchers.IO) {
                    try {
                        // Простой запрос без лишних параметров
                        RetrofitInstance.api.getAllCountries()
                    } catch (e: Exception) {
                        Log.e(TAG, "API call failed: ${e.message}", e)

                        // Пробуем альтернативный URL если первый не сработал
                        try {
                            val alternativeApi = retrofit2.Retrofit.Builder()
                                .baseUrl("https://restcountries.com/v3.1/")
                                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                                .build()
                                .create(ApiService::class.java)

                            alternativeApi.getAllCountries()
                        } catch (e2: Exception) {
                            Log.e(TAG, "Alternative API also failed", e2)
                            throw e
                        }
                    }
                }

                Log.d(TAG, "API call successful, received ${countries.size} countries")

                tvStatus.text = "Загружено ${countries.size} стран"
                btnLoad.isEnabled = true

                // Показываем первые 10 стран
                val firstTen = countries.take(10)
                showSimpleList(firstTen)

            } catch (e: Exception) {
                Log.e(TAG, "Error in loadData: ${e.message}", e)
                tvStatus.text = "Ошибка: ${e.message}"
                btnLoad.isEnabled = true
                e.printStackTrace()

                // Пока
            }
        }
    }

    private fun showSimpleList(countries: List<Country>) {
        Log.d(TAG, "showSimpleList started with ${countries.size} countries")

        try {
            recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                    Log.d(TAG, "onCreateViewHolder called")
                    val textView = TextView(parent.context)
                    textView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    textView.setPadding(20, 20, 20, 20)
                    textView.textSize = 18f
                    return object : RecyclerView.ViewHolder(textView) {}
                }

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                    val country = countries[position]
                    val textView = holder.itemView as TextView
                    textView.text = "${country.name.common} - ${country.capital?.firstOrNull() ?: "нет столицы"}"
                    Log.d(TAG, "onBindViewHolder for position $position: ${country.name.common}")
                }

                override fun getItemCount() = countries.size
            }
            Log.d(TAG, "Adapter set successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting adapter: ${e.message}", e)
        }
    }
}