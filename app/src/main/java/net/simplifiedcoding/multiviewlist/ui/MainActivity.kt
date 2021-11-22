package net.simplifiedcoding.multiviewlist.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import net.simplifiedcoding.multiviewlist.data.network.Resource
import net.simplifiedcoding.multiviewlist.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<HomeViewModel>()
    private val homeAdapter = HomeRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = homeAdapter
        }

        viewModel.homeListItemsLiveData.observe(this) {
            when (it) {
                is Resource.Failure -> binding.progressBar.hide()
                Resource.Loading -> binding.progressBar.show()
                is Resource.Success -> {
                    homeAdapter.items = it.value
                    binding.progressBar.hide()
                }
            }
        }
        homeAdapter.itemClickListener = { view, item, position ->
            val message = when (item) {
                is HomeRecyclerViewItem.Director -> item.name
                is HomeRecyclerViewItem.Movie -> item.title
                is HomeRecyclerViewItem.Title -> item.title
            }
            message?.let { snackbar(it) }
        }
    }
}