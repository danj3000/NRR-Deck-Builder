package org.anrdigital.rebootdeckbuilder.fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.anrdigital.rebootdeckbuilder.R
import org.anrdigital.rebootdeckbuilder.ViewModel.BrowseCardsViewModel
import org.anrdigital.rebootdeckbuilder.ViewModel.FullScreenViewModel
import org.anrdigital.rebootdeckbuilder.adapters.BrowseCardRecyclerViewAdapter
import org.anrdigital.rebootdeckbuilder.db.CardRepository
import org.anrdigital.rebootdeckbuilder.game.Card
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.java.standalone.KoinJavaComponent

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnBrowseCardsClickListener] interface.
 */
class BrowseCardsFragment : Fragment(), SearchView.OnQueryTextListener, OnBrowseCardsClickListener {
    private lateinit var sv: SearchView
    private var mAdapter: BrowseCardRecyclerViewAdapter? = null
    val vm: BrowseCardsViewModel by sharedViewModel()
    val fullVM: FullScreenViewModel by sharedViewModel()
    var cardRepo = KoinJavaComponent.inject(CardRepository::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_browse_cards, container, false)
        setHasOptionsMenu(true)
        vm.init()
        updateTitle()
        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            view.layoutManager = LinearLayoutManager(context)
            mAdapter = BrowseCardRecyclerViewAdapter(vm.cardList, this, cardRepo.value)
            view.adapter = mAdapter
        }
        return view
    }

    private fun updateTitle() {
        activity?.title = getString(R.string.title_browse_cards) + " [" + vm.browseFormat.name + "]"
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if(isVisible) {
            vm.searchText = newText
            updateResults()
        }
        return true
    }

    private fun updateResults() {
        updateTitle()
        vm.doSearch(vm.searchText)
        mAdapter!!.notifyDataSetChanged()
    }

    fun notifyDataUpdated() {
        updateResults()
    }

    // on list card clicked
    override fun onCardClicked(card: Card, position: Int) {
        sv.clearFocus()
        fullVM.cardCodes = vm.cardList!!.codes
        fullVM.position = position
        val action = BrowseCardsFragmentDirections.actionBrowseCardsFragmentToFullscreenCardsFragment2()
        val navController = NavHostFragment.findNavController(this)
        navController.navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.browse, menu)

        val filterItem = menu.findItem(R.id.action_filter)
        filterItem.setOnMenuItemClickListener {
            val choosePacksDlg = ChoosePacksDialogFragment(vm.packFilter, vm.browseFormat, true )
            fragmentManager?.let { it1 -> choosePacksDlg.show(it1, "choosePacks") }
            false
        }

        sv = menu.findItem(R.id.action_search).actionView as SearchView
        sv.setOnQueryTextListener(this)
        sv.maxWidth = Integer.MAX_VALUE
        if (vm.searchText != null && vm.searchText != "")
        {
            sv.isIconified = false

            sv.setQuery(vm.searchText, false)
            sv.clearFocus()
        }

    }


}