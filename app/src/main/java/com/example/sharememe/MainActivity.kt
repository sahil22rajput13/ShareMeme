package com.example.sharememe

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.sharememe.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getData()


    }


    private fun getData() {
        var loadurl: String? = null
        binding.progressbar.visibility = View.VISIBLE
        Retrofitinstance.retrofit.getData().enqueue(object : Callback<ResponseData?> {
            override fun onResponse(call: Call<ResponseData?>, response: Response<ResponseData?>) {
                loadurl = response.body()?.url
                binding.tittle.text = response.body()?.title

                Glide.with(this@MainActivity).load(loadurl).listener(object :
                    RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressbar.visibility = View.VISIBLE
                        return false
                    }


                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressbar.visibility = View.GONE
                        return false
                    }

                }).into(
                    binding
                        .MemeImg
                )

            }


            override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
            }
        })
        binding.sharebtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, "Hey Check this.. $loadurl} ")
            intent.type = "text/plain "
            val chooser = Intent.createChooser(intent, "Share Me")
            startActivity(chooser)
        }
        binding.nextbtn.setOnClickListener { getData() }
    }


}