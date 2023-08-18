package com.example.offersatyoursteps.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.adapters.OfferAdapter
import com.example.offersatyoursteps.adapters.ProductSubcategoryAdapter
import com.example.offersatyoursteps.utilities.OfferConstants
import com.example.offersatyoursteps.databinding.FragmentOfferNearMeBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.services.Dataservices
import com.example.offersatyoursteps.utilities.SPAN_COUNT
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class OfferNearMeFragment : Fragment() {
    
    private lateinit var userModel : UserModel
    private lateinit var binding : FragmentOfferNearMeBinding
    private var productList : MutableList<OfferProductDetails> = mutableListOf()
    private lateinit var itemAdapter : OfferAdapter
    private lateinit var recProgressBar : ProgressBar
    
    private lateinit var backPressedCallback : OnBackPressedCallback
    
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
            "Product_Details",
            productList,
            userModel.cCity.toString()
        ) { isGetComplete ->
            if (isGetComplete) {
                recProgressBar.visibility = View.INVISIBLE
                itemAdapter =
                    OfferAdapter(this.requireContext(), productList) { productDetail ->
                        val fragment = ProductDetailsFragment.newInstance(productDetail)
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
            } else {
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
    
    
    override fun onResume() {
        super.onResume()
        println("near me on resume outside if")
        if (productList.isNotEmpty()) {
            productList.clear()
            println("near me on resume inside if")
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        backPressedCallback.remove()
    }
    
}