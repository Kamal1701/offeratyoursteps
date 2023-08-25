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
import com.example.offersatyoursteps.databinding.FragmentOfferNearMeBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.OFFER_NEAR_ME_TITLE
import com.example.offersatyoursteps.utilities.PRODUCT_INFO_TABLE
import com.example.offersatyoursteps.utilities.SPAN_COUNT
import com.example.offersatyoursteps.utilities.USER_INFO


class OfferNearMeFragment : Fragment() {
    
    private lateinit var userModel : UserModel
    private lateinit var binding : FragmentOfferNearMeBinding
    private var productList : MutableList<OfferProductDetails> = mutableListOf()
    private lateinit var itemAdapter : OfferAdapter
    private lateinit var recProgressBar : ProgressBar
    private lateinit var noOffersToday : TextView
    
    private lateinit var backPressedCallback : OnBackPressedCallback
    
    override fun onSaveInstanceState(outState : Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(USER_INFO, userModel)
    }
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userModel = it?.getParcelable<UserModel>(USER_INFO)!!
        }
        
    }
    
    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentOfferNearMeBinding.inflate(inflater, container, false)
        recProgressBar = binding.recycleProgressBar
        recProgressBar.visibility = View.VISIBLE
        noOffersToday = binding.noOfferProduct
        noOffersToday.visibility = View.INVISIBLE
    
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = OFFER_NEAR_ME_TITLE
        
        return binding.root
    }
    
    companion object {
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            OfferNearMeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                }
            }
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        DatabaseServices.getLocationProductDetails(
            productList,
            userModel.customerCity.toString()
        ) { isGetComplete ->
            if (isGetComplete) {

                itemAdapter =
                    OfferAdapter(this.requireContext(), productList) { productDetail ->
                        val fragment = ProductDetailsFragment.newInstance(userModel, productDetail)
                        requireActivity().supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(R.id.nav_host_fragment_content_home_page, fragment)
                            addToBackStack(null)
                        }
                    }
                val offerRecycleView = binding.offerNearMeRecycleView
                offerRecycleView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
                offerRecycleView.adapter = itemAdapter
                offerRecycleView.setHasFixedSize(true)
                offerRecycleView.adapter.let {
                    if(itemAdapter.itemCount == 0){
                        noOffersToday.visibility = View.VISIBLE
                    } else {
                        noOffersToday.visibility = View.INVISIBLE
                    }
                }
            } else {
                recProgressBar.visibility = View.INVISIBLE
                Log.d("DEBUG", "OfferNearMe - no record returned")
            }
        }
        
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                
                val alertDialog = AlertDialog.Builder(activity)
                    .setTitle("Offers At Your Step")
                    .setMessage("Do you want to exit?")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { _, _ ->
                        requireActivity().finish()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener { _, _ -> })
                    .create()
                alertDialog.show()
                
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )
    }
    
    override fun onViewStateRestored(savedInstanceState : Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null) {
            userModel = savedInstanceState.getParcelable(USER_INFO)!!
        }
    }
    
    override fun onResume() {
        super.onResume()
        if (productList.isNotEmpty()) {
            productList.clear()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }
    
}