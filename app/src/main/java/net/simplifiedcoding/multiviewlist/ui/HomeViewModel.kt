package net.simplifiedcoding.multiviewlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import net.simplifiedcoding.multiviewlist.data.network.Resource
import net.simplifiedcoding.multiviewlist.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _homeListItemsLiveData = MutableLiveData<Resource<List<HomeRecyclerViewItem>>>()
    val homeListItemsLiveData: LiveData<Resource<List<HomeRecyclerViewItem>>>
        get() = _homeListItemsLiveData

    init {
        getHomeListItems()
    }

    private fun getHomeListItems() = viewModelScope.launch {
        _homeListItemsLiveData.postValue(Resource.Loading)
        val moviesDef = async { repository.getMovies() }
        val directorsDef = async { repository.getDirectors() }

        val movies = moviesDef.await()
        val directors = directorsDef.await()

        val homeItemsList = mutableListOf<HomeRecyclerViewItem>()
        if (movies is Resource.Success && directors is Resource.Success) {
            homeItemsList.add(HomeRecyclerViewItem.Title(1, "Recommended Movies"))
            homeItemsList.addAll(movies.value)
            homeItemsList.add(HomeRecyclerViewItem.Title(2, "The Directors"))
            homeItemsList.addAll(directors.value)
            _homeListItemsLiveData.postValue(Resource.Success(homeItemsList))
        } else {
            Resource.Failure(false, null, null)
        }
    }

}