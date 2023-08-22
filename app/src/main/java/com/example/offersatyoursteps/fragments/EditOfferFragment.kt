package com.example.offersatyoursteps.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.adapters.EditOfferAdapter
import com.example.offersatyoursteps.databinding.FragmentEditOfferBinding
import com.example.offersatyoursteps.models.OfferProductDetails
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.DatabaseServices
import com.example.offersatyoursteps.utilities.EDIT_OFFER_PRODUCT
import com.example.offersatyoursteps.utilities.PRODUCT_INFO_TABLE
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.auth.FirebaseAuth


class EditOfferFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var userModel : UserModel
    private lateinit var binding : FragmentEditOfferBinding
    private var productList : MutableList<OfferProductDetails> = mutableListOf()
    private lateinit var editOfferAdapter : EditOfferAdapter
    private lateinit var editPrgBar : ProgressBar
    private lateinit var editNoProduct : TextView
    private lateinit var mAuth : FirebaseAuth
    
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
    ) : View? {
        // Inflate the layout for this fragment
        binding = FragmentEditOfferBinding.inflate(inflater, container, false)
        editPrgBar = binding.editProgressBar
        editNoProduct = binding.editNoProduct
        editPrgBar.visibility = View.INVISIBLE
        editNoProduct.visibility = View.INVISIBLE
        mAuth = FirebaseAuth.getInstance()
        
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = EDIT_OFFER_PRODUCT
    
        return binding.root
    }
    
    companion object {
      
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            EditOfferFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                }
            }
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val userId = mAuth.currentUser?.uid!!
    
        DatabaseServices.getProductDetailByUserId(
            PRODUCT_INFO_TABLE,
            productList,
            userId
        ) { isGetComplete ->
            if (isGetComplete) {
                editOfferAdapter =
                    EditOfferAdapter(this.requireContext(), productList){ productDetail ->
                        val fragment = EditOfferPageFragment.newInstance(userModel, productDetail)
                        requireActivity().supportFragmentManager.commit {
                            setReorderingAllowed(true)
                            replace(R.id.nav_host_fragment_content_home_page, fragment)
                            addToBackStack(null)
                        }
                    }
                val editRecycleView = binding.editOfferRecycleView
                editRecycleView.layoutManager = LinearLayoutManager(context)
                editRecycleView.adapter = editOfferAdapter
                editRecycleView.setHasFixedSize(true)
                editRecycleView.adapter.let {
                    if(editOfferAdapter.itemCount == 0){
                        editNoProduct.visibility = View.VISIBLE
                    } else {
                        editNoProduct.visibility = View.INVISIBLE
                    }
                }
            } else {
                editPrgBar.visibility = View.INVISIBLE
//                noOffersToday.visibility = View.VISIBLE
                Log.d("DEBUG", "Edit Offer Fragment - no record returned")
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