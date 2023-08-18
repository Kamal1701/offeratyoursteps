package com.example.offersatyoursteps.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.offersatyoursteps.R
import com.example.offersatyoursteps.adapters.OfferAdapter
import com.example.offersatyoursteps.adapters.ProductSubcategoryAdapter
import com.example.offersatyoursteps.databinding.FragmentProductSubcategoryBinding
import com.example.offersatyoursteps.models.UserModel
import com.example.offersatyoursteps.services.Dataservices
import com.example.offersatyoursteps.utilities.All_OFFERS_TITLE
import com.example.offersatyoursteps.utilities.OFFER_NEAR_ME_TITLE
import com.example.offersatyoursteps.utilities.OfferConstants
import com.example.offersatyoursteps.utilities.SPAN_COUNT
import com.example.offersatyoursteps.utilities.USER_INFO
import com.google.firebase.firestore.auth.User


class ProductSubcategoryFragment : Fragment() {
    
    private lateinit var binding: FragmentProductSubcategoryBinding
    
    private lateinit var userModel : UserModel
    
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
        binding = FragmentProductSubcategoryBinding.inflate(inflater, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        toolbar?.title = OFFER_NEAR_ME_TITLE
        return binding.root
    }
    
    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userModel : UserModel) =
            ProductSubcategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER_INFO, userModel)
                }
            }
    }
    
    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
//        val offerList = OfferConstants.getOfferData()
        val itemAdapter = ProductSubcategoryAdapter(this.requireContext(), Dataservices.subcategories){
            productSubcategory ->
//            userModel.prodSubcategory = productSubcategory.title
            val fm = OfferNearMeFragment.newInstance(userModel)
//            parentFragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, fm).commit()
            requireActivity().supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.nav_host_fragment_content_home_page, fm)
            }
        }
        val offerRecycleView = binding.productSubcategoryRecycleView
        offerRecycleView.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        offerRecycleView.adapter = itemAdapter
        
    }
}