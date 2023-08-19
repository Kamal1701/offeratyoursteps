package com.example.offersatyoursteps.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.adapters.OfferAdapter
import com.example.offersatyoursteps.databinding.FragmentAllOffersBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.All_OFFERS_TITLE
import com.example.offersatyoursteps.utilities.PRODUCT_DETAIL_TITLE
import com.example.offersatyoursteps.utilities.PRODUCT_INFO_TABLE
import com.example.offersatyoursteps.utilities.SPAN_COUNT
import com.example.offersatyoursteps.utilities.USER_INFO


class AllOffersFragment : Fragment() {
    // TODO: Rename and change types of parameters
    
    private lateinit var binding : FragmentAllOffersBinding
    private lateinit var recProgressBar : ProgressBar
    private lateinit var noOfferToday:TextView
    private var productList : MutableList<OfferProductDetails> = mutableListOf()
    
    private var userModel = UserModel("", "", "", "", "", "","", "","")
    private lateinit var backPressedCallback : OnBackPressedCallback
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userModel = it.getParcelable<UserModel>(USER_INFO)!!
        }
    }
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View {
        // Inflate the layout for this fragment
        binding = FragmentAllOffersBinding.inflate(inflater, container, false)
        recProgressBar = binding.recProgressBar
        noOfferToday = binding.noAllOfferProduct
        recProgressBar.visibility = View.VISIBLE
        noOfferToday.visibility = View.GONE
        
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = All_OFFERS_TITLE
        
        return binding.root
    }
    
    companion object {
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            AllOffersFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                }
            }
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        DatabaseServices.getAllProductDetails(PRODUCT_INFO_TABLE, productList) { isGetComplete ->
            if (isGetComplete) {
                recProgressBar.visibility = View.INVISIBLE
                val itemAdapter =
                    OfferAdapter(this.requireContext(), productList) { productDetail ->
                        val fragment = ProductDetailsFragment.newInstance(userModel, productDetail)
                        requireActivity().supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(R.id.nav_host_fragment_content_home_page, fragment)
                            addToBackStack(null)
                        }
                    }
                val offerRecycleView = binding.offerAllRecycleView
                offerRecycleView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
                offerRecycleView.adapter = itemAdapter
                offerRecycleView.adapter.let {
                    if(itemAdapter.itemCount == 0){
                        noOfferToday.visibility = View.VISIBLE
                    } else {
                        noOfferToday.visibility = View.INVISIBLE
                    }
                }
            } else {
                recProgressBar.visibility = View.INVISIBLE
                Log.d("DEBUG", "OfferNearMe - no record returned")
            }
        }
    
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.popBackStack("NearMe",0)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }
    
    override fun onResume() {
        super.onResume()
        if(productList.isNotEmpty()){
            productList.clear()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }
}